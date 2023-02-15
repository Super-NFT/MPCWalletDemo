package com.go23wallet.mpcwalletdemo.view;

import com.go23wallet.mpcwalletdemo.utils.Constant;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

public abstract class LoadMoreListener extends RecyclerView.OnScrollListener {

    //声明一个LinearLayoutManager
    private LayoutManager mLayoutManager;

    //已经加载出来的Item的数量
    private int totalItemCount;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的最后一个
    private int lastVisibleItem;

    private static final int DEFAULT_PRELOAD_NUMBER = 5;

    //是否正在上拉数据
    private boolean loading = true;

    private boolean isEnd = false;

    public LoadMoreListener(LayoutManager linearLayoutManager) {
        this.mLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItem = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        if (visibleItemCount > 0
                && !loading
                && lastVisibleItem >= totalItemCount - DEFAULT_PRELOAD_NUMBER//最后一个item可见
                && totalItemCount % Constant.PAGE_SIZE == 0
                && !isEnd) {//数据不足一屏幕不触发加载更多
            onLoadMore();
            loading = true;
        }
    }

    public void setIsEnd(boolean isEnd) {
        loading = false;
        this.isEnd = isEnd;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     */
    public abstract void onLoadMore();

}