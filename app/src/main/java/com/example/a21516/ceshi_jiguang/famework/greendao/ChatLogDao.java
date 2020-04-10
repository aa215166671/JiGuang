package com.example.a21516.ceshi_jiguang.famework.greendao;

//此代码由greenDAO生成，请勿编辑。
//：聊天记录表


import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.example.a21516.ceshi_jiguang.famework.greendao.model.ChatLog;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import java.nio.DoubleBuffer;
import java.util.IllegalFormatCodePointException;

/**
 * 聊天日志”表的DAO。
 */
public class ChatLogDao extends AbstractDao<ChatLog,Long> {
    public static final String TABLENAME = "CHAT_LOG";

    /**
     * 实体ChatLog的属性。
     * 可用于QueryBuilder和引用列名。
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, Long.class, "userId", false, "USER_ID");
        public final static Property Time = new Property(2, String.class, "time", false, "TIME");
        public final static Property Content = new Property(3, String.class, "content", false, "CONTENT");

    }

    public ChatLogDao(DaoConfig config) {
        super(config);
    }

    public ChatLogDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }
/**创建基础数据库表。
 * ifNotExists 为如果不存在
 */

public static void createTable(Database db,boolean ifNotExists){
    String constraint =ifNotExists?"IF NOT EXISTS":"";
    db.execSQL("CREATE TABLE"+constraint+"\"CHAT_LOG\"("+ //
            "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT,"+//0:id
            "\"USER_ID\" INTEGER,"+//1:userId
            "\"TIME\" TEXT,"+//2:time
            "\"CONTENT\" TEXT);");//content
}

 /**删除基础数据库表。*/
 public static void dropTable(Database db,boolean ifExists){
     String sql="DROP TABLE"+(ifExists?"IF EXISTS":"")+"\"CHAT_LOG\"";
     db.execSQL(sql);
 }

 /* 把一个值绑定到一个参数*/
 @Override
 protected void bindValues(DatabaseStatement stmt, ChatLog entity) {
     stmt.clearBindings();

     Long id=entity.getId();
     if (id!=null){
         stmt.bindLong(1,id);
     }
     Long userId=entity.getUserId();
     if (userId!=null){
         stmt.bindLong(2,userId);
     }
     String time=entity.getTime();
     if (time!=null){
         stmt.bindString(3,time);
     }
     String content=entity.getContent();
     if (content!=null){
         stmt.bindString(4,content);
     }
 }
    @Override
    protected final void bindValues(SQLiteStatement stmt, ChatLog entity) {
        stmt.clearBindings();

        Long id=entity.getId();
        if (id!=null){
            stmt.bindLong(1,id);
        }
        Long userId=entity.getUserId();
        if (userId!=null){
            stmt.bindLong(2,userId);
        }
        String time=entity.getTime();
        if (time!=null){
            stmt.bindString(3,time);
        }
        String content=entity.getContent();
        if (content!=null){
            stmt.bindString(4,content);
        }
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset+0)?null:cursor.getLong(offset+0);
    }

    @Override
    protected ChatLog readEntity(Cursor cursor, int offset) {
        ChatLog entity=new ChatLog(//
                cursor.isNull(offset+0)?null:cursor.getLong(offset+0),//id
                cursor.isNull(offset+1)?null:cursor.getLong(offset+1),//userId
                cursor.isNull(offset+2)?null:cursor.getString(offset+2),//time
                cursor.isNull(offset+3)?null:cursor.getString(offset+3)//content
                 );
        return entity;
    }

    @Override
    protected void readEntity(Cursor cursor, ChatLog entity, int offset) {
        entity.setId(cursor.isNull(offset+0)?null:cursor.getLong(offset+0));
        entity.setUserId(cursor.isNull(offset+1)?null:cursor.getLong(offset+1));
        entity.setTime(cursor.isNull(offset+2)?null:cursor.getString(offset+2));
        entity.setContent(cursor.isNull(offset+3)?null:cursor.getString(offset+3));
    }



    @Override
    protected final Long updateKeyAfterInsert(ChatLog entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    protected Long getKey(ChatLog entity) {
        if (entity!=null){
            return entity.getId();
        }else {
            return null;
        }
    }

    @Override
    protected boolean hasKey(ChatLog entity) {
        return entity.getId()!=null;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
}