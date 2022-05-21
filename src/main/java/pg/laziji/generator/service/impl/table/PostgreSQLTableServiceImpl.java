package pg.laziji.generator.service.impl.table;

import org.postgresql.Driver;
import org.springframework.stereotype.Service;

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
    protected Class<Driver> getDriverClass() {
        return Driver.class;
    }

}
