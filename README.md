# Udger client for Java (data ver. 4)
Local parser is very fast and accurate useragent string detection solution. Enables developers to locally install and integrate a highly-scalable product.
We provide the detection of the devices (personal computer, tablet, Smart TV, Game console etc.), operating system and client SW type (browser, e-mail client etc.).
It also provides information about IP addresses (Public proxies, VPN services, Tor exit nodes, Fake crawlers, Web scrapers .. etc.)


- Tested with more the 1.000.000 unique user agents.
- Up to date data provided by https://udger.com/
- Support for >=Java6


### Performance
Udger java parser uses LRU cache for last N requests. The size of cache can be defined in constructor, default size is 10000 requests. Parser's performance is tuned continuously, currently it reaches following rates:

- >100.000 requests per second if LRU is hitted
- 2.000 requests per second without caching

Using the in memory DB option will even make it faster.

### Compile from git repo

```sh
    $ git clone https://github.com/udger/udger-java-v4
    $ cd udger-java-v4/
    $ mvn package
```

### Requirements
Udger data is stored in SQLite database file. Udger-java connects to SqLite using JDBC driver. SQLiteJDBC jdbc driver is recommended. If you are using Maven2, add the following XML fragments into your pom.xml file:

```xml
    <dependency>
      <groupId>org.xerial</groupId>
      <artifactId>sqlite-jdbc</artifactId>
      <version>3.39.3.0</version>
    </dependency>
```

### Usage

#### How to Specify Udger Database

Example how to create UdgerParser from udger db file `C:\work\udgerdb_v4.dat` (in Windows)

```java
    UdgerParser.ParserDbData parserDbData = new UdgerParser.ParserDbData("C:/work/udgerdb_v4.dat");
    UdgerParser up = = new UdgerParser(parserDbData);
    ...
    up.close();
```

and from a UNIX (Linux, Mac OS X, etc) udger db file `/home/john/work/udgerdb_v4.dat`

```java
    UdgerParser.ParserDbData parserDbData = new UdgerParser.ParserDbData("/home/john/work/udgerdb_v4.dat");
    UdgerParser up = = new UdgerParser(parserDbData);
    ...
    up.close();
```

UdgerParser implements Closeable interface, therefore it must be either opened in `try (...)` statement or explicitly closed.
Since the SQLite connection creating is time consuming task, it is recommended to keep the UdgerParser's instances in
an instance pool. UdgerParser is not thread safe object, therefore it can't be used from multiple thread simultaneously.

Intention of class `UdgerParser.ParserDbData` is to keep precalculated DB-specific data and then improve instantiation
of `UdgerParser`. Using `UdgerParser.ParserDbData` the Udger database can be switched in runtime.


#### How to make use of In Memory feature

The Udger client supports the SQLite DB transactions with the database being in memory. Enabling this feature will make the parser even faster to parse the user agents. Internally the client will re-create the Udger SQLite database from the file into the systems main memory and perform all transactions to it. Since this will require additional memory for operation, it needs to be used carefully with object pools. During pooling with multiple parsers in the pool, this feature will create a separate in memory DB for each new parser and have a single connection to it. This will further allow more concurrency since all connections (from all pooled parsers) now have their own copy of the database.
To enable in memory feature simply use the below constructor and pass inMemoryEnabled as `true`. The internal LRU cache can be used by setting a size > 0 or disabled by passing 0 for the third argument.

Example:

```java
    UdgerParser.ParserDbData parserDbData = new UdgerParser.ParserDbData("/home/john/work/udgerdb_v4.dat");
    UdgerParser up = new UdgerParser(parserDbData, true, 10000);
    ...
    uo.close();
```

### Usage with maven

```xml
<dependency>
    <groupId>org.udger.parserv4</groupId>
    <artifactId>udger-parser-v4</artifactId>
    <version>2.0.0</version>
</dependency>
```

#### Sample.java

```java
public class Sample {
    public static void main(String[] args) {
        UdgerParser.ParserDbData parserDbData = new UdgerParser.ParserDbData("/home/john/work/udgerdb_v4.dat");
        try (UdgerParser up = new UdgerParser(parserDbData)) {
            UdgerUaResult uaRet = up.parseUa("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/601.3.9 (KHTML, like Gecko) Version/9.0.2 Safari/601.3.9");
            UdgerIpResult ipRet = up.parseIp("108.61.199.93");
            UdgerUaRequest.Builder builder = new UdgerUaRequest.Builder();
            UdgerUaRequest req = builder.withUaString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36")
                    .withSecChUa("\"Chromium\";v=\"106\", \"Google Chrome\";v=\"106\", \"Not;A=Brand\";v=\"99\"")
                    .withSecChUaFullVersionList("")
                    .withSecChUaMobile("?0")
                    .withSecChUaFullVersion("\"106.0.5249.91\"")
                    .withSecChUaPlatform("\"Windows\"")
                    .withSecChUaPlatformVersion("\"14.0.0\"")
                    .withSecChUaModel("")
                    .build();

            UdgerUaResult uaRetReq = up.parseUa(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Automatic updates download
- for auto-update data use Udger data updater (https://udger.com/support/documentation/?doc=62)

### Author
The Udger.com Team (info@udger.com)

### v3 format
For the previous data format (v3), please use https://github.com/udger/udger-java
