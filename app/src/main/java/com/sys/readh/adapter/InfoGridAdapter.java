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
import com.sys.readh.adapter.impls.MyOnClickListener;
import com.sys.readh.adapter.items.InfoGrid;
import com.sys.readh.utils.AppUtils;
import com.sys.readh.utils.LogUtil;
import com.sys.readh.utils.SharePerKeys;

import java.util.ArrayList;

public class InfoGridAdapter extends RecyclerView.Adapter<InfoGridAdapter.ViewHoler> {
    Context context;
    TextView tvlable,tvvalue;
    ImageView imageView;
    ArrayList<InfoGrid> infoGrids;
    MyOnClickListener myOnClickListener;
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
                    if(myOnClickListener !=null)
                        myOnClickListener.itemOnclick(v,infoGrid);
                }
            });
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
    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
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
