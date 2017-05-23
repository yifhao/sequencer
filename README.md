# sequencer
发号器实现
## leaf
基于数据库和双缓冲的leaf

http://tech.meituan.com/MT_Leaf.html

每次取step(比如2000)个号, 使用到20%时, 加载下一个范围nextRange(比如2000个)号.
currentRange的号码使用完毕后, 切换nextRange到currentRange.

发号速度, mysql 2000-3000qps/s*2000=4M/s-6M/s

每秒钟可以取百万级别的号

mysql为单点

## MangGuoID
实现类似于mongodb的ObjectID的uuid.  
使用128位  
32位ip,16位pid,16位类加载时的随机数,48位时间戳,16位序列号.  
pid通过jmx获取, 无法获取的话, 使用随机数代替.  
时间戳为毫秒,可以使用8925年.序列号使用AtomicInteger, 在类加载时随机赋初值.

服务化框架－分布式Unique ID的生成方法一览  
http://calvin1978.blogcn.com/articles/uuid.html