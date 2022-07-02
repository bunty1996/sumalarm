package com.level_sense.app.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.level_sense.app.Fragment.models.ArticleModel;
import com.level_sense.app.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder>{
    Context context;
    ArrayList<ArticleModel> articleArrayList;
    private ArticleAdapter.ArticleClickedInterface click;
    String str="0";
    public TextView articleTitle,articleCreatedDate,articleDescrp,articleReadMore;
    public ImageView articleImg;

     public ArticleAdapter(Context context, ArrayList<ArticleModel> articleArrayList) {
        this.context = context;
        this.articleArrayList = articleArrayList;
    }

    @Override
    public ArticleAdapter.ArticleHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.article_row,parent,false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ArticleHolder articleHolder, int position) {
        ArticleModel articleModel = articleArrayList.get(position);
        articleTitle.setText(articleModel.getTitle());
        String startTime = articleModel.getCreated_at();//2019-12-12T00:28:42-06:00  2013-02-27 21:06:30 2018-12-05T10:37:43.937Z
        String[] separated = startTime.split("T");
      /*  separated[0]; // this will contain "2016-10-02"
        separated[1];*/ // this will contain "00:00:00.000Z"
        Log.e("onBindViewHolder: ", articleModel.getImgList() + "");
        Log.e("separated", separated[0] + "//" + separated[1]);

        Date initDate = null;
        try {
            initDate = new SimpleDateFormat("yyyy-MM-dd").parse(separated[0]);
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd,yyyy");
            String parsedDate = formatter.format(initDate);
            Log.e("parsedDate", parsedDate);
            articleCreatedDate.setText(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {

           articleDescrp.setText(articleModel.getBody_html());
//            Spanned spanned=Html.fromHtml(articleModel.body_html);
//            articleDescrp.setText(spanned);

        } catch (Exception ex) {

        }
//
//        if (articleModel.getSrc() != null && !articleModel.getSrc().isEmpty()) {
//            Picasso.get().load(articleModel.getSrc()).into(articleImg);
//        } else {
//            Picasso.get().load(R.drawable.dummy).into(articleImg);
//        }
 if(!articleModel.getImgList().isEmpty()) {
    Picasso.get().load(articleModel.getImgList().get(0)).into(articleImg);
     Log.e( "images ",articleModel.getImgList().get(0)+"" );
   }
 else {
     Picasso.get().load(R.drawable.dummy).into(articleImg);
 }

    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }




    public class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ArticleHolder(View itemView) {
            super(itemView);
            articleTitle=itemView.findViewById(R.id.articleTitle);
            articleCreatedDate=itemView.findViewById(R.id.articleCreatedDate);
            articleImg=itemView.findViewById(R.id.articleImg);
            articleDescrp=itemView.findViewById(R.id.articleDescrp);
            articleReadMore=itemView.findViewById(R.id.articleReadMore);
            articleTitle.setOnClickListener(this);
            articleReadMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.articleTitle:
                    str="1";
                    click.onArticleTextClicked(getAdapterPosition());
                    break;

                case R.id.articleReadMore:
                    str="1";
                    Log.e("ijoko",articleDescrp.getLineCount()+"");
                   // click.onArticleReadMoreClicked(getAdapterPosition());
                  /*  articleReadMore.setText(R.string.seeLess);
                    articleDescrp.setMaxLines(articleDescrp.getLineCount());*/
//                    if(articleDescrp.getMaxLines()==articleDescrp.getLineCount()){
//                        articleReadMore.setText(R.string.readMore);
//                        articleDescrp.setMaxLines(3);
//
//                    }else{
//                        articleReadMore.setText(R.string.seeLess);
//                        articleDescrp.setMaxLines(articleDescrp.getLineCount());
//                    }
                    click.onArticleReadMoreClicked(getAdapterPosition());
                    break;
            }
        }
    }

    public void onArticleClickListener(ArticleAdapter.ArticleClickedInterface click) {
        this.click = click;

    }

    public interface ArticleClickedInterface {
        void onArticleTextClicked(int position);
        void onArticleReadMoreClicked(int position);
    }

}
