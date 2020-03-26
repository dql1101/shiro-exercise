package com.dql.shiro.service.impl;

import com.dql.shiro.service.RedisClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

public class RedisClusterServiceImpl implements RedisClusterService {

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public void psetex(String key, long expire, String value) {
        jedisCluster.psetex(key, expire, value);
    }
}
