package com.maxcloud.renter.activity.message;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.db.ChatDbHelper;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.entity.user.MsgContent;
import com.maxcloud.renter.entity.user.MsgGroup;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.message.MsgGroupAdapter;

public class MyMessageActivity extends CustomTitleActivity {
    private ListView mListView;

    private MsgGroupAdapter _adapter;
    private ChatDbHelper mDbHelper;

    /**
     * 保存历史记录。
     *
     * @param msgContents 要保存的消息列表。
     */
    public void saveMsgContent(MsgContent... msgContents) {
        try {
            String account = LoginInfo.get().getAccount();
            for (MsgContent msgContent : msgContents) {
                mDbHelper.insertHistory(msgContent, true);

                String fromId = msgContent.getFromAccount();
                String fromName = msgContent.getFromName();
                if (account.equals(fromId)) {
                    fromId = msgContent.getToAccount();
                    fromName = msgContent.getToName();
                }

                _adapter.addItem(new MsgGroup(0, fromId, fromName, msgContent.getContent(), msgContent.getCreateTime()));
            }

            _adapter.notifyDataSetChanged();
        } catch (Exception e) {
            L.e("saveMsgContent", e);
        }
    }

    /**
     * 添加消息组。
     *
     * @param msgGroups 消息组列表。
     */
    public void updateMsgGroup(MsgGroup... msgGroups) {
        try {
            if (null != msgGroups) {
                for (MsgGroup msgGroup : msgGroups) {
                    _adapter.addItem(msgGroup);
                }
            }
            _adapter.notifyDataSetChanged();
        } catch (Exception e) {
            L.e("updateMsgGroup", e);
        }
    }

    /**
     * 同步历史消息。
     */
    public void syncHistoryMsg() {
        try {
            long maxId = mDbHelper.maxId();
            new SyncHistoryMsgTask(this).execute(maxId);
        } catch (Exception e) {
            L.e("syncHistoryMsg", e);
        }
    }

    /**
     * 显示聊天界面。
     *
     * @param index 要聊天消息组的索引。
     */
    public void showChat(int index) {
        MsgGroup msgGroup = _adapter.getItem(index);

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.BUNDLE_ACCOUNT, msgGroup.getAccount());
        intent.putExtra(ChatActivity.BUNDLE_NAME, msgGroup.getName());

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mListView = (ListView) findViewById(android.R.id.list);

        mDbHelper = new ChatDbHelper(this, LoginInfo.get().getAccount());
        _adapter = new MsgGroupAdapter(this);

        mListView.setAdapter(_adapter);
        mListView.setOnItemClickListener(new GroupMessageClickListener(this));

        new GroupMessageTask(this).execute();
    }

    private static class GroupMessageTask extends AsyncTask<Void, MsgGroup, Void> {
        private MyMessageActivity _activity;

        public GroupMessageTask(MyMessageActivity activity) {
            _activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                MaxService service = MaxService.get();
                String account = LoginInfo.get().getAccount();

                MsgGroup[] msgGroups = service.groupUnreadMessage(account);

                if (null != msgGroups && msgGroups.length > 0) {
                    publishProgress(msgGroups);
                }
            } catch (Exception e) {
                L.e("groupMessage", e);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _activity.showTitleProgress();
        }

        @Override
        protected void onProgressUpdate(MsgGroup... msgGroups) {
            super.onProgressUpdate(msgGroups);

            _activity.updateMsgGroup(msgGroups);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            _activity.syncHistoryMsg();
            _activity.hideTitleProgress();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            _activity.hideTitleProgress();
        }
    }

    private static class SyncHistoryMsgTask extends AsyncTask<Long, MsgContent, Void> {
        private MyMessageActivity _activity;
        private Object _historyLock = new Object();

        public SyncHistoryMsgTask(MyMessageActivity activity) {
            _activity = activity;
        }

        @Override
        protected Void doInBackground(Long... params) {
            try {
                long maxId = params[0];
                MaxService service = MaxService.get();
                String account = LoginInfo.get().getAccount();

                while (!isCancelled()) {
                    MsgContent[] msgContents = service.getHistoryMessage(account, maxId + 1);
                    if (null != msgContents && msgContents.length > 0) {
                        for (MsgContent msgContent : msgContents) {
                            maxId = Math.max(maxId, msgContent.getId());
                        }

                        publishProgress(msgContents);
                        synchronized (_historyLock) {
                            _historyLock.wait();
                        }
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                L.e("getHistoryMessage", e);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            synchronized (_historyLock) {
                _historyLock.notify();
            }
        }

        @Override
        protected void onProgressUpdate(MsgContent... msgContents) {
            super.onProgressUpdate(msgContents);

            try {
                for (MsgContent msgContent : msgContents) {
                    _activity.saveMsgContent(msgContent);
                }
            } finally {
                synchronized (_historyLock) {
                    _historyLock.notify();
                }
            }
        }
    }

    private static class GroupMessageClickListener implements AdapterView.OnItemClickListener {
        private MyMessageActivity _activity;

        public GroupMessageClickListener(MyMessageActivity activity) {
            _activity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _activity.showChat(position);
        }
    }
}
