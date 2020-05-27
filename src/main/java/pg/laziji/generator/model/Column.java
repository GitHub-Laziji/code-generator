package pg.laziji.generator.model;


import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Column {

    private String tableName;
    private String columnName;
    private String dataType;
    private String columnComment;
    private Integer columnSize;
    private Integer decimalDigits;
    private boolean nullAble;
    private boolean autoIncrement;

    private String attributeName;
    private String uppercaseAttributeName;
    private String attributeType;


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

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
        if (this.columnName != null) {
            this.uppercaseAttributeName = WordUtils.capitalizeFully(this.columnName.toLowerCase(), new char[]{'_'})
                    .replace("_", "");
            this.attributeName = StringUtils.uncapitalize(this.uppercaseAttributeName);
        }
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

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public String getUppercaseAttributeName() {
        return uppercaseAttributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

}
