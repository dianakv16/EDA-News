package uk.ac.kent.dkv3.eda_news;

/**
 * Created by dkv3 on 21/04/2017.
 */

public class Article {

    private String imageUrl;
    private int recordId;
    private String title;
    private String date;
    private String shortInfo;
    private String contents;
    private String webPage;
    private Boolean favourite;



    public Article(String imageUrl, int recordId, String title, String date, String shortInfo,
                   String contents, String webPage, Boolean favourite) {
        this.imageUrl = imageUrl;
        this.recordId = recordId;
        this.title = title;
        this.date = date;
        this.shortInfo = shortInfo;
        this.contents = contents;
        this.webPage = webPage;
        this.favourite = favourite;

    }

    public Article(){
        this.imageUrl = "";
        this.recordId = 0;
        this.title = "";
        this.date = "";
        this.shortInfo = "";
        this.contents = "";
        this.webPage = "";
        this.favourite = false;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortInfo() {
        return shortInfo;
    }

    public void setShortInfo(String shortInfo) {
        this.shortInfo = shortInfo;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }



}
