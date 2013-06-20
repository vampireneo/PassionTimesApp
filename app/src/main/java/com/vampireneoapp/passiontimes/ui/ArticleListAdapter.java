package com.vampireneoapp.passiontimes.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.Article;
import com.vampireneoapp.passiontimes.core.ThumbnailLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class ArticleListAdapter extends SingleTypeAdapter<Article> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");
    private final ThumbnailLoader avatars;

    /**
     * @param inflater
     * @param items
     */
    public ArticleListAdapter(LayoutInflater inflater, List<Article> items, ThumbnailLoader avatars) {
        super(inflater, R.layout.article_list_item);

        this.avatars = avatars;
        setItems(items);
    }

    /**
     * @param inflater
     */
    public ArticleListAdapter(LayoutInflater inflater, ThumbnailLoader avatars) {
        this(inflater, null, avatars);

    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getId();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.iv_avatar, R.id.tv_name };
    }

    @Override
    protected void update(int position, Article article) {

        avatars.bind(imageView(0), article);

        setText(1, String.format("%1$s %2$s", article.getTitle(), article.getAuthor()));

    }

}
