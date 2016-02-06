# jedis-utils
A tiny utility library for jedis, mainly designed to make it more fun on Java 8. It manages transactions and pipelines automatically and provides basic type conversion.
Personally, this project was used to play with publishing artifacts to the maven central.

## Setting up
```xml
<dependency>
    <groupId>com.github.andrepnh</groupId>
    <artifactId>jedis-utils</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Command blocks
Command blocks are a way of grouping related commands in a single lambda expression. Commands inside a block can be refactored to run inside automatically managed transactions, pipelines or both.
All command blocks take a `Jedis` instance and a `Consumer`. A simple command block, running outside transaction or pipelines, can take a `Function` if you wish to return values directly:
```java
String compellingRequest = new CommandBlock()
        .using(jedisPool.getResource()) // This one is closed after the commands are executed
        .call(jedis -> {
            String request = Optional.ofNullable(jedis.get("request"))
                    .orElse("Give me your extra resources");
            return request + "!";
        });	
```

To make this command block transacted, just add `.transacted()`; to make it pipelined, use `pipelined()`. The same `GET` command, now running inside a transaction and pipeline (talk about overkill), would be:
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
Notice that `Result` is used instead of `Response`. Results are thin wrappers for a jedis `Response` or for output of non-multikey commands. For convenience, you can use typed results that converts redis output strings to primitive types:

```java
final Result<Boolean> someBooleanResult = new BooleanResult();
new CommandBlock()
        .using(jedisPool.getResource())
        .transacted()
        .pipelined()
        .consume(pipeline -> {
            someBooleanResult.wrapping(pipeline.get("someBoolean"));
        });
try {
    Boolean someBoolean = someBooleanResult.get(); // Result.get() returns null just like Response.get()
} catch (ConversionException ex) {
    // The key exists, but doesn't contain a valid boolean. Valid boolean is
    // any string that equals, ignoring case, "true" or "false".
}
```

You also have access to a list containing all redis responses. This means that this:
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
try (Jedis jedis = jedisPool.getResource()) {
    Pipeline pipeline = jedis.pipelined();
    pipeline.multi();
    // Code goes here
    execResponses = pipeline.exec();
    pipeline.sync();
}
responses = execResponses.get();
```
