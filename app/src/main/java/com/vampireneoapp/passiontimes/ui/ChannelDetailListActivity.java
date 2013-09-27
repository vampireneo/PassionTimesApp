package com.vampireneoapp.passiontimes.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.widget.TextView;

import com.vampireneoapp.passiontimes.PassionTimesServiceProvider;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.Channel;
import com.vampireneoapp.passiontimes.core.ChannelDetail;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.vampireneoapp.passiontimes.core.Constants.Extra.CHANNEL;

public class ChannelDetailListActivity extends ItemListActivity<ChannelDetail>  {

    @InjectView(R.id.tv_title) protected TextView title;

    @Inject PassionTimesServiceProvider serviceProvider;

    protected Channel channel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.channel_detail_list);

        if(getIntent() != null && getIntent().getExtras() != null) {
            channel = (Channel) getIntent().getExtras().getSerializable(CHANNEL);
        }

        //title.setText(channel.getTitle());

        //new GetChannelDetailTask(this).execute(Integer.toString(channel.getId()));
    }

    @Override
    public Loader<List<ChannelDetail>> onCreateLoader(int i, Bundle bundle) {
        final List<ChannelDetail> initialItems = items;
        final Activity a = this;
        return new ThrowableLoader<List<ChannelDetail>>(this, items) {
            @Override
            public List<ChannelDetail> loadData() throws Exception {

                try {
                    List<ChannelDetail> latest = serviceProvider.getService(a).getChannelDetails(Integer.toString(channel.getId()));

                    if (latest != null)
                        return latest;
                    else
                        return Collections.emptyList();
                } catch (OperationCanceledException e) {
                    /*Activity activity = a;
                    if (activity != null)
                        activity.finish();*/
                    return initialItems;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<ChannelDetail>> listLoader, List<ChannelDetail> channelDetails) {

    }

    @Override
    public void onLoaderReset(Loader<List<ChannelDetail>> listLoader) {

    }

/*
    @Override
    protected SingleTypeAdapter<ChannelDetail> createAdapter(List<ChannelDetail> items) {
        return new ChannelDetailListAdapter(this.getLayoutInflater(), items);
    }
*/


    /*private class GetChannelDetailTask extends AsyncTask<String, Void, List<ChannelDetail>> {
        public Activity activity;

        public GetChannelDetailTask(Activity a)
        {
            activity = a;
        }

        @Override
        protected List<ChannelDetail> doInBackground(String... id) {
            List<ChannelDetail> channelDetails = null;
            try {
                channelDetails = serviceProvider.getService(activity).getChannelDetails(id[0]);

            } catch (AccountsException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return channelDetails;
        }

        @Override
        protected void onPostExecute(List<ChannelDetail> channelDetails) {
            //content.setText(article.getContentHtml());

            *//*WebSettings settings = content.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            content.loadData(article.getContent(), "text/html; charset=utf-8", "utf-8");
            content.setBackgroundColor(0x00000000);*//*
        }
    }*/
}
