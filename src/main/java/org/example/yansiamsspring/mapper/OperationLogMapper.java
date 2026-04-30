package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.yansiamsspring.pojo.OperationLog;

import java.util.List;

@Mapper
public interface OperationLogMapper {

    @Insert("INSERT INTO operation_log (user_id, username, name, operation_type, target_type, target_id, detail, detail_text, result, ip, operation_time) " +
            "VALUES (#{userId}, #{username}, #{name}, #{operationType}, #{targetType}, #{targetId}, #{detail}, #{detailText}, #{result}, #{ip}, NOW())")
    void insert(OperationLog log);

    @Select("<script>" +
            "SELECT * FROM operation_log WHERE 1=1 " +
            "<if test='operationType != null and operationType != \"\"'> AND operation_type = #{operationType} </if>" +
            "<if test='targetType != null and targetType != \"\"'> AND target_type = #{targetType} </if>" +
            " ORDER BY operation_time DESC LIMIT 200" +
            "</script>")
    List<OperationLog> findList(String operationType, String targetType);
}
