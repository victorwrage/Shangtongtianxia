package com.zdv.shangtongtianxia.bean;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.zdv.shangtongtianxia.bean.MessageBean;
import com.zdv.shangtongtianxia.bean.SearchHistoryBean;

import com.zdv.shangtongtianxia.bean.MessageBeanDao;
import com.zdv.shangtongtianxia.bean.SearchHistoryBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig messageBeanDaoConfig;
    private final DaoConfig searchHistoryBeanDaoConfig;

    private final MessageBeanDao messageBeanDao;
    private final SearchHistoryBeanDao searchHistoryBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        messageBeanDaoConfig = daoConfigMap.get(MessageBeanDao.class).clone();
        messageBeanDaoConfig.initIdentityScope(type);

        searchHistoryBeanDaoConfig = daoConfigMap.get(SearchHistoryBeanDao.class).clone();
        searchHistoryBeanDaoConfig.initIdentityScope(type);

        messageBeanDao = new MessageBeanDao(messageBeanDaoConfig, this);
        searchHistoryBeanDao = new SearchHistoryBeanDao(searchHistoryBeanDaoConfig, this);

        registerDao(MessageBean.class, messageBeanDao);
        registerDao(SearchHistoryBean.class, searchHistoryBeanDao);
    }
    
    public void clear() {
        messageBeanDaoConfig.clearIdentityScope();
        searchHistoryBeanDaoConfig.clearIdentityScope();
    }

    public MessageBeanDao getMessageBeanDao() {
        return messageBeanDao;
    }

    public SearchHistoryBeanDao getSearchHistoryBeanDao() {
        return searchHistoryBeanDao;
    }

}
