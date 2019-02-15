package com.example.newsfeed;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.newsfeed.model.Story;
import com.example.newsfeed.utilities.NetworkUtils;

import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>>, SwipeRefreshLayout.OnRefreshListener {

    private NewsAdapter mNewsAdapter;
    private static final int NEWS_ID = 0 ;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView messageTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageTv = findViewById(R.id.message_tv);
        mRecyclerView = findViewById(R.id.news_rv);
        mNewsAdapter = new NewsAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mNewsAdapter);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        getSupportLoaderManager().initLoader(NEWS_ID, null, this).forceLoad();
    }

    @NonNull
    @Override
    public Loader<List<Story>> onCreateLoader(int i, @Nullable Bundle bundle) {
        hideMessage();
        return new StoriesLoader(this, NetworkUtils.getApiUrl());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Story>> loader, List<Story> stories) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (stories == null) {
            showMessage(getString(R.string.error));
        } else if (stories.isEmpty()) {
            showMessage(getString(R.string.no_data));
        } else {
            hideMessage();
            mNewsAdapter.setStories(stories);
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<List<Story>> loader) {
    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(NEWS_ID, null, this).forceLoad();
    }
    private void showMessage(String msg){
        messageTv.setText(msg);
        messageTv.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
    private void hideMessage(){
        messageTv.setVisibility(View.GONE);
    }
}
