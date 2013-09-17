package com.vampireneoapp.passiontimes.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.picasso.Picasso;
import com.vampireneoapp.passiontimes.BootstrapApplication;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.Article;

import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class ArticleListAdapter extends SingleTypeAdapter<Article> {

    //private final ThumbnailLoader avatars;

    /**
     * @param inflater
     * @param items
     */
    public ArticleListAdapter(LayoutInflater inflater, List<Article> items) {
        super(inflater, R.layout.article_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public ArticleListAdapter(LayoutInflater inflater) {
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
        return new int[] { R.id.iv_avatar, R.id.tv_title, R.id.tv_author, R.id.tv_desc };
    }

    @Override
    protected void update(int position, Article article) {

        Picasso.with(BootstrapApplication.getInstance())
                .load(article.getThumbnail())
                .placeholder(R.drawable.passiontimes_logo)
                .into(imageView(0));

        setText(1, article.getTitle());
        setText(2, article.getAuthor());
        setText(3, article.getDesc());
    }

}
