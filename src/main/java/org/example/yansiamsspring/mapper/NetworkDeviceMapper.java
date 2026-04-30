package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.NetworkDevice;

import java.util.List;

@Mapper
public interface NetworkDeviceMapper {

    @Select("SELECT * FROM network_device WHERE deleted = 0 ORDER BY id ASC")
    List<NetworkDevice> findAll();

    @Select("SELECT * FROM network_device WHERE id = #{id} AND deleted = 0")
    NetworkDevice findById(@Param("id") Long id);

    @Insert("INSERT INTO network_device (device_no, sn, remark, status) VALUES (#{deviceNo}, #{sn}, #{remark}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(NetworkDevice device);

    @Update("UPDATE network_device SET device_no=#{deviceNo}, sn=#{sn}, remark=#{remark}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(NetworkDevice device);

    @Update("UPDATE network_device SET deleted=1, updated_at=NOW() WHERE id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("UPDATE network_device SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
