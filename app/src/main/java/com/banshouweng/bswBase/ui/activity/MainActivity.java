package com.banshouweng.bswBase.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.banshouweng.bswBase.R;
import com.banshouweng.bswBase.base.BaseBean;
import com.banshouweng.bswBase.base.activity.BaseLayoutActivity;
import com.banshouweng.bswBase.bean.DouBanBean;
import com.banshouweng.bswBase.netWork.NetUtils;
import com.banshouweng.bswBase.utils.CommonUtils;
import com.banshouweng.bswBase.utils.TxtUtils;
import com.banshouweng.bswBase.utils.rxbus2.Logger;
import com.banshouweng.bswBase.widget.BswRecyclerView.BswRecyclerView;
import com.banshouweng.bswBase.widget.BswRecyclerView.ConvertViewCallBack;
import com.banshouweng.bswBase.widget.BswRecyclerView.OnLoadListener;
import com.banshouweng.bswBase.widget.BswRecyclerView.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class MainActivity extends BaseLayoutActivity {

    private Button view, mergeBtn2;
    private int count = 0;

    private EditText clickEt;
    private BswRecyclerView<BaseBean> bswList;

    private NetUtils netUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title);

        netUtils = new NetUtils(mContext, netRequestCallBack);
//        netUtils.get("top250", new HashMap<String, Object>(), DouBanBean.class, null, false);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "sdasff");
        params.put("age", 15);
        netUtils.post("top250", params, params, DouBanBean.class, null, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void findViews() {
        view = getView(R.id.merge_btn);
        mergeBtn2 = getView(R.id.merge_btn2);
        clickEt = getView(R.id.click_et);
        bswList = getView(R.id.bsw_list);
    }

    private List<BaseBean> getBaseBeans() {
        List<BaseBean> baseBeans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            baseBeans.add(new BaseBean());
        }
        return baseBeans;
    }

    @Override
    protected void formatViews() {
        bswList.initAdapter(R.layout.tv_list_item, convertViewCallBack);
        bswList.setLayoutManager().setDecoration();
        bswList.setLoadListener(new OnLoadListener() {
            @Override
            public void loadData() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bswList.setData(getBaseBeans());
                                    CommonUtils.log().i(getName(), "加载到第" + bswList.getChildCount() + "个");
                                }
                            });
                        }
                    }
                }).start();
                CommonUtils.log().i(getName(), "加载了");
            }

            @Override
            public boolean canLoadMore() {
                return true;
            }

            @Override
            public boolean allLoaded() {
                return false;
            }
        });
        bswList.setData(getBaseBeans());

        setOnClickListener(R.id.merge_btn, R.id.merge_btn2, R.id.click_et);
    }

    @Override
    protected void formatData() {
        mergeBtn2.setText(String.format(Locale.CHINA, CommonUtils.text().getString(mContext, R.string.all), "55555"));
//        view.setText(String.format(Locale.CHINA, TxtUtils.getText(mContext, R.string.all), "12345"));
        if (getIntent() != null) {
            view.setText(getIntent().getStringExtra("name"));
        }
    }

    @Override
    protected void getBundle(Bundle bundle) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.merge_btn:
                view.setText(getName(BaseBean.class));
                CommonUtils.log().i("111", count + "");
                break;

            case R.id.merge_btn2:
//                backTo(TestActivity.class);
//                jumpTo(TestActivity.class);
                finishActivity();
                break;
        }
    }

    private ConvertViewCallBack<BaseBean> convertViewCallBack = new ConvertViewCallBack<BaseBean>() {
        @Override
        public BaseBean convert(RecyclerViewHolder holder, BaseBean baseBean, int position, int layoutTag) {
            holder.setText(R.id.item_tv, "第" + (position + 1) + "项");
            return baseBean;
        }
    };

    private NetUtils.NetRequestCallBack netRequestCallBack = new NetUtils.NetRequestCallBack() {
        @Override
        public void success(String action, BaseBean baseBean, Map tag) {
            switch (action) {
                case "top250":
                    DouBanBean douBanBean = ((DouBanBean) baseBean);
                    System.out.println(douBanBean.toString());
                    break;
            }
        }

        @Override
        public void error(String action, Throwable e, Map tag) {

        }
    };

    @Override
    public void onClick(View v) {

    }
}
