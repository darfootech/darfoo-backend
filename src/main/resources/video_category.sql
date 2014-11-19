DROP TABLE IF EXISTS `video_category`;
CREATE TABLE `video_category` (
  `ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `TITLE` VARCHAR(255) NOT NULL,
  `DESCRIPTION` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;