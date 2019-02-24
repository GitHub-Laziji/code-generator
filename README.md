# Mybatis Generator
![](https://img.shields.io/github/languages/top/github-laziji/mybatis-generator.svg?style=flat)
![](https://img.shields.io/github/stars/gitHub-laziji/mybatis-generator.svg?style=social)



Java数据库`Mapper, Dao, Service`代码自动生成器


> 欢迎贡献各种模版

目前项目中包含两个模版在`resources`下, 如果模版不合适可以自己模仿其中的模版进行修改

- `mybatis2` 是根据 [commons-mybatis](https://github.com/GitHub-Laziji/commons-mybatis) 通用`Mapper`编写的, 依赖`commons-mybatis 2.0`
- `mybatis-default` 这个生成的是简单的`mybatis`实体类、Dao接口以及XML, 不依赖其他包


# 配置文件
在`resources`下创建`application-${name}.yml`文件, `${name}`随意, 例如: `application-example.yml`, 可创建多个

配置文件属性:
- `spring.datasource` 填入自己的项目数据库相关配置
- `generator.package` 项目包名
- `generator.template.mapping` 用于自定义生成文件的包格式以及文件名
- `generator.template.path` 表示模版文件的路径目前可以选`mybatis` 或 `mybatis-default`

`generator.template.mapping`中可选的动态属性包含:
- `{packagePath}` 包文件路径 例如: `com/xxx/xxx`
- `{className}` 类名 由表名使用驼峰命名法得来
- `{customClassName}` 自定义类名 (若未指定自定义类名, 则就是类名)
- `{suffix}` 类名后缀 DO或VO (根据是否为视图)

一般按以下配置即可, 也可以自行扩展
```yml
spring:
  datasource:
    url: jdbc:mysql://xxx.xxx.xxx.xxx:3306/xxxx?characterEncoding=utf-8
    username: xxxxxx
    password: xxxxxx

generator:
  package: com.xxx.xxx
  template:
    path: mybatis2
    mapping: |
      Model.java.vm: main/java/{packagePath}/database/model/{customClassName}.java
      Query.java.vm: main/java/{packagePath}/database/query/{customClassName}Query.java
      Dao.java.vm: main/java/{packagePath}/database/dao/{customClassName}Dao.java
      Service.java.vm: main/java/{packagePath}/database/service/{customClassName}Service.java
```

# 使用
在test文件下创建测试类
- `@ActiveProfiles("example")`中填入刚才配置文件名的`name`
- `tableNames`需要生成的表, 可以多个
- `zipPath` 代码导出路径

调用`generatorService.generateZip`传入参数可以是表名数组`String[]`或者`TableItem[]`

运行测试方法
```Java
package pg.laziji.generator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pg.laziji.generator.mybatis.GeneratorService;

import javax.annotation.Resource;
import java.io.IOException;

@ActiveProfiles("example")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleTest {

    @Resource
    private GeneratorService generatorService;

    @Test
    public void test(){
        String zipPath = "/home/code.zip";

//        String[] tableNames = new String[]{"table1","table2"};
//        generatorService.generateZip(tableNames,zipPath);

        TableItem[] tableItems = new TableItem[]{
                new TableItem("table1", "TableA"),
                new TableItem("table2", "TableB")
        };
        generatorService.generateZip(tableItems,zipPath);
    }
}
```
