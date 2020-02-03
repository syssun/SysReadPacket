package com.sys.readh.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sys.readh.MainActivity;
import com.sys.readh.R;
import com.sys.readh.utils.AppUtils;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {
    private SharedPreferences sharedPreferences;
    TextView v = null;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_welcome);
        v = findViewById(R.id.tvversion);
        v.setText("红包神器" + AppUtils.getVersionName(this.getApplicationContext()));
        init();

        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("sys_version_mm", AppUtils.getMMVersion(this.getApplicationContext()));
            editor.putString("sys_version_wework", AppUtils.getWeWorkVersion(this.getApplicationContext()));
            editor.putBoolean("sys_seting_autoclose", true);
            editor.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void init() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //判断是否已经登录过
                String sys_userphone = sharedPreferences.getString("sys_userphone", "");
                if (sys_userphone.isEmpty())
                    toLogin();
                else
                    toMain();
            }
        }, 1000);
    }

    public void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
