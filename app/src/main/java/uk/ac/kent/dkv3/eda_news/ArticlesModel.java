package uk.ac.kent.dkv3.eda_news;

/**
 * Created by dkv3 on 21/04/2017.
 */


import android.content.Context;
import android.support.v7.util.ListUpdateCallback;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ArticlesModel {

    private static ArticlesModel ourInstance = new ArticlesModel();

    public static ArticlesModel getInstance() {
        return ourInstance;
    }

    private ArrayList<Article> articleList = new ArrayList<>();

    public OnListUpdateListener listUpdateListener;

    private NewsDatabase dbHelper;

    private Context context;

    private Boolean favourites = false;

    public void setFavouriteValue(Boolean fav){
        this.favourites = fav;
    }

    public void setOnListUpdateListener(OnListUpdateListener listUpdateListener) {
        this.listUpdateListener = listUpdateListener;
    }

    public interface OnListUpdateListener {
        void onListUpdate(ArrayList<Article> contactList);
    }

    //Method called when the list is updated
    private void notifyListener(){
        if (listUpdateListener != null)
            listUpdateListener.onListUpdate(articleList);
    }


    private Response.Listener<JSONArray> netListener = new Response.Listener<JSONArray>(){
        @Override
        public void onResponse(JSONArray response) {
            //Clean articleList from old data
            articleList.clear();
            try {
                dbHelper = NewsDatabase.getInstance(context);
                dbHelper.open();
                for (int i = 0; i < response.length(); i++){
                    JSONObject obj = response.getJSONObject(i);
                    int recordId = obj.getInt("record_id");
                    Article article = new Article(
                            obj.getString("image_url"),
                            recordId,
                            obj.getString("title"),
                            obj.getString("date"),
                            obj.getString("short_info"),
                            "",
                            "",
                            false
                    );

                    long insertId = dbHelper.findArticle(article);

                    //The article is not in the database
                    if (insertId == -1) {
                        insertId = dbHelper.createArticle(article);
                    }
                    //If the article doesn't have its full content, as well as other details,
                    //it calls loadMoreData to get them
                    if (article.getContents().equals("")) {
                        loadMoreData(recordId);
                    }
                }
                dbHelper.close();
            } catch (JSONException e) {
                //Parsing error in json data
                e.printStackTrace();
            }
            notifyListener();
        }
    };


    private Response.ErrorListener errorListener = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
            Log.d("VOLLEY ERROR", error.toString());
        }
    };

    //Method called when an article is chosen as favourite by the user
    public Boolean setFavourite(int id){
        Boolean savedFavourite = false;
        try {
            dbHelper = NewsDatabase.getInstance(context);
            dbHelper.open();
            //The articles are saved in the database starting from 1
            //but their id starts from 0, so that's why there's a +1
            savedFavourite = dbHelper.findArticle(id+1);

        } catch (SQLException e) {
            // Something wrong with the database
            e.printStackTrace();
        }
        return savedFavourite;
    }


    private ArticlesModel() {}

    //Method used to get articles that contain a specific word entered by the user
    public ArrayList<Article> getArticlesSearch(String query){
        articleList.clear();
        try {
            dbHelper = NewsDatabase.getInstance(context);
            dbHelper.open();
            Cursor cursor = dbHelper.getArticlesSearch(query);
            cursor.moveToFirst();
            //If the database query got any result
            if (cursor.getCount() > 0) {
                while (!cursor.isAfterLast()) {
                    Article article = new Article(cursor.getString(1), cursor.getInt(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                            cursor.getString(7), cursor.getInt(8) == 1);
                    articleList.add(article);
                    cursor.moveToNext();
                }
            }
        } catch (SQLException e) {
            // Something wrong with the database
            e.printStackTrace();
        }
        return articleList;

    }

    //Method that returns an ArrayList containing all the articles saved in the dabatase
    public ArrayList<Article> getArticleList() {
        articleList.clear();
        //If the class calling the method wants the favourites list
        if (this.favourites){
            try {
                dbHelper = NewsDatabase.getInstance(context);
                dbHelper.open();
                Cursor cursor = dbHelper.getFavourites();
                cursor.moveToFirst();
                //If the database query got any result
                if (cursor.getCount() > 0) {
                    while (!cursor.isAfterLast()) {
                        Article article = new Article(cursor.getString(1), cursor.getInt(2),
                                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                                cursor.getString(7), cursor.getInt(8) == 1);
                        articleList.add(article);
                        cursor.moveToNext();
                    }
                }
            } catch (SQLException e) {
                // Something wrong with the database
                e.printStackTrace();
            }
         //If the class calling the method wants the regular articles list
        }else {
            try {
                dbHelper = NewsDatabase.getInstance(context);
                dbHelper.open();
                Cursor cursor = dbHelper.getAllArticles();
                cursor.moveToFirst();
                //If the database query got any result
                if (cursor.getCount() > 0) {
                    while (!cursor.isAfterLast()) {
                        Article article = new Article(cursor.getString(1), cursor.getInt(2),
                                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                                cursor.getString(7), cursor.getInt(8) == 1);
                        articleList.add(article);
                        cursor.moveToNext();
                    }
                }
            } catch (SQLException e) {
                // Something wrong with the database
                e.printStackTrace();
            }
        }
        return articleList;


    }

    public Article findById(int id){
        Article article = new Article();
        try {
            dbHelper = NewsDatabase.getInstance(context);
            dbHelper.open();
            Cursor cursor = dbHelper.findArticleById(id);
            cursor.moveToFirst();
            //If the database query got any result
            if (cursor.getCount() > 0) {
                article = new Article(cursor.getString(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getInt(8) == 1);
            }


        } catch (SQLException e) {
            // Something wrong with the database
            e.printStackTrace();
        }
        return article;
    }

    //Method that gets a Json object with all the news articles from the specified URL
    public void loadData(final Context context){
        this.context = context;
        //Create a request object
        JsonArrayRequest request = new JsonArrayRequest(
                "http://www.efstratiou.info/projects/newsfeed/getList.php",
                netListener, errorListener);


        //Submit request
        ArticlesApp.getInstance().getRequestQueue().add(request);

    }

    //Method that gets a Json object with details of the news article with ID recordID
    //from the specified URL
    public void loadMoreData(int recordId){

        //Create a request object
        JsonObjectRequest requestMore = new JsonObjectRequest(
                Request.Method.GET, "http://www.efstratiou.info/projects/newsfeed/getItem.php?id="+recordId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject obj = response;
                            Article article = new Article(
                                    obj.getString("image_url"),
                                    obj.getInt("record_id"),
                                    obj.getString("title"),
                                    obj.getString("date"),
                                    obj.getString("short_info"),
                                    obj.getString("contents"),
                                    obj.getString("web_page"),
                                    false
                            );
                            dbHelper.updateArticle(article);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        notifyListener();
                    }
                }, errorListener);

        //Submit request
        ArticlesApp.getInstance().getRequestQueue().add(requestMore);

    }
}
