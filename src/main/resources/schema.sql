-- =============================================
-- 言寺资产管理系统 - 数据库初始化脚本
-- 数据库: yansiams
-- =============================================

-- 删除原有表（按外键依赖顺序）
DROP TABLE IF EXISTS `operation_log`;
DROP TABLE IF EXISTS `login_log`;
DROP TABLE IF EXISTS `asset_flow_log`;
DROP TABLE IF EXISTS `lending_record`;
DROP TABLE IF EXISTS `device_change_order`;
DROP TABLE IF EXISTS `pc_asset`;
DROP TABLE IF EXISTS `network_device`;
DROP TABLE IF EXISTS `daily_supply`;
DROP TABLE IF EXISTS `camera_device`;
DROP TABLE IF EXISTS `meeting_device`;
DROP TABLE IF EXISTS `asset_relation`;
DROP TABLE IF EXISTS `asset_batch`;
DROP TABLE IF EXISTS `asset`;
DROP TABLE IF EXISTS `asset_type`;
DROP TABLE IF EXISTS `asset_category`;
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
-- 设备大类表
-- =============================================
CREATE TABLE `asset_category` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '大类名称',
    `code` VARCHAR(20) NOT NULL UNIQUE COMMENT '大类编码',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=启用, 0=禁用',
    `data_source` VARCHAR(20) NOT NULL DEFAULT 'asset' COMMENT '数据源: asset 或 pc_asset',
    `list_columns` TEXT DEFAULT NULL COMMENT '列表列定义(JSON数组)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备大类表';

INSERT INTO `asset_category` (`name`, `code`, `sort_order`, `data_source`, `list_columns`) VALUES
('华为电脑', 'pc', 1, 'pc_asset', '["computerNo","hostSn","monitorSn","macAddress","department","keeper","status","lastInspectionTime","remark"]'),
('网络设备', 'network', 2, 'network_device', '["deviceNo","sn","remark","status"]'),
('日常用品', 'daily', 3, 'daily_supply', '["supplyType","quantity","warningQuantity","status"]'),
('监控设备', 'camera', 4, 'camera_device', '["deviceNo","sn","remark","status"]'),
('会议设备', 'meeting', 5, 'meeting_device', '["deviceNo","sn","remark","status"]');

