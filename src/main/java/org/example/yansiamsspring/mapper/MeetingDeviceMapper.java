package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.MeetingDevice;

import java.util.List;

@Mapper
public interface MeetingDeviceMapper {

    @Select("SELECT * FROM meeting_device WHERE deleted = 0 ORDER BY id ASC")
    List<MeetingDevice> findAll();

    @Select("SELECT * FROM meeting_device WHERE id = #{id} AND deleted = 0")
    MeetingDevice findById(@Param("id") Long id);

    @Insert("INSERT INTO meeting_device (device_no, sn, remark, status) VALUES (#{deviceNo}, #{sn}, #{remark}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MeetingDevice device);

    @Update("UPDATE meeting_device SET device_no=#{deviceNo}, sn=#{sn}, remark=#{remark}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(MeetingDevice device);

    @Update("UPDATE meeting_device SET deleted=1, updated_at=NOW() WHERE id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("UPDATE meeting_device SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
