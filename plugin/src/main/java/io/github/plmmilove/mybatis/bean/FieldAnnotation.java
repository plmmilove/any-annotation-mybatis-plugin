package io.github.plmmilove.mybatis.bean;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class FieldAnnotation {

  @JsonAlias("anno")
  private String annotation;

  @JsonAlias("annoClassName")
  private String annotationClassName;

  @JsonAlias("classLevelAnno")
  private GeneralAnnotation classLevelAnnotation;
}
