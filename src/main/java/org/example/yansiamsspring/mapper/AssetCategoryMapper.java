package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.AssetCategory;

import java.util.List;

@Mapper
public interface AssetCategoryMapper {

    @Select("SELECT * FROM asset_category ORDER BY sort_order ASC, id ASC")
    List<AssetCategory> findAll();

    @Select("SELECT * FROM asset_category WHERE status = 1 ORDER BY sort_order ASC, id ASC")
    List<AssetCategory> findActive();

    @Select("SELECT * FROM asset_category WHERE code = #{code}")
    AssetCategory findByCode(@Param("code") String code);

    @Insert("INSERT INTO asset_category (name, code, sort_order, status, data_source, list_columns) " +
            "VALUES (#{name}, #{code}, #{sortOrder}, #{status}, #{dataSource}, #{listColumns})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AssetCategory category);

    @Update("UPDATE asset_category SET name=#{name}, code=#{code}, sort_order=#{sortOrder}, status=#{status}, " +
            "data_source=#{dataSource}, list_columns=#{listColumns}, updated_at=NOW() WHERE id=#{id}")
    int update(AssetCategory category);

    @Delete("DELETE FROM asset_category WHERE id = #{id}")
    int delete(@Param("id") Long id);
}
