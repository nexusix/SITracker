package com.andrada.sitracker.task.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.andrada.sitracker.contracts.AuthorUpdateProgressListener;

import org.androidannotations.annotations.EReceiver;

/**
 * Created by ggodonoga on 06/06/13.
 */
@EReceiver
public class UpdateBroadcastReceiver extends BroadcastReceiver{

    public static final String UPDATE_RECEIVER_ACTION = "com.andrada.sitracker.updated";

    private AuthorUpdateProgressListener mListener = null;

    public UpdateBroadcastReceiver(AuthorUpdateProgressListener listener) {
        mListener = listener;
    }

    public UpdateBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //See if there is something we can notify
        if (mListener != null) {
            mListener.onAuthorsUpdated();
        }
    }
}
