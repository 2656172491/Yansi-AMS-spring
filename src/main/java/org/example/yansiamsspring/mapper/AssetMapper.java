package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.Asset;

import java.util.List;

@Mapper
public interface AssetMapper {

    @Select("SELECT * FROM asset WHERE deleted = 0 ORDER BY id ASC")
    List<Asset> findAll();

    @Select("SELECT * FROM asset WHERE id = #{id} AND deleted = 0")
    Asset findById(@Param("id") Long id);

    @Select("SELECT * FROM asset WHERE asset_type = #{assetType} AND deleted = 0")
    Asset findByAssetType(@Param("assetType") String assetType);

    @Insert("INSERT INTO asset (asset_type, quantity, in_use_quantity, warning_quantity, remark, status) " +
            "VALUES (#{assetType}, #{quantity}, #{inUseQuantity}, #{warningQuantity}, #{remark}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Asset asset);

    @Update("UPDATE asset SET asset_type=#{assetType}, quantity=#{quantity}, in_use_quantity=#{inUseQuantity}, " +
            "warning_quantity=#{warningQuantity}, remark=#{remark}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(Asset asset);

    @Update("UPDATE asset SET deleted=1, updated_at=NOW() WHERE id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("UPDATE asset SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE asset SET quantity=#{quantity}, updated_at=NOW() WHERE id=#{id}")
    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Update("UPDATE asset SET in_use_quantity=#{inUseQuantity}, updated_at=NOW() WHERE id=#{id}")
    int updateInUseQuantity(@Param("id") Long id, @Param("inUseQuantity") Integer inUseQuantity);
}
