package com.vampireneoapp.passiontimes.ui;

import android.accounts.AccountsException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.vampireneoapp.passiontimes.PassionTimesServiceProvider;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.Channel;
import com.vampireneoapp.passiontimes.core.ChannelDetail;
import com.vampireneoapp.passiontimes.core.ThumbnailLoader;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.vampireneoapp.passiontimes.core.Constants.Extra.CHANNEL;

public class ChannelDetailListActivity extends BootstrapActivity  {

    @InjectView(R.id.tv_title) protected TextView title;

    @Inject PassionTimesServiceProvider serviceProvider;
    @Inject protected ThumbnailLoader thumbnailLoader;

    protected Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.channel_detail_view);

        if(getIntent() != null && getIntent().getExtras() != null) {
            channel = (Channel) getIntent().getExtras().getSerializable(CHANNEL);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title.setText(channel.getTitle());
        //author.setText(article.getAuthor());
        //date.setText(article.getTs());

        new GetChannelDetailTask(this).execute(Integer.toString(channel.getId()));
    }

    private class GetChannelDetailTask extends AsyncTask<String, Void, List<ChannelDetail>> {
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

            /*WebSettings settings = content.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            content.loadData(article.getContent(), "text/html; charset=utf-8", "utf-8");
            content.setBackgroundColor(0x00000000);*/
        }
    }

}
