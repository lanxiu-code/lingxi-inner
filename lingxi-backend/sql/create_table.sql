# 数据库初始化

-- 创建库
create database if not exists lingxi;

-- 切换库
use lingxi;
-- 用户表
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(256) null comment '用户昵称',
    userAccount  varchar(256) null comment '账号',
    userProfile  varchar(1024) null comment '用户简介',
    avatarUrl    varchar(1024) null comment '用户头像',
    gender       tinyint null comment '性别',
    userPassword varchar(512)       not null comment '密码',
    phone        varchar(128) null comment '电话',
    email        varchar(512) null comment '邮箱',
    userStatus   int      default 0 not null comment '状态 0 - 正常',
    userRole     int      default 0 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    tags         varchar(1024) null comment '标签 json 列表',
    ipInfo      json DEFAULT NULL COMMENT 'ip信息',
    followCount int(11) DEFAULT '0' COMMENT '关注人数',
    fansCount   int(11) DEFAULT '0' COMMENT '粉丝人数',
    status       int(11) DEFAULT '0' COMMENT '使用状态 0.正常 1拉黑',
    lastOptTime datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '最后上下线时间',
    activeStatus int(11) DEFAULT '2' COMMENT '在线状态 1在线 2离线',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0 not null comment '是否删除'
) comment '用户';

-- 队伍表
create table team
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(256)       not null comment '队伍名称',
    description varchar(1024) null comment '描述',
    icon        varchar(1024) null comment '队伍图标',
    maxNum      int      default 1 not null comment '最大人数',
    expireTime  datetime null comment '过期时间',
    userId      bigint comment '用户id（队长 id）',
    status      int      default 0 not null comment '0 - 公开，1 - 私有，2 - 加密',
    password    varchar(512) null comment '密码',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete    tinyint  default 0 not null comment '是否删除'
) comment '队伍';

-- 用户队伍关系
create table user_team
(
    id         bigint auto_increment comment 'id'
        primary key,
    userId     bigint comment '用户id',
    teamId     bigint comment '队伍id',
    joinTime   datetime null comment '加入时间',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete   tinyint  default 0 not null comment '是否删除'
) comment '用户队伍关系';


-- 标签表（可以不创建，因为标签字段已经放到了用户表中）
create table tag
(
    id         bigint auto_increment comment 'id'
        primary key,
    tagName    varchar(256) null comment '标签名称',
    userId     bigint null comment '用户 id',
    parentId   bigint null comment '父标签 id',
    isParent   tinyint null comment '0 - 不是, 1 - 父标签',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete   tinyint  default 0 not null comment '是否删除',
    constraint uniIdx_tagName
        unique (tagName)
) comment '标签';

