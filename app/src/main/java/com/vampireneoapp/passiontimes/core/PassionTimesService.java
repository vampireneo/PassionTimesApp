
package com.vampireneoapp.passiontimes.core;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

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
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.vampireneoapp.passiontimes.core.Constants.Http.HEADER_PARSE_APP_ID;
import static com.vampireneoapp.passiontimes.core.Constants.Http.HEADER_PARSE_REST_API_KEY;
import static com.vampireneoapp.passiontimes.core.Constants.Http.PARSE_APP_ID;
import static com.vampireneoapp.passiontimes.core.Constants.Http.PARSE_REST_API_KEY;
import static com.vampireneoapp.passiontimes.core.Constants.Http.PT_URL_ARTICLE;
import static com.vampireneoapp.passiontimes.core.Constants.Http.PT_URL_ARTICLE_LIST;
import static com.vampireneoapp.passiontimes.core.Constants.Http.PT_URL_BASE;
import static com.vampireneoapp.passiontimes.core.Constants.Http.PT_URL_CHANNEL_LIST;
import static com.vampireneoapp.passiontimes.core.Constants.Http.URL_CHECKINS;
import static com.vampireneoapp.passiontimes.core.Constants.Http.URL_NEWS;
import static com.vampireneoapp.passiontimes.core.Constants.Http.URL_USERS;

/**
 * Bootstrap API service
 */
public class PassionTimesService {

    private UserAgentProvider userAgentProvider;

    /**
     * GSON instance to use for all request  with date format set up for proper parsing.
     */
    public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    /**
     * You can also configure GSON with different naming policies for your API. Maybe your api is Rails
     * api and all json values are lower case with an underscore, like this "first_name" instead of "firstName".
     * You can configure GSON as such below.
     *
     * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
     *
     */


    /**
     * Read and connect timeout in milliseconds
     */
    private static final int TIMEOUT = 30 * 1000;


    private static class UsersWrapper {

        private List<User> results;
    }

    private static class NewsWrapper {

        private List<News> results;
    }

    private static class CheckInWrapper {

        private List<CheckIn> results;

    }

    private static class ArticlesWrapper {
        private List<Article> results;
    }

    private static class ChannelsWrapper {
        private List<Channel> results;
    }

    private static class JsonException extends IOException {

        private static final long serialVersionUID = 3774706606129390273L;

        /**
         * Create exception from {@link com.google.gson.JsonParseException}
         *
         * @param cause
         */
        public JsonException(JsonParseException cause) {
            super(cause.getMessage());
            initCause(cause);
        }
    }


    private final String apiKey;
    private final String username;
    private final String password;

    /**
     * Create bootstrap service
     *
     * @param username
     * @param password
     */
    public PassionTimesService(final String username, final String password) {
        this.username = username;
        this.password = password;
        this.apiKey = null;
    }

    /**
     * Create bootstrap service
     *
     * @param userAgentProvider
     * @param apiKey
     */
    public PassionTimesService(final String apiKey, final UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
        this.username = null;
        this.password = null;
        this.apiKey = apiKey;
    }

    /**
     * Create bootstrap service
     *
     */
    public PassionTimesService() {
        this.userAgentProvider = null;
        this.username = null;
        this.password = null;
        this.apiKey = null;
    }

    /**
     * Execute request
     *
     * @param request
     * @return request
     * @throws java.io.IOException
     */
    protected HttpRequest execute(HttpRequest request) throws IOException {
        if (!configure(request).ok())
            throw new IOException("Unexpected response code: " + request.code());
        return request;
    }

    private HttpRequest configure(final HttpRequest request) {
        request.connectTimeout(TIMEOUT).readTimeout(TIMEOUT);
//        request.userAgent(userAgentProvider.get());

        if(isPostOrPut(request))
            request.contentType(Constants.Http.CONTENT_TYPE_JSON); // All PUT & POST requests to Parse.com api must be in JSON - https://www.parse.com/docs/rest#general-requests

        return addCredentialsTo(request);
    }

    private boolean isPostOrPut(HttpRequest request) {
        return request.getConnection().getRequestMethod().equals(HttpRequest.METHOD_POST)
               || request.getConnection().getRequestMethod().equals(HttpRequest.METHOD_PUT);

    }

    private HttpRequest addCredentialsTo(HttpRequest request) {

        // Required params for
        request.header(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY );
        request.header(HEADER_PARSE_APP_ID, PARSE_APP_ID);

        /**
         * NOTE: This may be where you want to add a header for the api token that was saved when you
         * logged in. In the bootstrap sample this is where we are saving the session id as the token.
         * If you actually had received a token you'd take the "apiKey" (aka: token) and add it to the
         * header or form values before you make your requests.
          */

        /**
         * Add the user name and password to the request here if your service needs username or password for each
         * request. You can do this like this:
         * request.basic("myusername", "mypassword");
         */

        return request;
    }

