package io.github.plmmilove.mybatis.bean;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralAnnotation {

  @JsonAlias("annoKey")
  private String annotationKey;

  @JsonAlias("anno")
  private String annotation;

  @JsonAlias("annoClassName")
  private String annotationClassName;
}
