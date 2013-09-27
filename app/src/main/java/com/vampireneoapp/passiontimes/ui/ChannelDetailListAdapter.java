package com.vampireneoapp.passiontimes.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.picasso.Picasso;
import com.vampireneoapp.passiontimes.BootstrapApplication;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.ChannelDetail;

import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class ChannelDetailListAdapter extends SingleTypeAdapter<ChannelDetail> {

    /**
     * @param inflater
     * @param items
     */
    public ChannelDetailListAdapter(LayoutInflater inflater, List<ChannelDetail> items) {
        super(inflater, R.layout.channel_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public ChannelDetailListAdapter(LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getId();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.iv_icon, R.id.tv_title, R.id.tv_author, R.id.tv_desc };
    }

    @Override
    protected void update(int position, ChannelDetail channelDetail) {

        Picasso.with(BootstrapApplication.getInstance())
                .load(channelDetail.getThumbnail())
                .placeholder(R.drawable.passiontimes_logo)
                .into(imageView(0));

        setText(1, channelDetail.getTopic());
        setText(2, channelDetail.getHost());
        setText(3, channelDetail.getMp3().get(0));
    }

}
