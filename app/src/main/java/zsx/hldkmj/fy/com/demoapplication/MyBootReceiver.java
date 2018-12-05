package zsx.hldkmj.fy.com.demoapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sunsiyuan on 2017/4/14.
 */

public class MyBootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"=========开机启动了~~~~~~~",Toast.LENGTH_SHORT).show();
        Log.e("TAG","开机广播发送聊");
    }
}
