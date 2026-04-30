package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.AssetBatch;

import java.util.List;

@Mapper
public interface AssetBatchMapper {

    @Select("SELECT * FROM asset_batch ORDER BY created_at DESC")
    List<AssetBatch> findAll();

    @Select("SELECT * FROM asset_batch WHERE id = #{id}")
    AssetBatch findById(@Param("id") Long id);

    @Select("SELECT * FROM asset_batch WHERE asset_type = #{assetType} ORDER BY created_at DESC")
    List<AssetBatch> findByAssetType(@Param("assetType") String assetType);

    @Insert("INSERT INTO asset_batch (batch_no, asset_type, purchase_date, supplier, quantity, remark) " +
            "VALUES (#{batchNo}, #{assetType}, #{purchaseDate}, #{supplier}, #{quantity}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AssetBatch batch);

    @Update("UPDATE asset_batch SET quantity=#{quantity}, remark=#{remark}, updated_at=NOW() WHERE id=#{id}")
    int update(AssetBatch batch);

    @Delete("DELETE FROM asset_batch WHERE asset_type = #{assetType}")
    int deleteByAssetType(@Param("assetType") String assetType);
}
