package com.andrepnh.jedis.utils.fluent;

import redis.clients.jedis.Jedis;

public class FluentJedis {
    
    public PlainCommands using(Jedis jedis) {
        return new PlainCommands(jedis);
    }

}
