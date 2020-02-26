package com.sys.readh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sys.readh.R;
import com.sys.readh.adapter.impls.MyOnClickListener;
import com.sys.readh.adapter.items.HomeGrid;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<HomeGrid> homeGrids;
    MyOnClickListener<HomeGrid> myOnClickListener;
    private  HomeAdapter(){}
    public HomeAdapter (Context context,ArrayList<HomeGrid> homeGrids){
        this.context = context;
        this.homeGrids = homeGrids;
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
                if(myOnClickListener !=null)
                    myOnClickListener.itemOnclick(v,h);
            }
        });
    }

    public MyOnClickListener getMyOnClickListener() {
        return myOnClickListener;
    }

    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }
    @Override
    public int getItemCount() {
        return homeGrids.size();
    }
}
