package com.bugly.sample;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeListener;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;


/**
 * Created by wangwei on 2019/4/12.
 */

public class BaseApplication extends Application {
    private static BaseApplication sInstance;
    private String TAG = this.getClass().getSimpleName() + "-ww";

    public BaseApplication() {
    }

    public static BaseApplication getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Application has not been created");
        }
        return sInstance;
    }

    public void onCreate() {
        super.onCreate();
        if (sInstance == null) {
            sInstance = this;
        }
        initUpgradeDialog();
        // initBugly();
    }

    //初始化BUgly 自定义升级弹框
    private void initUpgradeDialog() {
        /**
         * 自定义初始化开关
         */
        Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
//        Beta.upgradeCheckPeriod = 60 * 1000;
        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000;
        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.ic_launcher;
        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher;
        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher;
        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /**
         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        /**
         * 设置Wifi下自动下载
         */
        Beta.autoDownloadOnWifi = true;

        /*在application中初始化时设置监听，监听策略的收取*/
        Beta.upgradeListener = new UpgradeListener() {
            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), UpgradeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
                }
            }
        };

        /* 设置更新状态回调接口 */
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeSuccess(boolean isManual) {
                Toast.makeText(getApplicationContext(), "UPGRADE_SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgradeFailed(boolean isManual) {
                Toast.makeText(getApplicationContext(), "UPGRADE_FAILED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrading(boolean isManual) {
                Toast.makeText(getApplicationContext(), "UPGRADE_CHECKING", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                Toast.makeText(getApplicationContext(), "onDownloadCompleted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onUpgradeNoVersion(boolean isManual) {
                Toast.makeText(getApplicationContext(), "UPGRADE_NO_VERSION", Toast.LENGTH_SHORT).show();
            }
        };

        Bugly.init(getApplicationContext(), "c45d2fc4ba", true);
    }

    private void initBugly() {
        //注意要设置在bugly init之前 自定义UI弹框
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        Bugly.init(getApplicationContext(), "c45d2fc4ba", true);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
