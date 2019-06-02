package pg.laziji.generator.service;

import pg.laziji.generator.model.TableItem;

public interface GeneratorService {

    void generateZip(String[] tableNames, String zipPath);

    void generateZip(TableItem[] tableItems, String zipPath);
}
