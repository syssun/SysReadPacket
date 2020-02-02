package com.sys.readh.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sys.readh.R;
import com.sys.readh.adapter.HomeAdapter;
import com.sys.readh.adapter.items.HomeGrid;
import com.sys.readh.loader.GlideImageLoader;
import com.sys.readh.utils.LogUtil;
import com.youth.banner.Banner;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    Banner banner;
    ArrayList<Integer> images;
    RecyclerView recyclerView;
    HomeAdapter homeGridAdapter;
    TextView t1,t2 ;
    private SharedPreferences sharedPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<Integer>();
        images.add(R.mipmap.banner_one);
        images.add(R.mipmap.banner_two);
        images.add(R.mipmap.banner_three);
        images.add(R.mipmap.banner_four);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        banner = root.findViewById(R.id.banner);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ArrayList<HomeGrid> homeGrids = new ArrayList<>();
        homeGrids.add(new HomeGrid(R.mipmap.kaqii, "开启监听", "jianting"));
        homeGrids.add(new HomeGrid(R.mipmap.tongzhi, "开启通知", "tongzhi"));

        homeGridAdapter = new HomeAdapter(getActivity(), homeGrids);
        recyclerView.setAdapter(homeGridAdapter);

         t1 = root.findViewById(R.id.t1);
         t2 = root.findViewById(R.id.t2);



        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        banner.setDelayTime(4000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();


    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
        boolean f = checkStealFeature1("com.sys.readh/com.sys.readh.services.SysReadAccessibilityService");
        if(f){
            t1.setText("红包监听已开启...");
        }else{
            t1.setText("红包监听已关闭...");
        }
        t2.setText(sharedPreferences.getString("sys_notificationListener",""));
    }
    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
    private boolean checkStealFeature1(String service) {
        int ok = 0;
        LogUtil.d("ok:"+ok);
        try {
            ok = Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter ms = new TextUtils.SimpleStringSplitter(':');
        if (ok == 1) {
            String settingValue = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                ms.setString(settingValue);
                while (ms.hasNext()) {
                    String accessibilityService = ms.next();
                    LogUtil.d(accessibilityService);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false ;
    }
}