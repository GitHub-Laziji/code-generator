package pg.laziji.generator.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.model.Column;
import pg.laziji.generator.service.BaseTableService;

import java.util.HashMap;
import java.util.Map;

@Service("oracle")
public class OracleTableServiceImpl extends BaseTableService {



    @Override
    protected String getDriverClassName() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    @Override
    protected String analysisDataType(Column column) {
       return null;
    }
}
