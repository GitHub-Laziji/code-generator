package pg.laziji.generator.mybatis.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pg.laziji.generator.mybatis.model.Column;
import pg.laziji.generator.mybatis.model.Table;

import java.util.List;

@Mapper
public interface GeneratorMapper {

    @Select("select table_name tableName, table_type tableType, table_comment tableComment from information_schema.tables where table_name = #{tableName} and table_schema = (select database())")
    Table queryTable(String tableName);

    @Select("select column_name columnName, data_type dataType, column_comment columnComment from information_schema.columns where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<Column> queryColumns(String tableName);
}
