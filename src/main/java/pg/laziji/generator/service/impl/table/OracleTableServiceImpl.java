package pg.laziji.generator.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.model.Column;
import pg.laziji.generator.service.BaseTableService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("oracle")
public class OracleTableServiceImpl extends BaseTableService {

    private static final Map<String, Class> typeMap = new HashMap<>();

    static {
        typeMap.put("integer", Integer.class);
        typeMap.put("float", Float.class);
        typeMap.put("number", Double.class);
        typeMap.put("date", Date.class);
        typeMap.put("timestamp", Date.class);
        typeMap.put("char", String.class);
        typeMap.put("varchar2", String.class);
        typeMap.put("nvarchar2", String.class);
        typeMap.put("clob", String.class);
        typeMap.put("nclob", String.class);
    }

    @Override
    protected String getDriverClassName() {
        return "oracle.jdbc.driver.OracleDriver";
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
