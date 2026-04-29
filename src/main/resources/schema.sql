-- =============================================
-- 言寺资产管理系统 - 数据库初始化脚本
-- 数据库: yansiams
-- =============================================

-- 删除原有表（按外键依赖顺序）
DROP TABLE IF EXISTS `lending_record`;
DROP TABLE IF EXISTS `device_change_order`;
DROP TABLE IF EXISTS `asset`;
DROP TABLE IF EXISTS `user`;

-- =============================================
-- 用户表
-- =============================================
CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录名',
    `password` VARCHAR(255) NOT NULL COMMENT 'bcrypt加密密码',
    `role` VARCHAR(20) NOT NULL COMMENT 'admin/teacher/assistant',
    `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=启用, 0=禁用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 默认管理员账号 (密码: admin123)
INSERT INTO `user` (`username`, `password`, `role`, `name`, `email`, `status`)
VALUES ('admin', '$2a$10$ef6coS3NtEghMeg06934COftXTpVy/YU2SN16aEyWBHxUH.3esWbG', 'admin', '系统管理员', NULL, 1);

-- =============================================
-- 资产表
-- =============================================
CREATE TABLE `asset` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `computer_no` VARCHAR(50) DEFAULT NULL COMMENT '电脑编号',
    `mac_address` VARCHAR(50) DEFAULT NULL COMMENT 'MAC地址',
    `department` VARCHAR(50) DEFAULT NULL COMMENT '保管部门',
    `keeper` VARCHAR(50) DEFAULT NULL COMMENT '保管人',
    `monitor_sn` VARCHAR(100) DEFAULT NULL COMMENT '显示器序列号',
    `host_sn` VARCHAR(100) DEFAULT NULL COMMENT '主机序列号',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=正常运行, 0=已停用',
    `asset_type` VARCHAR(20) NOT NULL DEFAULT 'unknown' COMMENT '设备类型: desktop/monitor/lock/camera/card/laptop/pointer/network/key/unknown',
    `stock_status` VARCHAR(20) NOT NULL DEFAULT 'in_stock' COMMENT '库存状态: in_stock/in_use/scrapped/returned',
    `purchase_time` DATETIME DEFAULT NULL COMMENT '采购入库时间',
    `purchase_batch` VARCHAR(50) DEFAULT NULL COMMENT '采购批次号',
    `last_inspection_time` DATETIME DEFAULT NULL COMMENT '最近巡检时间',
    `last_inspection_user` VARCHAR(50) DEFAULT NULL COMMENT '最近巡检人',
    `last_inspection_batch` VARCHAR(50) DEFAULT NULL COMMENT '最近巡检批次号',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '软删除: 0=正常, 1=已删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产表';

-- =============================================
-- 设备更换单
-- =============================================
CREATE TABLE `device_change_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_no` VARCHAR(50) NOT NULL COMMENT '单号',
    `order_type` VARCHAR(20) NOT NULL COMMENT 'replace(更换)/recycle(回收)',
    `asset_id` BIGINT NOT NULL COMMENT '涉及设备ID',
    `new_asset_id` BIGINT DEFAULT NULL COMMENT '新设备ID(更换时)',
    `reporter` VARCHAR(50) NOT NULL COMMENT '报修人',
    `reporter_dept` VARCHAR(50) DEFAULT NULL COMMENT '报修人部门',
    `fault_desc` TEXT COMMENT '故障描述',
    `handler` VARCHAR(50) DEFAULT NULL COMMENT '经办人',
    `handler_id` BIGINT DEFAULT NULL COMMENT '经办人用户ID',
    `status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT 'pending/checking/done',
    `result` VARCHAR(20) DEFAULT NULL COMMENT 'repaired/replaced/recycled',
    `remark` TEXT COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备更换单';

-- =============================================
-- 借出记录
-- =============================================
CREATE TABLE `lending_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `asset_id` BIGINT NOT NULL COMMENT '借出设备ID',
    `borrower` VARCHAR(50) NOT NULL COMMENT '借用人',
    `borrower_dept` VARCHAR(50) DEFAULT NULL COMMENT '借用部门',
    `lend_time` DATETIME NOT NULL COMMENT '借出时间',
    `expected_return` DATE DEFAULT NULL COMMENT '预计归还',
    `actual_return` DATETIME DEFAULT NULL COMMENT '实际归还',
    `handler` VARCHAR(50) NOT NULL COMMENT '经办人',
    `handler_id` BIGINT DEFAULT NULL COMMENT '经办人用户ID',
    `status` VARCHAR(20) NOT NULL DEFAULT 'lent' COMMENT 'lent/returned',
    `remark` TEXT COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='借出记录';
