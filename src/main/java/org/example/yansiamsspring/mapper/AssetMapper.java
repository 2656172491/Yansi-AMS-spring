package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.Asset;

import java.util.List;

@Mapper
public interface AssetMapper {

    @Select("SELECT * FROM asset WHERE deleted = 0 ORDER BY created_at DESC")
    List<Asset> findAll();

    @Select("SELECT * FROM asset WHERE id = #{id} AND deleted = 0")
    Asset findById(@Param("id") Long id);

    @Select("SELECT * FROM asset WHERE stock_status = #{stockStatus} AND asset_type = #{assetType} AND deleted = 0")
    List<Asset> findByStockStatusAndType(@Param("stockStatus") String stockStatus, @Param("assetType") String assetType);

    @Select("SELECT * FROM asset WHERE stock_status = 'in_stock' AND deleted = 0")
    List<Asset> findStockPool();

    @Select("SELECT * FROM asset WHERE stock_status = 'in_stock' AND asset_type = #{assetType} AND deleted = 0")
    List<Asset> findStockPoolByType(@Param("assetType") String assetType);

    @Insert("INSERT INTO asset (computer_no, internal_code, mac_address, department, keeper, monitor_sn, host_sn, remark, status, " +
            "asset_type, stock_status, purchase_time, purchase_batch, batch_id, deleted, created_at, updated_at) " +
            "VALUES (#{computerNo}, #{internalCode}, #{macAddress}, #{department}, #{keeper}, #{monitorSn}, #{hostSn}, #{remark}, #{status}, " +
            "#{assetType}, #{stockStatus}, #{purchaseTime}, #{purchaseBatch}, #{batchId}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Asset asset);

    @Update("UPDATE asset SET computer_no=#{computerNo}, mac_address=#{macAddress}, department=#{department}, " +
            "keeper=#{keeper}, monitor_sn=#{monitorSn}, host_sn=#{hostSn}, remark=#{remark}, status=#{status}, " +
            "asset_type=#{assetType}, stock_status=#{stockStatus}, updated_at=NOW() WHERE id=#{id}")
    int update(Asset asset);

    @Update("UPDATE asset SET stock_status=#{stockStatus}, department=#{department}, keeper=#{keeper}, updated_at=NOW() WHERE id=#{id}")
    int updateStockStatus(@Param("id") Long id, @Param("stockStatus") String stockStatus,
                          @Param("department") String department, @Param("keeper") String keeper);

    @Update("UPDATE asset SET deleted=1, updated_at=NOW() WHERE id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("UPDATE asset SET last_inspection_user=#{user}, last_inspection_time=NOW(), last_inspection_batch=#{batch}, updated_at=NOW() WHERE id=#{id} AND deleted=0")
    int updateInspectionInfo(@Param("id") Long id, @Param("user") String user, @Param("batch") String batch);

    @Select("SELECT DISTINCT department FROM asset WHERE deleted = 0 AND department IS NOT NULL AND department != ''")
    List<String> findDepartments();

    @Select("SELECT asset_type, stock_status, COUNT(*) as cnt FROM asset WHERE deleted = 0 GROUP BY asset_type, stock_status")
    List<java.util.Map<String, Object>> countByTypeAndStatus();

    @Select("SELECT department, COUNT(*) as cnt FROM asset WHERE deleted = 0 AND department IS NOT NULL AND department != '' GROUP BY department ORDER BY cnt DESC LIMIT 10")
    List<java.util.Map<String, Object>> countByDepartment();

    @Select("SELECT asset_type, purchase_batch, MIN(created_at) as created_at, COUNT(*) as cnt FROM asset WHERE deleted = 0 AND stock_status = 'in_stock' GROUP BY asset_type, purchase_batch ORDER BY MIN(created_at) DESC")
    List<java.util.Map<String, Object>> countByBatch();

    @Select("SELECT * FROM asset WHERE deleted = 0 AND stock_status = 'in_stock' AND asset_type = #{assetType} AND purchase_batch = #{batch}")
    List<Asset> findStockByBatch(@Param("assetType") String assetType, @Param("batch") String batch);

    @Select("SELECT MAX(CAST(SUBSTRING(computer_no, 5) AS UNSIGNED)) FROM asset WHERE computer_no LIKE 'ASM-%'")
    Long getMaxComputerNo();

    @Select("SELECT COUNT(*) FROM asset WHERE deleted = 0 AND host_sn = #{sn}")
    int countByHostSn(@Param("sn") String sn);

    @Select("SELECT COUNT(*) FROM asset WHERE deleted = 0 AND monitor_sn = #{sn}")
    int countByMonitorSn(@Param("sn") String sn);

    // 统一列表查询（支持多条件筛选）
    @Select("<script>" +
            "SELECT * FROM asset WHERE deleted = 0 " +
            "<if test='assetType != null and assetType != \"\"'> AND asset_type = #{assetType} </if>" +
            "<if test='stockStatus != null and stockStatus != \"\"'> AND stock_status = #{stockStatus} </if>" +
            "<if test='batchId != null'> AND batch_id = #{batchId} </if>" +
            "<if test='department != null and department != \"\"'> AND department = #{department} </if>" +
            "<if test='keeper != null and keeper != \"\"'> AND keeper = #{keeper} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            " ORDER BY created_at DESC" +
            "</script>")
    List<Asset> findWithFilters(@Param("assetType") String assetType,
                                @Param("stockStatus") String stockStatus,
                                @Param("batchId") Long batchId,
                                @Param("department") String department,
                                @Param("keeper") String keeper,
                                @Param("status") Integer status);

    @Update("UPDATE asset SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") int status);

    @Update("UPDATE asset SET department=#{department}, keeper=#{keeper}, updated_at=NOW() WHERE id=#{id}")
    int updateKeeper(@Param("id") Long id, @Param("department") String department, @Param("keeper") String keeper);

    @Select("SELECT * FROM asset WHERE deleted = 0 AND batch_id = #{batchId} ORDER BY created_at ASC")
    List<Asset> findByBatchId(@Param("batchId") Long batchId);

    @Select("SELECT asset_type, COUNT(*) as cnt FROM asset WHERE deleted = 0 GROUP BY asset_type")
    List<java.util.Map<String, Object>> countByType();

    @Select("SELECT MAX(CAST(SUBSTRING(internal_code, #{prefixLen}+1) AS UNSIGNED)) FROM asset WHERE internal_code LIKE CONCAT(#{prefix}, '-%')")
    Long getMaxInternalCode(@Param("prefix") String prefix, @Param("prefixLen") int prefixLen);
}
