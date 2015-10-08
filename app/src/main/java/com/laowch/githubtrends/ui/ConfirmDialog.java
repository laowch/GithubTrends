package com.laowch.githubtrends.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by laowch on 4/15/14.
 */
public class ConfirmDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private final static String EXTRA_TITLE = "extra_title";

    private final static String EXTRA_MESSAGE = "extra_message";

    private final static String EXTRA_POSITIVE_TEXT = "extra_positive_text";

    private final static String EXTRA_NEGATIVE_TEXT = "extra_negative_text";

    public final static int HIDE_BUTTON = -1;

    private IConfirmDialogListener confirmDialogListener;

    private INegativeClickListener negativeClickListener;

    public static ConfirmDialog newInstance(final String pTitle, final String pMessage) {
        return newInstance(pTitle, pMessage, 0, 0);
    }

    public static ConfirmDialog newInstance(final String pTitle, final String pMessage, final int positiveText, final int negativeText) {
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TITLE, pTitle);
        bundle.putString(EXTRA_MESSAGE, pMessage);
        bundle.putInt(EXTRA_POSITIVE_TEXT, positiveText);
        bundle.putInt(EXTRA_NEGATIVE_TEXT, negativeText);
        dialog.setArguments(bundle);

        return dialog;
    }


    public ConfirmDialog() {
    }

    public void setConfirmDialogListener(IConfirmDialogListener pConfirmDialogListener) {
        this.confirmDialogListener = pConfirmDialogListener;
    }

    public void setNegativeClickListener(INegativeClickListener negativeClickListener) {
        this.negativeClickListener = negativeClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString(EXTRA_TITLE));
        builder.setMessage(getArguments().getString(EXTRA_MESSAGE));

        final int negativeText = getArguments().getInt(EXTRA_NEGATIVE_TEXT, 0);
        final int positiveText = getArguments().getInt(EXTRA_POSITIVE_TEXT, 0);

        if (positiveText != HIDE_BUTTON) {
            builder.setPositiveButton(positiveText == 0 ? android.R.string.ok : positiveText, this);
        }

        if (negativeText != HIDE_BUTTON) {
            builder.setNegativeButton(negativeText == 0 ? android.R.string.cancel : negativeText, this);
        }

        return builder.create();
    }

    @Override
    public void onClick(final DialogInterface pDialog, final int pWhich) {
        switch (pWhich) {
            case DialogInterface.BUTTON_NEGATIVE:
                if (this.negativeClickListener != null) {
                    this.negativeClickListener.onNegativeButtonClick();
                }

                break;
            case DialogInterface.BUTTON_POSITIVE:
                if (this.confirmDialogListener != null) {
                    this.confirmDialogListener.onConfirm();
                }
                break;
            default:
                break;
        }

    }

    public static interface IConfirmDialogListener {
        public void onConfirm();
    }

    public static interface INegativeClickListener {
        public void onNegativeButtonClick();
    }
}
