package com.lgb.myfitness.module.bpm;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.lgb.mvp.AppActivity;
import com.lgb.myfitness.module.bpm.main.BPMStatisticsDetailFragment;
import com.lgb.myfitness.module.bpm.settings.about.BPMDeclarationFragment;
import com.lgb.myfitness.module.bpm.settings.about.BPMImpressumFragment;
import com.lgb.myfitness.module.bpm.main.BPMStatisticsFragment;
import com.lgb.myfitness.module.bpm.main.BPMTestFragment;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsLanguageFragment;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsProfileFragment;
import com.lgb.myfitness.module.bpm.settings.BPMSettingsAboutUsFragment;
import com.lgb.xpro.utils.XFragment;

import java.util.ArrayList;
import java.util.List;

public class BPMFragmentManager {

    //单例
    private static class Instance {public static final BPMFragmentManager instance = new BPMFragmentManager();}
    public static BPMFragmentManager getInstance() {return BPMFragmentManager.Instance.instance;}

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
        bean2.add(BPMDeclarationFragment.class);
        bean2.add(BPMImpressumFragment.class);

        tagList.add(bean1);
        tagList.add(bean2);
        xFragment = new XFragment(appActivity.getFragmentManager(), layoutRes, tagList);
    }

    public void showFragment(@NonNull Class cls) {xFragment.showFragment(cls);}
    public void showFragment(@NonNull Class cls, Bundle bundle) {xFragment.showFragment(cls, bundle);}
    public void showMainFragment(@NonNull Class cls) {xFragment.showMainFragment(cls);}
    public void returnMainFragment(@NonNull Class cls) {xFragment.returnMainFragment(cls);}
    public void removeFragment(@NonNull Class cls) {xFragment.removeFragment(cls);}
    public Fragment findFragment(@NonNull Class cls) {return xFragment.findFragment(cls);}

    public void clean() {
        appActivity = null;
    }
}
