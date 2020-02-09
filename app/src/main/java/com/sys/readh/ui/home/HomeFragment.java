package com.sys.readh.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sys.readh.utils.AppUtils;
import com.sys.readh.utils.LogUtil;
import com.sys.readh.utils.SharePerKeys;
import com.sys.readh.utils.StringUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    Banner banner;
    ArrayList<Integer> images;
    RecyclerView recyclerView;
    HomeAdapter homeGridAdapter;
    TextView t1, t2;
    View root;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<Integer>();
        images.add(R.mipmap.tb_wazi);
        images.add(R.mipmap.tb_kuzi);
        images.add(R.mipmap.tb_wx);
        images.add(R.mipmap.tb_zj);
        images.add(R.mipmap.tb_jiaorouji);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return root;
    }

    void initView() {
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
        banner.setOnBannerListener(new OnBannerListener() { //banner 点击事件
            @Override
            public void OnBannerClick(int position) {
                LogUtil.d(position + "");
                try {
                    switch (position) {
                        case 0:
                            AppUtils.copyText(getActivity(), StringUtil.tb_wazi);
                            break;
                        case 1:
                            AppUtils.copyText(getActivity(), StringUtil.tb_kuzi);
                            break;
                        case 2:
                            AppUtils.copyText(getActivity(), StringUtil.tb_wx);
                            break;
                        case 3:
                            AppUtils.copyText(getActivity(), StringUtil.tb_zj);
                            break;
                        case 4:
                            AppUtils.copyText(getActivity(), StringUtil.tb_jiaorouji);
                            break;
                    }
                    if (!AppUtils.isAppInstalled(getActivity(), "com.taobao.taobao")) {
                        Toast.makeText(getActivity(), "手机没有安装淘宝客户端！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //跳转到淘宝
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.taobao.taobao"); //这行代
                    intent.setAction("android.intent.action.VIEW");
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("淘宝", e);
                }
            }
        });

    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
        boolean f = AppUtils.checkStealFeature1(getContext(), "com.sys.readh/com.sys.readh.services.SysReadAccessibilityService");
        if (f) {
            t1.setText("红包监听已开启...");
        } else {
            t1.setText("红包监听已关闭...");
        }
        t2.setText(sharedPreferences.getString(SharePerKeys.sys_notificationListener, ""));
    }
    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

}