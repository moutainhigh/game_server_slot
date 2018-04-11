set names 'utf8';
set character_set_database = 'utf8';
set character_set_server = 'utf8';

USE `texas`;

alter table t_rank add scoreList varchar(255) DEFAULT '' COMMENT '已经领取ID';