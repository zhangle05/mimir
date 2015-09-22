-- MySQL dump 10.13  Distrib 5.6.21, for osx10.8 (x86_64)
--
-- Host: localhost    Database: mimir
-- ------------------------------------------------------
-- Server version	5.6.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `mimir_blog`
--

DROP TABLE IF EXISTS `mimir_blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mimir_blog` (
  `BLOG_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BLOG_TITLE` varchar(255) DEFAULT NULL,
  `BLOG_AUTHOR` varchar(255) DEFAULT NULL,
  `BLOG_CREATETIME` datetime DEFAULT NULL,
  `BLOG_LASTUPDATETIME` datetime DEFAULT NULL,
  `BLOG_ABSTRA` varchar(255) DEFAULT NULL,
  `BLOG_HTMLBODY` text,
  PRIMARY KEY (`BLOG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mimir_blog`
--

LOCK TABLES `mimir_blog` WRITE;
/*!40000 ALTER TABLE `mimir_blog` DISABLE KEYS */;
INSERT INTO `mimir_blog` VALUES (1,'La Nuit de la Philo 2014','Danny','2015-07-13 10:57:47','2015-07-13 10:57:47','For “La nuit de la Philo 2014″ at Institut Francais du Japon – Tokyo , which offered various artists...','For “La nuit de la Philo 2014″ at Institut Francais du Japon – Tokyo , which offered various artists, performances and conferences. We was happy to realized, this year again, the poster of the event. You can see the original poster on the Institut Francais du Japon – Tokyo website, but for the our blog only, we want also to show you one proposal of the poster design we also made to our client.'),(2,'Our greetings cards for the cute new year','Danny','2015-07-13 11:00:56','2015-07-13 11:00:56','Japanese Life Tree is our New year greeting card printed on a Deluxe Paper with red edges. Just a Ja...','Japanese Life Tree is our New year greeting card printed on a Deluxe Paper with red edges. Just a Japanese small life in a Swiss pine tree. (Do not forget that we are a swiss-Japanese studio), Bonus, for our customers and friends, a box of Japanese chocolate customised with our design! To all, we wish you a good and cute new year !'),(3,'New design for the French Business Campus','Danny','2015-07-13 11:01:48','2015-07-13 11:01:48','For the second edition of the French Business Campus organized by the CCIFJ and Meiji University, we...','For the second edition of the French Business Campus organized by the CCIFJ and Meiji University, we redesign the original logotype and, most important, we created the mascot of the event. A little Japanese student girl (we will reveal the name maybe for next edition). We also happy to meet some student at Meiji University. We realized logotype, characters, poster, mail marking, brochure documents, indoor giant posters.'),(4,'A quick stop at the Kurami station.','Zoe','2015-07-13 11:02:39','2015-07-13 11:02:39','This weekend, on the road to Enoshima (A really beautiful small island not so far), we make a brief ...','This weekend, on the road to Enoshima (A really beautiful small island not so far), we make a brief stop at the small train station named “Kurami” (倉見駅 Kurami-eki). It is about one hour from Shinjuku.'),(5,'A franco-japanese logotype','Zoe','2015-07-13 11:03:42','2015-07-13 11:03:42','For the coming of the French president Francois Hollande, the CCIFJ organized a meeting between Fren...','For the coming of the French president Francois Hollande, the CCIFJ organized a meeting between French and Japanese company and asked us to create the corporate around this day. We realized the graphic design of the official brochure, board, menus, cover bento and the official logo of Franco-Japanese meetings. The logotype is based of Japanese origami and colour of French and Japanese flag.'),(6,'Hibernate自动生成\"index\"字段 ','Alex','2015-07-13 11:07:18','2015-07-13 11:07:18','基于Spring框架和Hibernate自动持久化的web项目，某个持久层对象A在映射到MySQL数据库表的时候总是会多出来一个名为\"index\"的整型字段，即使把A所在的java文件中所有index...','基于Spring框架和Hibernate自动持久化的web项目，某个持久层对象A在映射到MySQL数据库表的时候总是会多出来一个名为\"index\"的整型字段，即使把A所在的java文件中所有index字符替换掉也没用。最后发现是在持久层对象B中有对A的引用，并且这个引用通过外键关联，而且要根据A的\"index\"字段排序。在B的java文件中有这样的hibernate标注：\n\n@OrderColumn(name = \"index\")。把这行标注中的\"index\"改掉之后，错误消失。'),(7,'Linux 打开文件数','Alex','2015-07-13 11:08:25','2015-07-13 11:08:25','linux打开文件数量的查看方法\n\n在网上查到两种查看linux打开文件数量的查看方法，但结果不相同，linux查看文件打开数量是以那个文件或命令为标准呢？\n\n\n搜索过关于ulimit命令的一些用法，...','\n\nlinux打开文件数量的查看方法\n\n在网上查到两种查看linux打开文件数量的查看方法，但结果不相同，linux查看文件打开数量是以那个文件或命令为标准呢？\n\n\n搜索过关于ulimit命令的一些用法，其中有\n\nulimit -n 4096\n也就是限制用户的最大文件打开数为4096个\n\n\n在网上查了关于怎么查看文件打开数的文章大致有两种说法\n\n\n/proc/sys/fs/file-nr \n该文件与 file-max 相关，它有三个值： \n\n\n已分配文件句柄的数目 \n\n已使用文件句柄的数目 \n\n文件句柄的最大数目 \n\n该文件是只读的，仅用于显示信息。 \n\n\n查看所有进程的文件打开数\n\nlsof |wc -l\n\n查看某个进程打开的文件数\n\nlsof -p pid |wc -l\n\n\n');
/*!40000 ALTER TABLE `mimir_blog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mimir_location`
--

DROP TABLE IF EXISTS `mimir_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mimir_location` (
  `LOCATION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOCATION_CITY` varchar(255) DEFAULT NULL,
  `LOCATION_DISTRICT` varchar(255) DEFAULT NULL,
  `LOCATION_PROVINCE` varchar(255) DEFAULT NULL,
  `LOCATION_STREET` varchar(255) DEFAULT NULL,
  `LOCATION_STREETNUMBER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`LOCATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mimir_location`
--

LOCK TABLES `mimir_location` WRITE;
/*!40000 ALTER TABLE `mimir_location` DISABLE KEYS */;
/*!40000 ALTER TABLE `mimir_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mimir_photo`
--

DROP TABLE IF EXISTS `mimir_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mimir_photo` (
  `PHOTO_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PHOTO_URI` varchar(255) DEFAULT NULL,
  `PHOTO_TITLE` varchar(255) DEFAULT NULL,
  `PHOTO_DESCRIPTION` varchar(255) DEFAULT NULL,
  `PHOTO_CREATETIME` datetime DEFAULT NULL,
  `PHOTO_THUMBNAIL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`PHOTO_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mimir_photo`
--

LOCK TABLES `mimir_photo` WRITE;
/*!40000 ALTER TABLE `mimir_photo` DISABLE KEYS */;
INSERT INTO `mimir_photo` VALUES (1,'http://img3.redocn.com/tupian/20111105/DIASOFTshiliangbiaozhi_425073_small.jpg','dophin','cute dophin','2015-07-02 00:00:00',NULL),(2,'http://img3.redocn.com/tupian/20111105/AHENNIGshiliangbiaozhi_424521_small.jpg','dophin2','cute dophin2','2015-07-02 00:00:00',NULL),(3,'http://img3.redocn.com/tupian/20120730/lansehaiyingwenhuanraoyuanxingtubiaoCDRwenjian_1128283_small.jpg','dophin3','cute dophin3','2015-07-02 00:00:00',NULL),(4,'http://img.redocn.com/sheying/20150323/yigezongsexiaoxiong_4045729_small.jpg','bear1','cute bear1','2015-07-02 00:00:00',NULL),(5,'http://img.redocn.com/sheying/20141113/xiaoxiong_3504733_small.jpg','bear2','cute bear2','2015-07-02 00:00:00',NULL);
/*!40000 ALTER TABLE `mimir_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mimir_user`
--

DROP TABLE IF EXISTS `mimir_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mimir_user` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_USERNAME` varchar(255) DEFAULT NULL,
  `USER_USERPASSWORD` varchar(255) DEFAULT NULL,
  `USER_USERNICKNAME` varchar(255) DEFAULT NULL,
  `USER_OPENID` varchar(255) DEFAULT NULL,
  `USER_REGTIME` datetime DEFAULT NULL,
  `USER_ISSUBSCRIBE` int(11) DEFAULT NULL,
  `USER_LASTLOGINTIME` datetime DEFAULT NULL,
  `USER_SCHOOLID` bigint(20) DEFAULT NULL,
  `USER_CLASSID` bigint(20) DEFAULT NULL,
  `USER_SCHOOLNAME` varchar(255) DEFAULT NULL,
  `USER_PHONENUMBER` varchar(255) DEFAULT NULL,
  `USER_EMAIL` varchar(255) DEFAULT NULL,
  `USER_STATUS` int(11) DEFAULT NULL,
  `USER_TOKEN` varchar(255) DEFAULT NULL,
  `USER_PROVINCE` varchar(255) DEFAULT NULL,
  `USER_CITY` varchar(255) DEFAULT NULL,
  `USER_DISTRICT` varchar(255) DEFAULT NULL,
  `USER_COUNTRY` varchar(255) DEFAULT NULL,
  `USER_UPDATETIME` datetime DEFAULT NULL,
  `USER_UNIONID` varchar(255) DEFAULT NULL,
  `USER_AVATAR` varchar(255) DEFAULT NULL,
  `USER_ASYNCWECHAT` int(11) DEFAULT NULL,
  `USER_REALLYNAME` varchar(255) DEFAULT NULL,
  `USER_ADDRESS` varchar(255) DEFAULT NULL,
  `USER_ROLEID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mimir_user`
--

LOCK TABLES `mimir_user` WRITE;
/*!40000 ALTER TABLE `mimir_user` DISABLE KEYS */;
INSERT INTO `mimir_user` VALUES (1,'supervisor','88a20de3bc0628b36f9c3b17d52e53d4','supervisor',NULL,NULL,0,'2015-09-22 15:33:46',0,0,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,3);
/*!40000 ALTER TABLE `mimir_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mimir_userrole`
--

DROP TABLE IF EXISTS `mimir_userrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mimir_userrole` (
  `USERROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERROLE_ROLENAME` varchar(255) DEFAULT NULL,
  `USERROLE_DESCRIPTION` varchar(255) DEFAULT NULL,
  `USERROLE_ACCESSLEVEL` int(11) DEFAULT NULL,
  PRIMARY KEY (`USERROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mimir_userrole`
--

LOCK TABLES `mimir_userrole` WRITE;
/*!40000 ALTER TABLE `mimir_userrole` DISABLE KEYS */;
INSERT INTO `mimir_userrole` VALUES (1,'user','registered user',1),(2,'admin','administrator',8),(3,'supervisor','supervisor',16);
/*!40000 ALTER TABLE `mimir_userrole` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-22 15:35:53
