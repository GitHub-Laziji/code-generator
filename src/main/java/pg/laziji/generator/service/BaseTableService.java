package pg.laziji.generator.service;

import org.springframework.beans.factory.annotation.Value;
import pg.laziji.generator.model.Column;
import pg.laziji.generator.model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BaseTableService implements TableService {


    @Value("${generator.datasource.url}")
    private String url;

    @Value("${generator.datasource.username}")
    private String username;

    @Value("${generator.datasource.password}")
    private String password;

    protected abstract String getDriverClassName();

    protected abstract String analysisDataType(Column column);

    @Override
    public Table getTable(String tableName) throws Exception {
        try (Connection connection = getConnection()) {
            Table table = getMetaDataTable(connection, tableName);
            if (table == null) {
                return null;
            }
            List<Column> columns = listMetaDataColumn(connection, tableName);
            table.setColumns(columns);
            return table;
        }
    }


    private Table getMetaDataTable(Connection connection, String tableName) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getTables(
                connection.getCatalog(),
                connection.getSchema(),
                tableName,
                null);
        if (resultSet.next()) {
            if (!Objects.equals(tableName, resultSet.getString("TABLE_NAME"))) {
                return null;
            }
            Table table = new Table();
            table.setTableName(tableName);
            table.setTableType(resultSet.getString("TABLE_TYPE"));
            table.setTableComment(resultSet.getString("REMARKS"));
            return table;
        }
        return null;
    }

    private List<Column> listMetaDataColumn(Connection connection, String tableName) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getColumns(connection.getCatalog(),
                connection.getSchema(),
                tableName,
                null);
        List<Column> columns = new ArrayList<>();
        while (resultSet.next()) {
            if (!Objects.equals(tableName, resultSet.getString("TABLE_NAME"))) {
                continue;
            }

            Column column = new Column();
            column.setTableName(tableName);
            column.setColumnName(resultSet.getString("COLUMN_NAME"));
            column.setDataType(resultSet.getString("TYPE_NAME"));
            column.setColumnSize(resultSet.getInt("COLUMN_SIZE"));
            column.setDecimalDigits(resultSet.getInt("DECIMAL_DIGITS"));
            column.setColumnComment(resultSet.getString("REMARKS"));

            String nullAble = resultSet.getString("IS_NULLABLE");
            if (nullAble != null) {
                column.setNullAble("YES".equals(nullAble));
            }
            String autoIncrement = resultSet.getString("IS_AUTOINCREMENT");
            if (autoIncrement != null) {
                column.setAutoIncrement("YES".equals(autoIncrement));
            }

            column.setAttributeType(analysisDataType(column));

            columns.add(column);
        }
        return columns;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(getDriverClassName());
        return DriverManager.getConnection(url, username, password);
    }
}
