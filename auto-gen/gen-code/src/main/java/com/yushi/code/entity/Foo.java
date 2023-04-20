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
   @TableColumn(comment = "id")
   private Long id;

   @TableColumn(comment = "名称")
   private String name;

   @TableColumn(comment = "描述")
   private String description;
}
