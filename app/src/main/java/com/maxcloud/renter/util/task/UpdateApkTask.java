package com.maxcloud.renter.util.task;

import android.os.AsyncTask;

import com.maxcloud.renter.util.AppHelper;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * 描    述：
 * 作    者：向晓阳
 * 时    间：2016/3/5
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class UpdateApkTask extends AsyncTask<Void, Integer, Exception> {
    private OnReportProgress _reportPro;
    private int _oldProgress = 0;

    public UpdateApkTask(OnReportProgress reportProg) {
        _reportPro = reportProg;
    }

    @Override
    protected Exception doInBackground(Void... params) {
        FileOutputStream fileStream = null;
        InputStream stream = null;

        try {
            File apkFile = new File(FileHelper.getUpdateApk());
            if (apkFile.exists()) {
                apkFile.delete();
            }
            File pFile = apkFile.getParentFile();
            if (!pFile.exists()) {
                pFile.mkdirs();
            }

            ResponseBody response = FileHelper.createResponse(AppHelper.isDebug() ? AppHelper.APK_D_FULL_PATH : AppHelper.APK_FULL_PATH);
            stream = response.byteStream();
            double totalSize = response.contentLength();
            if (totalSize <= 0) {
                totalSize = 6262784;
            }

            double saveLength = 0;
            fileStream = new FileOutputStream(apkFile);
            byte buffer[] = new byte[4 * 1024];
            int length = stream.read(buffer);
            _oldProgress = 100;
            publishProgress(0);

            while (length != -1) {
                fileStream.write(buffer, 0, length);
                fileStream.flush();
                saveLength += length;

                publishProgress((int) (saveLength / totalSize * 100));

                length = stream.read(buffer);
            }

            publishProgress(100);
        } catch (Exception e) {
            return e;
        } finally {
            try {
                if (null != fileStream) {
                    fileStream.close();
                }
                if (null != stream) {
                    stream.close();
                }
            } catch (Exception e) {
                L.e("downloadApk", e);
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        _reportPro.onPreUpdate();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 100) {
            progress = 100;
        }

        if (_oldProgress != progress) {
            _oldProgress = progress;
            _reportPro.onProgressUpdate(progress);
        }
    }

    @Override
    protected void onPostExecute(Exception e) {
        _reportPro.onPostUpdate(e);
    }

    public interface OnReportProgress {
        void onPreUpdate();

        void onProgressUpdate(int progress);

        void onPostUpdate(Exception e);
    }
}
