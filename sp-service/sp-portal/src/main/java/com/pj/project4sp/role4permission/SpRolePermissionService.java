package com.pj.project4sp.role4permission;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色权限中间表 
 * @author Runtian
 *
 */
@Service
public class SpRolePermissionService {

	
	@Resource
	SpRolePermissionMapper spRolePermissionMapper;
	
	
	/**
	 * 获取指定角色的所有权限码 
	 */
    @Cacheable(value="api_pcode_list", key="#roleId")	// @增加缓存
    public List<String> getPcodeByRid(long roleId){
		return spRolePermissionMapper.selectList(Wrappers.<SpRolePermission>query().lambda()
						.select(SpRolePermission::getPermissionCode)
						.eq(SpRolePermission::getRoleId, roleId))
				.stream().map(SpRolePermission::getPermissionCode)
				.collect(Collectors.toList());
    }

	/**
	 * 获取指定角色的所有权限码 (Object类型) 
	 */
    @Cacheable(value="api_pcode_list2", key="#roleId")	// @增加缓存
    public List<Object> getPcodeByRid2(long roleId){
    	return spRolePermissionMapper.selectList(Wrappers.<SpRolePermission>query().lambda()
				.select(SpRolePermission::getPermissionCode)
				.eq(SpRolePermission::getRoleId, roleId))
				.stream().map(SpRolePermission::getPermissionCode)
				.map(String::valueOf)
				.collect(Collectors.toList());
//		List<String> codeList = spRolePermissionMapper.getPcodeByRoleId(role_id);					// 所有权限id
//		return codeList.stream().map(String::valueOf).collect(Collectors.toList());				// 转Object
    }

    /**
     * [T] 修改角色的一组权限关系
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value= {"api_pcode_list", "api_pcode_list2"}, key="#role_id")	// @清除缓存 
    public int updateRoleMenu(long role_id, String[] pcodeArray){

    	// 万一为空 
    	if(pcodeArray == null){
    		pcodeArray = new String[0];
    	}
    	
    	// 先删
		spRolePermissionMapper.delete(Wrappers.<SpRolePermission>query().lambda()
				.eq(SpRolePermission::getRoleId, role_id));

    	// 再添加
    	for(String pcode : pcodeArray){
			SpRolePermission spRolePermission = new SpRolePermission();
			spRolePermission.setRoleId(role_id);
			spRolePermission.setPermissionCode(pcode);
			spRolePermissionMapper.insert(spRolePermission);
        }
    	
    	// 返回
        return pcodeArray.length;
    }
	
	
	
}
