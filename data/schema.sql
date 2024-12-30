-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema pay_my_buddy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema pay_my_buddy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pay_my_buddy` DEFAULT CHARACTER SET utf8 ;
USE `pay_my_buddy` ;

-- -----------------------------------------------------
-- Table `pay_my_buddy`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy`.`user` (
                                                     `id` INT NOT NULL AUTO_INCREMENT,
                                                     `username` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy`.`transaction` (
                                                            `id` INT NOT NULL AUTO_INCREMENT,
                                                            `sender` INT NULL,
                                                            `receiver` INT NULL,
                                                            `description` VARCHAR(100) NOT NULL,
    `amount` DOUBLE NOT NULL,
    `date` DATETIME NOT NULL,
    `type` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_transaction_user1_idx` (`sender` ASC) VISIBLE,
    INDEX `fk_transaction_user2_idx` (`receiver` ASC) VISIBLE,
    CONSTRAINT `fk_transaction_user1`
    FOREIGN KEY (`sender`)
    REFERENCES `pay_my_buddy`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_transaction_user2`
    FOREIGN KEY (`receiver`)
    REFERENCES `pay_my_buddy`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy`.`account` (
                                                        `id` INT NOT NULL AUTO_INCREMENT,
                                                        `user_id` INT NOT NULL,
                                                        PRIMARY KEY (`id`),
    INDEX `fk_account_user_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_account_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `pay_my_buddy`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`connections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pay_my_buddy`.`connections` (
                                                            `friend_id` INT NOT NULL,
                                                            `user_id` INT NOT NULL,
                                                            PRIMARY KEY (`user_id`, `friend_id`),
    CONSTRAINT `fk_connections_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `pay_my_buddy`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
