package com.lgb.myfitness.module.bpm;

import android.support.annotation.NonNull;

import com.lgb.mvp.AppActivity;
import com.lgb.myfitness.R;
import com.lgb.myfitness.bpm.main.BPMFragment;
import com.lgb.myfitness.bpm.main.BPMSettingsFragment;
import com.lgb.myfitness.bpm.main.BPMStatisticsDetailFragment;
import com.lgb.myfitness.bpm.main.BPMStatisticsFragment;
import com.lgb.myfitness.bpm.main.BPMTestFragment;
import com.lgb.myfitness.bpm.settings.BPMSettingsLanguageFragment;
import com.lgb.myfitness.bpm.settings.BPMSettingsProfileFragment;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsAboutUsFragment;
import com.lgb.xpro.utils.XFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentManager {

    //单例
    private static class Instance {public static final FragmentManager instance = new FragmentManager();}
    public static FragmentManager getInstance() {return FragmentManager.Instance.instance;}

    private XFragment xFragment;
    private AppActivity appActivity;

    public void init(AppActivity appActivity, int layoutRes) {

        List<XFragment.XFragmentBean> tagList = new ArrayList<>();
        // 一级界面
        XFragment.XFragmentBean bean1 = new XFragment.XFragmentBean(BPMFragment.class);
        bean1.add(BPMTestFragment.class);
        bean1.add(BPMStatisticsFragment.class);
        bean1.add(BPMStatisticsDetailFragment.class);
        XFragment.XFragmentBean bean2 = new XFragment.XFragmentBean(BPMSettingsFragment.class);
        bean2.add(BPMSettingsProfileFragment.class);
        bean2.add(BPMSettingsLanguageFragment.class);
        bean2.add(BPMSettingsAboutUsFragment.class);

        tagList.add(bean1);
        tagList.add(bean2);
        xFragment = new XFragment(appActivity.getFragmentManager(), layoutRes, tagList);
    }

    public void showFragment(@NonNull Class cls) {xFragment.showFragment(cls);}
    public void showMainFragment(@NonNull Class cls) {xFragment.showMainFragment(cls);}
    public void returnMainFragment(@NonNull Class cls) {xFragment.returnMainFragment(cls);}
    public void removeFragment(@NonNull Class cls) {xFragment.removeFragment(cls);}

    public void clean() {
        appActivity = null;
    }
}
