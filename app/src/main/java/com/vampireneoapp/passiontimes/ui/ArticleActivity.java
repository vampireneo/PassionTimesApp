package com.vampireneoapp.passiontimes.ui;

import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.vampireneoapp.passiontimes.PassionTimesServiceProvider;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.Article;
import com.vampireneoapp.passiontimes.core.ThumbnailLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.vampireneoapp.passiontimes.core.Constants.Extra.ARTICLE;
import static com.vampireneoapp.passiontimes.core.Constants.Http.PT_URL_ARTICLE;
import static com.vampireneoapp.passiontimes.core.Constants.Http.PT_URL_BASE;

public class ArticleActivity extends BootstrapActivity  {

    @InjectView(R.id.tv_title) protected TextView title;
    @InjectView(R.id.tv_author) protected TextView author;
    @InjectView(R.id.tv_date) protected TextView date;
    @InjectView(R.id.tv_content) protected TextView content;

    @Inject PassionTimesServiceProvider serviceProvider;
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

        title.setText(article.getTitle());
        author.setText(article.getAuthor());
        date.setText(article.getTs());

        new GetArticleTask(this).execute(article.getId());
    }

    private class GetArticleTask extends AsyncTask<String, Void, Article> {
        public Activity activity;

        public GetArticleTask(Activity a)
        {
            activity = a;
        }

        @Override
        protected Article doInBackground(String... id) {
            Article article = null;
            try {
                article = serviceProvider.getService(activity).getArticle(id[0]);
                Spanned html = Html.fromHtml(article.getContent(), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String s) {
                        Drawable d = null;
                        try {
                            InputStream src= imageFetch(s);
                            d = Drawable.createFromStream(src, "src");
                            if (d != null) {
                                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                            }
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        return d;
                    }
                }, null);
                article.setContentHtml(html);
            } catch (AccountsException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return article;
        }

        @Override
        protected void onPostExecute(Article article) {
            content.setText(article.getContentHtml());
            /*WebSettings settings = content.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            content.loadData(article.getContent(), "text/html; charset=utf-8", "utf-8");
            content.setBackgroundColor(0x00000000);*/
        }

        protected InputStream imageFetch(String source) throws MalformedURLException, IOException {
            URL url = new URL(source);
            Object o = url.getContent();
            InputStream content = (InputStream)o;
            return content;
        }
    }

}
