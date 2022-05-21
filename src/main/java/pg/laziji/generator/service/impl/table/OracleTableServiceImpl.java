package pg.laziji.generator.service.impl.table;

import oracle.jdbc.driver.OracleDriver;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("oracle")
public class OracleTableServiceImpl extends BaseTableService {

    {
        addTypeHandler("integer", Integer.class);
        addTypeHandler("float", Double.class);
        addTypeHandler("date", Date.class);
        addTypeHandler("timestamp", Date.class);
        addTypeHandler("char", String.class);
        addTypeHandler("varchar2", String.class);
        addTypeHandler("nvarchar2", String.class);
        addTypeHandler("long", String.class);
        addTypeHandler("clob", String.class);
        addTypeHandler("nclob", String.class);
        addTypeHandler("number", column -> column.getDecimalDigits() == null || column.getDecimalDigits() == 0 ? Long.class : Double.class);
    }

    @Override
    protected Class<OracleDriver>  getDriverClass() {
        return OracleDriver.class;
    }

}
