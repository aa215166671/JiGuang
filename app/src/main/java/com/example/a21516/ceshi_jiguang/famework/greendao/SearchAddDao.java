package com.example.a21516.ceshi_jiguang.famework.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.example.a21516.ceshi_jiguang.famework.greendao.model.SearchAdd;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.IllegalFormatCodePointException;

/*
 * 搜索好友历史
 * */
public class SearchAddDao extends AbstractDao<SearchAdd,Long>{
    public static final String TABLENAME = "SEARCH_ADD";

    /**
     *实体请求列表的属性。
     *可用于QueryBuilder和引用列名。
     */

    public static class Properties{
        public final static Property Id=new Property(0,Long.class,"id",true,"_id");
        public final static Property Content=new Property(1,String.class,"content",false,"CONTENT");
    }

    public SearchAddDao(DaoConfig config){
        super(config);
    }

    @Override
    public SearchAdd readEntity(Cursor cursor, int offset) {
        SearchAdd entity=new SearchAdd(
                cursor.isNull(offset+0)?null:cursor.getLong(offset+0),
                cursor.isNull(offset+1)?null:cursor.getString(offset+1)
        );
        return entity;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset+0)?null:cursor.getLong(offset+0);
    }

    @Override
    protected void readEntity(Cursor cursor, SearchAdd entity, int offset) {

        entity.setId(cursor.isNull(offset+0)?null:cursor.getLong(offset+0));//id
        entity.setContent(cursor.isNull(offset+1)?null:cursor.getString(offset+1));//content
    }

    @Override
    protected void bindValues(DatabaseStatement stmt, SearchAdd entity) {
        stmt.clearBindings();

        Long id=entity.getId();
        if (id!=null){
            stmt.bindLong(1,id);
        }
        String contnet=entity.getContent();
        if (contnet!=null){
            stmt.bindString(2,contnet);
        }
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, SearchAdd entity) {
        stmt.clearBindings();

        Long id=entity.getId();
        if (id!=null){
            stmt.bindLong(1,id);
        }
        String contnet=entity.getContent();
        if (contnet!=null){
            stmt.bindString(2,contnet);
        }
    }

    @Override
    protected Long updateKeyAfterInsert(SearchAdd entity, long rowId) {
        return null;
    }

    @Override
    protected Long getKey(SearchAdd entity) {
        return null;
    }

    @Override
    protected boolean hasKey(SearchAdd entity) {
        return false;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }

    public SearchAddDao(DaoConfig config,DaoSession daoSession){
        super(config,daoSession);
    }

    /*创建数据库*/
    public static void createTabele(Database db,boolean ifNoexists){
        String constraint=ifNoexists?"IF NOT EXISTS":"";
        db.execSQL("CREATE TABLE"+constraint+"\"SEARCH_ADD\"("+//
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT,"+//0:id
                "\"CONTENT\"TEXT);"); //1:content
    }
    /*删除数据库*/
    public static void dropTable(Database db,boolean ifNoexists){
        String sql="DROP TABLE"+(ifNoexists?"IF EXISTS":"")+"\"SEARCH_ADD\"";
        db.execSQL(sql);
    }
}
