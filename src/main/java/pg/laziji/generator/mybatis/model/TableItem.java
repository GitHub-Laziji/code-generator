package pg.laziji.generator.mybatis.model;

import java.util.Map;

public class TableItem {

    private String tableName;
    private String customClassName;
    private Map<String, String> options;

    public TableItem(String tableName) {
        this.tableName = tableName;
    }

    public TableItem(String tableName, Map<String, String> options) {
        this(tableName);
        this.options = options;
    }

    public TableItem(String tableName, String customClassName) {
        this.tableName = tableName;
        this.customClassName = customClassName;
    }

    public TableItem(String tableName, String customClassName, Map<String, String> options) {
        this(tableName, customClassName);
        this.options = options;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCustomClassName() {
        return customClassName;
    }

    public void setCustomClassName(String customClassName) {
        this.customClassName = customClassName;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
