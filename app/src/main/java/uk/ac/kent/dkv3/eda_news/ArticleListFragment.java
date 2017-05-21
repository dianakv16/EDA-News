package uk.ac.kent.dkv3.eda_news;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by dkv3 on 21/04/2017.
 */

public class ArticleListFragment extends Fragment implements ArticlesModel.OnListUpdateListener{

    private RecyclerView articleListView;
    private LinearLayoutManager layoutManager;
    private ArticleListAdapter adapter;

    private OnArticleItemClickedListener mListener;

    public ArticleListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);


        //Wire recycler view
        articleListView = (RecyclerView) view.findViewById(R.id.article_list_view);

        //Setup layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //Attach to recycler view
        articleListView.setLayoutManager(layoutManager);

        //Setup adapter
        adapter = new ArticleListAdapter(getActivity());

        //Attach to recycler view
        articleListView.setAdapter(adapter);




        //Connect to the model
        ArticlesModel model = ArticlesModel.getInstance();
        model.setOnListUpdateListener(this);
        if (getActivity().getLocalClassName().equals("ArticleListActivity")) {
            model.setFavouriteValue(false);
        } else if (getActivity().getLocalClassName().equals("FavouriteListActivity")){
            model.setFavouriteValue(true);
        }


        if (getActivity().getLocalClassName().equals("ArticleListActivity")) {
            model.loadData(getActivity());
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArticleItemClickedListener) {
            mListener = (OnArticleItemClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnArticleItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListUpdate(ArrayList<Article> articleList) {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }


    //Method that get the Intent of the search box sent by the Article List Activity
    public void search(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            ArticlesModel model = ArticlesModel.getInstance();
            //ArrayList<Article> articleList = model.getArticlesSearch(query);
            //onListUpdate(articleList);

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnArticleItemClickedListener {
        // Called when a article item from the list is clicked
        void onArticleItemClicked(int position);
    }

}
