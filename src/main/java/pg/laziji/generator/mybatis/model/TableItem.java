package pg.laziji.generator.mybatis.model;

import java.util.Map;

public class TableItem {

    private String tableName;
    private String className;
    private Map<String, String> options;

    public TableItem(String tableName) {
        this.tableName = tableName;
    }

    public TableItem(String tableName, Map<String, String> options) {
        this(tableName);
        this.options = options;
    }

    public TableItem(String tableName, String className) {
        this.tableName = tableName;
        this.className = className;
    }

    public TableItem(String tableName, String className, Map<String, String> options) {
        this(tableName, className);
        this.options = options;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
