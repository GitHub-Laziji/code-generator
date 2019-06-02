package pg.laziji.generator.service;

import pg.laziji.generator.model.Table;

public interface TableService {

    Table getTable(String tableName) throws Exception;
}