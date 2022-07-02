package com.level_sense.app.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.level_sense.app.Auth.DashBoardActivity;
import com.level_sense.app.Fragment.models.ArticleModel;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.adapter.ArticleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleFragment extends Fragment  {
    @BindView(R.id.recycleViewArticle)
    RecyclerView recycleViewArticle;
    @BindView(R.id.custom_pb)
    View custom_pb;
    ArrayList<ArticleModel> articleArrayList= new ArrayList<>();
    ArticleAdapter articleAdapter ;

    //TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ArticleFragment newInstance() {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView=inflater.inflate(R.layout.fragment_article, container, false);
        ButterKnife.bind(this, rootView);
       DashBoardActivity.setDrawerState(true);
        getActivity().setTitle(R.string.article);
        articleArrayList.clear();
        callApiGetArticleList();
        recycleViewArticle.setLayoutManager(new LinearLayoutManager(getActivity()));
        articleAdapter= new ArticleAdapter(getActivity(),articleArrayList);
        recycleViewArticle.setAdapter(articleAdapter);

        articleAdapter.onArticleClickListener(new ArticleAdapter.ArticleClickedInterface() {
            @Override
            public void onArticleTextClicked(int position) {
                //callApiGetArticleList(articleArrayList.get(position).getId());

                ArticleDetailFragment ldf = new ArticleDetailFragment();
                Bundle args = new Bundle();
                args.putString("article_id",articleArrayList.get(position).getId());
                ldf.setArguments(args);
                ((DashBoardActivity) getActivity()).setFragment(ldf);

//Inflate the fragment
               // getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();
//            new DashBoardActivity().setFragment(ldf);
            }

            @Override
            public void onArticleReadMoreClicked(int position) {
                //callApiGetArticleList(articleArrayList.get(position).getId());
                ArticleDetailFragment ldf = new ArticleDetailFragment();
                Bundle args = new Bundle();
                args.putString("article_id",articleArrayList.get(position).getId());
                ldf.setArguments(args);
                ((DashBoardActivity) getActivity()).setFragment(ldf);
            }
        });

        return rootView;
    }

    private void callApiGetArticleList() {
        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setLoaderView(custom_pb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                Log.e("article",requestCode+fullResponse+dataObject);
                try {
                    JSONObject object = new JSONObject(fullResponse);
                    Log.e("object",object+"");
                    JSONArray array = object.optJSONArray("articles");
                    for (int i=0;i<array.length();i++) {
                        JSONObject data = array.optJSONObject(i);
                        ArticleModel articleModel = new ArticleModel();
                        articleModel.setId(data.optString("id"));
                        articleModel.setTitle(data.optString("title"));
                        articleModel.setCreated_at(data.optString("created_at"));
                        Spanned spanned=Html.fromHtml(data.optString("body_html"));
                        articleModel.setBody_html(spanned.toString());
                        articleModel.setStr("1");

//                        JSONObject imageData = data.optJSONObject("image");
//                        articleModel.setSrc(imageData.optString("src"));

                        JSONArray jsonArray = data.optJSONArray("imageList");
                        ArrayList<String> picList = new ArrayList<>();
                        picList.clear();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            picList.add(String.valueOf(jsonArray.get(j)));
                        }
                        articleModel.setImgList(picList);
                        articleArrayList.add(articleModel);
                    }
                    articleAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        baseRequest.callArticleAPIGET(1, new HashMap<String, String>(), getResources().getString(R.string.api_shopify_articles));

    }


}
