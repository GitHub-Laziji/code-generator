# mybatis-generator

Java数据库`Mapper, Dao, Service`代码自动生成器


代码模版位于`resources`下, 可根据需要自行修改

> 当前模版是根据 [commons-mybatis](https://github.com/GitHub-Laziji/commons-mybatis) 架构编写的, 若不适合可以自行修改模版


# 配置文件
在`resources`下创建`application-${name}.yml`文件, `${name}`随意, 例如: `application-example.yml`, 可创建多个

配置文件内容如下, 填入数据库配置, 以及生成代码的包名 

模版文件映射用于自定义生成文件的包格式以及文件名

动态属性包含
- {packageFilePath} 包文件路径 例如: `com/xxx/xxx`
- {className} 类名 由表名改为驼峰命名法得来
- {suffix} 类名后缀 DO或VO

一般按以下配置即可 

现在项目中有两套模版`template.path` 可以选`mybatis` 或 `mybatis-default`

也可以自行扩展
```yml
spring:
  datasource:
    url: jdbc:mysql://xxx.xxx.xxx.xxx:3306/xxxx?characterEncoding=utf-8
    username: xxxxxx
    password: xxxxxx

generator:
  package: com.xxx.xxx
  template:
    path: mybatis
    mapping: |
      Model.java.vm: main/java/{packageFilePath}/database/model/{className}.java
      Query.java.vm: main/java/{packageFilePath}/database/query/{className}Query.java
      Dao.java.vm: main/java/{packageFilePath}/database/dao/{className}.java
      Service.java.vm: main/java/{packageFilePath}/database/service/{className}Service.java
      Mapper.xml.vm: main/resources/mapper/{className}Mapper.xml
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
