package com.vampireneoapp.passiontimes.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.picasso.Picasso;
import com.vampireneoapp.passiontimes.BootstrapApplication;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.Channel;

import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class ChannelListAdapter extends SingleTypeAdapter<Channel> {

    /**
     * @param inflater
     * @param items
     */
    public ChannelListAdapter(LayoutInflater inflater, List<Channel> items) {
        super(inflater, R.layout.channel_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public ChannelListAdapter(LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = Integer.toString(getItem(position).getId());
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.iv_icon, R.id.tv_title, R.id.tv_author, R.id.tv_desc };
    }

    @Override
    protected void update(int position, Channel channel) {

        Picasso.with(BootstrapApplication.getInstance())
                .load(channel.getIcon())
                .placeholder(R.drawable.passiontimes_logo)
                .into(imageView(0));

        setText(1, channel.getTitle());
        setText(2, channel.getHost());
        setText(3, channel.getDesc());
    }

}
