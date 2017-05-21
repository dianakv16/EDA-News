package uk.ac.kent.dkv3.eda_news;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by dkv3 on 21/04/2017.
 */

public class ArticlesApp extends Application {

    private static ArticlesApp instance;

    private RequestQueue requestQueue;

    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        //Instance of the singleton application object
        instance = this;
        //Create the request queue
        requestQueue = Volley.newRequestQueue(this);
        //Create the image loader
        int cacheSize = 4 * 1024 * 1024; // 4MiB
        imageLoader = new ImageLoader(requestQueue, new LruBitmapCache(cacheSize));
    }

    public static ArticlesApp getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
