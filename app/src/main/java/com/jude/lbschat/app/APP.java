package com.jude.lbschat.app;

import android.app.Activity;
import android.app.Application;

import com.jude.beam.Beam;
import com.jude.beam.bijection.ActivityLifeCycleDelegate;
import com.jude.beam.bijection.ActivityLifeCycleDelegateProvider;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.beam.expansion.overlay.ViewExpansionDelegate;
import com.jude.beam.expansion.overlay.ViewExpansionDelegateProvider;
import com.jude.lbschat.R;
import com.jude.lbschat.domain.Dir;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

/**
 * Created by zhuchenxi on 16/4/21.
 */
public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.initialize(this);
        JUtils.setDebug(true, "LBSChat");
        JFileManager.getInstance().init(this, Dir.values());
        Beam.init(this);
        Beam.setViewExpansionDelegateProvider(new ViewExpansionDelegateProvider() {
            @Override
            public ViewExpansionDelegate createViewExpansionDelegate(BeamBaseActivity activity) {
                return new NewViewExpansion(activity);
            }
        });
        Beam.setActivityLifeCycleDelegateProvider(new ActivityLifeCycleDelegateProvider() {
            @Override
            public ActivityLifeCycleDelegate createActivityLifeCycleDelegate(Activity activity) {
                return new ActivityDelegate(activity);
            }
        });
        ListConfig.setDefaultListConfig(new ListConfig()
                .setPaddingNavigationBarAble(true)
                .setRefreshAble(true)
                .setContainerLayoutRes(R.layout.activity_recyclerview));
    }
}
