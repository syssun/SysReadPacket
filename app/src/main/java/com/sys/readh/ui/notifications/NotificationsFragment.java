package com.sys.readh.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sys.readh.R;
import com.sys.readh.activitys.LoginActivity;
import com.sys.readh.adapter.InfoGridAdapter;
import com.sys.readh.adapter.items.InfoGrid;
import com.sys.readh.utils.AppUtils;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{
    InfoGridAdapter infoGridAdapter;
    ArrayList<InfoGrid> infoGrids = new ArrayList<>();
    RecyclerView recyclerView ;
    private SharedPreferences sharedPreferences;
    private CheckBox mycheck;
    Button button;
    Context context;
    TextView textView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        context = this.getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = root.findViewById(R.id.r4);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String sys_userphone = sharedPreferences.getString("sys_userphone","");
        infoGrids.add(new InfoGrid("name","用户",sys_userphone));
        infoGrids.add(new InfoGrid("version","应用版本",AppUtils.getVersionName(this.getContext())));
        infoGrids.add(new InfoGrid("mm","当前微信",getVersion("sys_version_mm")));
        infoGrids.add(new InfoGrid("wework","当前企业微信",getVersion("sys_version_wework")));

        String mmbtnid = sharedPreferences.getString("sys_mmbtnid","");
        String weworkbtnid = sharedPreferences.getString("sys_weworkbtnid","");
        infoGrids.add(new InfoGrid("mmbtnid","微信BTNID",mmbtnid,true));
        infoGrids.add(new InfoGrid("weworkbtnid","企业微信BTNID",weworkbtnid,true));

        infoGridAdapter = new InfoGridAdapter(getActivity(),infoGrids);
        recyclerView.setAdapter(infoGridAdapter);

        textView = root.findViewById(R.id.textnoteid);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://www.cnblogs.com/syscn/p/12249913.html");	//指定网址
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);			//指定Action
                intent.setData(uri);							//设置Uri
                context.startActivity(intent);
            }
        });
        mycheck = root.findViewById(R.id.mycheck);
        mycheck.setOnCheckedChangeListener(this);
        boolean ischeck = sharedPreferences.getBoolean("sys_seting_autoclose",false);
        mycheck.setChecked(ischeck);

        button = root.findViewById(R.id.Logoutbtn);
        if(!sys_userphone.isEmpty()){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.GONE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return root;
    }
    public void logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("sys_userphone");
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private String getVersion(String key){
        String a = "" ;
        if("".equals(sharedPreferences.getString(key,""))){
            if("sys_version_wework".equals(key))
                a = AppUtils.getWeWorkVersion(this.getContext());
            else  if("sys_version_mm".equals(key))
                a = AppUtils.getMMVersion(this.getContext());
        }else{
            a = sharedPreferences.getString(key,"");
        }
        return a;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(isChecked){
            editor.putBoolean("sys_seting_autoclose",true);
        }else{
            editor.putBoolean("sys_seting_autoclose",false);
        }
        editor.commit();
    }

}