package com.vampireneoapp.passiontimes.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.vampireneoapp.passiontimes.BootstrapApplication;
import com.vampireneoapp.passiontimes.PassionTimesServiceProvider;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.authenticator.LogoutService;
import com.vampireneoapp.passiontimes.core.Channel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.vampireneoapp.passiontimes.core.Constants.Extra.CHANNEL;

public class ChannelListFragment extends ItemListFragment<Channel> {


    @Inject PassionTimesServiceProvider serviceProvider;
    @Inject LogoutService logoutService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.getInstance().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.no_channel);
    }

    @Override //For Fragments.
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
    public Loader<List<Channel>> onCreateLoader(int id, Bundle args) {
        final List<Channel> initialItems = items;
        return new ThrowableLoader<List<Channel>>(getActivity(), items) {
            @Override
            public List<Channel> loadData() throws Exception {

                try {
                    List<Channel> latest = null;

                    if(getActivity() != null)
                        latest = serviceProvider.getService(getActivity()).getChannels();

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
        Channel channel = ((Channel) l.getItemAtPosition(position));
        startActivity(new Intent(getActivity(), ChannelDetailListActivity.class).putExtra(CHANNEL, channel));
    }

    @Override
    public void onLoadFinished(Loader<List<Channel>> loader, List<Channel> items) {
        super.onLoadFinished(loader, items);

    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_channels;
    }

    @Override
    protected SingleTypeAdapter<Channel> createAdapter(List<Channel> items) {
        return new ChannelListAdapter(getActivity().getLayoutInflater(), items);
    }
}
