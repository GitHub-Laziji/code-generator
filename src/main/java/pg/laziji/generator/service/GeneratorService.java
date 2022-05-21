package pg.laziji.generator.service;

import pg.laziji.generator.model.TableItem;

public interface GeneratorService {

    void generate(String[] tableNames, String outputPath);

    void generate(TableItem[] tableItems, String outputPath);
}
