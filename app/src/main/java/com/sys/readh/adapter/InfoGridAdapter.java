package com.sys.readh.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                            setShare("sys_mmbtnid","请输入微信BTNID");
                            break;
                        case "weworkbtnid":
                            setShare("sys_weworkbtnid","请输入企业微信BTNID");
                            break;
                    }
                }
            });
    }

    public void setShare(final String key, String title){
        final EditText inputServer = new EditText(context);
        String inittext = sharedPreferences.getString(key,"");
        inputServer.setText(inittext);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key,inputServer.getText().toString());
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
