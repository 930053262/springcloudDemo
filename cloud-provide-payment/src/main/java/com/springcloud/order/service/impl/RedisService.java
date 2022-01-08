package com.springcloud.order.service.impl;

import com.springcloud.order.service.IRedis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis服务
 *
 * @author
 */
@Service
@Slf4j
public class RedisService implements IRedis {



    private static final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final String REDIS_CASE_NUMBER_GENERATE = "case:generate:caseNumber:";
    public static final String REDIS_CASE_NUMBER_GENERATE_LOCK = "case:generate:caseNumber:lock";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient client;

    @Override
    public void set(String key, String value, int validTime) {
        redisTemplate.opsForValue().set(key, value, validTime, TimeUnit.SECONDS);
    }

    @Override
    public void expire(String key, int validTime) {
        redisTemplate.expire(key, validTime, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }


    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Integer append(String key, String str) {
        return redisTemplate.opsForValue().append(key, str);
    }

    @Override
    public List<String> getList(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public Long len(String key) {
        return redisTemplate.opsForValue().size(key);
    }


    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String hGet(String key, String field) {
        Object object = redisTemplate.opsForHash().get(key, field);
        if (object == null) {
            return null;
        }
        return (String) object;
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        Map<Object, Object> hGetAll = redisTemplate.opsForHash().entries(key);
        return convert(hGetAll);
    }


    @Override
    public void hSet(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public void hDel(String key, List<String> fields) {
        redisTemplate.opsForHash().delete(key, fields.toArray());
    }

    @Override
    public void hSet(String key, Map<String, String> hash) {
        redisTemplate.opsForHash().putAll(key, hash);
    }

    @Override
    public Boolean hasKey(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    @Override
    public Long hIncrByLong(String key, String field, Long value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    @Override
    public Double hIncrByDouble(String key, String field, Double value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    @Override
    public Integer hSize(String key) {
        return redisTemplate.opsForHash().size(key).intValue();
    }

    @Override
    public void lPush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public void lPush(String key, List<String> values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public void lrPush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public void lrPush(String key, List<String> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public void lSet(String key, Long index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public void lRem(String key, long count, String value) {
        redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public String lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public String lrPop(String key) {

        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public String lIndex(String key, Long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public Integer lLen(String key) {
        return Objects.requireNonNull(redisTemplate.opsForList().size(key)).intValue();
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public void lSet(String key, Integer index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public void sAdd(String key, List<String> values) {
        redisTemplate.opsForSet().add(key, String.valueOf(values));
    }

    @Override
    public void sRem(String key, List<String> values) {
        redisTemplate.opsForSet().remove(key,values.toArray());
    }

    @Override
    public String sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    @Override
    public Set<String> sDiff(List<String> keys) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().difference(key, keys);
    }

    @Override
    public Long sDiffStore(List<String> keys, String storeKey) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().differenceAndStore(key, keys, storeKey);
    }

    @Override
    public Set<String> sInter(List<String> keys) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().intersect(key, keys);
    }

    @Override
    public Long sInterStore(List<String> keys, String storeKey) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().intersectAndStore(key, keys, storeKey);
    }

    @Override
    public Set<String> sUnion(List<String> keys) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().union(key, keys);
    }

    @Override
    public Long sUnionStore(List<String> keys, String storeKey) {
        String key = keys.get(0);
        keys.remove(0);
        return redisTemplate.opsForSet().unionAndStore(key, keys, storeKey);
    }


    @Override
    public Integer sSize(String key) {
        return Objects.requireNonNull(redisTemplate.opsForSet().size(key)).intValue();
    }

    @Override
    public Boolean sIsMember(String key, String member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    @Override
    public String sRandMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    @Override
    public Set<String> sMembersAll(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    private Map<String, String> convert(Map<Object, Object> objectMap) {
        Map<String, String> stringMap = new HashMap<>();
        objectMap.keySet().forEach(it -> {
            stringMap.put(String.valueOf(it), String.valueOf(objectMap.get(it)));
        });
        return stringMap;
    }

    /**
     * <p>Description: [
     * 生成案件编号,生成规则：
     * LD/XZ/MS/SS/XS (2位）+年（4位）+自增序列号（5位）共11位
     * @author v-guoxingliang.ea
     * @return java.lang.String
     */
    public String getCaseNumber(String caseType) {
        RLock lock = client.getLock(this.REDIS_CASE_NUMBER_GENERATE_LOCK);
        try {
            lock.lock(10, TimeUnit.SECONDS);
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String seq = seqGenerator(new SimpleDateFormat("yyyy").format(new Date()), 4, 1, this.REDIS_CASE_NUMBER_GENERATE);
            return caseType + date + seq;
        } catch (Exception e) {
            log.warn("caseType=[{}]", caseType, e);
            throw new RuntimeException("抛出异常了");
        } finally {
            lock.unlock();
        }
    }

    /**
     * <p>Description: [自增序列号]</p>
     * Created on: 2018/7/31  14:51
     * @author v-guoxingliang.ea
     * @param prefix 前缀
     * @param numLength 要生成多少位的数字
     * @param startNum 起始数字
     * @return java.lang.String
     */
    public String seqGenerator(String prefix, int numLength, int startNum, String key) {
        String upperCode = "";
        //查找以prefix作为key值的数据长度
        Long size = redisTemplate.opsForList().size(key);
        //有数据
        if (size > 0) {
            //获取该key下面的所有值 （-1所有值；1下一个值）
            List all = redisTemplate.opsForList().range(key, 0, -1);
            //返回最后一个值
            upperCode = all.get(all.size() - 1).toString();
        }
        String returnCode = "";
        //后缀数字
        int suffix;
        if (!StringUtils.isEmpty(upperCode)) {//有数据
            //截取前缀开始的后面数字
            suffix = Integer.parseInt(upperCode);
            //最后的序号加一
            suffix++;
        } else {//没有数据
            suffix = startNum;
        }
        //后缀不够numLength长，前面补充0
        String str = "%0" + numLength + "d";
        returnCode = String.format(str, suffix);
        //存入Redis
        redisTemplate.opsForList().rightPush(key, returnCode);
        return returnCode;
    }

}
