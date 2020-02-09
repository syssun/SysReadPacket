package com.sys.readh.activitys;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.sys.readh.MainActivity;
import com.sys.readh.R;
import com.sys.readh.utils.AppAcount;
import com.sys.readh.utils.CodeUtils;
import com.sys.readh.utils.LogUtil;
import com.sys.readh.utils.RegexUtils;
import com.sys.readh.utils.SharePerKeys;
import com.sys.readh.views.InputView;
import com.sys.readh.views.RandomValidateCode;

public class LoginActivity extends BaseActivity {
    private RandomValidateCode mValidateCode;
    private SharedPreferences sharedPreferences;
    private String code  ;
    InputView userphone,usercode ;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initNavBar(false,"欢迎登录",false);
        button = findViewById(R.id.loginbtn);
        userphone = findViewById(R.id.userphone);
        usercode = findViewById(R.id.usercode);
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        mValidateCode = (RandomValidateCode) findViewById(R.id.id_validate_code);
        code = CodeUtils.randomText(); //初始化code
        mValidateCode.setText(code);
    }
    @Override
    protected void onResume() {
        super.onResume();
        initEvent();
    }
    public void onClick(View view){
        String userphonestr = userphone.getInputStr();
        String usercodestr = usercode.getInputStr();
        if(userphonestr ==null || userphonestr.isEmpty()){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(AppAcount.username_admin.equals(userphonestr)){ //不需要校验的用户
            ccomit(userphonestr);
            return ;
        }


        if(usercodestr ==null || usercodestr.isEmpty()){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(!code.equalsIgnoreCase(usercodestr)){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(!RegexUtils.isMobileExact(userphonestr)){
            Toast.makeText(this,"手机号错误",Toast.LENGTH_SHORT).show();
            return ;
        }
        ccomit(userphonestr);
    }
    private void ccomit(String userphonestr){
        //将用户名保存到缓存
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharePerKeys.sys_userphone,userphonestr);
        editor.commit();
        button.setClickable(false);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void initEvent() {
        mValidateCode.setValidateCodeOnClickListener(new RandomValidateCode.ValidateCodeOnClickListener() {
            @Override
            public void onClick(View view) {
                code = CodeUtils.randomText();
                mValidateCode.setText(code);
                mValidateCode.postInvalidate();
                LogUtil.d(code);
            }
        });
    }

}
