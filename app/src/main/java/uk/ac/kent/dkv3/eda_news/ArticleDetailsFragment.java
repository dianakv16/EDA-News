package uk.ac.kent.dkv3.eda_news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by dkv3 on 21/04/2017.
 */

public class ArticleDetailsFragment extends Fragment {

    private NetworkImageView photo;
    private TextView title;
    private TextView date;
    private TextView contents;

    public ArticleDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);

        title = (TextView) view.findViewById(R.id.title);
        date = (TextView) view.findViewById(R.id.date);
        photo = (NetworkImageView) view.findViewById(R.id.photo);
        contents = (TextView) view.findViewById(R.id.contents);
        return view;

    }

    //Method that updates the details of the article in the view
    public void updateDetails(int id){
        Article article = new Article();
        int size = ArticlesModel.getInstance().getArticleList().size();

        if (size > 0) {
            article = ArticlesModel.getInstance().getArticleList().get(id);
        }

        title.setText(article.getTitle());
        date.setText(article.getDate());
        photo.setImageUrl(article.getImageUrl(), ArticlesApp.getInstance().getImageLoader());
        contents.setText(article.getContents());


    }

}
