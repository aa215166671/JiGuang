package com.example.a21516.ceshi_jiguang.famework.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.example.a21516.ceshi_jiguang.famework.greendao.model.RequestList;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// 接收好友请求的类
/*
*请求列表”表的DAO。
 */
public class RequestListDao extends AbstractDao<RequestList,Long>{
    public static final String TABLENAME = "REQUEST_LIST";
/**
     *实体请求列表的属性。
     *可用于QueryBuilder和引用列名。
     */
    public static class Properties{
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Msg = new Property(1, String.class, "msg", false, "MSG");
        public final static Property UserName = new Property(2, String.class, "userName", false, "USER_NAME");
        public final static Property NakeName = new Property(3, String.class, "nakeName", false, "NAKE_NAME");
        public final static Property Time = new Property(4, String.class, "time", false, "TIME");
        public final static Property Img = new Property(5, String.class, "img", false, "IMG");

    }

    public RequestListDao(DaoConfig config){
        super(config);
    }

    @Override
    protected RequestList readEntity(Cursor cursor, int offset) {
        RequestList entity=new RequestList(
          cursor.isNull(offset+0)?null:cursor.getLong(offset+0),//id
          cursor.isNull(offset+1)?null:cursor.getString(offset+1),//msg
          cursor.isNull(offset+2)?null:cursor.getString(offset+2),//user_Name
          cursor.isNull(offset+3)?null:cursor.getString(offset+3),//nake_Name
          cursor.isNull(offset+4)?null:cursor.getString(offset+4),//time
          cursor.isNull(offset+5)?null:cursor.getString(offset+5)//img
        );
        return entity;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset+0)?null:cursor.getLong(offset+0);
    }

    @Override
    protected void readEntity(Cursor cursor, RequestList entity, int offset) {
        entity.setId(cursor.isNull(offset+0)?null:cursor.getLong(offset+0));//id
        entity.setMsg(cursor.isNull(offset+1)?null:cursor.getString(offset+1));//msg
        entity.setUserName(cursor.isNull(offset+2)?null:cursor.getString(offset+2));//user_Name
        entity.setNakeName(cursor.isNull(offset+3)?null:cursor.getString(offset+3));//nake_Name
        entity.setTime( cursor.isNull(offset+4)?null:cursor.getString(offset+4));//time
        entity.setImg(cursor.isNull(offset+5)?null:cursor.getString(offset+5));  //img

    }

    @Override
    protected void bindValues(DatabaseStatement stmt, RequestList entity) {
        stmt.clearBindings();
        Long id=entity.getId();
        if (id!=null){
            stmt.bindLong(1,id);
        }
        String msg=entity.getMsg();
        if (msg!=null){
            stmt.bindString(2,msg);
        }
        String userName=entity.getUserName();
        if (userName!=null){
            stmt.bindString(3,userName);
        }
        String nakeName=entity.getNakeName();
        if (nakeName!=null){
            stmt.bindString(4,nakeName);
        }
        String time=entity.getTime();
        if (time!=null){
            stmt.bindString(5,time);
        }
        String img=entity.getImg();
        if (img!=null){
            stmt.bindString(6,img);
        }
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, RequestList entity) {
        stmt.clearBindings();
        Long id=entity.getId();
        if (id!=null){
            stmt.bindLong(1,id);
        }
        String msg=entity.getMsg();
        if (msg!=null){
            stmt.bindString(2,msg);
        }
        String userName=entity.getUserName();
        if (userName!=null){
            stmt.bindString(3,userName);
        }
        String nakeName=entity.getNakeName();
        if (nakeName!=null){
            stmt.bindString(4,nakeName);
        }
        String time=entity.getTime();
        if (time!=null){
            stmt.bindString(5,time);
        }
        String img=entity.getImg();
        if (img!=null){
            stmt.bindString(6,img);
        }
    }

    @Override
    protected Long updateKeyAfterInsert(RequestList entity, long rowId) {
        return null;
    }

    @Override
    protected Long getKey(RequestList entity) {
        return null;
    }

    @Override
    protected boolean hasKey(RequestList entity) {
        return false;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }

    public RequestListDao(DaoConfig config,DaoSession daoSession){
        super(config,daoSession);
    }

/**创建基础数据库表。*/

    public static void createTable(Database db,boolean ifNoExists){
        String constraint=ifNoExists?"IF NOT EXISTS":"";
        db.execSQL("CREATE TABLE"+constraint+"\"REQUEST_LIST\"("+//
                "\"_id\"INTEGER PRIMARY KEY AUTOINCREMENT,"+//0:id
                "\"MSG\"TEXT,"+//1:msg
                "\"USER_NAME\"TEXT,"+//2:user_Name
                "\"NAKE_NAME\"TEXT,"+//3:NAKE_Name
                "\"TIME\"TEXT,"+//4:time
                "\"IMG\"TEXT);");//5;img
    }
    /**删除基础数据库表。*/
    public static void dropTable(Database db,boolean ifExists){
        String sql="DROP TABLE"+(ifExists?"IF EXISTS":"")+"\"REQUEST_LIST\"";
        db.execSQL(sql);
    }
}
