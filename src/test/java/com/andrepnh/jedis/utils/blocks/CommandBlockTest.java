/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andrepnh.jedis.utils.blocks;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
 *
 * @author André Pinheiro de Melo
 */
public class CommandBlockTest {

    private static JedisPool POOL;
    
    @BeforeClass
    public static void setupPool() {
        POOL = new JedisPool(new GenericObjectPoolConfig(), 
                Protocol.DEFAULT_HOST, 
                Protocol.DEFAULT_PORT, 
                Protocol.DEFAULT_TIMEOUT, 
                null, 
                5);
    }
    
    @AfterClass
    public static void destroyPool() {
        POOL.destroy();
    }
   
    @After
    public void flushDb() {
        try (Jedis jedis = POOL.getResource()) {
            jedis.flushDB();
        }
    }

//    @Test
//    public void testSomeMethod() {
//final Result<String> request = new Result<>();
//final Result<Long> times = new Result<>();
//List<Object> responses;
//Response<List<Object>> execResponses;
//try (Jedis jedis = POOL.getResource()) {
//    Pipeline pipeline = jedis.pipelined();
//    pipeline.multi();
//    // Code goes here
//    execResponses = pipeline.exec();
//    pipeline.sync();
//}
//responses = execResponses.get();
//List<Object> responses = new FluentJedis()
//        .using(POOL.getResource())
//        .pipelined()
//        .transacted()
//        .call(pipeline -> {
//            // Code goes here
//        });	
//String compellingRequest = request.asOptional()
//        .orElse("Give me your extra resources") + "!";
//String spam = Collections.nCopies(times.get().intValue(), compellingRequest)
//        .stream()
//        .collect(Collectors.joining("\n"));
//        System.out.println(spam);
//    }

}