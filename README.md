# jedis-utils
A simple utility library for jedis, targeting java 8. Features include easier management of transactions, pipelines or both and basic conversion of strings returned from commands.

## Typed jedis
`TypedJedis` is a thin wrapper for `Jedis` that converts some commands output to primitives (wrapped in `Optional`s). Usage is simple:
```java
try (Jedis jedis = jedisPool.getResource()) { 
    TypedJedis typedJedis = new TypedJedis(jedis);
    Optional<Long> someNumber = typedJedis.hgetLong("myHash", "someField");
    Optional<Double> anotherNumber = typedJedis.getSetDouble("myKey", 0);
    try {
        // This will fail. Any problems during conversion results in ConversionException. 
        // In this case, problem is anything besides true or false (case ignored).
        Optional<Boolean> myValue = typedJedis.getBoolean("yak");
    } catch (ConversionException ex) {
        // Do something
    }
}
```

The current version supports `GET`, `HGET` and `GETSET`, but more commands will be added in future.

## Command blocks
Command blocks are a way of grouping related commands in a single lambda expression. Commands inside a block can be easily reused or refactored to run inside transactions, pipelines or both.

#### Plain commands
Starting with plain commands, no transactions or pipelines. This:
```java
String compellingRequest = new CommandBlock()
        .using(jedisPool.getResource())
        .call(jedis -> {
            String request = Optional.ofNullable(jedis.get("request"))
                    .orElse("Give me your extra resources");
            return request + "!";
        });	
```

... is the same as:
```java
String compellingRequest;
try (Jedis jedis = jedisPool.getResource()) {
    String request = Optional.ofNullable(jedis.get("request"))
            .orElse("Give me your extra resources");
    compellingRequest = request + "!";
}
```

#### Transactions and pipelines
To make a command block transacted, just add `.transacted()` after `using` and the transaction will be available for consumption in your lambda expression. Notice that you don't have to manage calls to `multi()` and `exec()`.
```java
final Result<String> request = new Result<>();
new CommandBlock()
        .using(jedisPool.getResource())
        .transacted()
        .consume(transaction -> {
            request.wrapping(transaction.get("request"));
        });	
String compellingRequest = request.asOptional()
        .orElse("Give me your extra resources") + "!";
```

If you want pipelines, just replace `transacted()` with `pipelined()`. If you want both, just add `pipelined()` and this complex chain of commands will run in a pipelined transaction:
```java
final Result<String> request = new Result<>();
new CommandBlock()
        .using(jedisPool.getResource())
        .transacted()
        .pipelined()
        .consume(pipeline -> {
            request.wrapping(pipeline.get("request"));
        });	
String compellingRequest = request.asOptional()
        .orElse("Give me your extra resources") + "!";
```

#### Retrieving results
As seen before, the `call` method for plain commands returns the result of it's `Function` parameter. This is handy when you expect a single result, but if that isn't the case you can use the `Result` class. It can wrap not only jedis `Response`, but also regular commands output. Just remember to replace `call` by `consume`; it takes a `Consumer`, so you won't have to return anything inside your lambda expression:
```java
final Result<String> request = new Result<>();
final Result<Long> times = new Result<>();
new CommandBlock()
        .using(jedisPool.getResource())
        .consume(jedis -> {
            request.wrapping(jedis.get("request"));
            times.wrapping(jedis.incrBy("times", 3));
        });	
String compellingRequest = request.asOptional()
        .orElse("Give me your extra resources") + "!";
String spam = Collections.nCopies(times.get().intValue(), compellingRequest)
        .stream()
        .collect(Collectors.joining("\n"));
```

In transactions and pipelines, you also have access to a list containing all redis responses. This means that this:
```java
List<Object> responses = new CommandBlock()
        .using(jedisPool.getResource())
        .pipelined()
        .transacted()
        .consume(pipeline -> {/* Code goes here */});
```
... is the same as:
```java
List<Object> responses;
Response<List<Object>> execResponses;
try (Jedis jedis = POOL.getResource()) {
    Pipeline pipeline = jedis.pipelined();
    pipeline.multi();
    // Code goes here
    execResponses = pipeline.exec();
    pipeline.sync();
}
responses = execResponses.get();
```

## Plans for version 1
* Support more commands in `TypedJedis`
* Actual releases, including in maven central :)
