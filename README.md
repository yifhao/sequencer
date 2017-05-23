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

##类似于MongoDB的UUID

