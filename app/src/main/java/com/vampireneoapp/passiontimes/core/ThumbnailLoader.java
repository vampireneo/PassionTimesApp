package com.vampireneoapp.passiontimes.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.github.kevinsawicki.http.HttpRequest;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.util.Ln;
import com.vampireneoapp.passiontimes.util.SafeAsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.view.View.VISIBLE;


/**
 * Avatar utilities
 */
public class ThumbnailLoader {

    private static final float CORNER_RADIUS_IN_DIP = 3;

    private static final int CACHE_SIZE = 75;

    private static abstract class FetchAvatarTask extends
            SafeAsyncTask<BitmapDrawable> {

        private static final Executor EXECUTOR = Executors
                .newFixedThreadPool(1);

        private FetchAvatarTask(Context context) {
            super(EXECUTOR);
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            Ln.d(e, "Avatar load failed");
        }
    }

    private final float cornerRadius;

    private final Map<Object, BitmapDrawable> loaded = new LinkedHashMap<Object, BitmapDrawable>(
            CACHE_SIZE, 1.0F) {

        private static final long serialVersionUID = -4191624209581976720L;

        @Override
        protected boolean removeEldestEntry(
                Map.Entry<Object, BitmapDrawable> eldest) {
            return size() >= CACHE_SIZE;
        }
    };

    private final Context context;

    private final File avatarDir;

    private final Drawable loadingAvatar;

    private final BitmapFactory.Options options;

    /**
     * Create avatar helper
     *
     * @param context
     */
    @Inject
    public ThumbnailLoader(final Context context) {
        this.context = context;

        loadingAvatar = context.getResources().getDrawable(R.drawable.gravatar_icon);

        avatarDir = new File(context.getCacheDir(), "avatars/" + context.getPackageName());
        if (!avatarDir.isDirectory())
            avatarDir.mkdirs();

        float density = context.getResources().getDisplayMetrics().density;
        cornerRadius = CORNER_RADIUS_IN_DIP * density;

        options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = ARGB_8888;
    }

    /**
     * Get image for article
     *
     * @param channel
     * @return image
     */
    protected BitmapDrawable getImage(final Channel channel) {
        File avatarFile = new File(avatarDir, channel.getId());

        if (!avatarFile.exists() || avatarFile.length() == 0)
            return null;

        Bitmap bitmap = decode(avatarFile);
        if (bitmap != null)
            return new BitmapDrawable(context.getResources(), bitmap);
        else {
            avatarFile.delete();
            return null;
        }
    }

    /**
     * Get image for article
     *
     * @param article
     * @return image
     */
    protected BitmapDrawable getImage(final Article article) {
        File avatarFile = new File(avatarDir, article.getId());

        if (!avatarFile.exists() || avatarFile.length() == 0)
            return null;

        Bitmap bitmap = decode(avatarFile);
        if (bitmap != null)
            return new BitmapDrawable(context.getResources(), bitmap);
        else {
            avatarFile.delete();
            return null;
        }
    }

