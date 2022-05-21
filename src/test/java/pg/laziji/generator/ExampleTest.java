package pg.laziji.generator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pg.laziji.generator.constant.KeyConsts;
import pg.laziji.generator.model.TableItem;
import pg.laziji.generator.service.impl.gen.CodeGeneratorServiceImpl;

import javax.annotation.Resource;

@ActiveProfiles("example")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleTest {

    @Resource
    private CodeGeneratorServiceImpl codeGeneratorService;

    @Test
    public void test() {
        codeGeneratorService.generate(new TableItem[]{
                TableItem.newBuilder()
                        .tableName("table1")
                        .dynamicPathVariable(KeyConsts.CLASS_NAME, "TableA")
                        .build(),
                new TableItem("table2")
        }, "./entity.zip");
    }
}
