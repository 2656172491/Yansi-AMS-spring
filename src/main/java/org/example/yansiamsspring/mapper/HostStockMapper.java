package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.HostStock;

import java.util.List;

@Mapper
public interface HostStockMapper {

    @Select("<script>" +
            "SELECT * FROM host_stock WHERE 1=1 " +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            " ORDER BY id ASC" +
            "</script>")
    List<HostStock> findList(@Param("status") String status);

    @Select("SELECT * FROM host_stock WHERE id = #{id}")
    HostStock findById(@Param("id") Long id);

    @Select("SELECT * FROM host_stock WHERE sn = #{sn}")
    HostStock findBySn(@Param("sn") String sn);

    @Insert("INSERT INTO host_stock (sn, status, remark) VALUES (#{sn}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(HostStock hostStock);

    @Update("UPDATE host_stock SET sn=#{sn}, remark=#{remark}, updated_at=NOW() WHERE id=#{id}")
    int update(HostStock hostStock);

    @Delete("DELETE FROM host_stock WHERE id = #{id}")
    int delete(@Param("id") Long id);

    @Update("UPDATE host_stock SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
