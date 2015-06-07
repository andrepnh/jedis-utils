package com.andrepnh.jedis.utils.fluent;

import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TransactedCommands extends BlockBasedCommands<Transaction> {

    TransactedCommands(Jedis jedis) {
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

    public PipelinedTransactedCommands pipelined() {
        return new PipelinedTransactedCommands(getJedis());
    }
    
}
