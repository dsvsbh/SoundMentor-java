-- 创建组织表
CREATE TABLE organization (
                              id INT AUTO_INCREMENT PRIMARY KEY COMMENT '组织ID',
                              name VARCHAR(100) NOT NULL COMMENT '组织名称',
                              description TEXT COMMENT '组织描述',
                              created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='组织表';

-- 创建用户表
CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
                      name VARCHAR(100) NOT NULL COMMENT '用户名称',
                      email VARCHAR(100) UNIQUE NOT NULL COMMENT '用户邮箱',
                      username VARCHAR(100) UNIQUE NOT NULL COMMENT '用户名',
                      password VARCHAR(255) NOT NULL COMMENT '用户密码',
                      created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

create table organization_user(
    id int auto_increment primary key,
    organization_id int not null,
    user_id int not null,
    organization_role int not null default 0 -- 用户在特定组织的角色 0:普通成员，1:管理员 2:创建者
)