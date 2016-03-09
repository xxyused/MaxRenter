package com.maxcloud.renter.util.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.maxcloud.renter.util.IdCardNoHelper;

public class IdCardNoWatcher implements TextWatcher {
	private EditText mEditText;
	private int mBeforeLength = 0;
	private boolean mIsChanged = false;
	private boolean mIsBatchEdit = false;

	public IdCardNoWatcher(EditText editText) {
		mEditText = editText;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (mIsBatchEdit) {
			return;
		}

		mBeforeLength = s.length();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (mIsBatchEdit) {
			return;
		}

		int textLength = s.length();
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

		mIsBatchEdit = true;
		mEditText.beginBatchEdit();

		int location = IdCardNoHelper.formatIdCardNo(s, mEditText.getSelectionEnd());

		mEditText.setSelection(location);

		mEditText.endBatchEdit();
		mIsBatchEdit = false;
		mIsChanged = false;
	}
}
