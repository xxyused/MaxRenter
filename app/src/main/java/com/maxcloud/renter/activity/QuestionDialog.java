package com.maxcloud.renter.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.maxcloud.renter.util.L;

/**
 * Created by MAX-XXY on 2016/2/26.
 */
public class QuestionDialog {
    private AlertDialog _alertDialog;

    /**
     * 显示确认对话框。
     *
     * @param titleResId     对话框标题。
     * @param msgResId       对话框消息。
     * @param positiveButton 对话框按钮。
     */
    public QuestionDialog(Context context, @StringRes int titleResId, @StringRes int msgResId, DialogButton positiveButton) {
        this(context, titleResId, msgResId, positiveButton, null);
    }

    /**
     * 显示确认对话框。
     *
     * @param titleResId     对话框标题。
     * @param msgResId       对话框消息。
     * @param positiveButton 对话框确认按钮。
     * @param negativeButton 对话框取消按钮。
     * @param msgFormatArgs  对话框消息参数。
     */
    public QuestionDialog(Context context, @StringRes int titleResId, @StringRes int msgResId, DialogButton positiveButton, DialogButton negativeButton, Object... msgFormatArgs) {
        CharSequence title = context.getString(titleResId);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(context.getString(msgResId, msgFormatArgs));
        builder.setCancelable(false);
        _alertDialog = builder.create();

        if (null != positiveButton) {
            positiveButton._contextClass = context.getClass().getSimpleName();
            positiveButton._dialogTitle = title;
            positiveButton._text = context.getString(positiveButton._textResId);
            _alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveButton._text, positiveButton);
        }
        if (null != negativeButton) {
            negativeButton._contextClass = context.getClass().getSimpleName();
            negativeButton._dialogTitle = title;
            negativeButton._text = context.getString(negativeButton._textResId);
            _alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeButton._text, negativeButton);
        }
    }

    public void show() {
        _alertDialog.show();
    }

    public static class DialogButton implements DialogInterface.OnClickListener {
        private String _contextClass;
        private CharSequence _dialogTitle;
        private CharSequence _text;
        private int _textResId;

        public DialogButton(@StringRes int textResId) {
            _textResId = textResId;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            L.useClick(_contextClass, _dialogTitle, _text);
        }
    }
}
