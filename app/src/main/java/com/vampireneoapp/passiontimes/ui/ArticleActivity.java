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

    @InjectView(R.id.iv_image) protected ImageView avatar;
    @InjectView(R.id.tv_title) protected TextView title;
    @InjectView(R.id.tv_author) protected TextView author;
    @InjectView(R.id.tv_content) protected TextView content;

    @Inject protected ThumbnailLoader thumbnailLoader;

    protected Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.article_view);

        if(getIntent() != null && getIntent().getExtras() != null) {
            article = (Article) getIntent().getExtras().getSerializable(ARTICLE);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumbnailLoader.bind(avatar, article);
        title.setText(article.getTitle());
        author.setText(article.getAuthor());
        content.setText(article.getDesc());
    }


}
