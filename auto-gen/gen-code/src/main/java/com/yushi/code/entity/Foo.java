package com.yushi.code.entity;

import com.yushi.code.east.annotation.TableColumn;
import com.yushi.code.east.annotation.TableId;
import com.yushi.code.east.annotation.TableInfo;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @author fdong
 * @since 2024.11.14
 */
@Data
@TableInfo
public class Foo {

   @TableId
   @TableColumn(comment = "id")
   private Long id;

   @TableColumn(comment = "名称")
   private String name;

   @TableColumn(comment = "排序")
   private Integer sort;

   @TableColumn(comment = "描述")
   private String description;

   @TableColumn(comment = "性别")
   private Boolean sex;

   @TableColumn(comment = "创建时间")
   private LocalDateTime createTime;
}
