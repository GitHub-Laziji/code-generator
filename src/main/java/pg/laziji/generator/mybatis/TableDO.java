package pg.laziji.generator.mybatis;

import org.apache.commons.lang.WordUtils;

import java.util.List;

public class TableDO {

    private String tableName;
    private List<ColumnDO> columns;
    private String className;
    private String suffix;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        this.className = WordUtils.capitalizeFully(tableName, new char[]{'_'}).replace("_", "");
        this.suffix = tableName.endsWith("_view")?"VO":"DO";
    }

    public List<ColumnDO> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDO> columns) {
        this.columns = columns;
    }

    public String getClassName() {
        return className;
    }

    public String getSuffix() {
        return suffix;
    }
}
