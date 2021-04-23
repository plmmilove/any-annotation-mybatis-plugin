package io.github.plmmilove.mybatis.plugin;

import static io.github.plmmilove.mybatis.constants.AnyAnnotationConstants.ANNOTATION_KEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.plmmilove.mybatis.bean.GeneralAnnotation;
import io.github.plmmilove.mybatis.util.AnyAnnotationUtil;
import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

public class ClassAnyAnnotationMybatisPlugin extends PluginAdapter {

  private final Log logger = LogFactory.getLog(getClass());

  @Override
  public boolean validate(List<String> warnings) {
    return true;
  }

  @Override
  public boolean modelBaseRecordClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    String annotation = this.getProperties().getProperty(ANNOTATION_KEY);
    if (AnyAnnotationUtil.isNotBlank(annotation)) {
      try {
        List<GeneralAnnotation> annotations =
            AnyAnnotationUtil.readValue(
                annotation, new TypeReference<List<GeneralAnnotation>>() {
                });

        annotations.forEach(anno -> AnyAnnotationUtil.addClassLevelAnnotation(anno, topLevelClass));
      } catch (JsonProcessingException e) {
        logger.error("", e);
      }
    }
    return true;
  }
}
