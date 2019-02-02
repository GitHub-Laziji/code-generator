package pg.laziji.generator.mybatis.model;

import org.apache.commons.lang.WordUtils;

import java.util.List;

public class Table {

    private String tableName;
    private List<Column> columns;
    private String className;
    private String suffix;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        this.suffix = tableName.endsWith("_view") ? "VO" : "DO";
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        if (className == null) {
            className = WordUtils.capitalizeFully(tableName, new char[]{'_'}).replace("_", "");
        }
        return className;
    }

    public String getSuffix() {
        return suffix;
    }
}
