package com.sys.readh.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sys.readh.R;
import com.sys.readh.activitys.SettingsActivity;
import com.sys.readh.adapter.items.HomeGrid;
import com.sys.readh.utils.SharePerKeys;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<HomeGrid> homeGrids;
    private SharedPreferences sharedPreferences;
    private  HomeAdapter(){}
    public HomeAdapter (Context context,ArrayList<HomeGrid> homeGrids){
        this.context = context;
        this.homeGrids = homeGrids;
        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            imageView = itemView.findViewById(R.id.grid_img);
            textView = itemView.findViewById(R.id.grid_text);
        }
    }


    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_grid, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        final HomeGrid h = homeGrids.get(position);
        holder.imageView.setImageResource(h.getIamge());
        holder.textView.setText(h.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sysapp",v.getId()+"");
                switch (h.getCtl()){
                    case "tongzhi":
                        context.startActivity( new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                        break ;
                    case "jianting" :
                        //判断btnid 是否填写
                        String mmbtnid = sharedPreferences.getString(SharePerKeys.sys_mmbtnid,"");
                        String weworkbtnid = sharedPreferences.getString(SharePerKeys.sys_weworkbtnid,"");
                        if("".equals(mmbtnid) && "".equals(weworkbtnid)){
                            Toast.makeText(context,"请在【我的】中正确填写BTNID",Toast.LENGTH_LONG).show();
                            break;
                        }
                        context.startActivity( new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                        break ;
                    case "shezhi" :
                        context.startActivity(new Intent(context, SettingsActivity.class));
                        break ;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return homeGrids.size();
    }
}
