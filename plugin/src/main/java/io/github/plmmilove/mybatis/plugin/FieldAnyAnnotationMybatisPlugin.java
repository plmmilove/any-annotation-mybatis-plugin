package io.github.plmmilove.mybatis.plugin;

import static io.github.plmmilove.mybatis.constants.AnyAnnotationConstants.ANNOTATION_KEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.plmmilove.mybatis.bean.FieldAnnotation;
import io.github.plmmilove.mybatis.util.AnyAnnotationUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

public class FieldAnyAnnotationMybatisPlugin extends PluginAdapter {

  private final Log logger = LogFactory.getLog(getClass());

  private String anyAnnotationKey;

  @Override
  public void setProperties(Properties properties) {
    super.setProperties(properties);
    anyAnnotationKey = properties.getProperty(ANNOTATION_KEY, ANNOTATION_KEY);
  }

  @Override
  public boolean validate(List<String> warnings) {
    return true;
  }

  @Override
  public boolean modelFieldGenerated(
      Field field,
      TopLevelClass topLevelClass,
      IntrospectedColumn introspectedColumn,
      IntrospectedTable introspectedTable,
      ModelClassType modelClassType) {
    String annotationStr = introspectedColumn.getProperties().getProperty(anyAnnotationKey);
    if (AnyAnnotationUtil.isNotBlank(annotationStr)) {
      try {
        List<FieldAnnotation> annotations =
            AnyAnnotationUtil.readValue(
                annotationStr, new TypeReference<List<FieldAnnotation>>() {
                });

        annotations.forEach(
            annotation -> {
              if (annotation.getClassLevelAnnotation() != null) {
                AnyAnnotationUtil.addClassLevelAnnotation(
                    annotation.getClassLevelAnnotation(), topLevelClass);
              }

              if (AnyAnnotationUtil.isNotBlank(annotation.getAnnotationClassName())) {
                Arrays.stream(annotation.getAnnotationClassName().split(";")).forEach(
                    topLevelClass::addImportedType);
              }

              if (AnyAnnotationUtil.isNotBlank(annotation.getAnnotation())) {
                field.getAnnotations().add(annotation.getAnnotation());
              }
            });
      } catch (JsonProcessingException e) {
        logger.error("", e);
      }
    }
    return true;
  }
}
