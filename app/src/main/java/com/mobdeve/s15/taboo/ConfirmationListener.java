package com.mobdeve.s15.taboo;

import androidx.fragment.app.DialogFragment;

public interface ConfirmationListener {
    //Use this interface for those yes/no dialogs across the app.
    public void onYes(DialogFragment dialog, String tag);
    public void onNo(DialogFragment dialog, String tag);
}
