package com.laowch.githubtrends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.laowch.githubtrends.Constants;
import com.laowch.githubtrends.R;
import com.laowch.githubtrends.model.Language;
import com.laowch.githubtrends.utils.AnalyticsHelper;
import com.laowch.githubtrends.utils.LanguageHelper;

import java.util.List;

/**
 * Created by lao on 15/9/28.
 */
public class AddLanguagesActivity extends BaseActivity {

    AddLanguageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_view);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        adapter = new AddLanguageAdapter(getContext());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        adapter.setItems(LanguageHelper.getInstance().getUnselectedLanguages());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_language, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_done:
                AnalyticsHelper.sendEvent("AddLangs", "Menu", "Done", 0l);

                List<Language> selectedItems = adapter.getSelectedItems();
                if (selectedItems.size() > 0) {
                    LanguageHelper.getInstance().addSelectedLanguages(selectedItems);
                }

                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Constants.ACTION_SELECTED_LANGUAGES_CHANGE));

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final List<Language> selectedItems = adapter.getSelectedItems();
        if (selectedItems.size() > 0) {
            ConfirmDialog confirmDialog = ConfirmDialog.newInstance("Confirm Exit", "Do you want to save your changes before exiting?", R.string.save, R.string.not_save);
            confirmDialog.setConfirmDialogListener(new ConfirmDialog.IConfirmDialogListener() {
                @Override
                public void onConfirm() {
                    AnalyticsHelper.sendEvent("AddLangs", "ExitDialog", "Save", 0l);
                    LanguageHelper.getInstance().addSelectedLanguages(selectedItems);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Constants.ACTION_SELECTED_LANGUAGES_CHANGE));
                    finish();
                }
            });
            confirmDialog.setNegativeClickListener(new ConfirmDialog.INegativeClickListener() {
                @Override
                public void onNegativeButtonClick() {
                    AnalyticsHelper.sendEvent("AddLangs", "ExitDialog", "GiveUp", 0l);
                    finish();
                }
            });
            confirmDialog.show(getSupportFragmentManager(), "confirm");
        } else {
            super.onBackPressed();
        }
    }
}
