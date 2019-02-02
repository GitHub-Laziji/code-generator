package pg.laziji.generator.mybatis.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pg.laziji.generator.mybatis.model.Column;

import java.util.List;

@Mapper
public interface GeneratorMapper {

    @Select("select column_name columnName, data_type dataType from information_schema.columns where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<Column> listColumns(String tableName);
}
