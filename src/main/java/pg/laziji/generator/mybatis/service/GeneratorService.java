package pg.laziji.generator.mybatis.service;

import pg.laziji.generator.mybatis.model.TableItem;

public interface GeneratorService {

    void generateZip(String[] tableNames, String zipPath);

    void generateZip(TableItem[] tableItems, String zipPath);
}
