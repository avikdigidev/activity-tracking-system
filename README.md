CREATE SCHEMA `userexperior` ;

CREATE TABLE `userexperior`.`activity_tracker` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`unique_id` BIGINT NOT NULL,
`activity_name` VARCHAR(10) NOT NULL,
`start_time` BIGINT NOT NULL,
`activity_duration` INT NOT NULL,
`date` date DEFAULT NULL,
PRIMARY KEY (`id`));