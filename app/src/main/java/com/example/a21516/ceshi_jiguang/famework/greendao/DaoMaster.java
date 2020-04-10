package com.example.a21516.ceshi_jiguang.famework.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.a21516.ceshi_jiguang.famework.greendao.model.RequestList;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

/**
 * The type Dao master.
 */
public class DaoMaster extends AbstractDaoMaster{
    public static final int SCHEMA_VERSION = 3;



    /*使用DAOs创建基础数据库表。*/
    public static void createAllTables(Database db,boolean ifNotExists){
        //创建聊天表
        ChatLogDao.createTable(db,ifNotExists);
        RequestListDao.createTable(db,ifNotExists);
        SearchAddDao.createTabele(db,ifNotExists);
        UserDao.createTable(db,ifNotExists);
    }
    /**使用DAOs删除基础数据库表。*/
    public static void dropTable(Database db,boolean ifNoexists){
        ChatLogDao.dropTable(db,ifNoexists);
        RequestListDao.dropTable(db,ifNoexists);
        SearchAddDao.dropTable(db,ifNoexists);
        UserDao.dropTable(db,ifNoexists);
    }

    /**
     *警告：升级时删除所有表！仅在开发期间使用。
     *使用{@link DevOpenHelper}的便利方法。
     * */
    public static DaoSession newDevSession(Context context,String name){
        Database db=new DevOpenHelper(context,name).getWritableDb();
        DaoMaster daoMaster=new DaoMaster(db);
        return daoMaster.newSession();
    }
    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }
    public DaoMaster(Database db) {
        super( db,SCHEMA_VERSION);
        registerDaoClass(ChatLogDao.class);
        registerDaoClass(RequestListDao.class);
        registerDaoClass(SearchAddDao.class);
        registerDaoClass(UserDao.class);
    }

    public DaoSession newSession(){
        return new DaoSession(db,IdentityScopeType.Session,daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type){
        return new DaoSession(db,type,daoConfigMap);
    }


    /**
     *在{@link#onCreate（Database）}中调用
     * {@link#createAllTables（数据库，布尔值）}
     * */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name,SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db,false);
        }
    }

    /**警告：升级时删除所有表！仅在开发期间使用。*/
    public static class DevOpenHelper extends OpenHelper {

        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropTable(db,true);
            onCreate(db);
        }
    }
}
