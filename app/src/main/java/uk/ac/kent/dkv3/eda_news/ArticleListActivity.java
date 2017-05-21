package uk.ac.kent.dkv3.eda_news;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;


public class ArticleListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ArticleListFragment.OnArticleItemClickedListener {

    boolean hasTwoPanes;

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
            Intent intent = new Intent(this, ArticleDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
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
        fragment.search(getIntent());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_list, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        final Intent intent = new Intent(this, ArticleListActivity.class);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //Get the value of the search box and set it as an extra in the Intent
            //to be sent to the activity itself
            public boolean onQueryTextChange(String query) {
                intent.putExtra("ACTION_SEARCH", query);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
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
}
