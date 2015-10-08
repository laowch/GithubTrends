package com.laowch.githubtrends.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.laowch.githubtrends.Constants;
import com.laowch.githubtrends.R;
import com.laowch.githubtrends.helper.OnStartDragListener;
import com.laowch.githubtrends.helper.SimpleItemTouchHelperCallback;
import com.laowch.githubtrends.utils.AnalyticsHelper;
import com.laowch.githubtrends.utils.IntentUtils;
import com.laowch.githubtrends.utils.LanguageHelper;

import java.util.Arrays;

/**
 * Created by lao on 15/9/28.
 */
public class CustomLanguagesActivity extends BaseActivity implements OnStartDragListener {


    final BroadcastReceiver languagesChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };


    private ItemTouchHelper mItemTouchHelper;

    CustomLanguageAdapter adapter;

    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_view);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        adapter = new CustomLanguageAdapter(getContext(), this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setItems(LanguageHelper.getInstance().getSelectedLanguages());

        switchMode(getIntent().getIntExtra("mode", Constants.MODE_DRAGGABLE));

        IntentFilter languageFilter = new IntentFilter(Constants.ACTION_SELECTED_LANGUAGES_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(languagesChangedReceiver, languageFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(languagesChangedReceiver);
    }

    public void updateView() {
        adapter.setItems(LanguageHelper.getInstance().getSelectedLanguages());
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_languages, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isRemoveMode = mode == Constants.MODE_REMOVE;

        menu.findItem(R.id.action_add).setVisible(!isRemoveMode);
        menu.findItem(R.id.action_remove).setVisible(!isRemoveMode);
        menu.findItem(R.id.action_done).setVisible(isRemoveMode);
        menu.findItem(R.id.action_save).setVisible(!isRemoveMode);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                AnalyticsHelper.sendEvent("CustomLangs", "Menu", "LangsAdd", 0l);
                IntentUtils.openAddLanguage(getContext());
                return true;
            case R.id.action_remove:
                AnalyticsHelper.sendEvent("CustomLangs", "Menu", "LangRemove", 0l);
                switchMode(Constants.MODE_REMOVE);
                return true;
            case R.id.action_done:
                AnalyticsHelper.sendEvent("CustomLangs", "Menu", "Done", 0l);
                switchMode(Constants.MODE_DRAGGABLE);
                return true;
            case R.id.action_save:
                AnalyticsHelper.sendEvent("CustomLangs", "Menu", "Save", 0l);
                LanguageHelper.getInstance().setSelectedLanguages(adapter.mItems);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Constants.ACTION_SELECTED_LANGUAGES_CHANGE));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchMode(int mode) {
        this.mode = mode;
        adapter.switchMode(mode);
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (Arrays.equals(adapter.mItems.toArray(), LanguageHelper.getInstance().getSelectedLanguages())) {
            super.onBackPressed();
        } else {
            ConfirmDialog confirmDialog = ConfirmDialog.newInstance("Confirm Exit", "Do you want to save your changes before exiting?", R.string.save, R.string.not_save);
            confirmDialog.setConfirmDialogListener(new ConfirmDialog.IConfirmDialogListener() {
                @Override
                public void onConfirm() {
                    AnalyticsHelper.sendEvent("CustomLangs", "ExitDialog", "Save", 0l);
                    LanguageHelper.getInstance().setSelectedLanguages(adapter.mItems);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Constants.ACTION_SELECTED_LANGUAGES_CHANGE));
                    finish();
                }
            });
            confirmDialog.setNegativeClickListener(new ConfirmDialog.INegativeClickListener() {
                @Override
                public void onNegativeButtonClick() {
                    AnalyticsHelper.sendEvent("CustomLangs", "ExitDialog", "GiveUp", 0l);
                    finish();
                }
            });
            confirmDialog.show(getSupportFragmentManager(), "confirm");
        }


    }
}
