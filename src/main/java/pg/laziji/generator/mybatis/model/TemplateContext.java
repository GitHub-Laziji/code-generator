package pg.laziji.generator.mybatis.model;

import org.springframework.core.env.Environment;
import pg.laziji.generator.mybatis.util.ConfigUtils;
import pg.laziji.generator.mybatis.util.SpringContextUtils;
import pg.laziji.generator.mybatis.util.TemplateUtils;

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
            context.getDynamicPathVariables().put("packagePath", packageName.replace(".", "/"));
        }

        public Builder table(Table table) {
            if (table != null) {
                context.setTable(table);
                context.getDynamicPathVariables().put("className", table.getClassName());
                context.getDynamicPathVariables().put("lowercaseClassName", table.getLowercaseClassName());
            }
            return this;
        }

        public Builder dynamicPathVariables(Map<String, String> variables) {
            if (variables != null) {
                for (Map.Entry<String, String> entry : variables.entrySet()) {
                    context.getDynamicPathVariables().put(entry.getKey(), entry.getValue());
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
