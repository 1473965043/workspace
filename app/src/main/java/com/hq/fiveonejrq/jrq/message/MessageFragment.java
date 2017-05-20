package com.hq.fiveonejrq.jrq.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.widget.RefreshView;

/**
 * Created by guodong on 2017/2/28.
 */

public class MessageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_layout, container, false);
        return view;
    }
}