    /**
     * Decode file to bitmap
     *
     * @param file
     * @return bitmap
     */
    protected Bitmap decode(final File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    /**
     * Fetch avatar from URL
     *
     * @param url
     * @param articleId
     * @return bitmap
     */
    protected BitmapDrawable fetchAvatar(final String url, final String articleId) {
        File rawAvatar = new File(avatarDir, articleId + "-raw");
        HttpRequest request = HttpRequest.get(url);
        if (request.ok())
            request.receive(rawAvatar);

        if (!rawAvatar.exists() || rawAvatar.length() == 0)
            return null;

        Bitmap bitmap = decode(rawAvatar);
        if (bitmap == null) {
            rawAvatar.delete();
            return null;
        }

        bitmap = ImageUtils.roundCorners(bitmap, cornerRadius);
        if (bitmap == null) {
            rawAvatar.delete();
            return null;
        }

        File roundedAvatar = new File(avatarDir, articleId.toString());
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(roundedAvatar);
            if (bitmap.compress(PNG, 100, output))
                return new BitmapDrawable(context.getResources(), bitmap);
            else
                return null;
        } catch (IOException e) {
            Ln.d(e, "Exception writing rounded thumbnail");
            return null;
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                    // Ignored
                }
            rawAvatar.delete();
        }
    }

    /**
     * Sets the logo on the {@link com.actionbarsherlock.app.ActionBar} to the channel's thumbnail.
     *
     * @param actionBar
     * @param channel
     * @return this helper
     */
    public ThumbnailLoader bind(final ActionBar actionBar, final Channel channel) {
        return bind2(actionBar, new AtomicReference<Channel>(channel));
    }

    /**
     * Sets the logo on the {@link com.actionbarsherlock.app.ActionBar} to the article's thumbnail.
     *
     * @param actionBar
     * @param channelReference
     * @return this helper
     */
    public ThumbnailLoader bind2(final ActionBar actionBar,
                                final AtomicReference<Channel> channelReference) {
        if (channelReference == null)
            return this;

        final Channel channel = channelReference.get();
        if (channel == null)
            return this;

        final String thumbnailUrl = channel.getIcon();
        if (TextUtils.isEmpty(thumbnailUrl))
            return this;

        final String channelId = channel.getId();

        BitmapDrawable loadedImage = loaded.get(channelId);
        if (loadedImage != null) {
            actionBar.setLogo(loadedImage);
            return this;
        }

        new FetchAvatarTask(context) {

            @Override
            public BitmapDrawable call() throws Exception {
                final BitmapDrawable image = getImage(channel);
                if (image != null)
                    return image;
                else
                    return fetchAvatar(thumbnailUrl, channelId.toString());
            }

            @Override
            protected void onSuccess(BitmapDrawable image) throws Exception {
                final Channel current = channelReference.get();
                if (current != null && channelId.equals(current.getId()))
                    actionBar.setLogo(image);
            }
        }.execute();

        return this;
    }

    /**
     * Sets the logo on the {@link com.actionbarsherlock.app.ActionBar} to the article's thumbnail.
     *
     * @param actionBar
     * @param article
     * @return this helper
     */
    public ThumbnailLoader bind(final ActionBar actionBar, final Article article) {
        return bind(actionBar, new AtomicReference<Article>(article));
    }

    /**
     * Sets the logo on the {@link com.actionbarsherlock.app.ActionBar} to the article's thumbnail.
     *
     * @param actionBar
     * @param articleReference
     * @return this helper
     */
    public ThumbnailLoader bind(final ActionBar actionBar,
                             final AtomicReference<Article> articleReference) {
        if (articleReference == null)
            return this;

        final Article article = articleReference.get();
        if (article == null)
            return this;

        final String thumbnailUrl = article.getThumbnail();
        if (TextUtils.isEmpty(thumbnailUrl))
            return this;

        final String articleId = article.getId();

        BitmapDrawable loadedImage = loaded.get(articleId);
        if (loadedImage != null) {
            actionBar.setLogo(loadedImage);
            return this;
        }

        new FetchAvatarTask(context) {

            @Override
            public BitmapDrawable call() throws Exception {
                final BitmapDrawable image = getImage(article);
                if (image != null)
                    return image;
                else
                    return fetchAvatar(thumbnailUrl, articleId.toString());
            }

            @Override
            protected void onSuccess(BitmapDrawable image) throws Exception {
                final Article current = articleReference.get();
                if (current != null && articleId.equals(current.getId()))
                    actionBar.setLogo(image);
            }
        }.execute();

        return this;
    }

    private ThumbnailLoader setImage(final Drawable image, final ImageView view) {
        return setImage(image, view, null);
    }

    private ThumbnailLoader setImage(final Drawable image, final ImageView view,
                                  Object tag) {
        view.setImageDrawable(image);
        view.setTag(R.id.iv_avatar, tag);
        view.setVisibility(VISIBLE);
        return this;
    }

    private String getAvatarUrl(String id) {
        if (!TextUtils.isEmpty(id))
            return "https://secure.gravatar.com/avatar/" + id + "?d=404";
        else
            return null;
    }

    private String getAvatarUrl(Article article) {
        return article.getThumbnail();
    }

    private String getAvatarUrl(Channel channel) {
        return channel.getIcon();
    }

    /**
     * Bind view to image at URL
     *
     * @param view
     * @param channel
     * @return this helper
     */
    public ThumbnailLoader bind(final ImageView view, final Channel channel) {
        if (channel == null)
            return setImage(loadingAvatar, view);

        String avatarUrl = getAvatarUrl(channel);

        if (TextUtils.isEmpty(avatarUrl))
            return setImage(loadingAvatar, view);

        final String channelId = channel.getId();

        BitmapDrawable loadedImage = loaded.get(channelId);
        if (loadedImage != null)
            return setImage(loadedImage, view);

        setImage(loadingAvatar, view, channelId);

        final String loadUrl = avatarUrl;
        new FetchAvatarTask(context) {

            @Override
            public BitmapDrawable call() throws Exception {
                if (!channelId.equals(view.getTag(R.id.iv_avatar)))
                    return null;

                final BitmapDrawable image = getImage(channel);
                if (image != null)
                    return image;
                else
                    return fetchAvatar(loadUrl, channelId.toString());
            }

            @Override
            protected void onSuccess(final BitmapDrawable image)
                    throws Exception {
                if (image == null)
                    return;
                loaded.put(channelId, image);
                if (channelId.equals(view.getTag(R.id.iv_avatar)))
                    setImage(image, view);
            }

        }.execute();

        return this;
    }

    /**
     * Bind view to image at URL
     *
     * @param view
     * @param article
     * @return this helper
     */
    public ThumbnailLoader bind(final ImageView view, final Article article) {
        if (article == null)
            return setImage(loadingAvatar, view);

        String avatarUrl = getAvatarUrl(article);

        if (TextUtils.isEmpty(avatarUrl))
            return setImage(loadingAvatar, view);

        final String articleId = article.getId();

        BitmapDrawable loadedImage = loaded.get(articleId);
        if (loadedImage != null)
            return setImage(loadedImage, view);

        setImage(loadingAvatar, view, articleId);

        final String loadUrl = avatarUrl;
        new FetchAvatarTask(context) {

            @Override
            public BitmapDrawable call() throws Exception {
                if (!articleId.equals(view.getTag(R.id.iv_avatar)))
                    return null;

                final BitmapDrawable image = getImage(article);
                if (image != null)
                    return image;
                else
                    return fetchAvatar(loadUrl, articleId.toString());
            }

            @Override
            protected void onSuccess(final BitmapDrawable image)
                    throws Exception {
                if (image == null)
                    return;
                loaded.put(articleId, image);
                if (articleId.equals(view.getTag(R.id.iv_avatar)))
                    setImage(image, view);
            }

        }.execute();

        return this;
    }
}

