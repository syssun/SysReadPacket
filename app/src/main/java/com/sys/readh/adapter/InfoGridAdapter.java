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
import com.sys.readh.adapter.items.InfoGrid;
import java.util.ArrayList;
public class InfoGridAdapter extends RecyclerView.Adapter<InfoGridAdapter.ViewHoler> {
    Context context;
    TextView tvlable,tvvalue;
    ImageView imageView;
    ArrayList<InfoGrid> infoGrids;
    MyOnClickListener<InfoGrid> myOnClickListener;
    private InfoGridAdapter(){}
    public InfoGridAdapter(Context context, ArrayList<InfoGrid> infoGrids){
        this.context = context;
        this.infoGrids = infoGrids ;
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
