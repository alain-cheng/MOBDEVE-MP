package com.mobdeve.s15.taboo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialog extends DialogFragment {
    ConfirmationListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>Are you sure?</font>"))
                .setPositiveButton("Yes", (dialog, id) ->
                        listener.onYes(ConfirmationDialog.this, getTag()))
                .setNegativeButton("Cancel", (dialog, id) ->
                        listener.onNo(ConfirmationDialog.this, getTag()));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button cancel = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button yes = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        yes.setBackgroundColor(getResources().getColor(R.color.background));
        cancel.setBackgroundColor(getResources().getColor(R.color.background));
        yes.setTextColor(getResources().getColor(R.color.white));
        cancel.setTextColor(getResources().getColor(R.color.white));

        return  alertDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity().toString()
                    + " must implement ConfirmationListener");
        }
    }
}
