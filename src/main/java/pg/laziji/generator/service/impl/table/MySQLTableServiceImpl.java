package pg.laziji.generator.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.model.Column;
import pg.laziji.generator.service.BaseTableService;

import java.util.HashMap;
import java.util.Map;

@Service("mysql")
public class MySQLTableServiceImpl extends BaseTableService {

    private static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("bit", "Boolean");
        typeMap.put("tinyint", "Integer");
        typeMap.put("smallint", "Integer");
        typeMap.put("mediumint", "Integer");
        typeMap.put("int", "Integer");
        typeMap.put("integer", "Integer");
        typeMap.put("bigint", "Long");
        typeMap.put("decimal", "Long");
        typeMap.put("float", "Float");
        typeMap.put("double", "Double");
        typeMap.put("date", "Date");
        typeMap.put("time", "Date");
        typeMap.put("year", "Date");
        typeMap.put("datetime", "Date");
        typeMap.put("timestamp", "Date");
        typeMap.put("char", "String");
        typeMap.put("varchar", "String");
        typeMap.put("tinyblob", "String");
        typeMap.put("tinytext", "String");
        typeMap.put("blob", "String");
        typeMap.put("text", "String");
        typeMap.put("mediumblob", "String");
        typeMap.put("mediumtext", "String");
        typeMap.put("longblob", "String");
        typeMap.put("longtext", "String");
    }

    @Override
    protected String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    protected String analysisDataType(Column column) {
        if (column == null || column.getDataType() == null) {
            return Object.class.getSimpleName();
        }
        return typeMap.getOrDefault(column.getDataType().toLowerCase().replace("unsigned", "").trim(),
                Object.class.getSimpleName());
    }
}
