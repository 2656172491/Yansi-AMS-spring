package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.PcAsset;

import java.util.List;

@Mapper
public interface PcAssetMapper {

    @Select("<script>" +
            "SELECT * FROM pc_asset WHERE deleted = 0 " +
            "<if test='computerNo != null and computerNo != \"\"'> AND computer_no LIKE CONCAT('%', #{computerNo}, '%') </if>" +
            "<if test='hostSn != null and hostSn != \"\"'> AND host_sn LIKE CONCAT('%', #{hostSn}, '%') </if>" +
            "<if test='monitorSn != null and monitorSn != \"\"'> AND monitor_sn LIKE CONCAT('%', #{monitorSn}, '%') </if>" +
            "<if test='department != null and department != \"\"'> AND department LIKE CONCAT('%', #{department}, '%') </if>" +
            "<if test='keeper != null and keeper != \"\"'> AND keeper LIKE CONCAT('%', #{keeper}, '%') </if>" +
            " ORDER BY updated_at DESC" +
            "</script>")
    List<PcAsset> findList(@Param("computerNo") String computerNo,
                           @Param("hostSn") String hostSn,
                           @Param("monitorSn") String monitorSn,
                           @Param("department") String department,
                           @Param("keeper") String keeper);

    @Select("SELECT * FROM pc_asset WHERE id = #{id} AND deleted = 0")
    PcAsset findById(@Param("id") Long id);

    @Insert("INSERT INTO pc_asset (computer_no, mac_address, department, keeper, monitor_sn, host_sn, remark, status, deleted, created_at, updated_at) " +
            "VALUES (#{computerNo}, #{macAddress}, #{department}, #{keeper}, #{monitorSn}, #{hostSn}, #{remark}, #{status}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PcAsset pcAsset);

    @Update("UPDATE pc_asset SET computer_no=#{computerNo}, mac_address=#{macAddress}, department=#{department}, " +
            "keeper=#{keeper}, monitor_sn=#{monitorSn}, host_sn=#{hostSn}, remark=#{remark}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(PcAsset pcAsset);

    @Update("UPDATE pc_asset SET deleted=1, updated_at=NOW() WHERE id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("UPDATE pc_asset SET last_inspection_user=#{user}, last_inspection_time=NOW(), updated_at=NOW() WHERE id=#{id} AND deleted=0")
    int updateInspectionInfo(@Param("id") Long id, @Param("user") String user);

    @Select("SELECT DISTINCT department FROM pc_asset WHERE deleted = 0 AND department IS NOT NULL AND department != ''")
    List<String> findDepartments();

    @Select("SELECT COUNT(*) FROM pc_asset WHERE deleted = 0")
    int count();
}
