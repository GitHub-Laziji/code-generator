package pg.laziji.generator.model;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Table {

    private String tableName;
    private String tableType;
    private String tableComment;
    private List<Column> columns;

    private String className;

    private String lowercaseClassName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        if (this.tableName != null) {
            this.className = WordUtils.capitalizeFully(tableName.toLowerCase(), new char[]{'_'})
                    .replace("_", "");
            this.lowercaseClassName = StringUtils.uncapitalize(this.className);
        }
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

    public String getClassName() {
        return className;
    }

    public String getLowercaseClassName() {
        return lowercaseClassName;
    }
}
