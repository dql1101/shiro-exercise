package com.dql.shiro.service;

public interface RedisClusterService {

    public void psetex(String key, long expire, String value);

}
