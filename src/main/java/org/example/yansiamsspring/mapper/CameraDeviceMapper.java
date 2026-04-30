package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.CameraDevice;

import java.util.List;

@Mapper
public interface CameraDeviceMapper {

    @Select("SELECT * FROM camera_device WHERE deleted = 0 ORDER BY id ASC")
    List<CameraDevice> findAll();

    @Select("SELECT * FROM camera_device WHERE id = #{id} AND deleted = 0")
    CameraDevice findById(@Param("id") Long id);

    @Insert("INSERT INTO camera_device (device_no, sn, remark, status) VALUES (#{deviceNo}, #{sn}, #{remark}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CameraDevice device);

    @Update("UPDATE camera_device SET device_no=#{deviceNo}, sn=#{sn}, remark=#{remark}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(CameraDevice device);

    @Update("UPDATE camera_device SET deleted=1, updated_at=NOW() WHERE id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("UPDATE camera_device SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
