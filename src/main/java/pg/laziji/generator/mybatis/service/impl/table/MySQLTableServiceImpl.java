package pg.laziji.generator.mybatis.service.impl.table;

import org.springframework.stereotype.Service;
import pg.laziji.generator.mybatis.service.BaseTableService;

@Service("mysql")
public class MySQLTableServiceImpl extends BaseTableService {

    @Override
    protected String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }
}
