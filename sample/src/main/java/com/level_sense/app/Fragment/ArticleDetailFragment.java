package com.level_sense.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.level_sense.app.Auth.DashBoardActivity;
import com.level_sense.app.Fragment.models.ArticleModel;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailFragment extends Fragment implements Html.ImageGetter {
    @BindView(R.id.custom_pb)
    View custom_pb;
    @BindView(R.id.articleTitle)
    TextView articleTitle;
    @BindView(R.id.articleCreatedDate)
    TextView articleCreatedDate;
    @BindView(R.id.articleDescrp)
    TextView articleDescrp;
    @BindView(R.id.articleImg)
    ImageView articleImg;
    String article_id;
    int imageNumber = 1;
    Drawable empty;
    ArrayList<ArticleModel> articleArrayList = new ArrayList<>();
    Fragment fragment;


    //TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ArticleDetailFragment newInstance() {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
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
        article_id = getArguments().getString("article_id");
        rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        ButterKnife.bind(this, rootView);
        callApiGetArticleList(article_id);
        DashBoardActivity.setDrawerState(false);
        getActivity().setTitle(R.string.article_details);
        DashBoardActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment = ArticleFragment.newInstance();
                setFragment(fragment);
            }
        });
        return rootView;
    }

    public void setFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

    private void callApiGetArticleList(final String article_id) {
        BaseRequest baseRequest = new BaseRequest(getActivity(), ArticleDetailFragment.this);
        baseRequest.setLoaderView(custom_pb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                articleArrayList.clear();

                Log.e("article2", requestCode + fullResponse + dataObject);
                try {
                    JSONObject object = new JSONObject(fullResponse);
                    Log.e("object", object + "");
                    JSONArray array = object.optJSONArray("articles");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.optJSONObject(i);
                        articleTitle.setText(data.optString("title"));
                        articleCreatedDate.setText(data.optString("created_at"));
                        //  Spanned spanned = Html.fromHtml(data.optString("body_html"),ArticleDetailFragment.this::getDrawable , null);
                        Spanned spanned = Html.fromHtml(data.optString("body_html"));
                        articleDescrp.setText(spanned.toString().trim());
//                        articleDescrp.setText(Html.fromHtml(data.optString("body_html")));

                        if (data.has("image")) {
                            try {
                                JSONObject imageData = data.optJSONObject("image");

                                String image = imageData.optString("src");
                                //   Picasso.with(getActivity()).load(image).into(articleImg);
                                //  Picasso.get().load(image).into(articleImg);

                            } catch (Exception ex) {

                            }
                            JSONArray jsonArray = data.optJSONArray("imageList");
                            ArrayList<String> picList = new ArrayList<>();
                            picList.clear();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                picList.add(String.valueOf(jsonArray.get(j)));
                                Picasso.get().load(jsonArray.get(0).toString()).into(articleImg);
                            }
                            if (picList.isEmpty()) {
                                Picasso.get().load(R.drawable.dummy).into(articleImg);
                            } else {
                                Picasso.get().load(picList.get(0)).into(articleImg);
                            }
                        }


                        String startTime = data.optString("created_at");//2019-12-12T00:28:42-06:00  2013-02-27 21:06:30 2018-12-05T10:37:43.937Z
                        String[] separated = startTime.split("T");
      /*  separated[0]; // this will contain "2016-10-02"
        separated[1];*/ // this will contain "00:00:00.000Z"

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
                    }

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

        baseRequest.callArticleAPIGET(1, new HashMap<String, String>(), getResources().getString(R.string.api_shopify_articles) + "?articleId=" + article_id);

    }

    @Override
    public Drawable getDrawable(String s) {
        LevelListDrawable d = new LevelListDrawable();
        empty = getResources().getDrawable(R.drawable.dummy1);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        new LoadImage().execute("https:" + s, d);
        return d;
    }


    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d("TAG", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                //mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                mDrawable.setLevel(1);
                CharSequence t = articleDescrp.getText();
                articleDescrp.setText(t);
            }
        }
    }
}
