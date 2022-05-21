package pg.laziji.generator.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.model.Column;
import pg.laziji.generator.service.BaseTableService;

import java.util.Date;

@Service("postgresql")
public class PostgreSQLTableServiceImpl extends BaseTableService {


    {
        addTypeHandler("int2", Short.class);
        addTypeHandler("int", Integer.class);
        addTypeHandler("int4", Integer.class);
        addTypeHandler("int8", Long.class);
        addTypeHandler("numeric", Long.class);
        addTypeHandler("bigserial", Long.class);
        addTypeHandler("float", Double.class);
        addTypeHandler("date", Date.class);
        addTypeHandler("timestamp", Date.class);
        addTypeHandler("varchar", String.class);
        addTypeHandler("text", String.class);
    }

    @Override
    protected String getDriverClassName() {
        return "org.postgresql.Driver";
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
