# Code Generator
![](https://img.shields.io/github/languages/top/github-laziji/code-generator.svg?style=flat)
![](https://img.shields.io/github/stars/gitHub-laziji/code-generator.svg?style=social)



自定义代码自动生成器, 目前包含Java数据库`Mapper, Dao, Service`层模板

> 欢迎贡献各种模版

# 已有模板
目前项目中包含这些模版在`resources`下, 如果模版不合适可以自己模仿其中的模版进行修改

- `mybatis2` 是根据 [commons-mybatis](https://github.com/GitHub-Laziji/commons-mybatis) 通用`Mapper`编写的, 依赖`commons-mybatis 2.0`
- `mybatis-default` 这个生成的是简单的`mybatis`实体类、Dao接口以及XML, 不依赖其他包
- `mybatis-plus` 生成mybatis-plus使用的实体类以及Mapper文件, 依赖`mybatis-plus`


# 配置文件
在`resources`下创建`application-${name}.yml`文件, `${name}`随意, 例如: `application-example.yml`, 可创建多个

配置文件属性:
- `generator.datasource` 填入自己的项目数据库相关配置
- `generator.package` 项目包名
- `generator.template.base-path` 表示模版文件的路径目前可选模板请看 [已有模板](#已有模板)
- `generator.template.output-paths` 模板输出相对路径, 每行一个, 格式为`[模板文件名]: [模板输出相对路径,可使用{}引入动态变量]`可选动态变量包含:
- - `{packagePath}` 包文件路径, 例如: `com/xxx/xxx`
- - `{className}` 类名, 由表名转成驼峰命名法得来 可覆盖
- - `{lowercaseClassName}` 首字母小写的类名
- - 以及其他`dynamicPath`域中的自定义属性, `'\'`为转义符, 其后的字符不进行解析

一般按以下配置即可, 数据库支持`mysql`和`oracle`
```yml
generator:
  datasource:
    type: mysql
    url: jdbc:mysql://xxx.xxx.xxx.xxx:3306/xxxx?characterEncoding=utf-8
    username: xxxxxx
    password: xxxxxx
    
  package: com.g.example
  template:
    base-path: mybatis-plus
    output-paths: |
      Entity.java.vm: src/main/java/{packagePath}/entity/{className}.java
      Mapper.java.vm: src/main/java/{packagePath}/mapper/{className}Mapper.java
      Service.java.vm: src/main/java/{packagePath}/service/{className}Service.java
      ServiceImpl.java.vm: src/main/java/{packagePath}/service/impl/{className}ServiceImpl.java
```

# 使用
在test文件下创建测试类
- `@ActiveProfiles("example")`中填入刚才配置文件名的`name`
- `tableNames`需要生成的表, 可以多个, `newBuilder`使用构造类 自定义类名, 以及其他参数
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
import pg.laziji.generator.model.TableItem;
import pg.laziji.generator.service.GeneratorService;

import javax.annotation.Resource;

@ActiveProfiles("example")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleTest {

    @Resource
    private GeneratorService generatorService;

    @Test
    public void test() {
        generatorService.generateZip(new TableItem[]{
                TableItem.newBuilder()
                        .tableName("table1")
                        .dynamicPathVariable("className", "TableA")
                        .build(),
                new TableItem("table2")
        }, "/home/code.zip");
    }
}
```

# 各个变量域包含的字段

### system
只能在VM模板中使用, 可以通过`TemplateContext.setSystemVariable(key, value)`添加和覆盖, 
相当于全局变量, 初始包含以下信息, value的值可以是`Class`, 例如放入`CommonUtils.class` 在模板中就可以调用该类中的静态方法
参考下面的`config`和`utils`

- `config` 可以在模板中通过这个获取所有配置文件信息`system.config.get("xxx")`
- `utils` 工具类, 里面包含格式化当前时间的函数, 可以扩展`system.utils.time("yyyy/MM/dd HH:mm:ss")`

- `username` 系统用户名
- `computerName` 计算机名
- `osName` 操作系统名称
- `osArch` 架构
- `osVersion` 系统版本

### dynamicPath
可以在VM模板和路径配置中使用, 只能存放`<String,String>`的键值对, 
可以通过`TableItem.Builder.dynamicPathVariable(key, value)`添加和覆盖, 
文件的动态路径变量

- `packagePath` 包路径 例如`com/e/test`
- `className` 类名 来自表名转驼峰命名法, 可覆盖
- `lowercaseClassName` 首字母小写的类名


### template
只能在VM模板中使用, 和`dynamicPath`的区别是值可以是`Object`类型, 
可以通过`TableItem.Builder.templateVariable(key, value)`添加和覆盖,
value的值可以是`Class`, 例如放入`CommonUtils.class` 在模板中就可以调用该类中的静态方法
参考系统变量的`config`和`utils`

- 默认无