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

    /*@Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);

        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }*/
}
