package com.example.a21516.ceshi_jiguang.famework.greendao;

import com.example.a21516.ceshi_jiguang.famework.greendao.model.ChatLog;
import com.example.a21516.ceshi_jiguang.famework.greendao.model.RequestList;
import com.example.a21516.ceshi_jiguang.famework.greendao.model.SearchAdd;
import com.example.a21516.ceshi_jiguang.famework.greendao.model.User;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

//此代码由greenDAO生成，请勿编辑。
public class DaoSession extends AbstractDaoSession{
    private final DaoConfig chatLogDaoConfig;
    private final DaoConfig requestListDaoConfig;
    private final DaoConfig searchAddDaoConfig;
    private final DaoConfig userDaoConfig;

    private final ChatLogDao chatLogDao;
    private final RequestListDao requestListDao;
    private final SearchAddDao searchAddDao;
    private final UserDao userDao;



    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);
        chatLogDaoConfig = daoConfigMap.get(ChatLogDao.class).clone();
        chatLogDaoConfig.initIdentityScope(type);

        requestListDaoConfig = daoConfigMap.get(RequestListDao.class).clone();
        requestListDaoConfig.initIdentityScope(type);

        searchAddDaoConfig = daoConfigMap.get(SearchAddDao.class).clone();
        searchAddDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        chatLogDao=new ChatLogDao(chatLogDaoConfig,this);
        requestListDao=new RequestListDao(requestListDaoConfig,this);
        searchAddDao=new SearchAddDao(searchAddDaoConfig,this);
        userDao=new UserDao(userDaoConfig,this);

        registerDao(ChatLog.class,chatLogDao);
        registerDao(RequestList.class,requestListDao);
        registerDao(SearchAdd.class,searchAddDao);
        registerDao(User.class,userDao);

    }
    public void clear(){
        chatLogDaoConfig.clearIdentityScope();
        requestListDaoConfig.clearIdentityScope();
        searchAddDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public ChatLogDao getChatLogDao() {
        return chatLogDao;
    }

    public RequestListDao getRequestListDao() {
        return requestListDao;
    }

    public SearchAddDao getSearchAddDao() {
        return searchAddDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
