-- auto-generated definition
create table user
(
    id           bigint auto_increment comment 'id
'
        primary key,
    username     varchar(256) charset utf8          null,
    userAccount  varchar(256) charset latin1        null comment '登陆账号',
    avatarUrl    varchar(1024) charset latin1       null comment '头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512) charset latin1        not null comment '密码',
    phone        varchar(128) charset latin1        null comment '电话',
    email        varchar(512) charset latin1        null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态0正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '注册时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    planetCode   varchar(512) charset latin1        null comment '编号'
)
    comment '用户' charset = utf8mb4;