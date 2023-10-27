package com.mobdeve.s15.taboo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

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
        builder.setMessage("Are you sure?").setPositiveButton("Yes", (dialog, id) ->
                        listener.onYes(ConfirmationDialog.this, getTag()))
                .setNegativeButton("Cancel", (dialog, id) ->
                        listener.onNo(ConfirmationDialog.this, getTag()));
        return builder.create();
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
