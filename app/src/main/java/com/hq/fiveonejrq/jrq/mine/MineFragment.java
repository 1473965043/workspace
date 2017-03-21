package com.hq.fiveonejrq.jrq.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hq.fiveonejrq.jrq.R;

/**
 * Created by Administrator on 2017/2/28.
 */

public class MineFragment extends Fragment {

    private Button btn, btn1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_layout, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        btn = (Button) view.findViewById(R.id.show);
        btn1 = (Button) view.findViewById(R.id.shouquan);
    }

}
