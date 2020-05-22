package pg.laziji.generator.model;


import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Column {

    private static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("bit", "Boolean");
        typeMap.put("boolean", "Boolean");
        typeMap.put("tinyint", "Integer");
        typeMap.put("smallint", "Integer");
        typeMap.put("mediumint", "Integer");
        typeMap.put("int", "Integer");
        typeMap.put("integer", "Integer");
        typeMap.put("bigint", "Long");
        typeMap.put("decimal", "Long");
        typeMap.put("float", "Float");
        typeMap.put("double", "Double");
        typeMap.put("number", "Double");
        typeMap.put("date", "Date");
        typeMap.put("time", "Date");
        typeMap.put("year", "Date");
        typeMap.put("datetime", "Date");
        typeMap.put("timestamp", "Date");
        typeMap.put("char", "String");
        typeMap.put("varchar", "String");
        typeMap.put("tinyblob", "String");
        typeMap.put("tinytext", "String");
        typeMap.put("blob", "String");
        typeMap.put("text", "String");
        typeMap.put("mediumblob", "String");
        typeMap.put("mediumtext", "String");
        typeMap.put("longblob", "String");
        typeMap.put("longtext", "String");
        typeMap.put("string", "String");
    }

    private String tableName;
    private String columnName;
    private String dataType;
    private String columnComment;
    private Integer columnSize;
    private boolean nullAble;
    private boolean autoIncrement;


    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isNullAble() {
        return nullAble;
    }

    public void setNullAble(boolean nullAble) {
        this.nullAble = nullAble;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getUppercaseAttributeName() {
        if (columnName == null) {
            return null;
        }
        return WordUtils.capitalizeFully(columnName.toLowerCase(), new char[]{'_'})
                .replace("_", "");
    }

    public String getAttributeName() {
        return StringUtils.uncapitalize(getUppercaseAttributeName());
    }

    public String getAttributeType() {
        if (dataType == null) {
            return null;
        }
        String type = dataType.toLowerCase().replace("unsigned", "").trim();
        return typeMap.getOrDefault(type, "Object");
    }
}
