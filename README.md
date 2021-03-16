# dragon-king
流量监控平台





### clickhouse建表语句

```sql

create table log (
                     appName String,
                     appType String,
                     entryMethodTime DateTime,
                     outMethodTime DateTime64,
                     requestStatus Int16,
                     remoteAddr String,
                     receiveHost String,
                     uri String,
                     className String,
                     method String,
                     requestMethod String,
                     consumingTime Int16
)engine = MergeTree
partition by (javaHash(appName), toYYYYMMDD(entryMethodTime))
order by (entryMethodTime, appName, uri, receiveHost);
```
