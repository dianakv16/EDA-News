package uk.ac.kent.dkv3.eda_news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by dkv3 on 21/04/2017.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    private ArticlesModel model = ArticlesModel.getInstance();
    private Context context;
    private ArticleListFragment.OnArticleItemClickedListener listener;

    public ArticleListAdapter(Context ctx) {
        super();
        context = ctx;

        if (context instanceof ArticleListFragment.OnArticleItemClickedListener){
            listener = (ArticleListFragment.OnArticleItemClickedListener) context;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView date;
        private NetworkImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            photo = (NetworkImageView) itemView.findViewById(R.id.photo);

            //Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    if (listener != null){
                        listener.onArticleItemClicked(getAdapterPosition());
                    }
                }
            });
        }

        //Method that sets the data for each article on the list
        public void setData(Article article){
            title.setText(article.getTitle());
            date.setText(article.getDate());
            photo.setImageUrl(article.getImageUrl(), ArticlesApp.getInstance().getImageLoader());

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create the new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_article_item, parent, false);
        //Save view in the view holder
        ViewHolder vh = new ViewHolder(v);
        //Return view holder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article;
        article = model.getArticleList().get(position);

        //Populate the view
        holder.setData(article);
    }

    @Override
    public int getItemCount() {
        return model.getArticleList().size();
    }

}
