package pg.laziji.generator.mybatis.service;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pg.laziji.generator.mybatis.model.TableItem;
import pg.laziji.generator.mybatis.model.TemplateContext;

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
    private String packageName;

    @Value("${generator.template.path:}")
    private String templatePath;

    @Value("${generator.template.mapping:}")
    private String templateMapping;


    public void generateZip(String[] tableNames, String zipPath) {
        TableItem[] tableItems = new TableItem[tableNames.length];
        for (int i = 0; i < tableNames.length; i++) {
            tableItems[i] = new TableItem(tableNames[i]);
        }
        generateZip(tableItems, zipPath);
    }

    public void generateZip(TableItem[] tableItems, String zipPath) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (
                ZipOutputStream zos = new ZipOutputStream(bos);
                FileOutputStream fos = new FileOutputStream(zipPath)
        ) {
            for (TableItem item : tableItems) {
                TemplateContext context = new TemplateContext();
                context.setTable(generatorMapper.queryTable(item.getTableName()));
                context.getTable().setColumns(generatorMapper.queryColumns(item.getTableName()));
                context.getTable().setCustomClassName(item.getClassName());
                context.setPackageName(packageName);
                context.setOptions(item.getOptions());

                generatorCode(context, zos);
            }
            fos.write(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void generatorCode(TemplateContext context, ZipOutputStream zos) {

        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        VelocityContext velocityContext = new VelocityContext(context.toMap());

        Map<String, String> templateMap = parseTemplateMapping(context);
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            Template template = Velocity.getTemplate(entry.getKey(), "UTF-8");
            try (StringWriter writer = new StringWriter()) {
                template.merge(velocityContext, writer);
                zos.putNextEntry(new ZipEntry(entry.getValue()));
                IOUtils.write(writer.toString(), zos, "UTF-8");
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> parseTemplateMapping(TemplateContext context) {
        String[] rows = templateMapping.split("\n");
        Map<String, String> templateMap = new HashMap<>();
        for (String row : rows) {
            String[] vs = row.split(":");
            for (Map.Entry<String, String> entry : context.getDynamicPathFields().entrySet()) {
                vs[1] = vs[1].replace("{" + entry.getKey() + "}", entry.getValue());
            }
            templateMap.put(templatePath + "/" + vs[0].trim(), vs[1]);
        }
        return templateMap;
    }
}
