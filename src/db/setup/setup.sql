DROP TABLE IF EXISTS `UserProfileImage`;
DROP TABLE IF EXISTS `UserNotifications`;
DROP TABLE IF EXISTS `UserAttributes`;
DROP TABLE IF EXISTS `ContactCategory`;
DROP TABLE IF EXISTS `ContactAttributes`;
DROP TABLE IF EXISTS `Contacts`;
DROP TABLE IF EXISTS `UserPreference`;
DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(200) DEFAULT NULL,
  `firstName` varchar(100) DEFAULT NULL,
  `firstNameTag` int(2) DEFAULT NULL,
  `middleName` varchar(100) DEFAULT NULL,
  `middleNameTag` int(2) DEFAULT NULL,
  `lastName` varchar(100) DEFAULT NULL,
  `lastNameTag` int(2) DEFAULT NULL,
  `autoDexNum` varchar(100) NOT NULL UNIQUE,
  `autoDexNumTag` int(2) DEFAULT NULL,
  `imieNumber` varchar(200) DEFAULT NULL,
  `dob` varchar(20) DEFAULT NULL,
  `dobTag` int(2) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `companyTag` int(2) DEFAULT NULL,
  `designation` varchar(100) DEFAULT NULL,
  `designationTag` int(2) DEFAULT NULL,
  `personalEmail` varchar(100) DEFAULT NULL,
  `personalEmailTag` int(2) DEFAULT NULL,
  `businessEmail` varchar(100) DEFAULT NULL,
  `businessEmailTag` int(2) DEFAULT NULL,
  `address1` varchar(2000) DEFAULT NULL,
  `address1Tag` int(2) DEFAULT NULL,
  `address2` varchar(2000) DEFAULT NULL,
  `address2Tag` int(2) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `stateTag` int(2) DEFAULT NULL,
  `zip` varchar(100) DEFAULT NULL,
  `zipTag` int(2) DEFAULT NULL,
  `homeLatitude` varchar(100) DEFAULT NULL,
  `homeLongitude` varchar(100) DEFAULT NULL,
  `roamingHomeLatitude` varchar(100) DEFAULT NULL,
  `roamingHomeLongtitude` varchar(100) DEFAULT NULL,
  `createUser` varchar(200) DEFAULT NULL,
  `updateUser` varchar(200) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `new` bit(1) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `cityTag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `Contacts` (
  `id` bigint(20) NOT NULL  AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `profileTag` varchar(200) DEFAULT NULL,
  `firstName` varchar(100) DEFAULT NULL,
  `middleName` varchar(100) DEFAULT NULL,
  `lastName` varchar(100) DEFAULT NULL,
  `phoneNumber` varchar(40) DEFAULT NULL,
  `contactUserId` bigint(20) DEFAULT NULL,
  `dob` varchar(20) DEFAULT NULL,
  `address1` varchar(2000) DEFAULT NULL,
  `address2` varchar(2000) DEFAULT NULL,
  `state` varchar(200) DEFAULT NULL,
  `zip` varchar(50) DEFAULT NULL,
  `createUser` varchar(200) DEFAULT NULL,
  `updateUser` varchar(200) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `useEmail` bit(1) DEFAULT NULL,
  `favorite` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  CONSTRAINT `Contacts_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);
CREATE TABLE `UserAttributes` (
  `id` bigint(20) NOT NULL  AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `value` varchar(40) DEFAULT NULL,
  `tag` int(2) DEFAULT NULL,
  `createUser` varchar(200) DEFAULT NULL,
  `updateUser` varchar(200) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UserAttributes_FK_1` (`userId`),
  CONSTRAINT `UserAttributes_FK_1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);

CREATE TABLE `UserNotifications` (
  `id` bigint(20) NOT NULL  AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `contactId` bigint(20) DEFAULT NULL,
  `type` varchar(40) DEFAULT NULL,
  `message` varchar(2000) DEFAULT NULL,
  `createUser` varchar(200) DEFAULT NULL,
  `updateUser` varchar(200) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UserNotifications_FK_1` (`userId`),
  CONSTRAINT `UserNotifications_FK_1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);

CREATE TABLE `UserProfileImage` (
  `id` bigint(20) NOT NULL  AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `createUser` varchar(200) DEFAULT NULL,
  `updateUser` varchar(200) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `image` longblob,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  CONSTRAINT `UserProfileImage_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);

CREATE TABLE `UserPreference` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `contactNearMeNotify` bit(1) DEFAULT NULL,
  `birthdayNotify` bit(1) DEFAULT NULL,
  `newAutoDexUserNotify` bit(1) DEFAULT NULL,
  `showNotificationAlert` bit(1) DEFAULT NULL,
  `vibrateOnNotificationAlert` bit(1) DEFAULT NULL,
  `popUpNotify` bit(1) DEFAULT NULL,
  `changeNumWithCountryCode` bit(1) DEFAULT NULL,
  `enableOtpNotification` bit(1) DEFAULT NULL,
  `manageBlockUnBlockContacts` bit(1) DEFAULT NULL,
  `manageUserInfo` bit(1) DEFAULT NULL,
  `showFirstLastNameWithImage` bit(1) DEFAULT NULL,
  `notifyMiles` bigint(20) DEFAULT NULL,
  `createUser` varchar(200) DEFAULT NULL,
  `updateUser` varchar(200) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UserPreference_FK_1` (`userId`),
  CONSTRAINT `UserPreference_FK_1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);

CREATE TABLE `ContactAttributes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contactId` bigint(20) DEFAULT NULL,
  `name` varchar(2000) DEFAULT NULL,
  `value` varchar(2000) DEFAULT NULL,
  `createUser` varchar(200) DEFAULT NULL,
  `updateUser` varchar(200) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ContactAttributes_FK_1` (`contactId`),
  CONSTRAINT `ContactAttributes_FK_1` FOREIGN KEY (`contactId`) REFERENCES `Contacts` (`id`)
);

CREATE TABLE `ContactCategory` (
  `id` bigint(20) NOT NULL  AUTO_INCREMENT,
  `contactId` bigint(20) DEFAULT NULL,
  `category` varchar(20) DEFAULT NULL,
  `createUser` varchar(200) DEFAULT NULL,
  `updateUser` varchar(200) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ContactCategory_FK_1` (`contactId`),
  CONSTRAINT `ContactCategory_FK_1` FOREIGN KEY (`contactId`) REFERENCES `Contacts` (`id`)
);

CREATE TABLE `password_recovery` (
  `id` bigint(20) NOT NULL  AUTO_INCREMENT,
  `userId` bigint(20), 
  `otp` varchar(10) NOT NULL,
  `retryCount` int,
  `createdDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT  FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);


drop table if exists oauth_client_details;
create table oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(255)
);
 
drop table if exists oauth_access_token;
create table oauth_access_token (
  token_id VARCHAR(255),
  token LONG VARBINARY,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication LONG VARBINARY,
  refresh_token VARCHAR(255)
);
 
drop table if exists oauth_refresh_token;
create table oauth_refresh_token (
  token_id VARCHAR(255),
  token LONG VARBINARY,
  authentication LONG VARBINARY
);

ALTER TABLE Contacts MODIFY COLUMN profileImage longblob NULL default null;



commit;