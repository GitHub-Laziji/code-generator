package pg.laziji.generator.mybatis.model;


import java.util.HashMap;
import java.util.Map;

public class TemplateContext {

    private Table table;
    private String packageName;
    private Map<String, String> options;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackagePath() {
        if (packageName == null) {
            return null;
        }
        return packageName.replace(".", "/");
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public Map<String, String> getDynamicPathFields() {
        Map<String, String> map = new HashMap<>();
        map.put("packagePath", getPackagePath());
        if (table != null) {
            map.put("className", table.getClassName());
            map.put("lowercaseClassName",table.getLowercaseClassName());
            map.put("customClassName", table.getCustomClassName());
        }
        if (options != null) {
            for (Map.Entry<String, String> entry : options.entrySet()) {
                map.put("options." + entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("table", table);
        map.put("options", options);
        map.put("packageName", packageName);
        return map;
    }


}
