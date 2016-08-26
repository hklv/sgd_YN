package com.ztesoft.sgd.base.jdbc;

import com.ztesoft.zsmart.core.jdbc.AbstractDto;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态生成sql语句
 *
 * @author sunhao
 * @version 1.0 2015年5月20日 下午4:51:26
 */
public abstract class DynamicGenerateSql {
    /**
     * generaterDtoSqlMap
     */
    private static Map<Class<? extends AbstractDto>, DtoSqlBean> generatorDtoSqlMap = new HashMap<Class<? extends AbstractDto>, DtoSqlBean>();

    /**
     * 锁
     */
    private static final Object lockObject = new Object();

    /**
     * 构造方法
     */
    private DynamicGenerateSql() {

    }

    /**
     * Description: 取sql bean<br>
     *
     * @param clazz Dto
     * @return DtoSqlBean <br>
     */
    public static DtoSqlBean getDtoSqlBean(Class<? extends AbstractDto> clazz) {
        DtoSqlBean bean = generatorDtoSqlMap.get(clazz);
        if (bean == null) {
            bean = initDtoSqlBean(clazz);
            generatorDtoSqlMap.put(clazz, bean);
        }

        return bean;
    }

    /**
     * Description: 初始化sql bean<br>
     *
     * @param clazz Dto
     * @return DtoSqlBean <br>
     */
    private static DtoSqlBean initDtoSqlBean(Class<? extends AbstractDto> clazz) {
        DtoSqlBean bean;
        synchronized (lockObject) {
            bean = generatorDtoSqlMap.get(clazz);
            if (bean == null) {
                GeneratorDtoSql dtoSql = new GeneratorDtoSql(clazz);
                bean = dtoSql.build();

                generatorDtoSqlMap.put(clazz, bean);
            }
        }

        return bean;
    }
}
