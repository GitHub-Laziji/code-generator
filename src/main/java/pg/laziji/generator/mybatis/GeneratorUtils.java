package pg.laziji.generator.mybatis;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
class GeneratorUtils {

    @Resource
    private Environment environment;

    void generatorCode(TableDO table, ZipOutputStream zip) {

        Map<String, Object> map = new HashMap<>();
        map.put("tableName", table.getTableName());
        map.put("className", table.getClassName());
        map.put("pathName", getPackageName().substring(getPackageName().lastIndexOf(".") + 1));
        map.put("columns", table.getColumns());
        map.put("package", getPackageName());
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
                String fileName = getFileName(template, table);
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

    private String getFileName(String template, TableDO table) {
        String packagePath = "main/java/" + getPackageName().replace(".", "/") + "/database/";
        String resourcesPath = "main/resources/" + getResourcesPath().replace(".", "/") + "/";

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

    private String getPackageName() {
        String val = environment.getProperty("generator.package");
        return val==null?"":val;
    }

    private String getResourcesPath(){
        String val = environment.getProperty("generator.resources");
        return val==null?"":val;
    }
}