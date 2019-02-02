package pg.laziji.generator.mybatis.model;

public class TableItem {

    private String tableName;
    private String className;

    public TableItem(String tableName) {
        this.tableName = tableName;
    }

    public TableItem(String tableName, String className) {
        this.tableName = tableName;
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
