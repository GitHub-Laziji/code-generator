package pg.laziji.generator.service.impl.table;


import com.mysql.jdbc.Driver;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("mysql")
public class MySQLTableServiceImpl extends BaseTableService {

    {
        addTypeHandler("bit", Boolean.class);
        addTypeHandler("tinyint", Integer.class);
        addTypeHandler("smallint", Integer.class);
        addTypeHandler("mediumint", Integer.class);
        addTypeHandler("int", Integer.class);
        addTypeHandler("integer", Integer.class);
        addTypeHandler("bigint", Long.class);
        addTypeHandler("decimal", Long.class);
        addTypeHandler("float", Float.class);
        addTypeHandler("double", Double.class);
        addTypeHandler("date", Date.class);
        addTypeHandler("time", Date.class);
        addTypeHandler("year", Date.class);
        addTypeHandler("datetime", Date.class);
        addTypeHandler("timestamp", Date.class);
        addTypeHandler("char", String.class);
        addTypeHandler("varchar", String.class);
        addTypeHandler("tinyblob", String.class);
        addTypeHandler("tinytext", String.class);
        addTypeHandler("blob", String.class);
        addTypeHandler("text", String.class);
        addTypeHandler("mediumblob", String.class);
        addTypeHandler("mediumtext", String.class);
        addTypeHandler("longblob", String.class);
        addTypeHandler("longtext", String.class);
    }

    @Override
    protected Class<Driver> getDriverClass() {
        return Driver.class;
    }

}
