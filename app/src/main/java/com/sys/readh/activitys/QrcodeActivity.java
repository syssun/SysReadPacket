package com.sys.readh.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.sys.readh.R;
import com.sys.readh.utils.LogUtil;
import com.sys.readh.utils.PermissionUtils;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QrcodeActivity extends BaseActivity implements QRCodeView.Delegate{
    private ZXingView mZXingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initNavBar(true,"扫一扫",false);
        mZXingView = findViewById(R.id.zxingview);
        mZXingView.setDelegate(this);
    }
    @Override
    protected void onStart() {
        super.onStart();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i("", "result:" + result);
        setTitle("扫描结果为：" + result);
        vibrate();
        Intent intent = new Intent(this, MyQrcodeActivity.class);
        intent.putExtra("qr_result",result);
        startActivity(intent);
        finish();
        //mZXingView.startSpot(); // 开始识别
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        LogUtil.d(isDark+":isDark");
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        String s = mZXingView.getScanBoxView().getTipText();
        if(isDark){
            if(s!=null && !"".equals(s)){}else{
                mZXingView.getScanBoxView().setTipText(ambientBrightnessTip);
            }
        }else{
            mZXingView.getScanBoxView().setTipText("");
        }

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtil.d("打开相机出错");
    }

}
