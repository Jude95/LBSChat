package com.jude.lbschat.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

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

import io.rong.imkit.RongIM;

/**
 * Created by zhuchenxi on 16/4/21.
 */
public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
                /* OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * xxx.xxx.xxx 是您的主进程或者使用了 RongIM 的其他进程 */
        if("com.jude.lbschat".equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            RongIM.init(this);
            if ("com.jude.lbschat".equals(getCurProcessName(getApplicationContext()))) {

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
    }
    /* 一个获得当前进程的名字的方法 */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
