package com.zxs.ssh.template.dao.common.api;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;

/**
 * Project Name:log-analysis-platform
 * File Name:ICommonDao
 * Package Name:com.yk.parking.log.analysis.platform.dao.common.api
 * Date:2018/7/10
 * Author:zhangju
 * Description:
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public interface ICommonDao {
    /**
     * 获取session
     *
     * @return session
     */
    Session getSession();

    /**
     * 添加
     *
     * @param object 实体对象
     * @return 添加状态，true：成功，false：失败
     */
    boolean save(Object object);

    /**
     * 有标识返回添加
     *
     * @param object 实体对象
     * @return 唯一标识
     */
    String saveModel(Object object);

    /**
     * 删除
     *
     * @param object 实体对象
     * @return 删除状态，true：成功，false：失败
     */
    boolean delete(Object object);

    /**
     * 更新
     *
     * @param object 实体对象
     * @return 更新状态，true：成功，false：失败
     */
    boolean update(Object object);

    /**
     * 获取与综合条件
     *
     * @param conditions 条件队列
     * @return 综合条件
     */
    String getAndCondition(HashSet<String> conditions);

    /**
     * 获取或综合条件
     *
     * @param conditions 条件队列
     * @return 综合条件
     */
    String getOrCondition(HashSet<String> conditions);

    /**
     * 获取分页查询器
     *
     * @param query 查询器
     * @param start 起始条数
     * @param limit 条数限制
     * @param <T>   实体泛型
     * @return 查询器
     */
    <T> Query<T> makePage(Query<T> query, int start, int limit);

    /**
     * 查询所有实体
     *
     * @param model 实体类
     * @param <T>   类型
     * @return 实体对象列表
     */
    <T> List<T> queryAll(Class<T> model);

    /**
     * 分页查询实体
     *
     * @param start 起始条数
     * @param limit 条数限制
     * @param model 实体类
     * @param <T>   类型
     * @return 实体对象列表
     */
    <T> List<T> queryPage(int start, int limit, Class<T> model);

    /**
     * 条件查询实体总数
     *
     * @param condition hibernate 查询语句
     * @param model     实体类
     * @param <T>       实体泛型
     * @return 实体总数
     */
    <T> long queryTotalCount(String condition, Class<T> model);

    /**
     * 查询实体总数
     *
     * @param model 实体类
     * @param <T>   类型
     * @return 实体总数
     */
    <T> long queryTotalCount(Class<T> model);

    /**
     * 查询特定标识实体对象
     *
     * @param condition hibernate 查询语句
     * @param model     实体类
     * @param <T>       类型
     * @return 实体对象
     */
    <T> T queryModel(String condition, Class<T> model);

    /**
     * 特定条件查询实体对象列表
     *
     * @param condition hibernate 查询条件
     * @param model     实体类
     * @param <T>       类型
     * @return 实体对象列表
     */
    <T> List<T> queryModels(String condition, Class<T> model);

    /**
     * 分页条件查询实体对象列表
     *
     * @param start 起始条数
     * @param limit 条数限制
     * @param hql   hibernate 查询语句
     * @param model 实体类
     * @param <T>   类型
     * @return 实体对象列表
     */
    <T> List<T> queryModels(int start, int limit, String hql, Class<T> model);
}
