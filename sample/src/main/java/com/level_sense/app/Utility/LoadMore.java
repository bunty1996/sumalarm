package com.level_sense.app.Utility;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

public class LoadMore {


    private Context mContext;
    private boolean loading;
    private RecyclerView mRecyclerView;
    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 2;
    private OnLoadMoreListener onLoadMoreListener;


    public LoadMore(RecyclerView rcv) {

        mRecyclerView = rcv;
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView
                    .getLayoutManager();
            mRecyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            Log.v("TAG", "last  " + (totalItemCount <= (lastVisibleItem + visibleThreshold)));


                            if (loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = false;
                            }
                        }
                    });
        }

    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoadingMore(boolean status) {
        loading = status;
    }


    public interface OnLoadMoreListener {

        void onLoadMore();
    }
}