create index idx_userId
    on tag (userId);

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
                            `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `roomId` bigint(20) NOT NULL COMMENT '会话表id',
                            `fromUid` bigint(20) NOT NULL COMMENT '消息发送者uid',
                            `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
                            `replyMsgId` bigint(20) NULL DEFAULT NULL COMMENT '回复的消息内容',
                            `status` int(11) NOT NULL COMMENT '消息状态 0正常 1删除',
                            `gapCount` int(11) NULL DEFAULT NULL COMMENT '与回复的消息间隔多少条',
                            `type` int(11) NULL DEFAULT 1 COMMENT '消息类型 1正常文本 2.撤回消息',
                            `extra` json DEFAULT NULL COMMENT '扩展信息',
                            `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                            `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `idx_room_id`(`roomId`) USING BTREE,
                            INDEX `idx_from_uid`(`fromUid`) USING BTREE,
                            INDEX `idx_create_time`(`createTime`) USING BTREE,
                            INDEX `idx_update_time`(`updateTime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `black`;
CREATE TABLE `black`  (
                          `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                          `type` int(11) NOT NULL COMMENT '拉黑目标类型 1.ip 2uid',
                          `target` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拉黑目标',
                          `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                          `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `idx_type_target`(`type`, `target`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '黑名单' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `user_emoji`;
CREATE TABLE `user_emoji` (
                              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `uid` bigint(20) NOT NULL COMMENT '用户表ID',
                              `expressionUrl` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表情地址',
                              `isDelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-正常,1-删除)',
                              `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                              `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `IDX_USER_EMOJIS_UID` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户表情包';

DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend` (
                               `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `uid` bigint(20) NOT NULL COMMENT '用户uid',
                               `friendUid` bigint(20) NOT NULL COMMENT '好友uid',
                               `isDelete` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-正常,1-删除)',
                               `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                               `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               KEY `idx_uid_friend_uid` (`uid`,`friendUid`) USING BTREE,
                               KEY `idx_create_time` (`createTime`) USING BTREE,
                               KEY `idx_update_time` (`updateTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户联系人表';

DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
                        `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                        `type` int(11) NOT NULL COMMENT '房间类型 1群聊 2单聊',
                        `hotFlag` int(11) DEFAULT '0' COMMENT '是否全员展示 0否 1是',
                        `activeTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '群最后消息的更新时间（热点群不需要写扩散，只更新这里）',
                        `lastMsgId` bigint(20) DEFAULT NULL COMMENT '会话中的最后一条消息id',
                        `extJson` json DEFAULT NULL COMMENT '额外信息（根据不同类型房间有不同存储的东西）',
                        `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                        `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                        PRIMARY KEY (`id`) USING BTREE,
                        KEY `idx_create_time` (`createTime`) USING BTREE,
                        KEY `idx_update_time` (`updateTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间表';
DROP TABLE IF EXISTS `room_friend`;
CREATE TABLE `room_friend` (
                               `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `roomId` bigint(20) NOT NULL COMMENT '房间id',
                               `uid1` bigint(20) NOT NULL COMMENT 'uid1（更小的uid）',
                               `uid2` bigint(20) NOT NULL COMMENT 'uid2（更大的uid）',
                               `roomKey` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '房间key由两个uid拼接，先做排序uid1_uid2',
                               `status` int(11) NOT NULL COMMENT '房间状态 0正常 1禁用(删好友了禁用)',
                               `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                               `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `room_key` (`roomKey`) USING BTREE,
                               KEY `idx_room_id` (`roomId`) USING BTREE,
                               KEY `idx_create_time` (`createTime`) USING BTREE,
                               KEY `idx_update_time` (`updateTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单聊房间表';

DROP TABLE IF EXISTS `room_group`;
CREATE TABLE `room_group` (
                              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `roomId` bigint(20) NOT NULL COMMENT '房间id',
                              `name` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群名称',
                              `avatar` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群头像',
                              `extJson` json DEFAULT NULL COMMENT '额外信息（根据不同类型房间有不同存储的东西）',
                              `delete_status` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-正常,1-删除)',
                              `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                              `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `idx_room_id` (`roomId`) USING BTREE,
                              KEY `idx_create_time` (`createTime`) USING BTREE,
                              KEY `idx_update_time` (`updateTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群聊房间表';

DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member` (
                                `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `groupId` bigint(20) NOT NULL COMMENT '群组id',
                                `uid` bigint(20) NOT NULL COMMENT '成员uid',
                                `role` int(11) NOT NULL COMMENT '成员角色 1群主 2管理员 3普通成员',
                                `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                                `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                                PRIMARY KEY (`id`) USING BTREE,
                                KEY `idx_group_id_role` (`groupId`,`role`) USING BTREE,
                                KEY `idx_create_time` (`createTime`) USING BTREE,
                                KEY `idx_update_time` (`updateTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群成员表';

DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
                           `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                           `uid` bigint(20) NOT NULL COMMENT 'uid',
                           `roomId` bigint(20) NOT NULL COMMENT '房间id',
                           `readTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '阅读到的时间',
                           `activeTime` datetime(3) DEFAULT NULL COMMENT '会话内消息最后更新的时间(只有普通会话需要维护，全员会话不需要维护)',
                           `lastMsgId` bigint(20) DEFAULT NULL COMMENT '会话最新消息id',
                           `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                           `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE KEY `uniq_uid_room_id` (`uid`,`roomId`) USING BTREE,
                           KEY `idx_room_id_read_time` (`roomId`,`readTime`) USING BTREE,
                           KEY `idx_create_time` (`createTime`) USING BTREE,
                           KEY `idx_update_time` (`updateTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话列表';

DROP TABLE IF EXISTS `secure_invoke_record`;
CREATE TABLE `secure_invoke_record` (
                                        `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                                        `secureInvokeJson` json NOT NULL COMMENT '请求快照参数json',
                                        `status` tinyint(8) NOT NULL COMMENT '状态 1待执行 2已失败',
                                        `nextRetryTime` datetime(3) NOT NULL COMMENT '下一次重试的时间',
                                        `retryTimes` int(11) NOT NULL COMMENT '已经重试的次数',
                                        `maxRetryTimes` int(11) NOT NULL COMMENT '最大重试次数',
                                        `failReason` text COMMENT '执行失败的堆栈',
                                        `createTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                                        `updateTime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        KEY `idx_next_retry_time` (`nextRetryTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='本地消息表';


# 触发器：自动更新关注数和粉丝数
create trigger user_friend_insert_trigger
    after insert on user_friend for each row
begin
    update user
    set followCount = followCount + 1
    where id = new.uid;
    update user
    set fansCount = fansCount + 1
    where id = new.friendUid;
end;


create trigger user_friend_delete_trigger
    after delete on user_friend for each row
begin
    update user
    set followCount = followCount - 1
    where id = old.uid;
    update user
    set fansCount = fansCount - 1
    where id = old.friendUid;
end;