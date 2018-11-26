package pg.laziji.generator.mybatis;


public class ColumnDO {

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
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getAttrLowerName() {
        return attrLowerName;
    }

    public void setAttrLowerName(String attrLowerName) {
        this.attrLowerName = attrLowerName;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

}
