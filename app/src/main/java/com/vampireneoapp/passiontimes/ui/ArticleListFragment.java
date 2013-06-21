package com.vampireneoapp.passiontimes.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.vampireneoapp.passiontimes.BootstrapApplication;
import com.vampireneoapp.passiontimes.PassionTimesServiceProvider;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.authenticator.LogoutService;
import com.vampireneoapp.passiontimes.core.Article;
import com.vampireneoapp.passiontimes.core.ThumbnailLoader;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.vampireneoapp.passiontimes.core.Constants.Extra.ARTICLE;

public class ArticleListFragment extends ItemListFragment<Article> {


    @Inject PassionTimesServiceProvider serviceProvider;
    @Inject ThumbnailLoader avatars;
    @Inject LogoutService logoutService;


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

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        getListAdapter().addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.article_list_item_labels, null));
    }

    @Override
    LogoutService getLogoutService() {
        return logoutService;
    }


    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        final List<Article> initialItems = items;
        return new ThrowableLoader<List<Article>>(getActivity(), items) {
            @Override
            public List<Article> loadData() throws Exception {

                try {
                    List<Article> latest = null;

                    if(getActivity() != null)
                        latest = serviceProvider.getService(getActivity()).getArticles();

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
        return R.string.error_loading_users;
    }

    @Override
    protected SingleTypeAdapter<Article> createAdapter(List<Article> items) {
        return new ArticleListAdapter(getActivity().getLayoutInflater(), items, avatars);
    }
}
