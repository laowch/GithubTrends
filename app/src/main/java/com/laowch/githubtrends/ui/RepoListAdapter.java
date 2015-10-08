package com.laowch.githubtrends.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laowch.githubtrends.R;
import com.laowch.githubtrends.model.Language;
import com.laowch.githubtrends.model.Repo;
import com.laowch.githubtrends.utils.AsyncImageView;
import com.laowch.githubtrends.utils.FavoReposHelper;
import com.laowch.githubtrends.utils.IntentUtils;
import com.laowch.githubtrends.utils.LanguageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lao on 15/9/23.
 */
public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.ViewHolder> {


    private List<Repo> mItems = new ArrayList<>();

    Context mContext;

    Language language;

    public RepoListAdapter(Context context, Language language) {
        this.mContext = context;
        this.language = language;
    }

    public void setItems(Repo[] repos) {
        mItems = Arrays.asList(repos);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Repo item = mItems.get(position);

        viewHolder.repo = item;

        viewHolder.title.setText(item.owner + " / " + item.name);
        if (TextUtils.isEmpty(item.description)) {
            viewHolder.description.setVisibility(View.GONE);
        } else {
            viewHolder.description.setText(item.description);
            viewHolder.description.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(item.meta)) {
            viewHolder.meta.setVisibility(View.GONE);
        } else {
            viewHolder.meta.setText(item.meta);
            viewHolder.meta.setVisibility(View.VISIBLE);
        }

        if (FavoReposHelper.getInstance().contains(item)) {
            viewHolder.starImage.setImageResource(R.drawable.ic_star_checked);
        } else {
            viewHolder.starImage.setImageResource(R.drawable.ic_star_unchecked);
        }

        if (TextUtils.isEmpty(language.path)) {
            // only show label in Tab 『All Language』
            if (TextUtils.isEmpty(item.language)) {
                viewHolder.label.setText("N/A");
            } else {
                Language language = LanguageHelper.getInstance().getLanguageByName(item.language);
                if (language == null) {
                    viewHolder.label.setText(item.language);
                } else {
                    viewHolder.label.setText(language.getShortName());
                }
            }
            viewHolder.label.setVisibility(View.VISIBLE);
        } else {
            viewHolder.label.setVisibility(View.GONE);
        }


        for (int i = 0; i < viewHolder.avatars.size(); i++) {
            if (i < item.contributors.size()) {
                viewHolder.avatars.get(i).loadImage(item.contributors.get(i).avatar);
                viewHolder.avatars.get(i).setVisibility(View.VISIBLE);
            } else {
                viewHolder.avatars.get(i).setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Repo repo;

        private final TextView title;
        private final TextView description;
        private final TextView meta;
        private final ImageView starImage;
        private final TextView label;
        private List<AsyncImageView> avatars;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            meta = (TextView) itemView.findViewById(R.id.meta);
            starImage = (ImageView) itemView.findViewById(R.id.star);
            label = (TextView) itemView.findViewById(R.id.label);
            avatars = new ArrayList<>();
            avatars.add((AsyncImageView) itemView.findViewById(R.id.avatar1));
            avatars.add((AsyncImageView) itemView.findViewById(R.id.avatar2));
            avatars.add((AsyncImageView) itemView.findViewById(R.id.avatar3));
            avatars.add((AsyncImageView) itemView.findViewById(R.id.avatar4));
            avatars.add((AsyncImageView) itemView.findViewById(R.id.avatar5));

            itemView.findViewById(R.id.repo_card).setOnClickListener(this);
            itemView.findViewById(R.id.contributors_line).setOnClickListener(this);
            starImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.repo_card:
                    IntentUtils.openExternalUrl(mContext, repo.url);
                    break;
                case R.id.contributors_line:
                    String contributorUrl;
                    if (repo.url.endsWith("/")) {
                        contributorUrl = repo.url + "graphs/contributors";
                    } else {
                        contributorUrl = repo.url + "/graphs/contributors";
                    }

                    IntentUtils.openExternalUrl(mContext, contributorUrl);
                    break;
                case R.id.star:
                    if (FavoReposHelper.getInstance().contains(repo)) {
                        FavoReposHelper.getInstance().removeFavo(repo);
                        starImage.setImageResource(R.drawable.ic_star_unchecked);
                    } else {
                        FavoReposHelper.getInstance().addFavo(repo);
                        starImage.setImageResource(R.drawable.ic_star_checked);
                    }
                    break;
            }
        }
    }
}
