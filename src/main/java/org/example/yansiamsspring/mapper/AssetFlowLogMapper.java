package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.yansiamsspring.pojo.AssetFlowLog;

import java.util.List;
import java.util.Map;

@Mapper
public interface AssetFlowLogMapper {

    @Insert("INSERT INTO asset_flow_log (asset_id, asset_type, flow_type, quantity, batch_no, operator, operator_id, flow_time) " +
            "VALUES (#{assetId}, #{assetType}, #{flowType}, #{quantity}, #{batchNo}, #{operator}, #{operatorId}, NOW())")
    void insert(AssetFlowLog log);

    @Select("SELECT DATE(flow_time) AS date, flow_type, SUM(quantity) AS total " +
            "FROM asset_flow_log " +
            "WHERE flow_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
            "GROUP BY DATE(flow_time), flow_type " +
            "ORDER BY date")
    List<Map<String, Object>> countByDays(int days);

    @Delete("DELETE FROM asset_flow_log WHERE asset_type = #{assetType}")
    int deleteByAssetType(@Param("assetType") String assetType);
}
