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

    @Insert("INSERT INTO asset (computer_no, mac_address, department, keeper, monitor_sn, host_sn, remark, status, " +
            "asset_type, stock_status, purchase_time, purchase_batch, deleted, created_at, updated_at) " +
            "VALUES (#{computerNo}, #{macAddress}, #{department}, #{keeper}, #{monitorSn}, #{hostSn}, #{remark}, #{status}, " +
            "#{assetType}, #{stockStatus}, #{purchaseTime}, #{purchaseBatch}, 0, NOW(), NOW())")
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
}
