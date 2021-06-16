# DumpTomcatSessions
A simple command line utility to dump Tomcat sessions using [JMX](https://tomcat.apache.org/tomcat-8.0-doc/monitoring.html#Enabling_JMX_Remote) for fun or profit.


```
Usage: java DumpTomcatSessions server port [user password]
```

## Example
```
$ java DumpTomcatSessions 192.168.1.2 1099
Catalina:type=Manager,context=/examples,host=localhost total sessions = 5
3D92A3204B58158299BC70B8E51F668B
E7C65D73EF4D887ACCD346E54E1AAB90
22654CB846F07225C7F3B5F74A2330C0
F57BDBD69C21AE9A049997D85A110458
C3820136711E9BFC01752AFE10C6D75F
Catalina:type=Manager,context=/manager,host=localhost total sessions = 1
87D328E47EECF9C710955ACDD134D931
Catalina:type=Manager,context=/sample,host=localhost total sessions = 3
3A033B7E8DE92A720DBDCE49419BFC2D
2EB4916DAC717C9E2670CF30CAAC447D
9F3069539D9169F466C92656530A642C
```
