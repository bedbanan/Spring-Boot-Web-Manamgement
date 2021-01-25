package com.lalala.dynamicquery;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用来扩展SpringDataJpa的功能, 支持动态jpql/nativesql查询并支持分页查询
 * 方便quartz的查询分页使用
 * 使用方法：注入ServiceImpl
 */
public interface DynamicQuery {

	 void save(Object entity);  //entity是虚拟的，谁都可以成为他

	 void update(Object entity);  //保存方法

	 <T> void delete(Class<T> entityClass, Object entityid);  //根据id删除

	 <T> void delete(Class<T> entityClass, Object[] entityids); //根据复合进行删除
	
	/**
	 * 执行nativeSql统计查询
	 * @param nativeSql
	 * @param params 占位符参数(例如?1)绑定的参数值
	 * @return 统计条数
	 */
	Long nativeQueryCount(String nativeSql, Object... params);

    /**
     * 执行nativeSql分页查询
     * @param resultClass
     * @param pageable
     * @param nativeSql
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> nativeQueryPagingList(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params);


}
