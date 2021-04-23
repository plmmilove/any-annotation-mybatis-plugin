package io.github.plmmilove.mybatis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.plmmilove.mybatis.bean.GeneralAnnotation;
import java.util.Arrays;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class AnyAnnotationUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static boolean isNotBlank(String s) {
    // StringUtils.isNotBlank
    return !isBlank(s);
  }

  public static boolean isBlank(String s) {
    // StringUtils.isBlank
    return s == null || s.trim().length() == 0;
  }

  public static <T> T readValue(String annotationStr, TypeReference<T> valueTypeRef)
      throws JsonProcessingException {
    return OBJECT_MAPPER.readValue(annotationStr, valueTypeRef);
  }

  public static void addClassLevelAnnotation(GeneralAnnotation anno, TopLevelClass topLevelClass) {
    if (isNotBlank(anno.getAnnotationClassName())) {
      Arrays.stream(anno.getAnnotationClassName().split(";"))
          .forEach(topLevelClass::addImportedType);
    }

    if (isBlank(anno.getAnnotation())) {
      return;
    }

    if (isNotBlank(anno.getAnnotationKey())) {
      if (topLevelClass.getAnnotations().stream()
          .noneMatch(e -> e.startsWith(anno.getAnnotationKey()))) {
        topLevelClass.getAnnotations().add(anno.getAnnotation());
      }
    } else {
      topLevelClass.getAnnotations().add(anno.getAnnotation());
    }
  }
}