-- =============================================
-- 设备类型表
-- =============================================
CREATE TABLE `asset_type` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '类型名称',
    `code` VARCHAR(20) NOT NULL UNIQUE COMMENT '类型编码',
    `category_code` VARCHAR(20) DEFAULT NULL COMMENT '所属大类编码',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=启用, 0=禁用',
    `sn_required` TINYINT NOT NULL DEFAULT 1 COMMENT '是否需要SN码: 1=需要, 0=不需要',
    `batch_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用批次管理: 1=启用, 0=禁用',
    `prefix` VARCHAR(10) DEFAULT NULL COMMENT '内部编码前缀(无SN设备用)',
    `list_columns` TEXT DEFAULT NULL COMMENT '列表列定义(JSON数组)',
    `fields_schema` TEXT DEFAULT NULL COMMENT '表单字段定义(JSON数组)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备类型表';

-- 默认设备类型
INSERT INTO `asset_type` (`name`, `code`, `category_code`, `sort_order`, `sn_required`, `batch_enabled`, `prefix`, `list_columns`, `fields_schema`) VALUES
('台式主机', 'desktop', 'pc', 1, 1, 1, 'PC', '["hostSn","monitorSn","macAddress"]', '["hostSn","monitorSn","macAddress","remark"]'),
('显示器', 'monitor', 'pc', 2, 1, 1, 'MN', '["monitorSn"]', '["monitorSn","remark"]'),
('笔记本', 'laptop', 'pc', 3, 1, 1, 'NB', '["hostSn","macAddress"]', '["hostSn","macAddress","remark"]'),
('智能门锁', 'lock', 'network', 4, 0, 0, 'LK', '["remark"]', '["remark"]'),
('监控', 'camera', 'camera', 5, 0, 0, 'CM', '["remark"]', '["remark"]'),
('一卡通', 'card', 'daily', 6, 1, 1, 'CD', '["hostSn"]', '["hostSn","remark"]'),
('翻页笔', 'pointer', 'daily', 7, 0, 1, 'PN', '["remark"]', '["remark"]'),
('网络设备', 'network', 'network', 8, 0, 0, 'NW', '["remark"]', '["remark"]'),
('机械钥匙', 'key', 'network', 9, 0, 0, 'KY', '["remark"]', '["remark"]'),
('未知', 'unknown', NULL, 99, 0, 1, 'UN', '["remark"]', '["remark"]');

-- =============================================
-- 采购批次表
-- =============================================
CREATE TABLE `asset_batch` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `batch_no` VARCHAR(50) NOT NULL COMMENT '批次号',
    `asset_type` VARCHAR(20) NOT NULL COMMENT '设备类型',
    `purchase_date` DATE DEFAULT NULL COMMENT '购入日期',
    `supplier` VARCHAR(100) DEFAULT NULL COMMENT '供应商',
    `quantity` INT NOT NULL DEFAULT 0 COMMENT '设备数量',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购批次表';

-- =============================================
-- 资产关联表（电脑套装绑定）
-- =============================================
CREATE TABLE `asset_relation` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `host_asset_id` BIGINT NOT NULL COMMENT '主机资产ID',
    `related_asset_id` BIGINT NOT NULL COMMENT '关联资产ID(如显示器)',
    `relation_type` VARCHAR(20) NOT NULL DEFAULT 'computer_set' COMMENT '关系类型',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_relation` (`host_asset_id`, `related_asset_id`, `relation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产关联表';

-- =============================================
-- 资产表
-- =============================================
CREATE TABLE `asset` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `asset_type` VARCHAR(20) NOT NULL COMMENT '设备类型编码',
    `quantity` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    `in_use_quantity` INT NOT NULL DEFAULT 0 COMMENT '在用数量',
    `warning_quantity` INT NOT NULL DEFAULT -1 COMMENT '预警数量, -1=不预警',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=正常, 0=停用',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '软删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产汇总表(按设备类型)';

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
    `asset_category` VARCHAR(20) DEFAULT NULL COMMENT '物资类别(assign类型时)',
    `asset_items` TEXT DEFAULT NULL COMMENT '多设备信息JSON(assign类型时)',
    `assign_dept` VARCHAR(50) DEFAULT NULL COMMENT '配出部门',
    `assign_keeper` VARCHAR(50) DEFAULT NULL COMMENT '配出保管人',
    `assign_computer_no` VARCHAR(50) DEFAULT NULL COMMENT '配出电脑编号',
    `assign_mac_address` VARCHAR(50) DEFAULT NULL COMMENT '配出MAC地址',
    `assign_host_sn` VARCHAR(100) DEFAULT NULL COMMENT '配出主机SN',
    `assign_monitor_sn` VARCHAR(100) DEFAULT NULL COMMENT '配出显示器SN',
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
-- 出入库流水表
-- =============================================
CREATE TABLE `asset_flow_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `asset_id` BIGINT DEFAULT NULL COMMENT '设备ID',
    `asset_type` VARCHAR(20) DEFAULT NULL COMMENT '设备类型',
    `flow_type` VARCHAR(10) NOT NULL COMMENT 'in=入库, out=出库',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `batch_no` VARCHAR(50) DEFAULT NULL COMMENT '批次号',
    `operator` VARCHAR(50) DEFAULT NULL COMMENT '操作人',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `flow_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出入库流水';

-- =============================================
-- 登录日志表
-- =============================================
CREATE TABLE `login_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '登录账号',
    `name` VARCHAR(50) DEFAULT NULL COMMENT '用户姓名',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT '登录IP',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '浏览器信息',
    `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=成功, 0=失败',
    `fail_reason` VARCHAR(200) DEFAULT NULL COMMENT '失败原因'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志';

-- =============================================
-- 操作日志表
-- =============================================
CREATE TABLE `operation_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '操作账号',
    `name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名',
    `operation_type` VARCHAR(20) NOT NULL COMMENT '入库/出库/修改/删除/配出/归还',
    `target_type` VARCHAR(20) DEFAULT NULL COMMENT 'asset/pc_asset/order/lending',
    `target_id` BIGINT DEFAULT NULL COMMENT '目标ID',
    `detail` TEXT DEFAULT NULL COMMENT '操作详情(JSON)',
    `detail_text` VARCHAR(500) DEFAULT NULL COMMENT '可读描述',
    `result` INT NOT NULL DEFAULT 1 COMMENT '1=成功, 0=失败',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT '操作IP',
    `operation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

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
-- 网络设备表
-- =============================================
CREATE TABLE `network_device` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `device_no` VARCHAR(50) DEFAULT NULL COMMENT '编号',
    `sn` VARCHAR(100) DEFAULT NULL COMMENT 'SN码',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=正常, 0=停用',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '软删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网络设备';

-- =============================================
-- 日常用品表
-- =============================================
CREATE TABLE `daily_supply` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `supply_type` VARCHAR(50) DEFAULT NULL COMMENT '设备类型',
    `quantity` INT NOT NULL DEFAULT 0 COMMENT '设备数量',
    `warning_quantity` INT NOT NULL DEFAULT 0 COMMENT '预警数量',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=正常, 0=停用',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '软删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日常用品';

-- =============================================
-- 监控设备表
-- =============================================
CREATE TABLE `camera_device` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `device_no` VARCHAR(50) DEFAULT NULL COMMENT '编号',
    `sn` VARCHAR(100) DEFAULT NULL COMMENT 'SN码',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=正常, 0=停用',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '软删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监控设备';

-- =============================================
-- 会议设备表
-- =============================================
CREATE TABLE `meeting_device` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `device_no` VARCHAR(50) DEFAULT NULL COMMENT '编号',
    `sn` VARCHAR(100) DEFAULT NULL COMMENT 'SN码',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1=正常, 0=停用',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '软删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议设备';

-- =============================================
-- 测试数据
-- =============================================

-- 用户测试数据 (密码均为 123456)
INSERT INTO `user` (`username`, `password`, `role`, `name`, `email`, `status`) VALUES
('teacher1', '$2a$10$ef6coS3NtEghMeg06934COftXTpVy/YU2SN16aEyWBHxUH.3esWbG', 'teacher', '张老师', 'zhang@school.edu.cn', 1),
('assistant1', '$2a$10$ef6coS3NtEghMeg06934COftXTpVy/YU2SN16aEyWBHxUH.3esWbG', 'assistant', '李同学', 'li@school.edu.cn', 1),
('assistant2', '$2a$10$ef6coS3NtEghMeg06934COftXTpVy/YU2SN16aEyWBHxUH.3esWbG', 'assistant', '王同学', NULL, 1);

-- 库存资产测试数据
INSERT INTO `asset` (`asset_type`, `quantity`, `in_use_quantity`, `warning_quantity`, `remark`, `status`, `deleted`) VALUES
('desktop', 5, 3, 2, '华为台式主机', 1, 0),
('monitor', 4, 3, 2, '华为显示器', 1, 0),
('laptop', 3, 2, 1, 'ThinkPad笔记本', 1, 0),
('pointer', 10, 2, 5, '无线翻页笔', 1, 0),
('lock', 8, 6, -1, '智能门锁', 1, 0),
('camera', 12, 10, -1, '监控摄像头', 1, 0),
('network', 6, 4, 2, '交换机/AP', 1, 0),
('key', 15, 10, -1, '机械钥匙', 1, 0),
('card', 200, 150, 50, '学生一卡通', 1, 0);

-- 采购批次测试数据
INSERT INTO `asset_batch` (`batch_no`, `asset_type`, `purchase_date`, `supplier`, `quantity`, `remark`) VALUES
('BATCH202601', 'desktop', '2026-01-15', '华为经销商', 3, '华为台式机采购'),
('BATCH202601', 'monitor', '2026-01-15', '华为经销商', 3, '华为显示器采购'),
('BATCH202601', 'pointer', '2026-01-20', '京东', 2, '翻页笔采购'),
('BATCH202602', 'laptop', '2026-02-10', '联想经销商', 2, 'ThinkPad笔记本采购');

-- 资产关联测试数据（主机+显示器套装）
INSERT INTO `asset_relation` (`host_asset_id`, `related_asset_id`, `relation_type`) VALUES
(1, 4, 'computer_set'),
(2, 5, 'computer_set'),
(3, 6, 'computer_set');

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

INSERT INTO `device_change_order` (`order_no`, `order_type`, `asset_id`, `reporter`, `reporter_dept`, `fault_desc`, `handler`, `handler_id`, `status`, `result`, `asset_category`, `asset_items`, `assign_dept`, `assign_keeper`, `assign_computer_no`, `assign_mac_address`, `assign_host_sn`, `assign_monitor_sn`) VALUES
('CHG-1714300005', 'assign', 1, '周老师', '信息中心', '新机房配出电脑', '王同学', 4, 'done', NULL, 'desktop', '[{"type":"desktop","sn":"HW-DESKTOP-001","remark":"主机"},{"type":"monitor","sn":"HW-MONITOR-001","remark":"显示器"}]', '信息中心', '周老师', 'ASM-00001', 'AA:BB:CC:DD:02:01', 'HW-DESKTOP-001', 'HW-MONITOR-001');

-- 借出记录测试数据
INSERT INTO `lending_record` (`asset_id`, `borrower`, `borrower_dept`, `lend_time`, `expected_return`, `handler`, `handler_id`, `status`, `remark`) VALUES
(7, '钱老师', '体育组', '2026-04-01 09:00:00', '2026-04-15', '李同学', 3, 'lent', '外出培训借用'),
(8, '吴老师', '英语组', '2026-04-10 14:00:00', '2026-04-20', '王同学', 4, 'lent', '公开课使用'),
(9, '郑老师', '数学组', '2026-03-20 10:00:00', '2026-03-25', '李同学', 3, 'returned', '已归还'),
(10, '冯老师', '语文组', '2026-04-25 08:00:00', '2026-05-10', '王同学', 4, 'lent', '教学展示借用');

-- 登录日志测试数据
INSERT INTO `login_log` (`user_id`, `username`, `name`, `ip`, `user_agent`, `login_time`, `status`, `fail_reason`) VALUES
(1, 'admin', '系统管理员', '127.0.0.1', 'Mozilla/5.0 Chrome/120', '2026-04-28 08:30:00', 1, NULL),
(2, 'teacher1', '张老师', '192.168.1.100', 'Mozilla/5.0 Chrome/120', '2026-04-28 09:00:00', 1, NULL),
(3, 'assistant1', '李同学', '192.168.1.101', 'Mozilla/5.0 Safari/17', '2026-04-29 08:00:00', 1, NULL),
(1, 'admin', '系统管理员', '127.0.0.1', 'Mozilla/5.0 Chrome/120', '2026-04-29 08:30:00', 1, NULL),
(NULL, 'hacker', NULL, '10.0.0.1', 'curl/7.88', '2026-04-29 12:00:00', 0, '用户不存在');

-- 操作日志测试数据
INSERT INTO `operation_log` (`user_id`, `username`, `name`, `operation_type`, `target_type`, `target_id`, `detail_text`, `result`, `ip`, `operation_time`) VALUES
(1, 'admin', '系统管理员', '入库', 'asset', 1, '入库了 3 台台式主机，批次号 BATCH202601', 1, '127.0.0.1', '2026-04-28 10:00:00'),
(3, 'assistant1', '李同学', '配出', 'asset', 1, '将台式主机 ASM-00001 配出给信息中心周老师', 1, '192.168.1.101', '2026-04-28 14:00:00'),
(2, 'teacher1', '张老师', '修改', 'pc_asset', 1, '修改了 PC-001 的保管人为赵主任', 1, '192.168.1.100', '2026-04-29 09:00:00'),
(3, 'assistant1', '李同学', '归还', 'lending', 3, '归还了翻页笔 ASM-00009，借用人郑老师', 1, '192.168.1.101', '2026-04-29 10:00:00');

-- 网络设备测试数据
INSERT INTO `network_device` (`device_no`, `sn`, `remark`, `status`) VALUES
('NET-001', 'SN-SW-24P-001', '24口交换机-机房A', 1),
('NET-002', 'SN-SW-24P-002', '24口交换机-机房B', 1),
('NET-003', 'SN-AP-001', '无线AP-教学楼1F', 1),
('NET-004', 'SN-AP-002', '无线AP-教学楼2F', 1),
('NET-005', 'SN-FW-001', '防火墙-主出口', 1);

-- 日常用品测试数据
INSERT INTO `daily_supply` (`supply_type`, `quantity`, `warning_quantity`, `status`) VALUES
('翻页笔', 15, 5, 1),
('鼠标', 30, 10, 1),
('键盘', 20, 8, 1),
('网线(根)', 50, 20, 1),
('电源线', 25, 10, 1);

-- 监控设备测试数据
INSERT INTO `camera_device` (`device_no`, `sn`, `remark`, `status`) VALUES
('CAM-001', 'SN-IPC-200W-001', '200W枪机-校门口', 1),
('CAM-002', 'SN-IPC-200W-002', '200W枪机-操场', 1),
('CAM-003', 'SN-IPC-400W-001', '400W球机-大厅', 1),
('CAM-004', 'SN-NVR-16CH-001', '16路录像机-监控室', 1);

-- 会议设备测试数据
INSERT INTO `meeting_device` (`device_no`, `sn`, `remark`, `status`) VALUES
('MTG-001', 'SN-MIC-001', '无线话筒-会议室A', 1),
('MTG-002', 'SN-MIC-002', '无线话筒-会议室B', 1),
('MTG-003', 'SN-PROJ-001', '投影仪-会议室A', 1),
('MTG-004', 'SN-PROJ-002', '投影仪-阶梯教室', 1);

-- 出入库流水测试数据
INSERT INTO `asset_flow_log` (`asset_id`, `asset_type`, `flow_type`, `quantity`, `batch_no`, `operator`, `operator_id`, `flow_time`) VALUES
(NULL, 'desktop', 'in', 3, 'BATCH202601', '系统管理员', 1, '2026-04-28 10:00:00'),
(NULL, 'monitor', 'in', 3, 'BATCH202601', '系统管理员', 1, '2026-04-28 10:00:00'),
(NULL, 'laptop', 'in', 2, 'BATCH202602', '系统管理员', 1, '2026-04-28 10:30:00'),
(NULL, 'pointer', 'in', 2, 'BATCH202601', '系统管理员', 1, '2026-04-28 11:00:00'),
(1, 'desktop', 'out', 1, NULL, '李同学', 3, '2026-04-28 14:00:00'),
(7, 'laptop', 'out', 1, NULL, '李同学', 3, '2026-04-29 09:00:00'),
(8, 'laptop', 'out', 1, NULL, '王同学', 4, '2026-04-29 10:00:00');
