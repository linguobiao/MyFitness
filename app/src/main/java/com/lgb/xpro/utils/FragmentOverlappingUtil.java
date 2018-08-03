package com.lgb.xpro.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by zhao_king on 2017/4/12
 * fragment 重叠的最好解决方法.
 */

public class FragmentOverlappingUtil {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    public static void createOverlappingUtil(Fragment fragment, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(fragment);
            } else {
                ft.show(fragment);
            } ft.commit();
        }
    }

    public static void saveOverlappingUtil(Fragment fragment, Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_SAVE_IS_HIDDEN, fragment.isHidden());
    }
}
