package com.maxcloud.renter.util.build;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.maxcloud.renter.customview.DoorTabLayout;
import com.maxcloud.renter.customview.DoorView;
import com.maxcloud.renter.entity.build.DoorInfo;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.OpenDoorResult;
import com.maxcloud.renter.util.L;

import java.util.Collections;
import java.util.List;

/**
 * Created by MAX-XXY on 2016/2/17.
 */
public class DoorManage {
    private static long[] mSuccessPattern = {100, 200, 100, 200}; // 停止 开启 停止 开启
    private static long[] mFailurePattern = {200, 1000}; // 停止 开启

    private View mBtnBindCard;
    private DoorTabLayout mDoorsView;

    //private Context _context;
    private DoorAdapter mDoorAdapter;
    private Handler _handler;

    public DoorManage(DoorAdapter.OnItemClickListener listener) {
        //_context = context;
        _handler = new Handler();
        mDoorAdapter = new DoorAdapter();
        mDoorAdapter.setDoorClickListener(listener);
    }

    public void reloadDoor(Context context, boolean notify) {
        mDoorAdapter.clear();

        LoginInfo loginInfo = LoginInfo.get();
        if (null != loginInfo && null != loginInfo.getDoors()) {
            List<DoorInfo> doorInfos = loginInfo.getDoors();
            if (doorInfos.size() > 1) {
                Collections.sort(doorInfos, new DoorInfo.SortComparator());
            }

            for (DoorInfo doorInfo : doorInfos) {
                DoorView view = new DoorView(context);

                mDoorAdapter.add(view, doorInfo);
            }
        }

        if (notify) {
            if (mDoorAdapter.getCount() > 0) {
                mDoorsView.setVisibility(View.VISIBLE);
                mBtnBindCard.setVisibility(View.GONE);
            } else {
                mDoorsView.setVisibility(View.GONE);
                mBtnBindCard.setVisibility(View.VISIBLE);
            }

            mDoorAdapter.notifyDataSetChanged();
        }
    }

    public void setView(DoorTabLayout doorsView, View btnBindCard) {
        mDoorsView = doorsView;
        mBtnBindCard = btnBindCard;

        if (mDoorAdapter.getCount() > 0) {
            mDoorsView.setVisibility(View.VISIBLE);
            mBtnBindCard.setVisibility(View.GONE);
        } else {
            mDoorsView.setVisibility(View.GONE);
            mBtnBindCard.setVisibility(View.VISIBLE);
        }

        mDoorsView.setAdapter(mDoorAdapter);
    }

    public void openDoor(DoorView doorView) {
        if (doorView.isBusying()) {
            return;
        }
//                    if (doorView.getRemainderCount() == 0) {
//                        Toast.makeText(getContext(), "您的手机开门次数已用完，请明天再试！", Toast.LENGTH_LONG).show();
//                        return;
//                    }

        mDoorsView.setCanScroll(false);
        doorView.showOpeningAnimation();

        new OpenDoorTask(this, doorView).
                execute(LoginInfo.get().getAccount(),
                        doorView.getServerId(),
                        String.valueOf(doorView.getDoorId()));
    }

    private void openDoorEnd(DoorView doorView, final String error) {
        if (TextUtils.isEmpty(error)) {
            doorView.showOpenSuccessAnimation();

            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDoorsView.setCanScroll(true);

                    Context context = mDoorsView.getContext().getApplicationContext();
                    Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                    if (mVibrator.hasVibrator()) {
                        mVibrator.cancel();
                    }

                    mVibrator.vibrate(mSuccessPattern, -1);
                }
            }, DoorView.MIN_TIME);
        } else {
            doorView.showOpenFailureAnimation();

            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDoorsView.setCanScroll(true);

                    Context context = mDoorsView.getContext().getApplicationContext();
                    Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                    if (mVibrator.hasVibrator()) {
                        mVibrator.cancel();
                    }

                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                    mVibrator.vibrate(mFailurePattern, -1);
                }
            }, DoorView.MIN_TIME);
        }
    }

    private static class OpenDoorTask extends AsyncTask<String, Integer, String> {
        private DoorManage _helper;
        private DoorView _doorView;

        public OpenDoorTask(DoorManage helper, DoorView doorView) {
            _helper = helper;
            _doorView = doorView;
        }

        @Override
        protected String doInBackground(String... params) {
            String account = params[0];
            String serverId = params[1];
            int doorId = Integer.valueOf(params[2]);
            String error = null;
            long curTime = System.currentTimeMillis();

            try {
                MaxService service = MaxService.get();
                OpenDoorResult result = service.openDoor(account, serverId, doorId);
                publishProgress(result.getRemainCount());
            } catch (Exception e) {
                L.e("openDoor", e);

                error = e.getMessage();
            }

            long delayed = DoorView.MIN_TIME - (System.currentTimeMillis() - curTime);
            if (delayed > 0) {
                try {
                    Thread.sleep(delayed);
                } catch (Exception e) {
                }
            }

            return error;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            _doorView.setRemainderCountByDelayed(values[0]);
        }

        @Override
        protected void onPostExecute(String error) {
            _helper.openDoorEnd(_doorView, error);
        }

        @Override
        protected void onCancelled() {
            _doorView.showOpenSuccessAnimation();
        }
    }
}
