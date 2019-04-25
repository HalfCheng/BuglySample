package com.bugly.sample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;

import java.util.Locale;

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;


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
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(this, "c45d2fc4ba", true);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

}
