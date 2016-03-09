package com.maxcloud.renter.util.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.BaseFragment;

/**
 * Created by MAX-XXY on 2016/2/16.
 */
public class FragmentStayTuned extends BaseFragment {
    private View _rootView;

    @Override
    protected String getTitle() {
        return getString(R.string.stay_tuned);
    }

    public FragmentStayTuned() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == _rootView) {
            _rootView = View.inflate(container.getContext(), R.layout.fragment_stay_tuned, null);
        }

        return _rootView;
    }
}
