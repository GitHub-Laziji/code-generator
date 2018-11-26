package pg.laziji.generator.mybatis;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
class GeneratorUtils {

    private static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("tinyint", "Integer");
        typeMap.put("smallint", "Integer");
        typeMap.put("mediumint", "Integer");
        typeMap.put("int", "Integer");
        typeMap.put("integer", "Integer");
        typeMap.put("bigint", "Long");
        typeMap.put("float", "Float");
        typeMap.put("double", "Double");
        typeMap.put("decimal", "Long");
        typeMap.put("bit", "Boolean");
        typeMap.put("char", "String");
        typeMap.put("varchar", "String");
        typeMap.put("tinytext", "String");
        typeMap.put("text", "String");
        typeMap.put("mediumtext", "String");
        typeMap.put("longtext", "String");
        typeMap.put("date", "Date");
        typeMap.put("datetime", "Date");
        typeMap.put("timestamp", "Date");
    }

    @Autowired
    private Environment environment;


    void generatorCode(String tableName, List<Map<String, String>> columns, ZipOutputStream zip) {

        TableDO tableDO = new TableDO();
        String suffix = "DO";
        if (tableName.endsWith("_view")) {
            suffix = "VO";
        }
        tableDO.setTableName(tableName);
        tableDO.setClassName(WordUtils.capitalizeFully(tableName, new char[]{'_'}).replace("_", ""));

        List<ColumnDO> columnList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnDO columnDO = new ColumnDO();
            columnDO.setColumnName(column.get("columnName"));
            columnDO.setDataType(column.get("dataType"));
            columnDO.setAttrName(WordUtils.capitalizeFully(columnDO.getColumnName(), new char[]{'_'}).replace("_", ""));
            columnDO.setAttrLowerName(StringUtils.uncapitalize(columnDO.getAttrName()));

            String attrType = typeMap.getOrDefault(columnDO.getDataType(), "Object");
            columnDO.setAttrType(attrType);

            columnList.add(columnDO);
        }
        tableDO.setColumns(columnList);

        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        Map<String, Object> map = new HashMap<>(16);
        map.put("tableName", tableDO.getTableName());
        map.put("className", tableDO.getClassName());
        map.put("pathName", getPackageName().substring(getPackageName().lastIndexOf(".") + 1));
        map.put("columns", tableDO.getColumns());
        map.put("package", getPackageName());
        map.put("suffix", suffix);
        VelocityContext context = new VelocityContext(map);

        List<String> templates = getTemplates();
        for (String template : templates) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                String fileName = getFileName(template, tableDO.getClassName(), suffix);
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

    private List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("mybatis/Model.java.vm");
        templates.add("mybatis/Query.java.vm");
        templates.add("mybatis/Dao.java.vm");
        templates.add("mybatis/Mapper.xml.vm");
        templates.add("mybatis/Service.java.vm");
        return templates;
    }

    private String getFileName(String template, String className, String suffix) {
        String packagePath = "main/java/" + getPackageName().replace(".", File.separator) + "/database/";
        String resourcesPath = "main/resources/" + environment.getProperty("generator.resources").replace(".", File.separator) + "/";

        String result = null;
        if (template.contains("Model.java.vm")) {
            result = packagePath + "model/" + className + suffix + ".java";
        }
        if (template.contains("Query.java.vm")) {
            result = packagePath + "query/" + className + "Query.java";
        }
        if (template.contains("Dao.java.vm")) {
            result = packagePath + "dao/" + className + "Dao.java";
        }
        if (template.contains("Service.java.vm")) {
            result = packagePath + "service/" + className + "Service.java";
        }
        if (template.contains("Mapper.xml.vm")) {
            result = resourcesPath + className + "Mapper.xml";
        }

        if (result == null) {
            return null;
        }
        return result.replace("/", File.separator);
    }

    private String getPackageName() {
        return environment.getProperty("generator.package");
    }
}
