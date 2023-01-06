DROP TABLE IF EXISTS company_member;

CREATE TABLE company_member(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    login_name VARCHAR(30) NOT NULL UNIQUE ,
    password VARCHAR(128) NOT NULL ,
    name VARCHAR(30) NOT NULL ,
    phone_verification TINYINT(1) NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS company_food;

CREATE TABLE company_food(
     id BIGINT AUTO_INCREMENT PRIMARY KEY ,
     company_member_id BIGINT NOT NULL ,
     name VARCHAR(30) NOT NULL ,
     price DECIMAL(10,0) NOT NULL ,
     registration_date TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS company_food_history;

CREATE TABLE company_food_history(
     id BIGINT AUTO_INCREMENT PRIMARY KEY ,
     company_food_id BIGINT NOT NULL ,
     price DECIMAL(10,0) NOT NULL,
     update_date TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS general_member;

CREATE TABLE general_member(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_name VARCHAR(30) NOT NULL UNIQUE ,
    password VARCHAR(128) NOT NULL ,
    name VARCHAR(30) NOT NULL ,
    phone_verification TINYINT(1) NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS orders;

CREATE TABLE orders(
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      general_member_id BIGINT NOT NULL ,
      registration_date TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS order_detail;

CREATE TABLE order_detail(
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      order_id BIGINT NOT NULL,
      company_member_id BIGINT NOT NULL ,
      company_food_id BIGINT NOT NULL ,
      food_price DECIMAL NOT NULL,
      food_amount  INT NOT NULL
);
