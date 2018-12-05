package zsx.hldkmj.fy.com.demoapplication;

import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText et_datatype,et_useraction,et_extdata;
    private static int MY_PERMISSION_REQUEST_CODE=1;
    private Button btn_send,btn_xf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isPermission = checkSelfPermissionAll(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        if (isPermission) {
//            Toast.makeText(MainActivity.this, "正在查看!", Toast.LENGTH_SHORT).show();

        }else{
            //        请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_CODE);

        }
        et_datatype= (EditText) findViewById(R.id.et_datatype);
        et_useraction= (EditText) findViewById(R.id.et_useraction);
        et_extdata= (EditText) findViewById(R.id.et_extdata);
        btn_send= (Button) findViewById(R.id.btn_send);
        btn_xf= (Button) findViewById(R.id.btn_xf);
        Toast.makeText(MainActivity.this,Tools.DATA,Toast.LENGTH_SHORT).show();

        Log.e("TAG","数据:::==="+Tools.DATA);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datatype = et_datatype.getText().toString().trim();
                String useraction = et_useraction.getText().toString().trim();
                String extdata = et_extdata.getText().toString().trim();
                Intent intent = new Intent();
                intent.setAction("com.topway.appmsg");
                if (datatype!=null&&datatype.length()>0){
                    intent.putExtra("datatype",Integer.parseInt(datatype));
                }else{
                    Toast.makeText(MainActivity.this,"请输入datatype内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (useraction!=null&&useraction.length()>0){
                    intent.putExtra("useraction",Integer.parseInt(useraction));
                }else{
                    Toast.makeText(MainActivity.this,"请输入useraction内容",Toast.LENGTH_SHORT).show();
                    return;
                }

                intent.putExtra("extdata",extdata);
                sendBroadcast(intent);
                Toast.makeText(MainActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this,Tools.DATA,Toast.LENGTH_SHORT).show();

            }
        });
        btn_xf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //这个目录也就是应用程序的根目录  /data/data/packageName
                File fileDir = getDir(MyFileName.DIR,MODE_PRIVATE);
                //该目录下面放置我们修复的dex文件
                String name= "classes2.dex";
                String filePath=fileDir.getAbsolutePath()+File.separator+name;

                File file = new File(filePath);
                if (file.exists()){

                    file.delete();

                }

                //下载好修复的classes2.dex搬到上面的应用目录
                try {
                    FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+name);

                    FileOutputStream fos = new FileOutputStream(filePath);

                    byte[] bytes = new byte[1024];
                    int len=0;
                    while ((len=fis.read(bytes))!=-1){

                        fos.write(bytes,0,len);

                    }

                    File f=new File(filePath);

                    if (f.exists()){

                        Toast.makeText(MainActivity.this,"dex 重写成功",Toast.LENGTH_SHORT).show();

                    }
                    //热修复
                    FixDexUtils.LoadFixedDex(getApplicationContext());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("TAG","走错误一");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG","走错误二");
                }
            }
        });
    }

    //    检查是否拥有指定的所有权限
    private boolean checkSelfPermissionAll(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isPermission = true;
            for (int grant : grantResults) {
                // 判断是否所有的权限都已经授予了
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isPermission = false;
                    break;
                }
            }
            if (isPermission) {
//                Toast.makeText(BaseActivity.this, "我看看", Toast.LENGTH_SHORT).show();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("备份通讯录需要访问")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("退出",null);
                builder.show();
            }
        }
    }


}
