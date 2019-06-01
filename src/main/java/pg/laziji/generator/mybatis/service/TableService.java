package pg.laziji.generator.mybatis.service;

import pg.laziji.generator.mybatis.model.Table;

public interface TableService {

    Table getTable(String tableName);
}