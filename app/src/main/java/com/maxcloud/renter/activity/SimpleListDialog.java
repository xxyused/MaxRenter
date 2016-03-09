package com.maxcloud.renter.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.maxcloud.renter.util.L;

public class SimpleListDialog extends DialogFragment {
    private ListView _listView;
    private ListAdapter _adapter;
    private AdapterView.OnItemClickListener _listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        _listView = new ListView(container.getContext());
        if (null != _adapter) {
            _listView.setAdapter(_adapter);
        }
        if (null != _listener) {
            _listView.setOnItemClickListener(_listener);
        }
        _listView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Window window = getDialog().getWindow();
                    ViewGroup.LayoutParams params = window.getAttributes();
                    params.width = getView().getWidth();
                    window.setAttributes((android.view.WindowManager.LayoutParams) params);
                } catch (Exception e) {
                    L.e("changeWidth", e);
                }
            }
        });

        return _listView;
    }

    /**
     * 设置ListView的数据绑定源。
     *
     * @param adapter 数据源
     */
    public void setAdapter(ListAdapter adapter) {
        _adapter = adapter;
        if (null != _listView) {
            _listView.setAdapter(_adapter);
        }
    }

    /**
     * 设置项目点击事件的监听。
     *
     * @param listener 监听。
     */
    public void setOnItemClick(AdapterView.OnItemClickListener listener) {
        _listener = listener;
        if (null != _listView) {
            _listView.setOnItemClickListener(_listener);
        }
    }
}
