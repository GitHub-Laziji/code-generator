package pg.laziji.generator.service.impl.gen;

import pg.laziji.generator.model.TableItem;
import pg.laziji.generator.service.GeneratorService;

public abstract class BaseGeneratorService implements GeneratorService {

    @Override
    public void generate(String[] tableNames, String outputPath) {
        TableItem[] tableItems = new TableItem[tableNames.length];
        for (int i = 0; i < tableNames.length; i++) {
            tableItems[i] = new TableItem(tableNames[i]);
        }
        generate(tableItems, outputPath);
    }

}
