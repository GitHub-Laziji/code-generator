package pg.laziji.generator.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GeneratorMapper {

    @Select("select column_name columnName, data_type dataType from information_schema.columns where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<ColumnDO> listColumns(String tableName);
}
