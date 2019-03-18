package pg.laziji.generator.mybatis.model;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Table {

    private String tableName;
    private String tableType;
    private String tableComment;
    private List<Column> columns;
    private String customClassName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getCustomClassName() {
        if(customClassName!=null){
            return customClassName;
        }
        return getClassName();
    }

    public void setCustomClassName(String customClassName) {
        this.customClassName = customClassName;
    }

    public String getSuffix() {
        if (tableType == null) {
            return null;
        }
        switch (tableType) {
            case "BASE TABLE":
                return "DO";
            case "VIEW":
                return "VO";
            default:
                return null;
        }
    }

    public String getClassName() {
        if (tableName == null) {
            return null;
        }
        return WordUtils.capitalizeFully(tableName, new char[]{'_'}).replace("_", "");
    }

    public String getLowercaseClassName() {
        return StringUtils.uncapitalize(getClassName());
    }
}
