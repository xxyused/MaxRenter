package com.maxcloud.renter.activity.build;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.entity.build.CardInfo;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.BuildInfoListResult;
import com.maxcloud.renter.service.entity.BuildInfoResult;
import com.maxcloud.renter.util.IdCardNoHelper;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.watcher.IdCardNoWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveCardActivity extends CustomTitleActivity implements ReadCardDialog.OnCardInfoListener {
    private static final String BUNDLE_NAME_INPUT_DATA = "InputData";
    private TextView mTxvBuild;
    private View mTipAddCard;
    private TextView mTxvCardType;
    private TextView mTxvCardTime;
    private EditText mEdtIdCardNo;
    private View _btnOk;

    private SimpleAdapter _adapter;
    private InputData _inputData = new InputData();
    private ClickListener _onClick = new ClickListener(this, _inputData);
    private IdCardNoChanged _idCardNoChanged = new IdCardNoChanged(this);
    private LoadBuildInfoTask _loadTask;
    private ActiveCardTask _task;

    @Override
    public void onCardInfoResult(CardInfo cardInfo, String error) {
        if (!TextUtils.isEmpty(error)) {
            showError(error);
        }

        if (null != cardInfo) {
            _inputData.CardNo = cardInfo.getCardno();
            _inputData.LowPersonId = cardInfo.getPersonid();
            _inputData.CardType = cardInfo.getCardtype();
            _inputData.Time = cardInfo.getTime();
        }

        showCardInfo();
    }

    /**
     * 启动激活卡任务。
     */
    public void startActiveCardTask() {
        if (null != _task) {
            _task.cancel(true);
        }

        _task = new ActiveCardTask(this, _inputData);
        _task.execute();
    }

    /**
     * 选择指定索引处的楼栋。
     *
     * @param index 楼栋所在索引。
     */
    public void selectedBuild(int index) {
        _inputData.fillFromIndex(index);
        mTxvBuild.setText(_inputData.BuildName);

        deleteCard();
    }

    /**
     * 删除卡信息。
     */
    public void deleteCard() {
        _inputData.clearCardInfo();

        showCardInfo();
    }

    /**
     * 检查输入数据是否有效。
     *
     * @return 如果有效，则返回 true ，否则，返回 false 。
     */
    public String checkInputData() {
        View focusView = null;
        String idCardNo = getIdCardNo();

        if (TextUtils.isEmpty(idCardNo)) {
            mEdtIdCardNo.setError(getString(R.string.id_card_no_empty));
            focusView = mEdtIdCardNo;
        }

        if (!IdCardNoHelper.verify(idCardNo)) {
            mEdtIdCardNo.setError(getString(R.string.id_card_no_invalid));
            focusView = mEdtIdCardNo;
        }

        if (null == focusView) {
            return idCardNo;
        } else {
            focusView.requestFocus();
            return null;
        }
    }

    /**
     * 显示楼栋选择对话框。
     */
    public void showBuildListDialog() {
        if (_inputData._items.size() > 0) {
            if (null == _adapter) {
                _adapter = new SimpleAdapter(this, _inputData._items, R.layout.item_list, new String[]{"name"}, new int[]{R.id.item});
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setAdapter(_adapter, new BuildSelectedListener(this));

            builder.show();
        } else {
            showToast(R.string.active_card_build_empty);
        }
    }

    /**
     * 显示读取卡信息对话框。
     */
    public void showReadCardDialog() {
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        Bundle args = new Bundle();
        args.putString(ReadCardDialog.PRO_SERVER_ID, _inputData.ServerId);
        args.putInt(ReadCardDialog.PRO_BUILDING_ID, _inputData.BuildingId);
        args.putString(ReadCardDialog.PRO_BUILDING_NAME, _inputData.BuildName);

        ReadCardDialog readCardDialog = new ReadCardDialog();
        readCardDialog.setArguments(args);
        readCardDialog.show(ft, getTitle().toString());
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_active_card);

        if (null != savedState) {
            if (savedState.containsKey(BUNDLE_NAME_INPUT_DATA)) {
                _inputData = savedState.getParcelable(BUNDLE_NAME_INPUT_DATA);
            }
        }

        mEdtIdCardNo = (EditText) findViewById(R.id.edtIdCardNo);
        mTxvBuild = (TextView) findViewById(R.id.txvBuild);
        mTipAddCard = findViewById(R.id.tipAddCard);
        mTxvCardType = (TextView) findViewById(R.id.txvCardType);
        mTxvCardTime = (TextView) findViewById(R.id.txvCardTime);
        View btnSelectBuild = findViewById(R.id.btnSelectBuild);
        View btnDelete = findViewById(R.id.btnDeleteCard);
        _btnOk = findViewById(R.id.btnOk);

        mEdtIdCardNo.addTextChangedListener(new IdCardNoWatcher(mEdtIdCardNo));
        btnSelectBuild.setOnClickListener(_onClick);
        mTipAddCard.setOnClickListener(_onClick);
        btnDelete.setOnClickListener(_onClick);
        _btnOk.setOnClickListener(_onClick);

        mEdtIdCardNo.removeTextChangedListener(_idCardNoChanged);
        LoginInfo loginInfo = LoginInfo.get();
        if (null != loginInfo && !TextUtils.isEmpty(loginInfo.getIdCardNo())) {
            _inputData.IdCardNo = loginInfo.getIdCardNo();
            mEdtIdCardNo.setEnabled(false);
        } else {
            mEdtIdCardNo.addTextChangedListener(_idCardNoChanged);
        }

        mEdtIdCardNo.setText(_inputData.IdCardNo);
        mTxvBuild.setText(_inputData.BuildName);

        if (!TextUtils.isEmpty(_inputData.IdCardNo)) {
            reloadBuild();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_NAME_INPUT_DATA, _inputData);

        super.onSaveInstanceState(outState);
    }

    private String getIdCardNo() {
        return IdCardNoHelper.parseIdCardNo(mEdtIdCardNo.getText()).trim();
    }

    private void reloadBuild() {
        hideSoftInput();

        if (null != _loadTask) {
            _loadTask.cancel(true);
        }

        _loadTask = new LoadBuildInfoTask(this, _inputData);
        _loadTask.execute(LoginInfo.get().getAccount());
    }

    private void showCardInfo() {
        if (_inputData.CardNo == 0) {
            mTipAddCard.setVisibility(View.VISIBLE);
            mTxvCardType.setText(null);
            mTxvCardTime.setText(null);

            _btnOk.setEnabled(false);
        } else {
            mTxvCardType.setText(_inputData.CardType == 3 ? "身份证" : "IC卡");
            mTxvCardTime.setText(_inputData.Time);
            mTipAddCard.setVisibility(View.GONE);

            _btnOk.setEnabled(true);
        }
    }

    private static class InputData implements Parcelable {
        public String IdCardNo;
        public String ServerId;
        public int BuildingId;
        public String BuildName;
        public long CardNo;
        public int CardType;
        public String Time;
        public int LowPersonId;
        private List<Map<String, String>> _items = new ArrayList<>();

        public void fillFromIndex(int index) {
            Map<String, String> item = _items.get(index);

            ServerId = item.get("serverId");
            BuildingId = Integer.valueOf(item.get("id"));
            BuildName = item.get("name");
        }

        public void clearItems() {
            _items.clear();
        }

        public void addItem(Map<String, String> item) {
            _items.add(item);
        }

        public void clearCardInfo() {
            CardNo = 0;
            LowPersonId = 0;
            CardType = 0;
            Time = null;
        }

        public static final Parcelable.Creator<InputData> CREATOR = new Parcelable.Creator<InputData>() {
            public InputData createFromParcel(Parcel source) {
                InputData inputData = new InputData();
                inputData.IdCardNo = source.readString();
                inputData.ServerId = source.readString();
                inputData.BuildingId = source.readInt();
                inputData.BuildName = source.readString();
                inputData.CardNo = source.readLong();
                inputData.CardType = source.readInt();
                inputData.Time = source.readString();
                inputData.LowPersonId = source.readInt();

                return inputData;
            }

            public InputData[] newArray(int size) {
                return new InputData[size];
            }
        };

        /**
         * Describe the kinds of special objects contained in this Parcelable's
         * marshalled representation.
         *
         * @return a bitmask indicating the set of special object types marshalled
         * by the Parcelable.
         */
        @Override
        public int describeContents() {
            return 0;
        }

        /**
         * Flatten this object in to a Parcel.
         *
         * @param dest  The Parcel in which the object should be written.
         * @param flags Additional flags about how the object should be written.
         *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
         */
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(IdCardNo);
            dest.writeString(ServerId);
            dest.writeInt(BuildingId);
            dest.writeString(BuildName);
            dest.writeLong(CardNo);
            dest.writeInt(CardType);
            dest.writeString(Time);
            dest.writeInt(LowPersonId);
        }
    }

    private static class IdCardNoChanged implements TextWatcher {
        private ActiveCardActivity _activity;

        public IdCardNoChanged(ActiveCardActivity activity) {
            _activity = activity;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String idCardNo = IdCardNoHelper.parseIdCardNo(s);
            InputData inputData = _activity._inputData;
            if (idCardNo.length() == 18) {
                if (!idCardNo.equals(inputData.IdCardNo)) {
                    inputData.IdCardNo = idCardNo;
                    if (IdCardNoHelper.verify(inputData.IdCardNo)) {
                        _activity.reloadBuild();
                    } else {
                        _activity.showToast(R.string.id_card_no_invalid);
                    }
                }
            } else {
                inputData.IdCardNo = idCardNo;
//                if (mBuildDatas.getCount() > 0) {
//                    mBuildDatas.clear();
//                    mTxvBuild.setText(null);
//                }
            }

//            if (null != _serverId) {
//                _serverId = null;
//                _buildId = 0;
//                _buildName = null;
//
//                mMsgHandler.sendMessage(WHAT_SHOW_BUILD_NAME);
//            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private static class ClickListener implements View.OnClickListener {
        private ActiveCardActivity _activity;
        private InputData _inputData;

        public ClickListener(ActiveCardActivity activity, InputData inputData) {
            _activity = activity;
            _inputData = inputData;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            String idCardNo;

            try {
                switch (viewId) {
                    case R.id.btnSelectBuild:
                        idCardNo = _activity.checkInputData();
                        if (null != idCardNo) {
                            _activity.showBuildListDialog();

//                            if (mBuildDatas.getCount() <= 0) {
//                                _activity.reloadBuild(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        mMsgHandler.sendMessage(WHAT_SHOW_BUILD_SELECT);
//                                    }
//                                });
//                            } else {
//                                mMsgHandler.sendMessage(WHAT_SHOW_BUILD_SELECT);
//                            }
                        }
                        break;
                    case R.id.tipAddCard:
                        idCardNo = _activity.checkInputData();
                        if (null != idCardNo) {
                            if (null == _inputData.ServerId || _inputData.BuildingId == 0) {
                                _activity.showToast(R.string.active_card_empty_build);
                                return;
                            }

                            _activity.showReadCardDialog();
                        }
                        break;
                    case R.id.btnDeleteCard:
                        _activity.deleteCard();
                        break;
                    case R.id.btnOk:
                        idCardNo = _activity.checkInputData();
                        if (null != idCardNo) {
                            if (null == _inputData.ServerId || _inputData.BuildingId == 0) {
                                _activity.showToast(R.string.active_card_empty_build);
                                return;
                            }
                            if (_inputData.CardNo == 0) {
                                _activity.showToast(R.string.active_card_self_card);
                                return;
                            }

                            _activity.startActiveCardTask();
                        }
                        break;
                }
            } catch (Exception e) {
                L.e("onClick", e);

                _activity.showError(e.getMessage());
            }

            L.useClick(v.getContext(), viewId);
        }


    }

    private static class BuildSelectedListener implements DialogInterface.OnClickListener {
        private ActiveCardActivity _activity;

        public BuildSelectedListener(ActiveCardActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            _activity.selectedBuild(which);
        }
    }

    private static class LoadBuildInfoTask extends AsyncTask<String, Void, String> {
        private ActiveCardActivity _activity;
        private InputData _inputData;

        public LoadBuildInfoTask(ActiveCardActivity activity, InputData inputData) {
            _activity = activity;
            _inputData = inputData;
        }

        @Override
        protected String doInBackground(String... params) {
            String account = params[0];

            try {
                MaxService service = MaxService.get();
                BuildInfoListResult result = service.getBuildByIdCardNo(account, _inputData.IdCardNo);
                _inputData.clearItems();

                for (BuildInfoResult buildInfo : result.getData()) {
                    Map<String, String> item = new HashMap<>();
                    item.put("serverId", buildInfo.getServerid());
                    item.put("id", buildInfo.getId());
                    item.put("name", buildInfo.getName());

                    _inputData.addItem(item);
                }
            } catch (Exception e) {
                L.e("getBuildByIdCardNo", e);

                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _activity.showProgressDialog(R.string.active_card_loading_build);
        }

        @Override
        protected void onPostExecute(String error) {
            _activity.closeProgressDialog();
            if (!TextUtils.isEmpty(error)) {
                _activity.showError(error);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            _activity.closeProgressDialog();
        }
    }

    private static class ActiveCardTask extends AsyncTask<Void, Void, String> {
        private ActiveCardActivity _activity;
        private InputData _inputData;

        public ActiveCardTask(ActiveCardActivity activity, InputData inputData) {
            _activity = activity;
            _inputData = inputData;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                MaxService service = MaxService.get();
                service.activeCard(LoginInfo.get().getAccount(), _inputData.ServerId, _inputData.LowPersonId);
            } catch (Exception e) {
                L.e("activeCard", e);

                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _activity.showProgressDialog(R.string.active_card_being_active);
        }

        @Override
        protected void onPostExecute(String error) {
            _activity.closeProgressDialog();
            if (TextUtils.isEmpty(error)) {
                _activity.showToast(R.string.active_card_success);
                _activity.finish();
            } else {
                _activity.showError(error);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            _activity.closeProgressDialog();
        }
    }
}
