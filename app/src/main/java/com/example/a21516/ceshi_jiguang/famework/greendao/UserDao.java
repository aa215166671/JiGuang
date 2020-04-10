package com.example.a21516.ceshi_jiguang.famework.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.example.a21516.ceshi_jiguang.famework.greendao.model.User;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class UserDao extends AbstractDao<User,Long>{
    public static final String TABLENAME = "USER";
    /**
     *实体请求列表的属性。
     *可用于QueryBuilder和引用列名。
     */
    public static class Properties{
        public final static Property Id=new Property(0,Long.class,"id",false,"_id");
    }

    public UserDao(DaoConfig config){
        super(config);
    }

    @Override
    protected User readEntity(Cursor cursor, int offset) {
        User entity=new User(
          cursor.isNull(offset+0)?null:cursor.getLong(offset+0)  //id
        );
        return entity;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset+0)?null:cursor.getLong(offset+0);
    }

    @Override
    protected void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId( cursor.isNull(offset+0)?null:cursor.getLong(offset+0));
    }

    @Override
    protected void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
        Long id=entity.getId();
        if (id!=null){
            stmt.bindLong(1,id);
        }
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
        Long id=entity.getId();
        if (id!=null){
            stmt.bindLong(1,id);
        }
    }

    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        return null;
    }

    @Override
    protected Long getKey(User entity) {
        return null;
    }

    @Override
    protected boolean hasKey(User entity) {
        return false;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }

    public UserDao(DaoConfig config,DaoSession daoSession){
        super(config,daoSession);
    }
    /** 创建数据库. */
    public static void createTable(Database db,boolean ifNoexists){
        String constraint=ifNoexists?"IF NOT EXISTS":"";
        db.execSQL("CREATE TABLE"+constraint+"\"USER\"("+
                "\"_id\"INTEGER PRIMARY KEY AUTOiNCREMENT)");//0:id
    }
    //删除数据库
    public static void dropTable(Database db,boolean ifNoexists){
        String sql="DROP TABLE"+(ifNoexists?"IF EXISTS":"")+"\"USER\"";
        db.execSQL(sql);
    }
}
