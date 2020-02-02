package com.sys.readh.activitys;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.IdRes;
import com.sys.readh.R;

public class BaseActivity extends Activity {
    ImageView navback,navuser;
    TextView titlev;
    protected void initNavBar(boolean ishowback, String title, boolean isuser){
        navback = fd(R.id.nav_back);
        navuser = fd(R.id.nav_user);
        titlev = fd(R.id.nav_title);
        navback.setVisibility(ishowback? View.VISIBLE: View.GONE);
        navuser.setVisibility(isuser? View.VISIBLE: View.GONE);
        titlev.setText(title);
        navback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected <T extends View> T fd(@IdRes int id){
        return findViewById(id);
    }

}
