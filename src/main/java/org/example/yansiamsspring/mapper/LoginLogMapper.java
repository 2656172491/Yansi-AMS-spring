package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.yansiamsspring.pojo.LoginLog;

import java.util.List;

@Mapper
public interface LoginLogMapper {

    @Insert("INSERT INTO login_log (user_id, username, name, ip, user_agent, login_time, status, fail_reason) " +
            "VALUES (#{userId}, #{username}, #{name}, #{ip}, #{userAgent}, NOW(), #{status}, #{failReason})")
    void insert(LoginLog log);

    @Select("<script>" +
            "SELECT * FROM login_log WHERE 1=1 " +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%') </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            " ORDER BY login_time DESC LIMIT 200" +
            "</script>")
    List<LoginLog> findList(String username, Integer status);
}
