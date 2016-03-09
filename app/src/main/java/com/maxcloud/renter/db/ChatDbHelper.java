package com.maxcloud.renter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maxcloud.renter.entity.user.MsgContent;
import com.maxcloud.renter.entity.user.MsgGroup;
import com.maxcloud.renter.util.FileHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：聊天记录数据库帮助类。
 * 作    者：向晓阳
 * 时    间：2016/3/8
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class ChatDbHelper extends SQLiteOpenHelper {
    private final static int DATABASE_VERSION = 7;
    public static final String DB_NAME = "Chat.dat";
    private static SimpleDateFormat mDateFmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final Map<Integer, String> mUpSql = new HashMap<Integer, String>();
    private static final Object mLock = new Object();

    private static void addVersion7SQL() {
        mUpSql.put(7, "ALTER TABLE History ADD [FromType] INTEGER DEFAULT 0;ALTER TABLE History ADD [ToType] INTEGER DEFAULT 0;");
    }

    private String dbPath;
    private File dbf = null;
    private SQLiteDatabase mDatabase = null;
    private String _account;

    public ChatDbHelper(Context context, String account) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        _account = account;
        try {
            dbPath = FileHelper.getDataDirectory();
        } catch (Exception e) {
            dbPath = null;
        }

        addVersion7SQL();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE [History](");
        sql.append("    [LocalId] INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append("    [Type] INTEGER,");
        sql.append("    [Id] BIGINT,");
        sql.append("    [FromAccount] VARCHAR(50),");
        sql.append("    [FromName] VARCHAR(50),");
        sql.append("    [ToAccount] VARCHAR(50),");
        sql.append("    [ToName] VARCHAR(50),");
        sql.append("    [Content] VARCHAR(500),");
        sql.append("    [CreateTime] VARCHAR(19),");
        sql.append("    [GroupAccount] VARCHAR(50),");
        sql.append("    [GroupName] VARCHAR(50),");
        sql.append("    [IsSucceed] INTEGER)");

        db.execSQL(sql.toString());

        db.setVersion(DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if (oldVersion < 8) {
        db.execSQL("DROP TABLE IF EXISTS History");
        onCreate(db);
//        } else {
//            for (int i = oldVersion + 1; i <= newVersion; i++) {
//                if (mUpSql.containsKey(i)) {
//                    db.execSQL(mUpSql.get(i));
//                }
//            }
//
//            db.setVersion(newVersion);
//        }
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mDatabase != null) {
            if (!mDatabase.isOpen()) {
                // darn! the user closed the database by calling mDatabase.close()
                mDatabase = null;
            } else if (!mDatabase.isReadOnly()) {
                return mDatabase;  // The database is already open for business
            }
        }

        synchronized (mLock) {
            boolean success = false;
            File dbp = createDbFile();
            if (null == dbp) {
                return null;
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbf, null);

            try {
                int version = db.getVersion();
                if (version != DATABASE_VERSION) {
                    db.beginTransaction();
                    try {
                        if (version == 0) {
                            onCreate(db);
                        } else if (version < DATABASE_VERSION) {
                            onUpgrade(db, version, DATABASE_VERSION);
                        }

                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                }

                onOpen(db);
                success = true;
                return db;
            } finally {
                if (success) {
                    if (mDatabase != null) {
                        mDatabase.close();
                    }
                    mDatabase = db;
                } else {
                    if (db != null) {
                        db.close();
                    }
                }
            }
        }
    }

    public void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public long maxId() {
        synchronized (mLock) {
            SQLiteDatabase db = this.getWritableDatabase();
            if (null == db) {
                return 0;
            }

            Cursor cursor = db.rawQuery("SELECT MAX(Id) FROM History", null);
            if (cursor.moveToNext()) {
                return cursor.getLong(0);
            }
            return 0;
        }
    }

    public boolean insertHistory(MsgContent msgContent, boolean isSucceed) {
        synchronized (mLock) {
            SQLiteDatabase db = this.getWritableDatabase();
            if (null == db) {
                return false;
            }

            long id = msgContent.getId();
            if (id == 0) {
                id = maxId() + 1;
                msgContent.setId(id);
            }

            String groupAccount = msgContent.getFromAccount();
            String groupName = msgContent.getFromName();
            if (_account.equals(groupAccount)) {
                groupAccount = msgContent.getToAccount();
                groupName = msgContent.getToName();
            }

            Cursor cursor = db.rawQuery("SELECT LocalId FROM History WHERE ID = ?", new String[]{String.valueOf(id)});
            if (cursor.moveToNext()) {
                msgContent.setLocalId(cursor.getLong(0));

                Object values[] = new Object[]{
                        msgContent.getType(), msgContent.getFromAccount(), msgContent.getFromName(),
                        msgContent.getToAccount(), msgContent.getToName(), msgContent.getContent(),
                        mDateFmt.format(msgContent.getCreateTime()), groupAccount, groupName, isSucceed ? 1 : 0, id
                };

                db.execSQL("UPDATE History SET Type = ?,FromAccount = ?,FromName = ?,ToAccount = ?,ToName = ?,Content = ?,CreateTime = ?,GroupAccount = ?,GroupName = ?,IsSucceed = ? WHERE Id = ?", values);
            } else {
                Object values[] = new Object[]{
                        msgContent.getType(), msgContent.getFromAccount(), msgContent.getFromName(),
                        msgContent.getToAccount(), msgContent.getToName(), msgContent.getContent(),
                        mDateFmt.format(msgContent.getCreateTime()), groupAccount, groupName, isSucceed ? 1 : 0
                };

                db.execSQL("INSERT INTO History(Type,Id,FromAccount,FromName,ToAccount,ToName,Content,CreateTime,GroupAccount,GroupName,IsSucceed) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", values);
                cursor = db.rawQuery("SELECT last_insert_rowid()", null);
                if (cursor.moveToNext()) {
                    msgContent.setLocalId(cursor.getLong(0));
                }
            }

            return true;
        }
    }

    public boolean updateState(long localId, long id, boolean isSucceed) {
        synchronized (mLock) {
            SQLiteDatabase db = this.getWritableDatabase();
            if (null == db) {
                return false;
            }

            Object values[] = new Object[]{id,
                    isSucceed ? 1 : 0, localId
            };

            db.execSQL("UPDATE History SET Id = ?, IsSucceed = ? WHERE LocalId = ?", values);

            return true;
        }
    }

    public MsgGroup[] groupMessage() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (null == db) {
            return null;
        }

        StringBuilder sql = new StringBuilder("SELECT NM.GroupAccount,NM.GroupName,NM.Content,NM.CreateTime FROM (");
        sql.append("  SELECT MAX(ID) ID");
        sql.append("  FROM History WHERE (FromAccount = ? OR ToAccount = ?) GROUP BY GroupAccount) TM ");
        sql.append("LEFT JOIN History NM ON NM.ID = TM.ID");

        List<MsgGroup> msgGroups = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), new String[]{_account, _account});
        while (cursor.moveToNext()) {
            String groupAccount = cursor.getString(0);
            String groupName = cursor.getString(1);
            String content = cursor.getString(2);
            Date sendTime = null;
            try {
                sendTime = mDateFmt.parse(cursor.getString(3));
            } catch (Exception e) {

            }

            MsgGroup group = new MsgGroup(0, groupAccount, groupName, content, sendTime);
            msgGroups.add(group);
        }

        MsgGroup arrays[] = new MsgGroup[msgGroups.size()];
        msgGroups.toArray(arrays);

        return arrays;
    }

    /**
     * 查询历史消息。
     *
     * @param otherId 对方账号。
     * @param minId   最小ID。
     * @param count   查询条数。
     * @return 历史消息列表。
     */
    public MsgContent[] queryHistory(String otherId, long minId, int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (null == db) {
            return null;
        }

        StringBuilder sql = new StringBuilder("SELECT LocalId,Type,Id,FromAccount,FromName,ToAccount,ToName,Content,CreateTime,IsSucceed");
        sql.append(" FROM History");
        sql.append(" WHERE ((FromAccount = ? AND ToAccount = ?) OR (FromAccount = ? AND ToAccount = ?))");
        if (minId != 0) {
            sql.append(String.format(" AND Id < %d", minId));
        }
        sql.append(String.format(" ORDER BY Id DESC LIMIT 0,%d", count));

        List<MsgContent> notifyViews = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), new String[]{_account, otherId, otherId, _account});
        while (cursor.moveToNext()) {
            long localId = cursor.getLong(0);
            int type = cursor.getInt(1);
            long id = cursor.getLong(2);
            String fromId = cursor.getString(3);
            String fromName = cursor.getString(4);
            String toId = cursor.getString(5);
            String toName = cursor.getString(6);
            String content = cursor.getString(7);
            Date sendTime = null;
            try {
                sendTime = mDateFmt.parse(cursor.getString(8));
            } catch (Exception e) {

            }
            int isSucceed = cursor.getInt(9);

            MsgContent msgContent = new MsgContent();
            msgContent.setLocalId(localId);
            msgContent.setType(type);
            msgContent.setId(id);
            msgContent.setContent(content);
            msgContent.setCreateTime(sendTime);

            msgContent.setFromAccount(fromId);
            msgContent.setFromName(fromName);

            msgContent.setToAccount(toId);
            msgContent.setToName(toName);

            msgContent.setSucceed(isSucceed == 1);
            msgContent.setMyselfSend(fromId.equals(_account));

            notifyViews.add(msgContent);
        }

        MsgContent arrays[] = new MsgContent[notifyViews.size()];
        notifyViews.toArray(arrays);

        return arrays;
    }

    private File createDbFile() {
        File dbp = new File(dbPath);
        dbf = new File(String.format("%s%s", dbPath, DB_NAME));
        if (!dbp.exists()) {
            dbp.mkdir();
        }
        if (!dbf.exists()) {
            try {
                dbf.createNewFile();
            } catch (Exception e) {
                return null;
            }
        }

        return dbf;
    }
}
