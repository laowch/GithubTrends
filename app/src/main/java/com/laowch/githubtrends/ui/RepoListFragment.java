package com.laowch.githubtrends.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.laowch.githubtrends.Constants;
import com.laowch.githubtrends.R;
import com.laowch.githubtrends.model.Language;
import com.laowch.githubtrends.model.Repo;
import com.laowch.githubtrends.request.GsonRequest;
import com.laowch.githubtrends.utils.AttrsHelper;

/**
 * Created by lao on 15/9/23.
 */
public class RepoListFragment extends Fragment {

    final static String TAG = "RepoListFragment";

    public static Fragment newInstance(Context context, Language language, String timeSpan) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("extra_language", language);
        bundle.putSerializable("extra_time_span", timeSpan);
        return Fragment.instantiate(context, RepoListFragment.class.getName(), bundle);
    }


    Language language;

    String timeSpan;

    SwipeRefreshLayout swipeRefreshLayout;

    RepoListAdapter adapter;

    RequestQueue mRequestQueue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language = (Language) getArguments().getSerializable("extra_language");
        timeSpan = (String) getArguments().getSerializable("extra_time_span");
        mRequestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        executeGetRepos();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(AttrsHelper.getColor(getContext(), R.attr.colorPrimary), AttrsHelper.getColor(getContext(), R.attr.colorPrimaryLight));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                executeGetRepos();
            }
        });

        adapter = new RepoListAdapter(getContext(), language);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    public void updateTimeSpan(String timeSpan) {
        this.timeSpan = timeSpan;
        executeGetRepos();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    private void executeGetRepos() {
        // network request
        String url = Constants.URL + language.path + "_" + timeSpan;

        GsonRequest<Repo[]> request = new GsonRequest(url, Repo[].class, new Response.Listener<Repo[]>() {
            @Override
            public void onResponse(Repo[] response) {
                adapter.setItems(response);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO process error
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        request.setTag(TAG);
        mRequestQueue.add(request);
    }
}
