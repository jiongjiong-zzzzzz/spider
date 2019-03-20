package com.zxs.ssh.template.dao.common.impl;

import com.zxs.ssh.template.dao.common.api.ICommonDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Project Name:log-analysis-platform
 * File Name:CommonDaoImpl
 * Package Name:com.yk.parking.log.analysis.platform.dao.common.impl
 * Date:2018/7/10
 * Author:zhangju
 * Description:
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Repository("commonDao")
@Transactional
public class CommonDaoImpl implements ICommonDao {
    private static final Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    /**
     * 获取session
     *
     * @return session
     */
    @Override
    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    /**
     * 添加
     *
     * @param object 实体对象
     * @return 添加状态，true：成功，false：失败
     */
    @Override
    public boolean save(Object object) {
        boolean state = false;
        try {
            Session session = this.getSession();
            session.save(object);
            session.flush();
            session.clear();
            state = true;
        } catch (Exception e) {
            logger.error("实体对象数据库保存错误", e);
        }
        return state;
    }

    /**
     * 有标识返回添加
     *
     * @param object 实体对象
     * @return 唯一标识
     */
    @Override
    public String saveModel(Object object) {
        String id = null;
        try {
            Session session = this.getSession();
            id = session.save(object).toString();
            session.flush();
            session.clear();
        } catch (Exception e) {
            logger.error("实体对象数据库保存错误", e);
        }
        return id;
    }

    /**
     * 删除
     *
     * @param object 实体对象
     * @return 删除状态，true：成功，false：失败
     */
    @Override
    public boolean delete(Object object) {
        boolean state = false;
        try {
            Session session = this.getSession();
            session.delete(object);
            session.flush();
            session.clear();
            state = true;
        } catch (Exception e) {
            logger.error("实体对象数据库删除错误", e);
        }
        return state;
    }

    /**
     * 更新
     *
     * @param object 实体对象
     * @return 更新状态，true：成功，false：失败
     */
    @Override
    public boolean update(Object object) {
        boolean state = false;
        try {
            Session session = this.getSession();
            session.update(object);
            session.flush();
            session.clear();
            state = true;
        } catch (Exception e) {
            logger.error("实体对象数据库更新错误", e);
        }
        return state;
    }

    /**
     * 获取与综合条件
     *
     * @param conditions 条件队列
     * @return 综合条件
     */
    @Override
    public String getAndCondition(HashSet<String> conditions) {
        StringBuilder oneCondition = new StringBuilder();
        if (conditions.isEmpty())
            return "(1=1)";

        Iterator<String> iterator = conditions.iterator();
        while (iterator.hasNext()) {
            String condition = iterator.next();
            if (!condition.isEmpty()) {
                oneCondition.append(condition);
                if (iterator.hasNext())
                    oneCondition.append(" and ");
            }
        }
        if (conditions.size() > 1)
            oneCondition = new StringBuilder("(" + oneCondition + ")");
        return oneCondition.toString();
    }

    /**
     * 获取或综合条件
     *
     * @param conditions 条件队列
     * @return 综合条件
     */
    @Override
    public String getOrCondition(HashSet<String> conditions) {
        StringBuilder oneCondition = new StringBuilder();
        if (conditions.isEmpty())
            return "(1=1)";
        Iterator<String> iterator = conditions.iterator();
        while (iterator.hasNext()) {
            String condition = iterator.next();
            if (!condition.isEmpty()) {
                oneCondition.append(condition);
                if (iterator.hasNext())
                    oneCondition.append(" or ");
            }
        }
        if (conditions.size() > 1)
            oneCondition = new StringBuilder("(" + oneCondition + ")");
        return oneCondition.toString();
    }

    /**
     * 获取分页查询器
     *
     * @param query 查询器
     * @param start 起始条数
     * @param limit 条数限制
     * @param <T>   实体泛型
     * @return 查询器
     */
    @Override
    public <T> Query<T> makePage(Query<T> query, int start, int limit) {
        query.setFirstResult(start);
        query.setMaxResults(limit);
        return query;
    }

