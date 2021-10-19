package com.pj.project4sp.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pj.project4sp.role4permission.SpRolePermission;
import com.pj.utils.sg.SoMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Mapper: 系统角色表
 * @author Runtian
 */
@Mapper
public interface SpRoleMapper extends BaseMapper<SpRole> {
}
