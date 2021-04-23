package io.github.plmmilove.mybatis.plugin;

import static io.github.plmmilove.mybatis.constants.AnyAnnotationConstants.ANNOTATION_KEY;

import io.github.plmmilove.mybatis.bean.GeneralAnnotation;
import io.github.plmmilove.mybatis.util.AnyAnnotationUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class LombokAnnotationMybatisPlugin extends PluginAdapter {

  private List<GeneralAnnotation> annotations = new ArrayList<>();

  private boolean getter;

  private boolean setter;

  private final Map<String, GeneralAnnotation> map = new HashMap<>();

  {
    map.put("@AllArgsConstructor",
        GeneralAnnotation.builder().annotationKey("@AllArgsConstructor")
            .annotation("@AllArgsConstructor")
            .annotationClassName("lombok.AllArgsConstructor").build());

    map.put("@Builder",
        GeneralAnnotation.builder().annotationKey("@Builder").annotation("@Builder")
            .annotationClassName("lombok.Builder").build());

    map.put("@Data", GeneralAnnotation.builder().annotationKey("@Data").annotation("@Data")
        .annotationClassName("lombok.Data").build());

    map.put("@EqualsAndHashCode", GeneralAnnotation.builder().annotationKey("@EqualsAndHashCode")
        .annotation("@EqualsAndHashCode")
        .annotationClassName("lombok.EqualsAndHashCode").build());

    map.put("@Getter", GeneralAnnotation.builder().annotationKey("@Getter").annotation("@Getter")
        .annotationClassName("lombok.Getter").build());

    map.put("@NoArgsConstructor", GeneralAnnotation.builder().annotationKey("@NoArgsConstructor")
        .annotation("@NoArgsConstructor")
        .annotationClassName("lombok.NoArgsConstructor").build());

    map.put("@Setter", GeneralAnnotation.builder().annotationKey("@Setter").annotation("@Setter")
        .annotationClassName("lombok.Setter").build());

    map.put("@ToString",
        GeneralAnnotation.builder().annotationKey("@ToString").annotation("@ToString")
            .annotationClassName("lombok.ToString").build());

    map.put("@Value", GeneralAnnotation.builder().annotationKey("@Value").annotation("@Value")
        .annotationClassName("lombok.Value").build());
  }

  @Override
  public void setProperties(Properties properties) {
    super.setProperties(properties);
    getter = "true".equals(properties.getProperty("getter"));
    setter = "true".equals(properties.getProperty("setter"));

    String annos = properties.getProperty(ANNOTATION_KEY, "@Getter;@Setter;@ToString");
    annotations = Arrays.stream(annos.split(";")).map(e -> map.get(e.trim()))
        .filter(Objects::nonNull).collect(Collectors.toList());
  }

  @Override
  public boolean validate(List<String> warnings) {
    return true;
  }

  @Override
  public boolean modelBaseRecordClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    annotations
        .forEach(anno -> AnyAnnotationUtil.addClassLevelAnnotation(anno, topLevelClass));
    return true;
  }

  @Override
  public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
      IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
      ModelClassType modelClassType) {
    return getter;
  }

  @Override
  public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
      IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
      ModelClassType modelClassType) {
    return setter;
  }
}
