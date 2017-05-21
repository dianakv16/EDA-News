package uk.ac.kent.dkv3.eda_news;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by dkv3 on 23/04/2017.
 */

public class FavouriteListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ArticleListFragment.OnArticleItemClickedListener {

    boolean hasTwoPanes;
    boolean favourites = true;

    @Override
    public void onArticleItemClicked(int position) {

        if (hasTwoPanes) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ArticleDetailsFragment fragment = (ArticleDetailsFragment) fragmentManager
                    .findFragmentById(R.id.details_fragment);
            fragment.updateDetails(position);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("ITEM_ID", position);
            Intent intent = new Intent(this, FavouriteDetailsActivity.class);
            intent.putExtras(bundle);
            //Starting the Favourite Details Activity to show the details of
            //a clicked article
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_article_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(R.id.details_fragment) == null){
            hasTwoPanes = false;
        } else {
            hasTwoPanes = true;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        ArticleListFragment fragment = (ArticleListFragment) fragmentManager
                .findFragmentById(R.id.list_fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_list, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home){
            Intent intent = new Intent(this, ArticleListActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_fav) {
            Intent intent = new Intent(this, FavouriteListActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_eda) {
            Intent intent = new Intent(this, EdaActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

