package com.sys.readh.ui.notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sys.readh.R;
import com.sys.readh.activitys.LoginActivity;
import com.sys.readh.adapter.InfoGridAdapter;
import com.sys.readh.adapter.impls.MyOnClickListener;
import com.sys.readh.adapter.items.InfoGrid;
import com.sys.readh.utils.AppUtils;
import com.sys.readh.utils.LogUtil;
import com.sys.readh.utils.SharePerKeys;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    InfoGridAdapter infoGridAdapter;
    ArrayList<InfoGrid> infoGrids ;
    RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private CheckBox mycheck;
    Button button;
    Context context;
    TextView textView;
    View root ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        context = this.getContext();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        initView(); //初始化ui
        return root;
    }
    void initView(){
        recyclerView = root.findViewById(R.id.r4);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String sys_userphone = sharedPreferences.getString(SharePerKeys.sys_userphone, "");
        infoGrids = new ArrayList<>();
        infoGrids.add(new InfoGrid("name", "用户", sys_userphone));
        infoGrids.add(new InfoGrid("version", "应用版本", AppUtils.getVersionName(this.getContext())));
        infoGrids.add(new InfoGrid("mm", "当前微信", getVersion(SharePerKeys.sys_version_mm)));
        infoGrids.add(new InfoGrid("wework", "当前企业微信", getVersion(SharePerKeys.sys_version_wework)));
        String mmbtnid = sharedPreferences.getString(SharePerKeys.sys_mmbtnid, "");
        String weworkbtnid = sharedPreferences.getString(SharePerKeys.sys_weworkbtnid, "");
        int sys_delay_ms = sharedPreferences.getInt(SharePerKeys.sys_delay_open, 0);
        infoGrids.add(new InfoGrid("mmbtnid", "微信BTNID", mmbtnid, true));
        infoGrids.add(new InfoGrid("weworkbtnid", "企业微信BTNID", weworkbtnid, true));
        infoGrids.add(new InfoGrid("sys_delay_open", "延迟开红包", sys_delay_ms + "ms", true));

        infoGridAdapter = new InfoGridAdapter(getActivity(), infoGrids);
        infoGridAdapter.setMyOnClickListener(new MyOnClickListenerImpl());
        recyclerView.setAdapter(infoGridAdapter);
        textView = root.findViewById(R.id.textnoteid);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.cnblogs.com/syscn/p/12249913.html");    //指定网址
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);            //指定Action
                intent.setData(uri);                            //设置Uri
                context.startActivity(intent);
            }
        });
        mycheck = root.findViewById(R.id.mycheck);
        mycheck.setOnCheckedChangeListener(this);
        boolean isCheck = sharedPreferences.getBoolean(SharePerKeys.sys_seting_autoclose, false);
        mycheck.setChecked(isCheck);

        button = root.findViewById(R.id.Logoutbtn);
        if (!sys_userphone.isEmpty()) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SharePerKeys.sys_userphone);
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String getVersion(String key) {
        String a = "";
        if ("".equals(sharedPreferences.getString(key, ""))) {
            if (SharePerKeys.sys_version_wework.equals(key))
                a = AppUtils.getWeWorkVersion(this.getContext());
            else if (SharePerKeys.sys_version_mm.equals(key))
                a = AppUtils.getMMVersion(this.getContext());
        } else {
            a = sharedPreferences.getString(key, "");
        }
        return a;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isChecked) {
            editor.putBoolean(SharePerKeys.sys_seting_autoclose, true);
        } else {
            editor.putBoolean(SharePerKeys.sys_seting_autoclose, false);
        }
        editor.commit();
    }

    public void setShare(final String key, String title, final int type) {
        final EditText editText = new EditText(context);
        if (type == 0) {
            int a = sharedPreferences.getInt(key, 0);
            editText.setText(a + "");
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (type == 1) {
            String inittext = sharedPreferences.getString(key, "");
            editText.setText(inittext);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(editText)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (type == 1)
                    editor.putString(key, editText.getText().toString());
                if (type == 0) {
                    String s = editText.getText().toString();
                    if ("".equals(s))
                        s = "0";
                    int c = Integer.parseInt(s);
                    if (c > 30000) {
                        AppUtils.toastShow(context, "输入的最大值为30000 ！");
                        return;
                    }
                    editor.putInt(key, c);
                }
                editor.commit();
                initView();
            }
        });
        builder.show();
    }
    class MyOnClickListenerImpl implements MyOnClickListener {
        @Override
        public void itemOnclick(View view, InfoGrid infoGrid) {
            switch (infoGrid.getCode()) {
                case "mmbtnid":
                    setShare(SharePerKeys.sys_mmbtnid, "请输入微信BTNID", 1);
                    break;
                case "weworkbtnid":
                    setShare(SharePerKeys.sys_weworkbtnid, "请输入企业微信BTNID", 1);
                    break;
                case "sys_delay_open":
                    setShare(SharePerKeys.sys_delay_open, "请输入延迟毫秒数(1s=1000ms)", 0);
                    break;
            }
        }
    }
}