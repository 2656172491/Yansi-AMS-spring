package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.AssetRelation;

import java.util.List;

@Mapper
public interface AssetRelationMapper {

    @Select("SELECT * FROM asset_relation WHERE host_asset_id = #{hostId}")
    List<AssetRelation> findByHostId(@Param("hostId") Long hostId);

    @Select("SELECT * FROM asset_relation WHERE related_asset_id = #{relatedId}")
    List<AssetRelation> findByRelatedId(@Param("relatedId") Long relatedId);

    @Insert("INSERT INTO asset_relation (host_asset_id, related_asset_id, relation_type) " +
            "VALUES (#{hostAssetId}, #{relatedAssetId}, #{relationType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AssetRelation relation);

    @Delete("DELETE FROM asset_relation WHERE id = #{id}")
    int delete(@Param("id") Long id);

    @Delete("DELETE FROM asset_relation WHERE host_asset_id = #{hostId} AND relation_type = #{relationType}")
    int deleteByHostAndType(@Param("hostId") Long hostId, @Param("relationType") String relationType);
}
