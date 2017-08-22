/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50546
Source Host           : localhost:3306
Source Database       : manage

Target Server Type    : MYSQL
Target Server Version : 50546
File Encoding         : 65001

Date: 2017-08-22 16:49:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `application`
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(30) NOT NULL COMMENT '网站域名',
  `appCode` varchar(255) NOT NULL COMMENT '允许登陆的网站的验证码',
  `startTime` date NOT NULL COMMENT '开始可以使用单点登录的时间',
  `endTime` date NOT NULL COMMENT '可以使用单点登陆的最后时间',
  `loginOutUrl` varchar(255) NOT NULL COMMENT '接受认证中心发送消息的地址',
  `acceptTokenUrl` varchar(255) NOT NULL COMMENT '接受token的回调地址',
  `companyId` int(11) NOT NULL COMMENT '应用所属于的公司的id',
  PRIMARY KEY (`id`),
  KEY `manage_application_2_company` (`companyId`),
  CONSTRAINT `manage_application_2_company` FOREIGN KEY (`companyId`) REFERENCES `company` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of application
-- ----------------------------
INSERT INTO `application` VALUES ('2', '127.0.0.1', 'f70b7462-3e42-470c-8592-50502f698301', '2017-07-01', '2018-07-01', 'http://127.0.0.1:8081/user/loginOut.action', 'http://127.0.0.1:8081/user/acceptToken.action', '1');
INSERT INTO `application` VALUES ('6', '192.168.1.23', '123', '2017-08-11', '2018-08-11', '123123', '231313', '2');
INSERT INTO `application` VALUES ('7', '192.168.2.6', '123', '2017-08-15', '2018-08-15', '1', '1', '2');

-- ----------------------------
-- Table structure for `company`
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '公司名称',
  `address` varchar(255) DEFAULT NULL COMMENT '公司地址',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES ('1', '测试公司', null, null);
INSERT INTO `company` VALUES ('2', '测试公司2', null, null);

-- ----------------------------
-- Table structure for `person`
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `username` varchar(255) NOT NULL COMMENT '账号',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `sex` varchar(255) DEFAULT NULL,
  `companyId` int(11) NOT NULL COMMENT '该用户属于哪个公司',
  PRIMARY KEY (`id`),
  KEY `manage_person_2_company` (`companyId`),
  CONSTRAINT `manage_person_2_company` FOREIGN KEY (`companyId`) REFERENCES `company` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES ('36', '23', '23', '23', '男', '1');
INSERT INTO `person` VALUES ('37', '24', '24', '24', '男', '2');
INSERT INTO `person` VALUES ('38', '26', '26', '26', '女', '2');
