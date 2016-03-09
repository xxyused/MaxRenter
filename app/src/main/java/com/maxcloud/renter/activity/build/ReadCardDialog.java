package com.maxcloud.renter.activity.build;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.entity.build.CardInfo;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.CardInfoResult;
import com.maxcloud.renter.util.L;

public class ReadCardDialog extends DialogFragment {
    public static final String PRO_SERVER_ID = "ServerId";
    public static final String PRO_BUILDING_ID = "BuildingId";
    public static final String PRO_BUILDING_NAME = "BuildingName";

    private TextView _txvText;
    private TextView _txvTip;
    private String _serverId;
    private int _buildingId;
    private String _buildingName;
    private ReadCardInfoTask _task;
    private CountdownRunnable _runnable;

    public String getServerId() {
        return _serverId;
    }

    public int getBuildingId() {
        return _buildingId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Bundle arguments = getArguments();
        if (null != arguments) {
            if (arguments.containsKey(PRO_SERVER_ID)) {
                _serverId = arguments.getString(PRO_SERVER_ID);
            }
            if (arguments.containsKey(PRO_BUILDING_ID)) {
                _buildingId = arguments.getInt(PRO_BUILDING_ID);
            }
            if (arguments.containsKey(PRO_BUILDING_NAME)) {
                _buildingName = arguments.getString(PRO_BUILDING_NAME);
            }
        }
        if(TextUtils.isEmpty(_buildingName)){
            _buildingName =getString(R.string.read_card_empty_build_name);
        }

        View v = inflater.inflate(R.layout.dialog_read_card, container, false);
        _txvText = (TextView) v.findViewById(R.id.txvText);
        _txvTip = (TextView) v.findViewById(R.id.txvTip);
        v.post(new ResetWidthRunnable(this));

        StringBuilder sb = new StringBuilder("请在倒计时结束前到<br>");
        sb.append(String.format("<b><font color=#000000>%s</font></b>", _buildingName));
        sb.append("<br>处刷卡");

        _txvTip.setText(Html.fromHtml(sb.toString()));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (null != _task) {
            _task.cancel(true);
        }

        _task = new ReadCardInfoTask(this);
        _task.execute();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (null != _task) {
            _task.cancel(true);
        }
    }

    /**
     * 启动倒计时。
     */
    public void startCountdown() {
        if (null == _runnable) {
            _runnable = new CountdownRunnable(this, _txvText);
        }
        _runnable.run();
    }

    private static class ResetWidthRunnable implements Runnable {
        private ReadCardDialog _dialog;

        public ResetWidthRunnable(ReadCardDialog dialog) {
            _dialog = dialog;
        }

        @Override
        public void run() {
            try {
                Window window = _dialog.getDialog().getWindow();
                ViewGroup.LayoutParams params = window.getAttributes();
                params.width = _dialog.getView().getWidth();
                window.setAttributes((android.view.WindowManager.LayoutParams) params);
            } catch (Exception e) {
                L.e("changeWidth", e);
            }
        }
    }

    private static class CountdownRunnable implements Runnable {
        private int _remainderTime = 60;
        private ReadCardDialog _dialog;
        private TextView _textView;

        public CountdownRunnable(ReadCardDialog dialog, TextView textView) {
            _dialog = dialog;
            _textView = textView;
        }

        @Override
        public void run() {
            _textView.setText(String.valueOf(_remainderTime));
            if (_remainderTime > 0) {
                _textView.postDelayed(this, 1000);
            } else {
                _dialog.dismiss();
            }
            _remainderTime--;
        }
    }

    private static class ReadCardInfoTask extends AsyncTask<Void, String, ReadCardInfo> {
        private ReadCardDialog _dialog;

        public ReadCardInfoTask(ReadCardDialog dialog) {
            _dialog = dialog;
        }

        @Override
        protected ReadCardInfo doInBackground(Void... params) {
            ReadCardInfo readCard = new ReadCardInfo();

            String account = LoginInfo.get().getAccount();
            String serverId = _dialog.getServerId();
            int buildingId = _dialog.getBuildingId();
            MaxService service = MaxService.get();

            try {
                while (!isCancelled()) {
                    CardInfoResult result = service.readCardInfo(account, serverId, buildingId);
                    if (result.getCardno() != 0) {
                        CardInfo card = new CardInfo();
                        card.setCardno(result.getCardno());
                        card.setCardtype(result.getCardtype());
                        card.setPersonid(result.getPersonid());
                        card.setTime(result.getTime());

                        readCard.setCard(card);

                        break;
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
                L.e("readCardInfo", e);

                readCard.setError(e.getMessage());
            }

            try {
                service.cancelReadCardInfo(account, serverId, buildingId);
            } catch (Exception e) {
                L.e("cancelReadCardInfo", e);
            }

            return readCard;
        }

        @Override
        protected void onPreExecute() {
            _dialog.startCountdown();
        }

        @Override
        protected void onPostExecute(ReadCardInfo cardInfo) {
            Activity activity = _dialog.getActivity();
            if (null != activity && activity instanceof OnCardInfoListener) {
                ((OnCardInfoListener) activity).onCardInfoResult(cardInfo.getCard(), cardInfo.getError());
            }

            _dialog.dismiss();
            _dialog = null;
        }
    }

    private static class ReadCardInfo {
        private CardInfo _card;
        private String _error;

        public CardInfo getCard() {
            return _card;
        }

        public void setCard(CardInfo card) {
            _card = card;
        }

        public String getError() {
            return _error;
        }

        public void setError(String error) {
            this._error = error;
        }
    }

    public interface OnCardInfoListener {
        void onCardInfoResult(CardInfo cardInfo, String error);
    }
}
