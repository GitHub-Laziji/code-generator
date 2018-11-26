package pg.laziji.generator.mybatis;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
class GeneratorUtils {

    static public void generatorCode(TableDO table, ZipOutputStream zip, Map<String, String> config) {
        String packageName = config.get("package");

        Map<String, Object> map = new HashMap<>();
        map.put("tableName", table.getTableName());
        map.put("className", table.getClassName());
        map.put("pathName", packageName.substring(packageName.lastIndexOf(".") + 1));
        map.put("columns", table.getColumns());
        map.put("package", packageName);
        map.put("suffix", table.getSuffix());

        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        VelocityContext context = new VelocityContext(map);

        List<String> templates = new ArrayList<>();
        templates.add("mybatis/Model.java.vm");
        templates.add("mybatis/Query.java.vm");
        templates.add("mybatis/Dao.java.vm");
        templates.add("mybatis/Mapper.xml.vm");
        templates.add("mybatis/Service.java.vm");

        for (String template : templates) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                String fileName = getFileName(template, table, config);
                if (fileName == null) {
                    continue;
                }
                zip.putNextEntry(new ZipEntry(fileName));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
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