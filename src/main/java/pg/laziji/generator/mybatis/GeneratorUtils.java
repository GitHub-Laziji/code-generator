package pg.laziji.generator.mybatis;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class GeneratorUtils {

    static public void generatorCode(TableDO table, ZipOutputStream zos, Map<String, String> config) {

        Map<String, Object> map = new HashMap<>();
        map.put("tableName", table.getTableName());
        map.put("className", table.getClassName());
        map.put("columns", table.getColumns());
        map.put("suffix", table.getSuffix());
        map.put("package", config.get("package"));

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
            String fileName = getFileName(path, table, config);
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

    static private String getFileName(String template, TableDO table, Map<String, String> config) {
        String packagePath = "main/java/" + config.get("package").replace(".", "/") + "/database/";
        String resourcesPath = "main/resources/" + config.get("resources").replace(".", "/") + "/";

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