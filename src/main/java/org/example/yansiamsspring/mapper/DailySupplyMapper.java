package org.example.yansiamsspring.mapper;

import org.apache.ibatis.annotations.*;
import org.example.yansiamsspring.pojo.DailySupply;

import java.util.List;

@Mapper
public interface DailySupplyMapper {

    @Select("SELECT * FROM daily_supply WHERE deleted = 0 ORDER BY id ASC")
    List<DailySupply> findAll();

    @Select("SELECT * FROM daily_supply WHERE id = #{id} AND deleted = 0")
    DailySupply findById(@Param("id") Long id);

    @Insert("INSERT INTO daily_supply (supply_type, quantity, warning_quantity, status) VALUES (#{supplyType}, #{quantity}, #{warningQuantity}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DailySupply supply);

    @Update("UPDATE daily_supply SET supply_type=#{supplyType}, quantity=#{quantity}, warning_quantity=#{warningQuantity}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(DailySupply supply);

    @Update("UPDATE daily_supply SET deleted=1, updated_at=NOW() WHERE id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("UPDATE daily_supply SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
