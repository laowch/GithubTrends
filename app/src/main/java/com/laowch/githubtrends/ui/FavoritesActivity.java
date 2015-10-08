package com.laowch.githubtrends.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.laowch.githubtrends.R;
import com.laowch.githubtrends.model.Language;
import com.laowch.githubtrends.utils.FavoReposHelper;

/**
 * Created by lao on 15/9/25.
 */
public class FavoritesActivity extends BaseActivity {

    RepoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_view);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        adapter = new RepoListAdapter(getContext(), new Language("favos", "favos_path"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setItems(FavoReposHelper.getInstance().getFavos());

    }


}
