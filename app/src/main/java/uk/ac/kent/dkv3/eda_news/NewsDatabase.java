package uk.ac.kent.dkv3.eda_news;

/**
 * Created by dkv3 on 21/04/2017.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NewsDatabase extends SQLiteOpenHelper {

    //DB settings
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "news";

    //Table settings
    public static final String TABLE_NAME = "articles";
    public static final String ID_COL = "id";
    public static final String RECORD_ID_COL = "recordId";
    public static final String IMAGE_COL = "imageUrl";
    public static final String TITLE_COL = "price";
    public static final String DATE_COL = "date";
    public static final String SHORT_INFO_COL = "shortInfo";
    public static final String CONTENTS_COL = "contents";
    public static final String WEBPAGE_COL = "webpage";
    public static final String FAVOURITE_COL = "favourite";

    // Create SQL command
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IMAGE_COL + " VARCHAR(255), "
            + RECORD_ID_COL + " INTEGER, "
            + TITLE_COL + " VARCHAR(100), "
            + DATE_COL + " TEXT, "
            + SHORT_INFO_COL + " VARCHAR(255), "
            + CONTENTS_COL + " VARCHAR (1024), "
            + WEBPAGE_COL + " VARCHAR(100), "
            + FAVOURITE_COL + " INTEGER);";

    private static NewsDatabase instance;

    //Database fields
    private SQLiteDatabase database;
    private Cursor dbCursor;
    private String[] allColumns = { ID_COL,
            IMAGE_COL, RECORD_ID_COL, TITLE_COL, DATE_COL,
            SHORT_INFO_COL, CONTENTS_COL, WEBPAGE_COL,
            FAVOURITE_COL };

    public static NewsDatabase getInstance(Context context){
        if (instance == null)
            instance = new NewsDatabase(context);
        return instance;
    }

    public NewsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void open() throws SQLException {
        database = getWritableDatabase();
    }

    public void close() {
        if (database != null)
            database.close();
        database = null;
    }

    public long createArticle(Article article) {
        ContentValues values = new ContentValues();
        values.put(IMAGE_COL, article.getImageUrl());
        values.put(RECORD_ID_COL, article.getRecordId());
        values.put(TITLE_COL, article.getTitle());
        values.put(DATE_COL,article.getDate());
        values.put(SHORT_INFO_COL,article.getShortInfo());
        values.put(CONTENTS_COL,article.getContents());
        values.put(WEBPAGE_COL,article.getWebPage());
        values.put(FAVOURITE_COL,article.getFavourite());
        long insertId = database.insert(TABLE_NAME, null, values);
        return insertId;
    }

    public void updateArticle(Article article){
        ContentValues values = new ContentValues();
        values.put(CONTENTS_COL,article.getContents());
        values.put(WEBPAGE_COL,article.getWebPage());
        if (database != null){
            database.update(TABLE_NAME, values, "recordId="+article.getRecordId(), null);
        }
    }


    public Cursor getAllArticles() {
        dbCursor = database.query(TABLE_NAME,
                allColumns, null, null, null, null, null);
        return dbCursor;
    }

    public int findArticle(Article article){
        int articleRecordId = -1;
        dbCursor = database.query(TABLE_NAME,
                new String[]{RECORD_ID_COL}, RECORD_ID_COL + " = " + article.getRecordId(), null,
                null, null, RECORD_ID_COL);
        dbCursor.moveToFirst();
        if (dbCursor != null && dbCursor.getCount()>0){
            articleRecordId = article.getRecordId();
        }
        dbCursor.close();
        return articleRecordId;
    }

    public Boolean findArticle(int id){
        Boolean foundArticle = false;
        dbCursor = database.query(TABLE_NAME,
                allColumns, ID_COL + " = " + id, null,
                null, null, ID_COL);
        dbCursor.moveToFirst();
        if (dbCursor != null && dbCursor.getCount()>0){
            ContentValues values = new ContentValues();
            values.put(FAVOURITE_COL, 1);
            database.update(TABLE_NAME, values, "id="+id, null);

            dbCursor = database.query(TABLE_NAME,
                    allColumns, ID_COL + " = " + id, null,
                    null, null, ID_COL);
            dbCursor.moveToFirst();
            foundArticle = true;
        }

        return foundArticle;
    }

    public Cursor getArticlesSearch(String query){
        dbCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + TITLE_COL + " LIKE '%?%'", new String[]{query});
        return dbCursor;
    }

    public Cursor findArticleById(int id){
        dbCursor = database.query(TABLE_NAME,
                new String[]{ID_COL}, ID_COL + " = " + id, null,
                null, null, ID_COL);
        return dbCursor;
    }


    public Cursor getFavourites(){
        dbCursor = database.query(TABLE_NAME,
                allColumns,
                FAVOURITE_COL +" = " + 1,
                null, null, null, ID_COL);
        dbCursor.moveToFirst();
        return dbCursor;
    }
}


