/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.eryansky.modules.activity.service;

import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.eryansky.common.exception.DaoException;
import com.eryansky.common.exception.ServiceException;
import com.eryansky.common.exception.SystemException;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.eryansky.common.orm.hibernate.Parameter;
import com.eryansky.common.utils.StringUtils;
import com.eryansky.common.utils.collections.Collections3;
import com.eryansky.modules.activity.entity.ActProcessType;
import com.eryansky.utils.CacheConstants;

/**
 * 流程类型类型实现类.
 */
@Service
public class ActProcessTypeManager extends
		EntityManager<ActProcessType, Long> {

	private HibernateDao<ActProcessType, Long> ActProcessTypeDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		ActProcessTypeDao = new HibernateDao<ActProcessType, Long>(
				sessionFactory, ActProcessType.class);
	}

	@Override
	protected HibernateDao<ActProcessType, Long> getEntityDao() {
		return ActProcessTypeDao;
	}

    @CacheEvict(value = { CacheConstants.ACT_TYPE_ALL_CACHE,
            CacheConstants.ACT_TYPE_GROUPS_CACHE},allEntries = true)
	public void saveOrUpdate(ActProcessType entity) throws DaoException,
			SystemException, ServiceException {
        logger.debug("清空缓存:{}", CacheConstants.ACT_TYPE_ALL_CACHE
                +","+CacheConstants.ACT_TYPE_GROUPS_CACHE);
        Assert.notNull(entity, "参数[entity]为空!");
		ActProcessTypeDao.saveOrUpdate(entity);
	}

    @CacheEvict(value = { CacheConstants.ACT_TYPE_ALL_CACHE,
            CacheConstants.ACT_TYPE_GROUPS_CACHE},allEntries = true)
	public void deleteByIds(List<Long> ids) throws DaoException, SystemException,
			ServiceException {
        logger.debug("清空缓存:{}", CacheConstants.ACT_TYPE_ALL_CACHE
                +","+CacheConstants.ACT_TYPE_GROUPS_CACHE);
        if(!Collections3.isEmpty(ids)){
            for (Long id:ids) {
                ActProcessType ActProcessType = this.getById(id);
                List<ActProcessType> subActProcessTypes = ActProcessType.getSubActProcessTypes();
                if (!subActProcessTypes.isEmpty()) {
                    ActProcessTypeDao.deleteAll(subActProcessTypes);
                }
                ActProcessTypeDao.delete(ActProcessType);
            }
        }else{
            logger.warn("参数[ids]为空.");
        }
	}


    @CacheEvict(value = { CacheConstants.ACT_TYPE_ALL_CACHE,
            CacheConstants.ACT_TYPE_GROUPS_CACHE},allEntries = true)
    @Override
    public void saveEntity(ActProcessType entity) throws DaoException, SystemException, ServiceException {
        logger.debug("清空缓存:{}", CacheConstants.ACT_TYPE_ALL_CACHE
                +","+CacheConstants.ACT_TYPE_GROUPS_CACHE);
        super.saveEntity(entity);
    }

    /**
	 * 根据名称name得到对象.
	 * 
	 * @param name
	 *            数据字典名称
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public ActProcessType getByName(String name) throws DaoException,
			SystemException, ServiceException {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		name = StringUtils.strip(name);// 去除两边空格
        Parameter parameter = new Parameter(name);
		List<ActProcessType> list = getEntityDao().find("from ActProcessType d where d.name = :p1",parameter);
		return list.isEmpty() ? null : list.get(0);
	}

    /**
     * 根据分组编码以及名称查找对象
     * @param groupActProcessTypeCode 分组类型编码
     * @param name   类型名称
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public ActProcessType getByGroupCode_Name(String groupActProcessTypeCode,String name) throws DaoException,
            SystemException, ServiceException {
        Assert.notNull(groupActProcessTypeCode, "参数[groupActProcessTypeCode]为空!");
        Assert.notNull(name, "参数[name]为空!");
        Parameter parameter = new Parameter(groupActProcessTypeCode,name);
        List<ActProcessType> list = getEntityDao().find("from ActProcessType d where d.groupActProcessType.code = :p1 and d.name = :p2",parameter);
        return list.isEmpty() ? null : list.get(0);
    }

	/**
	 * 根据编码code得到对象.
	 *
	 * @param code
	 *            数据字典编码
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public ActProcessType getByCode(String code) throws DaoException,
			SystemException, ServiceException {
		if (StringUtils.isBlank(code)) {
			return null;
		}
		code = StringUtils.strip(code);// 去除两边空格
        Parameter parameter = new Parameter(code);
		List<ActProcessType> list = getEntityDao().find("from ActProcessType d where d.code = :p1",parameter);
		return list.isEmpty() ? null : list.get(0);
	}


    @Cacheable(value = { CacheConstants.ACT_TYPE_ALL_CACHE})
	public List<ActProcessType> getAll() throws DaoException, SystemException,
			ServiceException {
		logger.debug("缓存:{}", CacheConstants.ACT_TYPE_ALL_CACHE);
		return ActProcessTypeDao.getAll();
	}

	/**
	 * 得到排序字段的最大值.
	 *
	 * @return 返回排序字段的最大值
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Integer getMaxSort() throws DaoException, SystemException,
			ServiceException {
		Iterator<?> iterator = ActProcessTypeDao.createQuery(
				"select max(d.orderNo)from ActProcessType d ").iterate();
		Integer max = null;
		while (iterator.hasNext()) {
			// Object[] row = (Object[]) iterator.next();
			max = (Integer) iterator.next();
		}
		if (max == null) {
			max = 0;
		}
		return max;
	}

    /**
     * 查找所有分组列表.
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    @Cacheable(value = {CacheConstants.ACT_TYPE_GROUPS_CACHE})
    public List<ActProcessType> getGroupActProcessTypes() throws DaoException, SystemException,
            ServiceException {
        List<ActProcessType> list = ActProcessTypeDao.find(
                "from ActProcessType d where d.groupActProcessType is null");
        logger.debug("缓存:{}", CacheConstants.ACT_TYPE_GROUPS_CACHE);
        return list;
    }


}
