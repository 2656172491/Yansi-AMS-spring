package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.DeviceChangeOrder;

import java.util.List;

@Mapper
public interface DeviceChangeOrderMapper {

    @Select("<script>" +
            "SELECT * FROM device_change_order WHERE 1=1 " +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "<if test='handlerId != null'> AND handler_id = #{handlerId} </if>" +
            " ORDER BY created_at DESC" +
            "</script>")
    List<DeviceChangeOrder> findList(@Param("status") String status, @Param("handlerId") Long handlerId);

    @Select("SELECT * FROM device_change_order WHERE id = #{id}")
    DeviceChangeOrder findById(@Param("id") Long id);

    @Insert("INSERT INTO device_change_order (order_no, order_type, asset_id, new_asset_id, reporter, reporter_dept, " +
            "fault_desc, handler, handler_id, status, remark, created_at, updated_at) " +
            "VALUES (#{orderNo}, #{orderType}, #{assetId}, #{newAssetId}, #{reporter}, #{reporterDept}, " +
            "#{faultDesc}, #{handler}, #{handlerId}, #{status}, #{remark}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DeviceChangeOrder order);

    @Update("UPDATE device_change_order SET status=#{status}, remark=#{remark}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("remark") String remark);

    @Update("UPDATE device_change_order SET status='done', result=#{result}, new_asset_id=#{newAssetId}, remark=#{remark}, updated_at=NOW() WHERE id=#{id}")
    int complete(@Param("id") Long id, @Param("result") String result, @Param("newAssetId") Long newAssetId, @Param("remark") String remark);
}
