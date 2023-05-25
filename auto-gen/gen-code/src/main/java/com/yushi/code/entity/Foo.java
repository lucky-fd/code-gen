package com.yushi.code.entity;

import com.yushi.code.east.annotation.TableColumn;
import com.yushi.code.east.annotation.TableId;
import com.yushi.code.east.annotation.TableInfo;
import lombok.Data;


/**
 * @author fdong
 * @since 2024.11.14
 */
@Data
@TableInfo
public class Foo {

   @TableId
   @TableColumn(comment = "id", sort = 1)
   private Long id;

   @TableColumn(comment = "名称", sort = 2)
   private String name;

   @TableColumn(comment = "描述", sort = 3)
   private String description;

   @TableColumn(comment = "描述", sort = 3)
   private String createTime;

   @TableColumn(comment = "描述", sort = 2)
   private Boolean sex;
}
