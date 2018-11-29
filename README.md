# mybatis-generator

Java数据库`Mapper, Dao, Service`代码自动生成器


代码模版位于`resources/mybatis/`中, 可根据需要自行修改

> 当前模版是根据 [commons-mybatis](https://github.com/GitHub-Laziji/commons-mybatis) 架构编写的, 若不适合可以自行修改模版


# 配置文件
在`resources`下创建`application-${name}.yml`文件, `${name}`随意, 例如: `application-example.yml`, 可创建多个

配置文件内容如下, 填入数据库配置, 以及生成代码的包名, 源文件路径
```
spring:
  datasource:
    url: jdbc:mysql://xxx.xxx.xxx.xxx:3306/xxxx?characterEncoding=utf-8
    username: xxxxxx
    password: xxxxxx

generator:
  package: com.xxx.xxx
  resources: mapper
```

# 使用
在test文件下创建测试类
- `@ActiveProfiles("example")`中填入刚才配置文件名的`name`
- `tableNames`需要生成的表, 可以多个
- `zipPath` 代码导出路径
运行测试方法即可
```
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
    public void test() throws IOException {
        String[] tableNames = new String[]{"example_table1", "example_table2"};
        String zipPath = "/home/code.zip";
        generatorService.generateZip(tableNames,zipPath);
    }
}

```
