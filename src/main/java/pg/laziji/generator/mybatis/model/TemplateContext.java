package pg.laziji.generator.mybatis.model;


import java.util.HashMap;
import java.util.Map;

public class TemplateContext {

    private Table table;
    private String packageName;

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

    public Map<String, String> getDynamicPathFields() {
        Map<String, String> map = new HashMap<>();
        map.put("packagePath", getPackagePath());
        if(table!=null){
            map.put("className", table.getClassName());
            map.put("customClassName", table.getCustomClassName());
            map.put("suffix", table.getSuffix());
        }
        return map;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("table", table);
        map.put("packageName", packageName);
        return map;
    }


}
