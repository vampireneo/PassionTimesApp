package com.vampireneoapp.passiontimes.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.Article;
import com.vampireneoapp.passiontimes.core.ThumbnailLoader;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.vampireneoapp.passiontimes.core.Constants.Extra.ARTICLE;

public class ArticleActivity extends BootstrapActivity {

    @InjectView(R.id.iv_avatar) protected ImageView avatar;
    @InjectView(R.id.tv_name) protected TextView name;

    @Inject protected ThumbnailLoader thumbnailLoader;

    protected Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_view);

        if(getIntent() != null && getIntent().getExtras() != null) {
            article = (Article) getIntent().getExtras().getSerializable(ARTICLE);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumbnailLoader.bind(avatar, article);
        name.setText(String.format("%s %s", article.getTitle(), article.getAuthor()));

    }


}
