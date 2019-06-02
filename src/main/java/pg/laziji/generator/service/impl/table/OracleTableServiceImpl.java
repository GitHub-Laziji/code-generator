package pg.laziji.generator.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.service.BaseTableService;

@Service("oracle")
public class OracleTableServiceImpl extends BaseTableService {

    @Override
    protected String getDriverClassName() {
        return "oracle.jdbc.driver.OracleDriver";
    }
}
