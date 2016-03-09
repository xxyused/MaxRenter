package com.maxcloud.renter.util.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.maxcloud.renter.util.IdCardNoHelper;
import com.maxcloud.renter.util.PhoneNoHelper;

public class PhoneNoWatcher implements TextWatcher {
	private EditText mEditText;
	private int mBeforeLength = 0;
	private boolean mIsChanged = false;
	private boolean mIsBatchEdit = false;

	private int mLocation = 0;// 记录光标的位置
	private StringBuffer mBuffer = new StringBuffer();
	private int mPlaceholderCount = 0;

	public PhoneNoWatcher(EditText editText) {
		mEditText = editText;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (mIsBatchEdit) {
			return;
		}

		mBeforeLength = s.length();
		if (mBuffer.length() > 0) {
			mBuffer.delete(0, mBuffer.length());
		}
		mPlaceholderCount = IdCardNoHelper.getPlaceholderCount(s);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (mIsBatchEdit) {
			return;
		}

		int textLength = s.length();
		mBuffer.append(s.toString());
		if (textLength == mBeforeLength || mIsChanged) {
			mIsChanged = false;
			return;
		}
		mIsChanged = true;
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (mIsBatchEdit || !mIsChanged) {
			return;
		}

		mLocation = mEditText.getSelectionEnd();

		mEditText.beginBatchEdit();
		mIsBatchEdit = true;

		int placeholderCount = PhoneNoHelper.formatPhoneNo(s);

		if (placeholderCount > mPlaceholderCount) {
			mLocation += (placeholderCount - mPlaceholderCount);
		}

		if (mLocation > s.length()) {
			mLocation = s.length();
		} else if (mLocation < 0) {
			mLocation = 0;
		}

		mEditText.setSelection(mLocation);
		mEditText.endBatchEdit();

		mIsBatchEdit = false;
		mIsChanged = false;
	}
}
