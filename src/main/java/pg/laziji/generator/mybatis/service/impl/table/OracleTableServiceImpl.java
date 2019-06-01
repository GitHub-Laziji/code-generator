package pg.laziji.generator.mybatis.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.mybatis.service.BaseTableService;

@Service("oracle")
public class OracleTableServiceImpl extends BaseTableService {

    @Override
    protected String getDriverClassName() {
        return "oracle.jdbc.driver.OracleDriver";
    }
}
