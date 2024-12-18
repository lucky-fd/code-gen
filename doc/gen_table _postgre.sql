
-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS gen_table CASCADE;

CREATE TABLE gen_table (
  table_id BIGSERIAL PRIMARY KEY NOT NULL, -- 编号
  table_name VARCHAR(200) DEFAULT '' NOT NULL, -- 表名称
  table_comment VARCHAR(500) DEFAULT '' NOT NULL, -- 表描述
  sub_table_name VARCHAR(64), -- 关联子表的表名
  sub_table_fk_name VARCHAR(64), -- 子表关联的外键名
  class_name VARCHAR(100) DEFAULT '' NOT NULL, -- 实体类名称
  tpl_category VARCHAR(200) DEFAULT 'crud' NOT NULL, -- 使用的模板（crud单表操作 tree树表操作）
  package_name VARCHAR(100), -- 生成包路径
  module_name VARCHAR(300), -- 生成模块名
  business_name VARCHAR(300), -- 生成业务名
  function_name VARCHAR(50), -- 生成功能名
  function_author VARCHAR(50), -- 生成功能作者
  gen_type CHAR(1) DEFAULT '0' NOT NULL, -- 生成代码方式（0zip压缩包 1自定义路径）
  gen_path VARCHAR(200) DEFAULT '/' NOT NULL, -- 生成路径（不填默认项目路径）
  options VARCHAR(1000), -- 其它生成选项
  create_by VARCHAR(64) DEFAULT '' NOT NULL, -- 创建者
  create_time TIMESTAMP WITHOUT TIME ZONE, -- 创建时间
  update_by VARCHAR(64) DEFAULT '' NOT NULL, -- 更新者
  update_time TIMESTAMP WITHOUT TIME ZONE, -- 更新时间
  remark VARCHAR(500), -- 备注
  template_type VARCHAR(255) DEFAULT 'base' NOT NULL, -- 模板类型
  module_business_name VARCHAR(255), -- 模块业务名称
  is_process BOOLEAN, -- 是否是流程模板
  is_associate BOOLEAN, -- 是否是关联查询菜单
  has_export_and_active BOOLEAN, -- 是否包含导出和归档
  sub_module_business_name VARCHAR(255) -- 子业务名称
);

COMMENT ON TABLE gen_table IS '代码生成业务表';
COMMENT ON COLUMN gen_table.table_id IS '编号';
COMMENT ON COLUMN gen_table.table_name IS '表名称';
COMMENT ON COLUMN gen_table.table_comment IS '表描述';
COMMENT ON COLUMN gen_table.sub_table_name IS '关联子表的表名';
COMMENT ON COLUMN gen_table.sub_table_fk_name IS '子表关联的外键名';
COMMENT ON COLUMN gen_table.class_name IS '实体类名称';
COMMENT ON COLUMN gen_table.tpl_category IS '使用的模板（crud单表操作 tree树表操作）';
COMMENT ON COLUMN gen_table.package_name IS '生成包路径';
COMMENT ON COLUMN gen_table.module_name IS '生成模块名';
COMMENT ON COLUMN gen_table.business_name IS '生成业务名';
COMMENT ON COLUMN gen_table.function_name IS '生成功能名';
COMMENT ON COLUMN gen_table.function_author IS '生成功能作者';
COMMENT ON COLUMN gen_table.gen_type IS '生成代码方式（0zip压缩包 1自定义路径）';
COMMENT ON COLUMN gen_table.gen_path IS '生成路径（不填默认项目路径）';
COMMENT ON COLUMN gen_table.options IS '其它生成选项';
COMMENT ON COLUMN gen_table.create_by IS '创建者';
COMMENT ON COLUMN gen_table.create_time IS '创建时间';
COMMENT ON COLUMN gen_table.update_by IS '更新者';
COMMENT ON COLUMN gen_table.update_time IS '更新时间';
COMMENT ON COLUMN gen_table.remark IS '备注';
COMMENT ON COLUMN gen_table.template_type IS '模板类型';
COMMENT ON COLUMN gen_table.module_business_name IS '模块业务名称';
COMMENT ON COLUMN gen_table.is_process IS '是否是流程模板';
COMMENT ON COLUMN gen_table.is_associate IS '是否是关联查询菜单';
COMMENT ON COLUMN gen_table.has_export_and_active IS '是否包含导出和归档';
COMMENT ON COLUMN gen_table.sub_module_business_name IS '子业务名称';

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------

