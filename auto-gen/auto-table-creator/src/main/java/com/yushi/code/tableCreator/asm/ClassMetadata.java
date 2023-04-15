package com.yushi.code.tableCreator.asm;

import com.yushi.code.tableCreator.asm.tree.AnnotationNode;
import com.yushi.code.tableCreator.asm.tree.ClassNode;
import com.yushi.code.tableCreator.util.BeanUtils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassMetadata {
  /** 注解类全限定名 */
  private final List<String> annotations;

  private final String name;

  public ClassMetadata(final ClassNode classNode) {
    final String name = classNode.name.replace("/", ".");
    final List<AnnotationNode> annotations = classNode.visibleAnnotations;
    this.name = name;
    this.annotations =
        annotations == null
            ? Collections.emptyList()
            : annotations.stream()
                .map(o -> Type.getType(o.desc))
                .map(Type::getClassName)
                .collect(Collectors.toList());
  }

  public Set<Class<Annotation>> getAnnotationSet() {
    Set<Class<Annotation>> annotationSet = new HashSet<>();
    for (final String anno : annotations) {
      final Class<Annotation> clazz = BeanUtils.getClazz(anno);
      if (Annotation.class.isAssignableFrom(clazz)) {
        annotationSet.add(clazz);
      }
    }
    return annotationSet;
  }

  public Class<?> getCurrentClass() {
    return BeanUtils.getClazz(name);
  }
}
