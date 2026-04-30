package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.MonitorStock;

import java.util.List;

@Mapper
public interface MonitorStockMapper {

    @Select("<script>" +
            "SELECT * FROM monitor_stock WHERE 1=1 " +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            " ORDER BY id ASC" +
            "</script>")
    List<MonitorStock> findList(@Param("status") String status);

    @Select("SELECT * FROM monitor_stock WHERE id = #{id}")
    MonitorStock findById(@Param("id") Long id);

    @Select("SELECT * FROM monitor_stock WHERE sn = #{sn}")
    MonitorStock findBySn(@Param("sn") String sn);

    @Insert("INSERT INTO monitor_stock (sn, status, remark) VALUES (#{sn}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MonitorStock monitorStock);

    @Update("UPDATE monitor_stock SET sn=#{sn}, remark=#{remark}, updated_at=NOW() WHERE id=#{id}")
    int update(MonitorStock monitorStock);

    @Delete("DELETE FROM monitor_stock WHERE id = #{id}")
    int delete(@Param("id") Long id);

    @Update("UPDATE monitor_stock SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
