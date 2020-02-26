package com.sys.readh.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sys.readh.R;
import com.sys.readh.adapter.InfoGridAdapter;
import com.sys.readh.adapter.impls.MyOnClickListener;
import com.sys.readh.adapter.items.InfoGrid;
import com.sys.readh.loader.GlideImageLoader;
import com.sys.readh.ui.notifications.NotificationsFragment;
import com.sys.readh.utils.AppUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    InfoGridAdapter infoGridAdapter;
    ArrayList<InfoGrid> infoGrids ;
    RecyclerView recyclerView;
    Context context;
    View root ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initData();
        initView();
        return root;
    }
    private void initData() {
        infoGrids = new ArrayList<>();
        infoGrids.add(new InfoGrid("qrcode", "扫描二维码", "", true));
    }
    private void initView() {
        recyclerView = root.findViewById(R.id.dr4);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        infoGridAdapter = new InfoGridAdapter(getActivity(), infoGrids);
        infoGridAdapter.setMyOnClickListener(new MmListener());
        recyclerView.setAdapter(infoGridAdapter);
    }





    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    class MmListener implements MyOnClickListener<InfoGrid>{

        @Override
        public void itemOnclick(View view, InfoGrid infoGrid) {
            switch (infoGrid.getCode()){
                case "qrcode":
                    AppUtils.toastShow(context,"this is qrcode");
                    break;
            }
        }
    }



}