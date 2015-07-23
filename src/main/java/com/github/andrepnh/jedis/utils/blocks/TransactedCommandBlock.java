package com.github.andrepnh.jedis.utils.blocks;

import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TransactedCommandBlock extends MultiKeyCommandBlock<Transaction> {

    TransactedCommandBlock(Jedis jedis) {
        super(jedis);
    }
    
    @Override
    Transaction before(Jedis jedis) {
        return jedis.multi();
    }
    
    @Override
    List<Object> after(Transaction transaction) {
        return transaction.exec();
    }

    /**
     * @return a command block that runs inside a pipeline and transaction.
     * Both are managed internally.
     */
    public PipelinedTransactedCommandBlock pipelined() {
        return new PipelinedTransactedCommandBlock(getJedis());
    }
    
}
