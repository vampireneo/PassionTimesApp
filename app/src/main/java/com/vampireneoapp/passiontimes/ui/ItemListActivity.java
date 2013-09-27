package com.vampireneoapp.passiontimes.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by Neo Choi on 20/9/13.
 */
public abstract class ItemListActivity<E> extends SherlockListActivity
        implements LoaderManager.LoaderCallbacks<List<E>> {

    protected List<E> items = Collections.emptyList();

    protected ListView listView;
    protected boolean listShown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);
            bar.setDisplayHomeAsUpEnabled(true);
        }

/*        Intent intent = getIntent();
        String path = intent.getStringExtra("com.example.android.apis.Path");

        if (path == null) {
            path = "";
        }

        setListAdapter(new SimpleAdapter(this, getData(path),
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));
        getListView().setTextFilterEnabled(true);*/
        /*if (!items.isEmpty())
            setListShown(true, false);*/

        //getLoaderManager().initLoader(0, null, this);
        getListView().setTextFilterEnabled(true);
    }

    /*protected Exception getException(final Loader<List<E>> loader) {
        if (loader instanceof ThrowableLoader)
            return ((ThrowableLoader<List<E>>) loader).clearException();
        else
            return null;
    }

    protected void showError(final int message) {
        Toaster.showLong(this, message);
    }

    private ItemListActivity<E> hide(final View view) {
        ViewUtils.setGone(view, true);
        return this;
    }

    private ItemListActivity<E> show(final View view) {
        ViewUtils.setGone(view, false);
        return this;
    }
    protected ProgressBar progressBar;

    protected TextView emptyView;

    private ItemListActivity<E> fadeIn(final View view, final boolean animate) {
        if (view != null)
            if (animate)
                view.startAnimation(AnimationUtils.loadAnimation(this,
                        android.R.anim.fade_in));
            else
                view.clearAnimation();
        return this;
    }

    public ItemListActivity<E> setListShown(final boolean shown,
                                            final boolean animate) {
        if (shown == listShown) {
            if (shown)
                // List has already been shown so hide/show the empty view with
                // no fade effect
                if (items.isEmpty())
                    hide(listView).show(emptyView);
                else
                    hide(emptyView).show(listView);
            return this;
        }

        listShown = shown;

        if (shown)
            if (!items.isEmpty())
                hide(progressBar).hide(emptyView).fadeIn(listView, animate)
                        .show(listView);
            else
                hide(progressBar).hide(listView).fadeIn(emptyView, animate)
                        .show(emptyView);
        else
            hide(listView).hide(emptyView).fadeIn(progressBar, animate)
                    .show(progressBar);

        return this;
    }

    protected void showList() {
        setListShown(true, true);
    }

    protected abstract int getErrorMessage(Exception exception);

    public void onLoadFinished(Loader<List<E>> loader, List<E> items) {

        setSupportProgressBarIndeterminateVisibility(false);

        Exception exception = getException(loader);
        if (exception != null) {
            showError(getErrorMessage(exception));
            showList();
            return;
        }

        this.items = items;
        //getListAdapter().get
        showList();
    }

    protected abstract SingleTypeAdapter<E> createAdapter(final List<E> items);

    protected HeaderFooterListAdapter<SingleTypeAdapter<E>> createAdapter() {
        SingleTypeAdapter<E> wrapped = createAdapter(this.items);
        return new HeaderFooterListAdapter<SingleTypeAdapter<E>>(getListView(),
                wrapped);
    }*/

    /*@Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);

        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }*/
}
