package com.laowch.githubtrends.utils;

import android.app.Application;
import android.content.Context;

import com.laowch.githubtrends.MyApplication;
import com.laowch.githubtrends.model.Repo;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by lao on 15/9/25.
 */
public class FavoReposHelper {


    static FavoReposHelper instance;

    public static synchronized FavoReposHelper getInstance() {
        return instance;
    }


    public static void init(Application application) {
        instance = new FavoReposHelper(application);
    }

    LinkedHashSet<Repo> reposSet = new LinkedHashSet<>();

    Context context;

    private FavoReposHelper(Context context) {
        this.context = context;
        String favoReposJson = PreferenceManager.getString(context, "favo_repos", null);
        Repo[] favoReops = MyApplication.getGson().fromJson(favoReposJson, Repo[].class);
        if (favoReops != null) {
            reposSet.addAll(Arrays.asList(favoReops));
        }
    }

    public boolean contains(Repo repo) {
        return reposSet.contains(repo);
    }

    public Repo[] getFavos() {
        return reposSet.toArray(new Repo[0]);
    }

    public void addFavo(Repo repo) {
        reposSet.add(repo);

        saveToPref();
    }

    public void removeFavo(Repo repo) {
        reposSet.remove(repo);

        saveToPref();
    }

    private void saveToPref() {
        Repo[] repos = reposSet.toArray(new Repo[0]);
        String reposJson = MyApplication.getGson().toJson(repos);
        PreferenceManager.putString(context, "favo_repos", reposJson);
    }
}
