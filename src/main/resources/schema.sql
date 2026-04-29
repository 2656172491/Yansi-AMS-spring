-- =============================================
-- 言寺资产管理系统 - 数据库初始化脚本
-- 数据库: yansiams
-- =============================================

-- 删除原有表（按外键依赖顺序）
DROP TABLE IF EXISTS `lending_record`;
DROP TABLE IF EXISTS `device_change_order`;
DROP TABLE IF EXISTS `pc_asset`;
DROP TABLE IF EXISTS `asset`;
DROP TABLE IF EXISTS `asset_type`;
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
-- 设备类型表
-- =============================================
CREATE TABLE `asset_type` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '类型名称',
    `code` VARCHAR(20) NOT NULL UNIQUE COMMENT '类型编码',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=启用, 0=禁用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备类型表';

-- 默认设备类型
INSERT INTO `asset_type` (`name`, `code`, `sort_order`) VALUES
('台式主机', 'desktop', 1),
('显示器', 'monitor', 2),
('笔记本', 'laptop', 3),
('智能门锁', 'lock', 4),
('监控', 'camera', 5),
('一卡通', 'card', 6),
('翻页笔', 'pointer', 7),
('网络设备', 'network', 8),
('机械钥匙', 'key', 9),
('未知', 'unknown', 99);

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

-- =============================================
-- 华为电脑管理表（已配出的台式主机+显示器成套）
-- =============================================
CREATE TABLE `pc_asset` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `computer_no` VARCHAR(50) DEFAULT NULL COMMENT '电脑编号',
    `mac_address` VARCHAR(50) DEFAULT NULL COMMENT 'MAC地址',
    `department` VARCHAR(50) DEFAULT NULL COMMENT '保管部门',
    `keeper` VARCHAR(50) DEFAULT NULL COMMENT '保管人',
    `monitor_sn` VARCHAR(100) DEFAULT NULL COMMENT '显示器序列号',
    `host_sn` VARCHAR(100) DEFAULT NULL COMMENT '主机序列号',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=正常运行, 0=已停用',
    `last_inspection_time` DATETIME DEFAULT NULL COMMENT '最近巡检时间',
    `last_inspection_user` VARCHAR(50) DEFAULT NULL COMMENT '最近巡检人',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '软删除: 0=正常, 1=已删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='华为电脑管理';

-- =============================================
-- 测试数据
-- =============================================

-- 用户测试数据 (密码均为 123456)
INSERT INTO `user` (`username`, `password`, `role`, `name`, `email`, `status`) VALUES
('teacher1', '$2a$10$ef6coS3NtEghMeg06934COftXTpVy/YU2SN16aEyWBHxUH.3esWbG', 'teacher', '张老师', 'zhang@school.edu.cn', 1),
('assistant1', '$2a$10$ef6coS3NtEghMeg06934COftXTpVy/YU2SN16aEyWBHxUH.3esWbG', 'assistant', '李同学', 'li@school.edu.cn', 1),
('assistant2', '$2a$10$ef6coS3NtEghMeg06934COftXTpVy/YU2SN16aEyWBHxUH.3esWbG', 'assistant', '王同学', NULL, 1);