DROP TABLE IF EXISTS gen_table_column CASCADE;

CREATE TABLE gen_table_column (
  column_id BIGSERIAL PRIMARY KEY NOT NULL, -- 编号
  table_id VARCHAR(64), -- 归属表编号
  column_name VARCHAR(200), -- 列名称
  column_comment VARCHAR(500), -- 列描述
  column_type VARCHAR(100), -- 列类型
  data_type VARCHAR(100), -- 列数据类型
  java_type VARCHAR(500), -- JAVA类型
  java_field VARCHAR(200), -- JAVA字段名
  is_pk CHAR(1), -- 是否主键（1是）
  is_increment CHAR(1), -- 是否自增（1是）
  is_required CHAR(1), -- 是否必填（1是）
  is_insert CHAR(1), -- 是否为插入字段（1是）
  is_edit CHAR(1), -- 是否编辑字段（1是）
  is_list CHAR(1), -- 是否列表字段（1是）
  is_query CHAR(1), -- 是否查询字段（1是）
  query_type VARCHAR(200) DEFAULT 'EQ', -- 查询方式（等于、不等于、大于、小于、范围）
  html_type VARCHAR(200), -- 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
  dict_type VARCHAR(200) DEFAULT '', -- 字典类型
  sort INT, -- 排序
  create_by VARCHAR(64) DEFAULT '', -- 创建者
  create_time TIMESTAMP WITHOUT TIME ZONE, -- 创建时间
  update_by VARCHAR(64) DEFAULT '', -- 更新者
  update_time TIMESTAMP WITHOUT TIME ZONE, -- 更新时间
  template_type VARCHAR(255) DEFAULT 'base' -- 模板类型
);

COMMENT ON TABLE gen_table_column IS '代码生成业务表字段';
COMMENT ON COLUMN gen_table_column.column_id IS '编号';
COMMENT ON COLUMN gen_table_column.table_id IS '归属表编号';
COMMENT ON COLUMN gen_table_column.column_name IS '列名称';
COMMENT ON COLUMN gen_table_column.column_comment IS '列描述';
COMMENT ON COLUMN gen_table_column.column_type IS '列类型';
COMMENT ON COLUMN gen_table_column.data_type IS '列数据类型';
COMMENT ON COLUMN gen_table_column.java_type IS 'JAVA类型';
COMMENT ON COLUMN gen_table_column.java_field IS 'JAVA字段名';
COMMENT ON COLUMN gen_table_column.is_pk IS '是否主键（1是）';
COMMENT ON COLUMN gen_table_column.is_increment IS '是否自增（1是）';
COMMENT ON COLUMN gen_table_column.is_required IS '是否必填（1是）';
COMMENT ON COLUMN gen_table_column.is_insert IS '是否为插入字段（1是）';
COMMENT ON COLUMN gen_table_column.is_edit IS '是否编辑字段（1是）';
COMMENT ON COLUMN gen_table_column.is_list IS '是否列表字段（1是）';
COMMENT ON COLUMN gen_table_column.is_query IS '是否查询字段（1是）';
COMMENT ON COLUMN gen_table_column.query_type IS '查询方式（等于、不等于、大于、小于、范围）';
COMMENT ON COLUMN gen_table_column.html_type IS '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';
COMMENT ON COLUMN gen_table_column.dict_type IS '字典类型';
COMMENT ON COLUMN gen_table_column.sort IS '排序';
COMMENT ON COLUMN gen_table_column.create_by IS '创建者';
COMMENT ON COLUMN gen_table_column.create_time IS '创建时间';
COMMENT ON COLUMN gen_table_column.update_by IS '更新者';
COMMENT ON COLUMN gen_table_column.update_time IS '更新时间';
COMMENT ON COLUMN gen_table_column.template_type IS '模板类型';
