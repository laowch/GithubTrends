package com.laowch.githubtrends.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.laowch.githubtrends.R;
import com.laowch.githubtrends.model.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lao on 15/9/28.
 */
public class AddLanguageAdapter extends RecyclerView.Adapter<AddLanguageAdapter.ViewHolder> {

    Context context;

    List<Language> mItems = new ArrayList<>();

    LinkedList<Language> selectedItems = new LinkedList<>();

    public AddLanguageAdapter(Context context) {
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_language_grid_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final Language language = mItems.get(position);
        viewHolder.text.setText(language.name);

        viewHolder.checkBox.setOnCheckedChangeListener(null);

        viewHolder.checkBox.setChecked(selectedItems.contains(language));

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedItems.add(language);
                } else {
                    selectedItems.remove(language);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(Language[] items) {
        mItems.clear();
        mItems.addAll(Arrays.asList(items));
        notifyDataSetChanged();
    }

    public List<Language> getSelectedItems() {
        return selectedItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        private CheckBox checkBox;

        public ViewHolder(final View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.toggle();
                }
            });
        }

    }
}
