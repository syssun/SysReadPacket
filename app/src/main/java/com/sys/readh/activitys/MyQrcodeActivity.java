package com.sys.readh.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sys.readh.R;
import com.sys.readh.utils.AppUtils;
import com.sys.readh.utils.LogUtil;
import com.sys.readh.utils.PermissionUtils;

public class MyQrcodeActivity extends  BaseActivity {
    Button button,cybutton;
    TextView textView;
    Context context;
    Intent intent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_my_qrcode);
        initNavBar(true,"扫码",false);

        initView();
    }
    private void initView() {
        button = findViewById(R.id.qrbutton);
        cybutton = findViewById(R.id.cybutton);
        textView = findViewById(R.id.qrtext);
        cybutton.setVisibility(View.GONE);

        cybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.copyText(context,textView.getText().toString());
                Toast.makeText(context,"复制成功",Toast.LENGTH_LONG).show();
                LogUtil.d("复制成功");
            }
        });


    }

    public void onClick(View view) {
        if (PermissionUtils.isGrantExternalRW(this, 1)) {
            Intent intent = new Intent(this, QrcodeActivity.class);
            startActivity(intent);
            finish();
        }else{
            AppUtils.toastShow(context,"请打开相机和存储权限！");
        }
    }

    //对获取权限处理的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                    String sdCard = Environment.getExternalStorageState();
                    if (sdCard.equals(Environment.MEDIA_MOUNTED)){

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppUtils.toastShow(context,"请打开相机和存储权限！");
                        }
                    });
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        String value = intent.getStringExtra("qr_result");
        textView.setText(value);
        String v = textView.getText().toString();
        if(v!=null && !"".equals(v)){
            cybutton.setVisibility(View.VISIBLE);
        }else{
            cybutton.setVisibility(View.GONE);
        }


    }

}
