package com.banshouweng.mybaseapplication.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.banshouweng.mybaseapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/5.
 */

public class MyViewHolder {
    @BindView(R.id.item_tv)
    TextView itemTv;

    MyViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
