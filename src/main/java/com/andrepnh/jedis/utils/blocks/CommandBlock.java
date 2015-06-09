package com.andrepnh.jedis.utils.blocks;

import redis.clients.jedis.Jedis;

/**
 * Used to start a new command block, whick may run inside a transaction, pipeline,
 * both or none. This class and all the others command blocks are not thread safe.
 */
public class CommandBlock {
    
    /**
     * Starts a command block using the parameterized {@link Jedis}. It will be
     * closed automatically after redis commands are executed.
     * 
     * @return a plain command block (without transaction or pipelines)
     */
    public PlainCommandBlock using(Jedis jedis) {
        return new PlainCommandBlock(jedis);
    }

}
