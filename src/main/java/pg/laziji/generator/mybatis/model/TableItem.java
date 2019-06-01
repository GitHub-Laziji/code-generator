package pg.laziji.generator.mybatis.model;

import java.util.HashMap;
import java.util.Map;

public class TableItem {

    private String tableName;
    private Map<String, String> dynamicPathVariables = new HashMap<>();
    private Map<String, Object> templateVariables = new HashMap<>();

    public static Builder newBuilder() {
        return new Builder();
    }

    public TableItem() {

    }

    public TableItem(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getDynamicPathVariables() {
        return dynamicPathVariables;
    }

    public void setDynamicPathVariables(Map<String, String> dynamicPathVariables) {
        this.dynamicPathVariables = dynamicPathVariables;
    }

    public Map<String, Object> getTemplateVariables() {
        return templateVariables;
    }

    public void setTemplateVariables(Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables;
    }

    public static class Builder {

        private TableItem item = new TableItem();

        public Builder tableName(String tableName) {
            item.setTableName(tableName);
            return this;
        }

        public Builder dynamicPathVariable(String key, String value) {
            item.getDynamicPathVariables().put(key, value);
            return this;
        }

        public Builder templateVariable(String key, Object value) {
            item.getTemplateVariables().put(key, value);
            return this;
        }

        public TableItem build() {
            return item;
        }
    }
}
