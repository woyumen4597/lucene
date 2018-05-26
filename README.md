Crawler
======

# _An search engine powered by [Apache Lucene](http://lucene.apache.org/)_

## 1. Overview

This is a simple search engine powered by Lucene,developed by Intellij IDEA.

## 2.Installing
_Use Maven for installing_
- Environment:JDK1.8+,Apache Maven,Mysql,[Luke](https://code.google.com/archive/p/luke/)(Optional).
- Requirements:[Apache Lucene](http://lucene.apache.org/),[Jsoup](https://jsoup.org/)
,[HttpClient](https://hc.apache.org/httpcomponents-client-ga/),[SpringFramework](https://projects.spring.io/spring-boot/)
,[Thymeleaf](https://www.thymeleaf.org/).
## 3.Steps
 
### Step1:clone this repository

```
git clone https://github.com/woyumen4597/lucene.git
cd lucene

```  
### Step2:Maven install
```
mvn install

```
### Step3:Configuration
<b>Change db.properties to your own database.</b>
Then switch to your database:
run this following sql:
```sql
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL COMMENT '0:未抓 1:已抽取 2:抽取失败',
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NewIndex1` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=26781 DEFAULT CHARSET=utf8

```

### Step4:Run
```
mkdir indexDir
```
Then find LuceneApplication,run the main method.
Wait a moment,and open http://localhost:8080/api/collect
to collect index.Thus you can watch mysql table task and your indexDir
with Luke.This may take much time.

### Step5:Search

When the page return ok,you can open http://localhost:8080 to start your search.
Congratulations!


## License

Feel free to use, reuse and abuse the code in this project.




