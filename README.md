# any-annotation-mybatis-plugin
A plugin to provide [mybatis generator](https://github.com/mybatis/generator) enhancement. It need work with [mybatis generator](https://github.com/mybatis/generator).



## Usage

```xml
<build>
  <plugins>
  	....
    <plugin>
      <groupId>org.mybatis.generator</groupId>
      <artifactId>mybatis-generator-maven-plugin</artifactId>
      <version>${mybatis.generator.version}</version>
      <configuration>
        <configurationFile>generatorConfig.xml</configurationFile>
        <verbose>true</verbose>
        <overwrite>true</overwrite>
      </configuration>
      <dependencies>
        <dependency>
          <groupId>mysql</groupId>
          <artifactId>mysql-connector-java</artifactId>
          <version>${mysql.version}</version>
        </dependency>
        <dependency>
          <groupId>io.github</groupId>
          <artifactId>any-annotation-mybatis-plugin</artifactId>
          <version>1.0.0</version>
        </dependency>
      </dependencies>
    </plugin>
  </plugins>
</build>
```



It provide 3 plugins:

### LombokAnnotationMybatisPlugin

```xml
<generatorConfiguration>
  <properties resource="mybatis.properties"/>

  <context id="MysqlTables" targetRuntime="MyBatis3" defaultModelType="flat">
    <plugin type="io.github.plmmilove.mybatis.plugin.LombokAnnotationMybatisPlugin">
      <property name="annotations" value="@Getter;@Setter;@ToString"/>
    </plugin>

    ......
      
     <table tableName="category">
      <generatedKey column="id" sqlStatement="Mysql" identity="true"/>
    </table>
  </context>
</generatorConfiguration>
```

when you add this plugin in mybatis generatorConfig,  those annotations will be add to the Entity (Pojo) class. Like:

```java
@Getter
@Setter
@ToString
public class Category {
  private Integer id;
  
  ......
}
```

 

Configuration:

| Property name | Type    | Default Value | Description                                                  |
| ------------- | ------- | ------------- | ------------------------------------------------------------ |
| annotations   | String  | -             | annotations that will be add to Entity (Pojo) class. <br />Support the following Lombok annotation:<br />`@AllArgsConstructor`<br/>`@Builder`<br/>`@Data`<br/>`@EqualsAndHashCode`<br/>`@Getter`<br/>`@NoArgsConstructor`<br/>`@Setter`<br/>`@ToString`<br/>`@Value`<br /><br /> Separate them by semicolon if you want to add <br />more than one annotation. Like:<br /> `@Getter;@Setter;@Builder`<br /> |
| getter        | Boolean | false         | will generate getter in  Entity (Pojo) class when this property is `true` |
| setter        | Boolean | false         | will generate getter in  Entity (Pojo) class when this property is `true` |





### ClassAnyAnnotationMybatisPlugin

support any annotation add to Entity (Pojo) class.

```xml
<generatorConfiguration>
  <properties resource="mybatis.properties"/>

  <context id="MysqlTables" targetRuntime="MyBatis3" defaultModelType="flat">
    <plugin type="io.github.plmmilove.mybatis.plugin.ClassAnyAnnotationMybatisPlugin">
      <property name="annotations" value="${class.annotation}"/>
    </plugin>

    ......
    <table tableName="category">
      <generatedKey column="id" sqlStatement="Mysql" identity="true"/>
    </table>
  </context>
</generatorConfiguration>
```



```properties
class.annotation=[{"anno":"@Validated", "annoClassName":"org.springframework.validation.annotation.Validated"}]
```

when you add this plugin in mybatis generatorConfig,  those annotations will be add to the Entity (Pojo) class. Like:

```java
import org.springframework.validation.annotation.Validated;

@Validated
public class Category {
   ......
}
```



Configuration:

| Property name | Type        | Default Value | Description  |
| ------------- | ----------- | ------------- | ------------ |
| annotations   | Json String | -             | Json String. |

annotation body,  a json formatted string. support the following attribute:

| Attribute     | Type   | Required | Description                                                  |
| ------------- | ------ | -------- | ------------------------------------------------------------ |
| annoKey       | String | false    | the key of the annotation, a annotation <br />can only add to the target class once, this used<br />to find duplicate annotation. |
| anno          | String | True     | annotation added to the Entity (Pojo) class                  |
| annoClassName | String | False    | A semicolon splited String.<br />Classes will add to the import. |



### FieldAnyAnnotationMybatisPlugin

support any annotation add to any column (property/attribute) in Entity (Pojo) class.

```xml
<generatorConfiguration>
  <properties resource="mybatis.properties"/>

  <context id="MysqlTables" targetRuntime="MyBatis3" defaultModelType="flat">
    <plugin type="io.github.plmmilove.mybatis.plugin.FieldAnyAnnotationMybatisPlugin"/>

    ......
      
	<table tableName="category">
      <generatedKey column="id" sqlStatement="Mysql" identity="true"/>
      <columnOverride column="create_time">
        <property name="annotations" value="${category.create_time}"/>
      </columnOverride>
    </table>
  </context>
</generatorConfiguration>
```

mybatis.properties:

```properties
category.create_time=[{"anno":"@JsonFormat(pattern = YMD_HMS, timezone = \\"GMT+8\\")","annoClassName":"com.fasterxml.jackson.annotation.JsonFormat;static io.github.plmmilove.DateUtil.YMD_HMS"}]
```

when you add this plugin in mybatis generatorConfig,  those annotations will be add to the Entity (Pojo) class. Like:

```java
import static io.github.plmmilove.DateUtil.YMD_HMS;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class Category {

  /**
   * This field was generated by MyBatis Generator. This field corresponds to the database column
   * category.id
   *
   * @mbg.generated
   */
  private Integer id;

  /**
   * This field was generated by MyBatis Generator. This field corresponds to the database column
   * category.name
   *
   * @mbg.generated
   */
  private String name;

  /**
   * This field was generated by MyBatis Generator. This field corresponds to the database column
   * category.create_time
   *
   * @mbg.generated
   */
  @JsonFormat(pattern = YMD_HMS, timezone = "GMT+8")
  private LocalDateTime createTime;
}
```


Configuration:

| Property name | Type   | Default Value | Description                                                  |
| ------------- | ------ | ------------- | ------------------------------------------------------------ |
| annotations   | String | annotations   | property key in columnOverride config; <br />you can define this key as you like. |

annotation body,  a json formatted string. support the following attribute:

| Attribute      | Type   | Required | Description                                                  |
| -------------- | ------ | -------- | ------------------------------------------------------------ |
| anno           | String | True     | A semicolon splited String.<br />Annotations will add to the column. |
| annoClassName  | String | True     | A semicolon splited String.<br />Classes will add to the import. |
| classLevelAnno | String | False    | Class Level annotations.<br />Format like class annotation   |

example:
```json
[{"anno":"@JsonFormat(pattern = YMD_HMS, timezone = \\"GMT+8\\")","annoClassName":"com.fasterxml.jackson.annotation.JsonFormat;static io.github.plmmilove.DateUtil.YMD_HMS"}]
```