    private String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }

    private <V> V fromJson(HttpRequest request, Class<V> target) throws IOException {
        Reader reader = request.bufferedReader();
        try {
            return GSON.fromJson(reader, target);
        } catch (JsonParseException e) {
            throw new JsonException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {
                // Ignored
            }
        }
    }

    /**
     * Get all bootstrap Users that exist on Parse.com
     *
     * @return non-null but possibly empty list of bootstrap
     * @throws java.io.IOException
     */
    public List<User> getUsers() throws IOException {
        try {
            HttpRequest request = execute(HttpRequest.get(URL_USERS));
            UsersWrapper response = fromJson(request, UsersWrapper.class);
            if (response != null && response.results != null)
                return response.results;
            return Collections.emptyList();
        } catch (HttpRequestException e) {
            throw e.getCause();
        }
    }

    public Article getArticle(String id) throws IOException {
        String request = readJSONFeed(PT_URL_BASE + PT_URL_ARTICLE + id);
        Article article = null;
        try {
            JSONObject jsonObject = new JSONObject(request);
            article = new Article();
            article.setContent(jsonObject.getString("content"));
            JSONArray imagesArray = jsonObject.getJSONArray("images");
            article.setImages(new ArrayList<String>());
            for (int i = 0; i < imagesArray.length();i++) {
                article.getImages().add(imagesArray.getString(i));
            }
            JSONArray imagesThumbnailArray = jsonObject.getJSONArray("images_thumbnail");
            article.setImagesThumbnail(new ArrayList<String>());
            for (int i = 0; i < imagesArray.length();i++) {
                article.getImagesThumbnail().add(imagesThumbnailArray.getString(i));
            }
        } catch (JSONException e) {
            Log.d("failed to parse JSON", e.getLocalizedMessage());
        }
        return article;
    }

    /**
     * Get all articles that exist on PassionTimes
     *
     * @return non-null but possibly empty list of article
     * @throws java.io.IOException
     */
    public List<Article> getArticles() throws IOException {
        try {
            //HttpRequest request = execute(HttpRequest.get(PT_URL_BASE + PT_URL_ARTICLE_LIST));
            ArticlesWrapper response = new ArticlesWrapper();
            response.results = new ArrayList<Article>();
            //HttpRequest request = HttpRequest.get(PT_URL_BASE + PT_URL_ARTICLE_LIST);
            String request = readJSONFeed(PT_URL_BASE + PT_URL_ARTICLE_LIST);
            try {
                JSONObject jsonObject = new JSONObject(request);
                Iterator keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = (String)keys.next();
                    JSONObject object = jsonObject.getJSONObject(key);
                    Article article = new Article();
                    article.setThumbnail(object.getString("thumbnail"));
                    article.setDesc(object.getString("desc"));
                    article.setAuthor(object.getString("author"));
                    article.setUrl(object.getString("url"));
                    article.setTitle(object.getString("title"));
                    article.setId(key);
                    article.setTs(object.getString("ts"));
                    article.setCategory(object.getString("category"));
                    article.setSubCategory(object.getString("subCategory"));
                    response.results.add(article);
                }
            } catch (JSONException e) {
                Log.d("failed to parse JSON", e.getLocalizedMessage());
            }

            Collections.sort(response.results, new Comparator<Article>() {
                public int compare(Article o1, Article o2) {
                    int result = o2.getTs().compareTo(o1.getTs());
                    return (result == 0 ? o2.getId().compareTo(o1.getId()) : result);
                }
            });

            //String test = fromJson(request, String.class);
            //ArticlesWrapper response = fromJson(request, ArticlesWrapper.class);
            if (response != null && response.results != null) {
                return response.results;
            }
            return Collections.emptyList();
        } catch (HttpRequestException e) {
            throw e.getCause();
        }
    }

    /**
     * Get all channels that exist on PassionTimes
     *
     * @return non-null but possibly empty list of article
     * @throws java.io.IOException
     */
    public List<Channel> getChannels() throws IOException {
        try {
            ChannelsWrapper response = new ChannelsWrapper();
            response.results = new ArrayList<Channel>();
            String request = readJSONFeed(PT_URL_BASE + PT_URL_CHANNEL_LIST);
            try {
                JSONObject jsonObject = new JSONObject(request);
                Iterator keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = (String)keys.next();
                    JSONObject object = jsonObject.getJSONObject(key);
                    Channel channel = new Channel();
                    channel.setId(key);
                    channel.setTitle(object.getString("title"));
                    channel.setIcon(object.getString("icon"));
                    channel.setDesc(object.getString("desc"));
                    channel.setHost(object.getString("host"));
                    if (object.has("fb"))
                        channel.setFb(object.getString("fb"));
                    ArrayList<String> adMp3 = new ArrayList<String>();
                    ArrayList<String> adMp4 = new ArrayList<String>();
                    channel.setAdMp3(adMp3);
                    channel.setAdMp4(adMp4);
                    response.results.add(channel);
                }
            } catch (JSONException e) {
                Log.d("failed to parse JSON", e.getLocalizedMessage());
            }

            Collections.sort(response.results, new Comparator<Channel>() {
                public int compare(Channel o1, Channel o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });

            if (response.results != null)
                return response.results;
            else
                return Collections.emptyList();
        } catch (HttpRequestException e) {
            throw e.getCause();
        }
    }

    /**
     * Get all bootstrap News that exists on Parse.com
     *
     * @return non-null but possibly empty list of bootstrap
     * @throws java.io.IOException
     */
    public List<News> getNews() throws IOException {
        try {
            HttpRequest request = execute(HttpRequest.get(URL_NEWS));
            NewsWrapper response = fromJson(request, NewsWrapper.class);
            if (response != null && response.results != null)
                return response.results;
            return Collections.emptyList();
        } catch (HttpRequestException e) {
            throw e.getCause();
        }
    }

    /**
     * Get all bootstrap Checkins that exists on Parse.com
     *
     * @return non-null but possibly empty list of bootstrap
     * @throws java.io.IOException
     */
    public List<CheckIn> getCheckIns() throws IOException {
        try {
            HttpRequest request = execute(HttpRequest.get(URL_CHECKINS));
            CheckInWrapper response = fromJson(request, CheckInWrapper.class);
            if (response != null && response.results != null)
                return response.results;
            return Collections.emptyList();
        } catch (HttpRequestException e) {
            throw e.getCause();
        }
    }

}
