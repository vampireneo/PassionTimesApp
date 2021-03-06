package com.vampireneoapp.passiontimes.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.vampireneoapp.passiontimes.BootstrapApplication;
import com.vampireneoapp.passiontimes.PassionTimesServiceProvider;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.authenticator.LogoutService;
import com.vampireneoapp.passiontimes.core.Article;
import com.vampireneoapp.passiontimes.core.Category;
import com.vampireneoapp.passiontimes.core.ThumbnailLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import sun.util.logging.resources.logging;

import static com.vampireneoapp.passiontimes.core.Constants.Extra.ARTICLE;

public class ArticleListFragment extends ItemListFragment<Article> implements ActionBar.OnNavigationListener {


    @Inject PassionTimesServiceProvider serviceProvider;
    @Inject LogoutService logoutService;

    private ArrayList<String> categoryList;
    private HashMap<String, Category> categoryMap;
    private String categoryId;
    private String subCategoryId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.getInstance().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.no_article);
    }

    @Override //For Fragments.
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        Context context = getSherlockActivity().getSupportActionBar().getThemedContext();
        ArrayAdapter<String> list = new ArrayAdapter<String>(context, R.layout.sherlock_spinner_item, categoryList);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(list, this);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        try {
            String name = categoryList.get(itemPosition);
            if (name.equals("以作者列出文章")) {
                //do sth
            }
            else {
                Category category = categoryMap.get(name);
                categoryId = category.getCategoryId();
                subCategoryId = category.getSubCategoryId();
                forceRefresh();
            }
        }
        catch (Exception e) {

        }
        //mSelected.setText("Selected: " + mLocations[itemPosition]);
        return true;
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        /*getListAdapter().addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.article_list_item_labels, null));*/
    }

    /*@Override
    LogoutService getLogoutService() {
        return logoutService;
    }*/


    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        final List<Article> initialItems = items;
        return new ThrowableLoader<List<Article>>(getActivity(), items) {
            @Override
            public List<Article> loadData() throws Exception {

                try {
                    List<Article> latest = null;

                    String[] articleCategories = getResources().getStringArray(R.array.articleCategories);
                    categoryList = new ArrayList<String>(Arrays.asList(articleCategories));
                    categoryMap = new HashMap<String, Category>();
                    List<Category> categories = serviceProvider.getService(getActivity()).getCategories();
                    for(int i = 0; i < categories.size(); i++) {
                        String catName = categories.get(i).getName();
                        categoryList.add(catName);
                        categoryMap.put(catName, categories.get(i));
                    }
                    //categoryList.add("testing 123");

                    if(getActivity() != null)
                        latest = serviceProvider.getService(getActivity()).getArticles(categoryId, subCategoryId);

                    if (latest != null)
                        return latest;
                    else
                        return Collections.emptyList();
                } catch (OperationCanceledException e) {
                    Activity activity = getActivity();
                    if (activity != null)
                        activity.finish();
                    return initialItems;
                }
            }
        };

    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Article article = ((Article) l.getItemAtPosition(position));

        startActivity(new Intent(getActivity(), ArticleActivity.class).putExtra(ARTICLE, article));
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> items) {
        super.onLoadFinished(loader, items);

    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_articles;
    }

    @Override
    protected SingleTypeAdapter<Article> createAdapter(List<Article> items) {
        return new ArticleListAdapter(getActivity().getLayoutInflater(), items);
    }
}
