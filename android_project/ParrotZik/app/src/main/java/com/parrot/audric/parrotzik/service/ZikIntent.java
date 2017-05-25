package com.parrot.audric.parrotzik.service;

import android.content.IntentFilter;

public enum ZikIntent {
    ACTION_ZIK_CONNECTED,
    ACTION_ZIK_DISCONNECTED,
    ACTION_ZIK_STATE_REFRESH;


    public static IntentFilter getGlobalFilter() {
        IntentFilter filter = new IntentFilter();
        for (ZikIntent intent : ZikIntent.values() ) {
            filter.addAction(intent.toString());
        }
        return filter;
    }
}
