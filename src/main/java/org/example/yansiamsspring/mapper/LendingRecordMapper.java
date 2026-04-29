package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.LendingRecord;

import java.util.List;

@Mapper
public interface LendingRecordMapper {

    @Select("<script>" +
            "SELECT * FROM lending_record WHERE 1=1 " +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            " ORDER BY created_at DESC" +
            "</script>")
    List<LendingRecord> findList(@Param("status") String status);

    @Select("SELECT * FROM lending_record WHERE id = #{id}")
    LendingRecord findById(@Param("id") Long id);

    @Select("SELECT * FROM lending_record WHERE status = 'lent' AND expected_return < CURDATE()")
    List<LendingRecord> findOverdue();

    @Insert("INSERT INTO lending_record (asset_id, borrower, borrower_dept, lend_time, expected_return, " +
            "handler, handler_id, status, remark, created_at, updated_at) " +
            "VALUES (#{assetId}, #{borrower}, #{borrowerDept}, #{lendTime}, #{expectedReturn}, " +
            "#{handler}, #{handlerId}, #{status}, #{remark}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LendingRecord record);

    @Update("UPDATE lending_record SET status='returned', actual_return=NOW(), updated_at=NOW() WHERE id=#{id}")
    int returnDevice(@Param("id") Long id);

    @Select("SELECT DATE_FORMAT(lend_time, '%Y-%m') as month, COUNT(*) as cnt FROM lending_record GROUP BY month ORDER BY month DESC LIMIT 12")
    List<java.util.Map<String, Object>> countByMonth();
}
