package com.hq.fiveonejrq.jrq.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;

/**
 * Created by guodong on 2017/2/28.
 */

public class MessageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_layout, container, false);
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = getActivity().getWindowManager();
        DisplayMetrics dp = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dp);
        int screenWidth = dp.widthPixels;
        int screenHeight = dp.heightPixels;

        Toast.makeText(getActivity(), screenWidth+"与"+screenHeight, Toast.LENGTH_SHORT).show();
        return view;
    }
}
