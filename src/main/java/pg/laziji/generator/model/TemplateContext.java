package pg.laziji.generator.model;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import pg.laziji.generator.constant.KeyConsts;
import pg.laziji.generator.util.ConfigUtils;
import pg.laziji.generator.util.SpringContextUtils;
import pg.laziji.generator.util.TemplateUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TemplateContext {

    private static final Map<String, Object> systemVariables = new HashMap<>();

    private final Map<String, String> dynamicPathVariables = new HashMap<>();
    private Map<String, Object> templateVariables;
    private Table table;

    static {
        Properties properties = System.getProperties();
        systemVariables.put("config", ConfigUtils.class);
        systemVariables.put("utils", TemplateUtils.class);

        systemVariables.put("username", System.getenv("USERNAME"));
        systemVariables.put("computerName", System.getenv("COMPUTERNAME"));
        systemVariables.put("osName", properties.getProperty("os.name"));
        systemVariables.put("osArch", properties.getProperty("os.arch"));
        systemVariables.put("osVersion", properties.getProperty("os.version"));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static void setSystemVariable(String key, Object value) {
        systemVariables.put(key, value);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("system", systemVariables);

        map.put("dynamicPath", dynamicPathVariables);
        map.put("template", templateVariables);
        map.put("table", table);
        return map;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Map<String, Object> getSystemVariables() {
        return systemVariables;
    }

    public Map<String, String> getDynamicPathVariables() {
        return dynamicPathVariables;
    }

    public Map<String, Object> getTemplateVariables() {
        return templateVariables;
    }

    public void setTemplateVariables(Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables;
    }


    public static class Builder {

        private Environment environment = SpringContextUtils.getBean(Environment.class);

        private TemplateContext context = new TemplateContext();

        private Builder() {
            String packageName = environment.getProperty("generator.package", "pkg");
            context.getDynamicPathVariables().put(KeyConsts.PACKAGE_PATH, packageName.replace(".", "/"));
        }

        public Builder table(Table table) {
            if (table != null) {
                context.setTable(table);
                context.getDynamicPathVariables().put(KeyConsts.CLASS_NAME, table.getClassName());
                context.getDynamicPathVariables().put(KeyConsts.LOWERCASE_CLASS_NAME, table.getLowercaseClassName());
            }
            return this;
        }

        public Builder dynamicPathVariables(Map<String, String> variables) {
            if (variables != null) {
                Map<String, String> dynamicPathVariables = context.getDynamicPathVariables();
                for (Map.Entry<String, String> entry : variables.entrySet()) {
                    dynamicPathVariables.put(entry.getKey(), entry.getValue());
                }
                String className = dynamicPathVariables.get(KeyConsts.CLASS_NAME);
                String lowercaseClassName = dynamicPathVariables.get(KeyConsts.CLASS_NAME);
                if (StringUtils.isNotBlank(className)) {
                    dynamicPathVariables.put(KeyConsts.LOWERCASE_CLASS_NAME, StringUtils.uncapitalize(className));
                } else if (StringUtils.isNotBlank(lowercaseClassName)) {
                    dynamicPathVariables.put(KeyConsts.CLASS_NAME, WordUtils.capitalizeFully(lowercaseClassName));
                }
            }
            return this;
        }

        public Builder templateVariables(Map<String, Object> variables) {
            context.setTemplateVariables(variables);
            return this;
        }

        public TemplateContext build() {
            return context;
        }
    }
}
