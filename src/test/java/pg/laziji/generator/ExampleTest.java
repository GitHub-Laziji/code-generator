package pg.laziji.generator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pg.laziji.generator.mybatis.GeneratorService;

import javax.annotation.Resource;

@ActiveProfiles("example")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleTest {

    @Resource
    private GeneratorService generatorService;

    @Value("${generator.template.mapping:}")
    private String ts;

    @Test
    public void test(){
        String[] tableNames = new String[]{"table1","table2"};
        String zipPath = "D:/TEST/code.zip";
        generatorService.generateZip(tableNames,zipPath);
        System.out.println(ts);
    }
}
