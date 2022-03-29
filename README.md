change the values in application properties:
file.path #location of ActivitiesToProcess folder
#admin credentials of MySQL

spring.datasource.username
spring.datasource.password

# DDL

CREATE SCHEMA `userexperior` ;

CREATE TABLE `userexperior`.`activity_tracker` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`unique_id` BIGINT NOT NULL,
`activity_name` VARCHAR(10) NOT NULL,
`start_time` BIGINT NOT NULL,
`activity_duration` INT NOT NULL,
`date` date DEFAULT NULL,
PRIMARY KEY (`id`));