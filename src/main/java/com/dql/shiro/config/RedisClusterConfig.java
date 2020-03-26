package com.dql.shiro.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisClusterConfig {
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;
    @Value("${spring.redis.timeout}")
    private Integer timeout;
    @Value("${spring.redis.jedis.pool.max-total}")
    private Integer maxTotal;
    @Value("${spring.redis.jedis.pool.max-wait-millis}")
    private Integer maxWaitMillis;
    @Value("${spring.redis.jedis.pool.min-evictable-idle-time-millis}")
    private Integer minEvictableIdleTimeMillis;
    @Value("${spring.redis.jedis.pool.num-tests-per-eviction-run}")
    private Integer numTestsPerEvictionRun;
    @Value("${spring.redis.jedis.pool.time-between-eviction-runs-millis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${spring.redis.jedis.pool.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.redis.jedis.pool.test-while-idle}")
    private boolean testWhileIdle;

    @Value("${spring.redis.cluster.nodes}")
    private String redisNodes;

    @Bean
    public JedisCluster getJedisCluster() {
        String[] cNodes = redisNodes.split(",");
        Set<HostAndPort> nodes =new HashSet<HostAndPort>();
        //分割出集群节点
        for(String node : cNodes) {
            String[] hp = node.split(":");
            nodes.add(new HostAndPort(hp[0],Integer.parseInt(hp[1])));
        }
        JedisPoolConfig config =new JedisPoolConfig();
        //最大空闲数
        config.setMaxIdle(maxIdle);
        //连接池的最大数据库连接数
        config.setMaxTotal(maxTotal);
        //最大建立连接等待时间
        config.setMaxWaitMillis(maxWaitMillis);
        //逐出连接的最小空闲时间, 默认（1800000毫秒）
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        config.setTestOnBorrow(testOnBorrow);
        // 在空闲时检查有效性, 默认false
        config.setTestWhileIdle(testWhileIdle);
        //创建集群对象
        return new JedisCluster(nodes,timeout,config);
    }

    /**
     * 设置数据存入redis 的序列化方式
     *</br>redisTemplate序列化默认使用的jdkSerializeable,存储二进制字节码,导致key会出现乱码，所以自定义
     *序列化类
     *
     * @paramredisConnectionFactory
     */
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws Exception {
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
