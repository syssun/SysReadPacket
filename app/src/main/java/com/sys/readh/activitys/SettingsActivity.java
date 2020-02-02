package com.sys.readh.activitys;

import android.os.Bundle;
import com.sys.readh.R;
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initNavBar(true,"其它设置",false);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.rl_fragment_container, new SettingFragment())
                .commit();
    }
}
