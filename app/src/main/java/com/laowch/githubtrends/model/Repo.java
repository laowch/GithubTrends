package com.laowch.githubtrends.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lao on 15/9/23.
 */
public class Repo {

    public String name;

    public String owner;

    @SerializedName("link")
    public String url;

    @SerializedName("des")
    public String description;
    public String meta;

    public String language;

    public List<User> contributors;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Repo) {
            return name.equals(((Repo) o).name) && owner.equals(((Repo) o).owner);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + owner.hashCode();
    }
}
