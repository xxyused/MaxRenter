package com.maxcloud.renter.activity;

import android.support.v4.app.Fragment;

import com.maxcloud.renter.util.L;
import com.umeng.analytics.MobclickAgent;

/**
 * 描    述：基础Fragment类，集成友盟统计。
 * 作    者：向晓阳
 * 时    间：2016/2/22
 * 版    权：迈斯云门禁网络科技有限公司
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 获取标题。
     *
     * @return 标题。
     */
    protected abstract String getTitle();

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart(getClass().getSimpleName());
        L.useResume(getClass().getSimpleName(), getTitle());
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(getClass().getSimpleName());
        L.usePause(getClass().getSimpleName(), getTitle());
    }
}
