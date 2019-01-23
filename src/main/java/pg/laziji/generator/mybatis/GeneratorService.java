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

    @Value("${generator.resources:mapper}")
    private String resourcesPath;


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

        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        VelocityContext context = new VelocityContext(map);


        String[] templatePaths = {
                "mybatis/Model.java.vm",
                "mybatis/Query.java.vm",
                "mybatis/Dao.java.vm",
                "mybatis/Mapper.xml.vm",
                "mybatis/Service.java.vm"
        };

        for (String path : templatePaths) {
            Template template = Velocity.getTemplate(path, "UTF-8");
            String fileName = getFileName(path, table);
            if (fileName == null) {
                continue;
            }
            try (StringWriter writer = new StringWriter()) {
                template.merge(context, writer);
                zos.putNextEntry(new ZipEntry(fileName));
                IOUtils.write(writer.toString(), zos, "UTF-8");
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileName(String template, TableDO table) {
        String packagePath = "main/java/" + this.packagePath.replace(".", "/") + "/database/";
        String resourcesPath = "main/resources/" + this.resourcesPath.replace(".", "/") + "/";

        String result = null;
        if (template.contains("Model.java.vm")) {
            result = packagePath + "model/" + table.getClassName() + table.getSuffix() + ".java";
        }
        if (template.contains("Query.java.vm")) {
            result = packagePath + "query/" + table.getClassName() + "Query.java";
        }
        if (template.contains("Dao.java.vm")) {
            result = packagePath + "dao/" + table.getClassName() + "Dao.java";
        }
        if (template.contains("Service.java.vm")) {
            result = packagePath + "service/" + table.getClassName() + "Service.java";
        }
        if (template.contains("Mapper.xml.vm")) {
            result = resourcesPath + table.getClassName() + "Mapper.xml";
        }

        if (result == null) {
            return null;
        }
        return result.replace("/", File.separator);
    }
}