    /**
     * 查询所有实体
     *
     * @param model 实体类
     * @return 实体对象列表
     */
    @Override
    public <T> List<T> queryAll(Class<T> model) {
        List<T> models = new ArrayList<>();
        models.addAll(this.queryModels(null, model));
        return models;
    }

    /**
     * 分页查询实体
     *
     * @param start 起始条数
     * @param limit 条数限制
     * @param model 实体类
     * @return 实体对象列表
     */
    @Override
    public <T> List<T> queryPage(int start, int limit, Class<T> model) {
        List<T> models = new ArrayList<>();
        String hql = "from " + model.getName() + " as model order by model.createTime Desc";
        models.addAll(this.queryModels(start, limit, hql, model));
        return models;
    }

    /**
     * 条件查询实体总数
     *
     * @param condition hibernate 查询语句
     * @param model     实体类
     * @param <T>       实体泛型
     * @return 实体总数
     */
    @Override
    public <T> long queryTotalCount(String condition, Class<T> model) {
        String hql;
        if (condition != null) {
            hql = "select count(*) from " + model.getName() + " as model where " + condition;
        } else {
            hql = "select count(*) from " + model.getName();
        }

        try {
            Session session = this.getSession();
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("total count query is failed", e);
        }
        return 0;
    }

    /**
     * 查询实体总数
     *
     * @param model 实体类
     * @param <T>   实体泛型
     * @return 实体总数
     */
    @Override
    public <T> long queryTotalCount(Class<T> model) {
        return this.queryTotalCount(null, model);
    }

    /**
     * 查询特定标识实体对象
     *
     * @param condition hibernate 查询语句
     * @param model     实体类
     * @return 实体对象
     */
    @Override
    public <T> T queryModel(String condition, Class<T> model) {
        try {
            Session session = this.getSession();
            if (condition != null && !condition.isEmpty()) {
                Query<T> query = session.createQuery("from " + model.getName() + " as model where " + condition, model);
                List<T> results = query.list();
                if (results.isEmpty()) {
                    return null;
                } else {
                    return results.get(0);
                }
            } else {
                Query<T> query = session.createQuery("from " + model.getName() + " as model", model);
                List<T> results = query.list();
                if (results.isEmpty()) {
                    return null;
                } else {
                    return results.get(0);
                }
            }
        } catch (Exception e) {
            logger.error("model condition query is failed", e);
        }
        return null;
    }

    /**
     * 特定条件查询实体对象列表
     *
     * @param condition hibernate 查询条件
     * @param model     实体类
     * @param <T>       类型
     * @return 实体对象列表
     */
    @Override
    public <T> List<T> queryModels(String condition, Class<T> model) {
        List<T> models = new ArrayList<>();
        try {
            Session session = this.getSession();
            if (condition != null && !condition.isEmpty()) {
                Query<T> query = session.createQuery("from " + model.getName() + " as model where " + condition, model);
                models.addAll(query.list());
            } else {
                Query<T> query = session.createQuery("from " + model.getName() + " as model", model);
                models.addAll(query.list());
            }

        } catch (Exception e) {
            logger.error("models condition query is failed", e);
        }
        return models;
    }

    /**
     * 分页条件查询实体对象列表
     *
     * @param start 起始条数
     * @param limit 条数限制
     * @param hql   查询语句
     * @param model 实体类
     * @return 实体对象列表
     */
    @Override
    public <T> List<T> queryModels(int start, int limit, String hql, Class<T> model) {
        List<T> models = new ArrayList<>();
        try {
            Session session = this.getSession();
            Query<T> query = this.makePage(session.createQuery(hql, model), start, limit);
            models.addAll(query.list());
        } catch (Exception e) {
            logger.error("models page condition query is failed", e);
        }
        return models;
    }
}
