package zsx.hldkmj.fy.com.demoapplication;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by ysq on 2018/11/23.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        FixDexUtils.LoadFixedDex(base);
        super.attachBaseContext(base);
    }
}
