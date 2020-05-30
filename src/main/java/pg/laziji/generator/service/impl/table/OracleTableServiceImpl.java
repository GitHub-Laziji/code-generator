package pg.laziji.generator.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.model.Column;
import pg.laziji.generator.service.BaseTableService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("oracle")
public class OracleTableServiceImpl extends BaseTableService {

    {
        addTypeHandler("integer", Integer.class);
        addTypeHandler("float", Float.class);
        addTypeHandler("date", Date.class);
        addTypeHandler("timestamp", Date.class);
        addTypeHandler("char", String.class);
        addTypeHandler("varchar2", String.class);
        addTypeHandler("nvarchar2", String.class);
        addTypeHandler("clob", String.class);
        addTypeHandler("nclob", String.class);
        addTypeHandler("number", column -> column.getDecimalDigits() == null || column.getDecimalDigits() == 0 ? Long.class : Double.class);
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
        return getTypeMappingOrDefault(
                column.getDataType().toLowerCase().replace("unsigned", "").trim(),
                column,
                Object.class
        ).getSimpleName();
    }
}