-- 库存资产测试数据
INSERT INTO `asset` (`computer_no`, `mac_address`, `host_sn`, `monitor_sn`, `asset_type`, `stock_status`, `purchase_batch`, `remark`, `status`, `deleted`) VALUES
('ASM-00001', NULL, 'HW-DESKTOP-001', NULL, 'desktop', 'in_stock', 'BATCH202601', '华为台式机', 1, 0),
('ASM-00002', NULL, 'HW-DESKTOP-002', NULL, 'desktop', 'in_stock', 'BATCH202601', '华为台式机', 1, 0),
('ASM-00003', NULL, 'HW-DESKTOP-003', NULL, 'desktop', 'in_stock', 'BATCH202601', '华为台式机', 1, 0),
('ASM-00004', NULL, NULL, 'HW-MONITOR-001', 'monitor', 'in_stock', 'BATCH202601', '华为显示器', 1, 0),
('ASM-00005', NULL, NULL, 'HW-MONITOR-002', 'monitor', 'in_stock', 'BATCH202601', '华为显示器', 1, 0),
('ASM-00006', NULL, NULL, 'HW-MONITOR-003', 'monitor', 'in_stock', 'BATCH202601', '华为显示器', 1, 0),
('ASM-00007', NULL, 'SN-LAPTOP-001', NULL, 'laptop', 'in_stock', 'BATCH202602', 'ThinkPad笔记本', 1, 0),
('ASM-00008', NULL, 'SN-LAPTOP-002', NULL, 'laptop', 'in_stock', 'BATCH202602', 'ThinkPad笔记本', 1, 0),
('ASM-00009', NULL, NULL, NULL, 'pointer', 'in_stock', 'BATCH202601', '翻页笔 无线', 1, 0),
('ASM-00010', NULL, NULL, NULL, 'pointer', 'in_stock', 'BATCH202601', '翻页笔 无线', 1, 0),
('ASM-00011', NULL, NULL, NULL, 'lock', 'in_use', NULL, '教学楼301门锁', 1, 0),
('ASM-00012', NULL, NULL, NULL, 'camera', 'in_use', NULL, '走廊监控摄像头', 1, 0),
('ASM-00013', NULL, NULL, NULL, 'network', 'in_use', NULL, '机房交换机', 1, 0);

-- 华为电脑管理测试数据（已配出的成套设备）
INSERT INTO `pc_asset` (`computer_no`, `mac_address`, `host_sn`, `monitor_sn`, `department`, `keeper`, `remark`, `status`, `deleted`) VALUES
('PC-001', 'AA:BB:CC:DD:01:01', 'HW-DESKTOP-100', 'HW-MONITOR-100', '教务处', '赵主任', '教务处主任办公电脑', 1, 0),
('PC-002', 'AA:BB:CC:DD:01:02', 'HW-DESKTOP-101', 'HW-MONITOR-101', '教务处', '刘老师', '教务处日常办公', 1, 0),
('PC-003', 'AA:BB:CC:DD:01:03', 'HW-DESKTOP-102', 'HW-MONITOR-102', '学生处', '陈老师', '学生处办公电脑', 1, 0),
('PC-004', 'AA:BB:CC:DD:01:04', 'HW-DESKTOP-103', 'HW-MONITOR-103', '总务处', '孙老师', '总务处财务专用', 1, 0),
('PC-005', 'AA:BB:CC:DD:01:05', 'HW-DESKTOP-104', 'HW-MONITOR-104', '信息中心', '周老师', '机房管理用机', 1, 0);

-- 设备更换单测试数据
INSERT INTO `device_change_order` (`order_no`, `order_type`, `asset_id`, `reporter`, `reporter_dept`, `fault_desc`, `handler`, `handler_id`, `status`, `result`) VALUES
('CHG-1714300001', 'replace', 1, '赵主任', '教务处', '电脑无法开机，电源指示灯不亮', '李同学', 3, 'pending', NULL),
('CHG-1714300002', 'replace', 2, '刘老师', '教务处', '显示器屏幕闪烁，影响使用', '李同学', 3, 'pending', NULL),
('CHG-1714300003', 'recycle', 3, '陈老师', '学生处', '设备老旧，申请回收更换', '王同学', 4, 'done', 'recycled'),
('CHG-1714300004', 'replace', 7, '孙老师', '总务处', '笔记本电池鼓包', '李同学', 3, 'done', 'replaced');

-- 借出记录测试数据
INSERT INTO `lending_record` (`asset_id`, `borrower`, `borrower_dept`, `lend_time`, `expected_return`, `handler`, `handler_id`, `status`, `remark`) VALUES
(7, '钱老师', '体育组', '2026-04-01 09:00:00', '2026-04-15', '李同学', 3, 'lent', '外出培训借用'),
(8, '吴老师', '英语组', '2026-04-10 14:00:00', '2026-04-20', '王同学', 4, 'lent', '公开课使用'),
(9, '郑老师', '数学组', '2026-03-20 10:00:00', '2026-03-25', '李同学', 3, 'returned', '已归还'),
(10, '冯老师', '语文组', '2026-04-25 08:00:00', '2026-05-10', '王同学', 4, 'lent', '教学展示借用');
