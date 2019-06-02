package pg.laziji.generator.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pg.laziji.generator.model.TableItem;
import pg.laziji.generator.model.TemplateContext;
import pg.laziji.generator.service.GeneratorService;
import pg.laziji.generator.service.TableService;
import pg.laziji.generator.util.SpringContextUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Value("${generator.template.path:}")
    private String templatePath;

    @Value("${generator.template.mapping:}")
    private String templateMapping;

    @Value("${generator.datasource.type:mysql}")
    private String datasourceType;

    @Override
    public void generateZip(String[] tableNames, String zipPath) {
        TableItem[] tableItems = new TableItem[tableNames.length];
        for (int i = 0; i < tableNames.length; i++) {
            tableItems[i] = new TableItem(tableNames[i]);
        }
        generateZip(tableItems, zipPath);
    }

    @Override
    public void generateZip(TableItem[] tableItems, String zipPath) {
        TableService tableService = SpringContextUtils.getBean(datasourceType, TableService.class);
        try (FileOutputStream fos = new FileOutputStream(zipPath)) {
            try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                for (TableItem item : tableItems) {
                    generatorCode(TemplateContext.newBuilder()
                            .templateVariables(item.getTemplateVariables())
                            .table(tableService.getTable(item.getTableName()))
                            .dynamicPathVariables(item.getDynamicPathVariables())
                            .build(), zos);
                }
            }
        } catch (Exception e) {
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
            int index = row.indexOf(":");
            if (index == -1) {
                continue;
            }
            String type = row.substring(0, index).trim();
            String path = row.substring(index + 1).trim();

            for (Map.Entry<String, String> entry : context.getDynamicPathVariables().entrySet()) {
                path = path.replace("{" + entry.getKey() + "}", entry.getValue());
            }
            templateMap.put(templatePath + "/" + type, path);
        }
        return templateMap;
    }
}
