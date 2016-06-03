package com.jotunheim.mimir.web.service;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * 功能描述：memcache工具
 * 
 * 
 * <p>
 * 修改历史：(修改人，修改时间，修改原因/内容)
 * </p>
 */
@Service
public class CacheService {

    private String mPrefix = "tps_";

    private static Log log = LogFactory.getLog(CacheService.class);

    private MemcachedClient mClient = null;

    private String mServerHost = "127.0.0.1:11211";

    /**
     * 有效时间，默认1个小时
     */
    private int expire = 1 * 60 * 60;

    /**
     * Default constructor
     */
    public CacheService() {

        try {
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                    AddrUtil.getAddresses(mServerHost));
            // 宕机报警
            builder.setFailureMode(true);
            // 使用二进制文件
            builder.setCommandFactory(new BinaryCommandFactory());
            builder.setConnectionPoolSize(2);
            mClient = builder.build();
            mClient.setEnableHeartBeat(false);
        } catch (IOException e) {
            log.error("初始化Memecache连接失败(" + mServerHost + ")", e);
        }
    }

    public void addCache(String key, Object value) {
        if (mClient == null) {
            return;
        }
        try {
            mClient.set(key, expire, value);
        } catch (Exception e) {
            log.error("添加缓存失败:key=" + key + " , value=" + value, e);
        }
    }

    public void addCache(String key, int expire, Object value) {
        try {
            mClient.set(key, expire, value);
        } catch (Exception e) {
            log.error("添加缓存失败:key=" + key + " , value=" + value, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getCache(String key) {
        if (mClient == null) {
            return null;
        }
        try {
            return (T) mClient.get(key);
        } catch (Exception e) {
            log.error("获取缓存数据失败:key=" + key, e);
        }
        return null;
    }

    public Object delCache(String key) {
        if (mClient == null) {
            return null;
        }
        try {
            return mClient.delete(key);
        } catch (Exception e) {
            log.error("移除缓存失败:key=" + key, e);
        }
        return null;
    }

    public void addCacheWithPrefix(String key, Object value) {
        addCache(mPrefix + key, value);
    }

    public void addCacheWithPrefix(String key, int expire, Object value) {
        addCache(mPrefix + key, expire, value);
    }

    public <T extends Object> T getCacheWithPrefix(String key) {
        return getCache(mPrefix + key);
    }

    public Object delCacheWithPrefix(String key) {
        return delCache(mPrefix + key);
    }

}
