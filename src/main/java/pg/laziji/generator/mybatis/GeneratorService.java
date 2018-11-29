package pg.laziji.generator.mybatis;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service
public class GeneratorService {

    @Resource
    private GeneratorMapper generatorMapper;

    @Resource
    private Environment environment;

    public void generateZip(String[] tableNames, String zipPath) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (
                ZipOutputStream zos = new ZipOutputStream(bos);
                FileOutputStream fos = new FileOutputStream(zipPath)
        ) {
            for (String tableName : tableNames) {
                TableDO table = new TableDO();
                table.setTableName(tableName);
                table.setColumns(generatorMapper.listColumns(tableName));
                GeneratorUtils.generatorCode(table, zos, getConfig());
            }
            fos.write(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("package", environment.getProperty("generator.package", ""));
        config.put("resources", environment.getProperty("generator.resources", ""));
        return config;
    }
}
