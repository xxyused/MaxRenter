package com.maxcloud.renter.activity.user;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.idcard.OcrHelper;
import com.idcard.OcrIdCard;
import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.entity.oss.OssObject;
import com.maxcloud.renter.entity.oss.StsAccessInfo;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.StsInfoResult;
import com.maxcloud.renter.util.AliOssHelper;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;

import java.io.File;

public class EditUserInfoActivity extends CustomTitleActivity {
    private final int TAKE_PICTURE = 1;

    private ImageView _imgIdCard;
    private View _imgIdCardTip;
    private View btnAddIdImg;
    private EditText _edtName;
    private EditText _edtIdNo;
    private EditText _edtAddress;

    private String _idCardPath;
    private ClickListener _onClick = new ClickListener(this);

    /**
     * 更新身份证信息。
     *
     * @param idCard 识别出来的身份证信息。
     * @param e      错误。
     */
    public void updateIdCardInfo(OcrIdCard idCard, Exception e) {
        if (null != e) {
            showError(e.getMessage());
        } else {
            _edtName.setText(idCard.getName());
            _edtIdNo.setText(idCard.getCardNum());
            _edtAddress.setText(idCard.getAddress());
        }
    }

    /**
     * 启动拍照。
     */
    public void startImageCapture() {
        _idCardPath = FileHelper.createCacheFile();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 加载路径
        Uri uri = Uri.fromFile(new File(_idCardPath));
        // 指定存储路径，这样就可以保存原图了
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, TAKE_PICTURE);
    }

    public void submit() {
        new SubmitDataTask(this).execute(_idCardPath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        _imgIdCard = (ImageView) findViewById(R.id.imgIdCard);
        _imgIdCardTip = findViewById(R.id.imgIdCardTip);
        btnAddIdImg = findViewById(R.id.btnReaddIdImg);
        View txvIdCardTip = findViewById(R.id.txvIdCardTip);
        _edtName = (EditText) findViewById(R.id.edtName);
        _edtIdNo = (EditText) findViewById(R.id.edtIdNo);
        _edtAddress = (EditText) findViewById(R.id.edtAddress);
        Button btnOk = (Button) findViewById(R.id.btnOk);

        _imgIdCardTip.setVisibility(View.VISIBLE);
        txvIdCardTip.setVisibility(View.VISIBLE);
        _edtName.setEnabled(true);
        _edtName.setHint(R.string.full_name_hint);
        _edtIdNo.setEnabled(true);
        _edtIdNo.setHint(R.string.id_card_no_hint);
        _edtAddress.setEnabled(true);
        _edtAddress.setHint(R.string.user_info_address_hint);
        btnOk.setText(R.string.user_info_edit_submit);

        _imgIdCardTip.setOnClickListener(_onClick);
        btnAddIdImg.setOnClickListener(_onClick);
        btnOk.setOnClickListener(_onClick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                //Bitmap bm = (Bitmap) data.getExtras().get("data");
                _imgIdCard.setImageBitmap(BitmapFactory.decodeFile(_idCardPath));
                _imgIdCardTip.setVisibility(View.GONE);
                btnAddIdImg.setVisibility(View.VISIBLE);

                new RecIdCardTask(this).execute(_idCardPath);
            }
        }
    }

    private static class ClickListener implements View.OnClickListener {
        private EditUserInfoActivity _activity;

        public ClickListener(EditUserInfoActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.imgIdCardTip:
                    _activity.startImageCapture();
                    break;
                case R.id.btnReaddIdImg:
                    _activity.startImageCapture();
                    break;
                case R.id.btnOk:
                    _activity.submit();
                    break;
            }

            L.useClick(v.getContext(), viewId);
        }
    }

    private static class RecIdCardTask extends AsyncTask<String, Void, OcrIdCardError> {
        private EditUserInfoActivity _activity;

        public RecIdCardTask(EditUserInfoActivity activity) {
            _activity = activity;
        }

        @Override
        protected OcrIdCardError doInBackground(String... params) {
            OcrIdCardError ocrIdCard = new OcrIdCardError();
            try {
                ocrIdCard.OcrIdCard = OcrHelper.recIdCard(params[0]);
            } catch (Exception e) {
                ocrIdCard.Error = e;
            }

            return ocrIdCard;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _activity.showProgressDialog(R.string.user_info_edit_rec_id_card);
        }

        @Override
        protected void onPostExecute(OcrIdCardError ocrIdCard) {
            super.onPostExecute(ocrIdCard);

            _activity.closeProgressDialog();
            _activity.updateIdCardInfo(ocrIdCard.OcrIdCard, ocrIdCard.Error);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            _activity.closeProgressDialog();
        }
    }

    private static class SubmitDataTask extends AsyncTask<String, Void, Void> {
        private EditUserInfoActivity _activity;

        public SubmitDataTask(EditUserInfoActivity activity) {
            _activity = activity;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String account = LoginInfo.get().getAccount();
                MaxService service = MaxService.get();
                StsInfoResult result = service.imageStsByPut(account, account, "432522198609235795", MaxService.IMAGE_TYPE_APPLY_ID_CARD);
                StsAccessInfo accessInfo = new StsAccessInfo();
                accessInfo.setAccessKey(result.getKey());
                accessInfo.setAccessSecret(result.getSecret());
                accessInfo.setAccessToken(result.getToken());

                OssObject ossObject = new OssObject();
                ossObject.setService(result.getEndpoint());
                ossObject.setBucket(result.getBucket());
                ossObject.setPath(result.getPath());

                AliOssHelper.uploadImage(accessInfo, ossObject, params[0]);
            } catch (Exception e) {
                L.e("uploadIdCard", e);
            }
            return null;
        }
    }

    private static class OcrIdCardError {
        public OcrIdCard OcrIdCard;
        public Exception Error;
    }
}
