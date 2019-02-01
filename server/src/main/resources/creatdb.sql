create table tb_finance_account(
  `id` varchar(40) NOT NULL,
  `balance` int not null,
  `app_code` varchar (10) not null,
  `open_id` varchar(50) not null,
  PRIMARY KEY (`id`)
);
create table tb_finance_account_mobile(
  `id` int NOT NULL AUTO_INCREMENT,
  `account_id` varchar (40) not null,
  `mobile` varchar (20) not null,
  primary key(`id`)
);
create table tb_account_bill_log(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_code` varchar(10) not null,
  `account_id` varchar(50) not null,
  `balance` int not null,
  `order_id` varchar(50),
  `create_time` datetime,
  `comment` varchar (100),
  primary key(`id`)
);

create table tb_finance_account_paying_order(
  `id` varchar(40) NOT NULL,
  `balance` int not null,
  `create_time` datetime,
  `paying_order` varchar (50) not null,
  primary key(`id`)
);