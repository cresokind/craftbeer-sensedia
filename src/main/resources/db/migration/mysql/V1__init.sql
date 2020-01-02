CREATE TABLE `beer` (
    `id` INT NOT NULL AUTO_INCREMENT , 
	`name` VARCHAR(100) NOT NULL , 
    `category` VARCHAR(50) NOT NULL , 
    `ingredients` VARCHAR(250) NOT NULL ,
    `alcohol_content` FLOAT NOT NULL , 
    `price` FLOAT NOT NULL ,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;