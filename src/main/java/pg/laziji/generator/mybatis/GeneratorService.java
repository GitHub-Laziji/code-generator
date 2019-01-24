package pg.laziji.generator.mybatis;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GeneratorService {

    @Resource
    private GeneratorMapper generatorMapper;

    @Value("${generator.package:com.g.example}")
    private String packagePath;

    @Value("${generator.template.path:}")
    private String templatePath;

    @Value("${generator.template.mapping:}")
    private String templateMapping;


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
                generatorCode(table, zos);
            }
            fos.write(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void generatorCode(TableDO table, ZipOutputStream zos) {

        Map<String, Object> map = new HashMap<>();
        map.put("tableName", table.getTableName());
        map.put("className", table.getClassName());
        map.put("columns", table.getColumns());
        map.put("suffix", table.getSuffix());
        map.put("package", packagePath);
        map.put("packageFilePath", packagePath.replace(".","/"));

        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        VelocityContext context = new VelocityContext(map);

        Map<String, String> templateMap = parseTemplateMapping(map);
        for (Map.Entry<String,String> entry:templateMap.entrySet()) {
            Template template = Velocity.getTemplate(entry.getKey(), "UTF-8");
            try (StringWriter writer = new StringWriter()) {
                template.merge(context, writer);
                zos.putNextEntry(new ZipEntry(entry.getValue()));
                IOUtils.write(writer.toString(), zos, "UTF-8");
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> parseTemplateMapping(Map<String, Object> info) {
        String[] rows = templateMapping.split("\n");
        Map<String, String> templateMap = new HashMap<>();
        for (String row : rows) {
            String[] vs = row.split(":");
            for (Map.Entry<String, Object> entry : info.entrySet()) {
                if (entry.getValue() instanceof String) {
                    vs[1] = vs[1].replace("{" + entry.getKey() + "}", entry.getValue().toString());
                }
            }
            templateMap.put(templatePath + "/" + vs[0].trim(), vs[1]);
        }
        return templateMap;
    }
}
