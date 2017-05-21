package uk.ac.kent.dkv3.eda_news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by dkv3 on 23/04/2017.
 */

public class FavouriteDetailsActivity extends AppCompatActivity {

    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_article_details);
        Bundle bundle = getIntent().getExtras();
        //Item that was selected in the article list
        this.itemId = bundle.getInt("ITEM_ID");
        //Get the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        ArticleDetailsFragment fragment = (ArticleDetailsFragment) fragmentManager
                .findFragmentById(R.id.details_fragment);
        //Calling the method of the fragment that updates the details of the article in the view
        fragment.updateDetails(itemId);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Action bar item clicks are handled here

        //Id of toolbar item
        int id = item.getItemId();
        //If the user clicks on the favourites icon
        if (id == R.id.favourite) {
            Boolean savedFavourite = false;
            String text = "";
            //Setting the article as a favourite
            savedFavourite = ArticlesModel.getInstance().setFavourite(itemId);

            if (savedFavourite){
                text = "Article saved to favourites";
            } else {
                text = "Please try again";
            }
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();

            return true;
            //If the user clicks on the share icon
        } else if (id == R.id.share){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            Article article;
            article = ArticlesModel.getInstance().getArticleList().get(itemId);
            String shareBodyText = "Check this article from EDA News! " + article.getWebPage();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Check this article from EDA News!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Share"));
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}

