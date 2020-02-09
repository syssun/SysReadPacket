package com.sys.readh.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.sys.readh.MainActivity;
import com.sys.readh.R;
import com.sys.readh.activitys.LoginActivity;
import com.sys.readh.adapter.items.InfoGrid;
import com.sys.readh.utils.AppUtils;
import com.sys.readh.utils.SharePerKeys;

import java.util.ArrayList;

public class InfoGridAdapter extends RecyclerView.Adapter<InfoGridAdapter.ViewHoler> {
    Context context;
    TextView tvlable,tvvalue;
    ImageView imageView;
    ArrayList<InfoGrid> infoGrids;
    private SharedPreferences sharedPreferences;
    private InfoGridAdapter(){}
    public InfoGridAdapter(Context context, ArrayList<InfoGrid> infoGrids){
        this.context = context;
        this.infoGrids = infoGrids ;
        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }
    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new InfoGridAdapter.ViewHoler(LayoutInflater.from(context).inflate(R.layout.info_grid,viewGroup,false));

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHoler viewHoler, int i) {
            final InfoGrid infoGrid = infoGrids.get(i);
            tvlable.setText(infoGrid.getLabel());
            tvvalue.setText(infoGrid.getValue());
            imageView.setVisibility(infoGrid.getFlag()? View.VISIBLE: View.GONE);
            viewHoler.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (infoGrid.getCode()){
                        case "mmbtnid":
                            setShare(SharePerKeys.sys_mmbtnid,"请输入微信BTNID",1);
                            break;
                        case "weworkbtnid":
                            setShare(SharePerKeys.sys_weworkbtnid,"请输入企业微信BTNID",1);
                            break;
                        case "sys_delay_open":
                            setShare(SharePerKeys.sys_delay_open,"请输入延迟毫秒数(1s=1000ms)",0);
                            break;
                    }
                }
            });
    }

    public void setShare(final String key, String title, final int type){
        final EditText editText = new EditText(context);
        if(type==0) {
            int a = sharedPreferences.getInt(key, 0);
            editText.setText(a+"");
            editText.setInputType( InputType.TYPE_CLASS_NUMBER);
        }else if(type==1) {
            String inittext = sharedPreferences.getString(key,"");
            editText.setText(inittext);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(editText)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(type==1)
                    editor.putString(key,editText.getText().toString());
                if(type==0) {
                    String s= editText.getText().toString();
                    if("".equals(s))
                        s="0";
                    int c = Integer.parseInt(s);
                    if(c>30000){
                        AppUtils.toastShow(context,"输入的最大值为30000 ！");
                        return ;
                    }
                    editor.putInt(key,c );
                }
                editor.commit();
                toMain();
            }
        });
        builder.show();
    }
    public  void toMain(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return infoGrids.size();
    }

    class ViewHoler extends RecyclerView.ViewHolder{

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            tvlable = itemView.findViewById(R.id.tv_label);
            tvvalue = itemView.findViewById(R.id.tv_value);
            imageView = itemView.findViewById(R.id.info_more);
        }
    }
}
