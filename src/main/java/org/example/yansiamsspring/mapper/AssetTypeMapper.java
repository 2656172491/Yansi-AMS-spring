package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.AssetType;

import java.util.List;

@Mapper
public interface AssetTypeMapper {

    @Select("SELECT * FROM asset_type ORDER BY sort_order ASC, id ASC")
    List<AssetType> findAll();

    @Select("SELECT * FROM asset_type WHERE status = 1 ORDER BY sort_order ASC, id ASC")
    List<AssetType> findActive();

    @Select("SELECT * FROM asset_type WHERE id = #{id}")
    AssetType findById(@Param("id") Long id);

    @Select("SELECT * FROM asset_type WHERE code = #{code}")
    AssetType findByCode(@Param("code") String code);

    @Insert("INSERT INTO asset_type (name, code, sort_order, status) VALUES (#{name}, #{code}, #{sortOrder}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AssetType assetType);

    @Update("UPDATE asset_type SET name=#{name}, code=#{code}, sort_order=#{sortOrder}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(AssetType assetType);

    @Delete("DELETE FROM asset_type WHERE id = #{id}")
    int delete(@Param("id") Long id);
}
