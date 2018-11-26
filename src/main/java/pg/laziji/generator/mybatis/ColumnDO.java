package pg.laziji.generator.mybatis;


import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ColumnDO {

    private static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("tinyint", "Integer");
        typeMap.put("smallint", "Integer");
        typeMap.put("mediumint", "Integer");
        typeMap.put("int", "Integer");
        typeMap.put("integer", "Integer");
        typeMap.put("bigint", "Long");
        typeMap.put("float", "Float");
        typeMap.put("double", "Double");
        typeMap.put("decimal", "Long");
        typeMap.put("bit", "Boolean");
        typeMap.put("char", "String");
        typeMap.put("varchar", "String");
        typeMap.put("tinytext", "String");
        typeMap.put("text", "String");
        typeMap.put("mediumtext", "String");
        typeMap.put("longtext", "String");
        typeMap.put("date", "Date");
        typeMap.put("datetime", "Date");
        typeMap.put("timestamp", "Date");
    }

    private String columnName;
    private String dataType;
    private String attrName;
    private String attrLowerName;
    private String attrType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
        this.attrName = WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
        this.attrLowerName = StringUtils.uncapitalize(this.attrName);
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
        this.attrType = typeMap.getOrDefault(dataType, "Object");
    }

    public String getAttrLowerName() {
        return attrLowerName;
    }

    public String getAttrName() {
        return attrName;
    }

    public String getAttrType() {
        return attrType;
    }
}
