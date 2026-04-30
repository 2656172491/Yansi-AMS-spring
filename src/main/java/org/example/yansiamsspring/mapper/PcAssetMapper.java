package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.PcAsset;

import java.util.List;

@Mapper
public interface PcAssetMapper {

    String JOIN_COLUMNS = "p.*, hs.sn as host_sn, ms.sn as monitor_sn";
    String JOIN_FROM = "pc_asset p LEFT JOIN host_stock hs ON p.host_id = hs.id LEFT JOIN monitor_stock ms ON p.monitor_id = ms.id";

    @Select("<script>" +
            "SELECT " + JOIN_COLUMNS + " FROM " + JOIN_FROM + " WHERE p.deleted = 0 " +
            "<if test='computerNo != null and computerNo != \"\"'> AND p.computer_no LIKE CONCAT('%', #{computerNo}, '%') </if>" +
            "<if test='hostSn != null and hostSn != \"\"'> AND hs.sn LIKE CONCAT('%', #{hostSn}, '%') </if>" +
            "<if test='monitorSn != null and monitorSn != \"\"'> AND ms.sn LIKE CONCAT('%', #{monitorSn}, '%') </if>" +
            "<if test='department != null and department != \"\"'> AND p.department LIKE CONCAT('%', #{department}, '%') </if>" +
            "<if test='keeper != null and keeper != \"\"'> AND p.keeper LIKE CONCAT('%', #{keeper}, '%') </if>" +
            " ORDER BY p.updated_at DESC" +
            "</script>")
    List<PcAsset> findList(@Param("computerNo") String computerNo,
                           @Param("hostSn") String hostSn,
                           @Param("monitorSn") String monitorSn,
                           @Param("department") String department,
                           @Param("keeper") String keeper);

    @Select("SELECT " + JOIN_COLUMNS + " FROM " + JOIN_FROM + " WHERE p.id = #{id} AND p.deleted = 0")
    PcAsset findById(@Param("id") Long id);

    @Insert("INSERT INTO pc_asset (computer_no, host_id, monitor_id, mac_address, department, keeper, remark, status, deleted, created_at, updated_at) " +
            "VALUES (#{computerNo}, #{hostId}, #{monitorId}, #{macAddress}, #{department}, #{keeper}, #{remark}, #{status}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PcAsset pcAsset);

    @Update("UPDATE pc_asset SET computer_no=#{computerNo}, host_id=#{hostId}, monitor_id=#{monitorId}, " +
            "mac_address=#{macAddress}, department=#{department}, keeper=#{keeper}, remark=#{remark}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(PcAsset pcAsset);

    @Update("UPDATE pc_asset SET deleted=1, updated_at=NOW() WHERE id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("UPDATE pc_asset SET last_inspection_user=#{user}, last_inspection_time=NOW(), updated_at=NOW() WHERE id=#{id} AND deleted=0")
    int updateInspectionInfo(@Param("id") Long id, @Param("user") String user);

    @Select("SELECT DISTINCT p.department FROM pc_asset p WHERE p.deleted = 0 AND p.department IS NOT NULL AND p.department != ''")
    List<String> findDepartments();

    @Select("SELECT COUNT(*) FROM pc_asset WHERE deleted = 0")
    int count();

    @Select("SELECT host_id FROM pc_asset WHERE id = #{id} AND deleted = 0")
    Long findHostIdById(@Param("id") Long id);

    @Select("SELECT monitor_id FROM pc_asset WHERE id = #{id} AND deleted = 0")
    Long findMonitorIdById(@Param("id") Long id);
}
