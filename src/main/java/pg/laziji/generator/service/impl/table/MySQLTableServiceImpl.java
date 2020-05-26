package pg.laziji.generator.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.model.Column;
import pg.laziji.generator.service.BaseTableService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("mysql")
public class MySQLTableServiceImpl extends BaseTableService {

    private static final Map<String, Class> typeMap = new HashMap<>();

    static {
        typeMap.put("bit", Boolean.class);
        typeMap.put("tinyint", Integer.class);
        typeMap.put("smallint", Integer.class);
        typeMap.put("mediumint", Integer.class);
        typeMap.put("int", Integer.class);
        typeMap.put("integer", Integer.class);
        typeMap.put("bigint", Long.class);
        typeMap.put("decimal", Long.class);
        typeMap.put("float", Float.class);
        typeMap.put("double", Double.class);
        typeMap.put("date", Date.class);
        typeMap.put("time", Date.class);
        typeMap.put("year", Date.class);
        typeMap.put("datetime", Date.class);
        typeMap.put("timestamp", Date.class);
        typeMap.put("char", String.class);
        typeMap.put("varchar", String.class);
        typeMap.put("tinyblob", String.class);
        typeMap.put("tinytext", String.class);
        typeMap.put("blob", String.class);
        typeMap.put("text", String.class);
        typeMap.put("mediumblob", String.class);
        typeMap.put("mediumtext", String.class);
        typeMap.put("longblob", String.class);
        typeMap.put("longtext", String.class);
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
        return typeMap.getOrDefault(
                column.getDataType().toLowerCase().replace("unsigned", "").trim(),
                Object.class).getSimpleName();
    }
}
