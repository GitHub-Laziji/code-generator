package pg.laziji.generator.service.impl.table;

import org.springframework.beans.factory.annotation.Value;
import pg.laziji.generator.model.Column;
import pg.laziji.generator.model.Table;
import pg.laziji.generator.service.TableService;

import java.sql.*;
import java.util.*;

public abstract class BaseTableService implements TableService {

    private final Map<String, TypeHandler> typeHandlerMap = new HashMap<>();

    @Value("${generator.datasource.url}")
    private String url;

    @Value("${generator.datasource.username}")
    private String username;

    @Value("${generator.datasource.password}")
    private String password;

    protected abstract Class<? extends Driver> getDriverClass();

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

    protected String analysisDataType(Column column) {
        if (column == null || column.getDataType() == null) {
            return Object.class.getSimpleName();
        }
        return getTypeMappingOrDefault(
                column.getDataType().toLowerCase().replace("unsigned", "").trim(),
                column,
                Object.class
        ).getSimpleName();
    }

    protected void addTypeHandler(String dataType, Class<?> clazz) {
        this.typeHandlerMap.put(dataType, column -> clazz);
    }

    protected void addTypeHandler(String dataType, TypeHandler handler) {
        this.typeHandlerMap.put(dataType, handler);
    }

    protected Class<?> getTypeMappingOrDefault(String dataType, Column column, Class<?> clazz) {
        TypeHandler handler = this.typeHandlerMap.get(dataType);
        if (handler == null) {
            return clazz;
        }
        return handler.handle(column);
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
        return DriverManager.getConnection(url, username, password);
    }

    @FunctionalInterface
    protected interface TypeHandler {
        Class<?> handle(Column column);
    }
}
