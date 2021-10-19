package com.pj.project4sp.role;

import cn.dev33.satoken.stp.StpUtil;
import com.pj.current.satoken.AuthConst;
import com.pj.project4sp.SP;
import com.pj.project4sp.user.SpUser;
import com.pj.utils.sg.AjaxError;
import com.pj.utils.sg.AjaxJson;
import com.pj.utils.sg.SoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller: 系统角色表
 * @author Runtian
 */
@RestController
@RequestMapping("/role/")
public class SpRoleController {

	/** 底层Mapper依赖 */
	@Resource
	SpRoleMapper spRoleMapper;

	// 增
	@RequestMapping("add")
	@Transactional(rollbackFor = Exception.class)
	AjaxJson add(SpRole s, HttpServletRequest request){
		StpUtil.checkPermission(AuthConst.p_role_list);	// 鉴权
		// 检验
		if(spRoleMapper.selectById(s.getId()) != null) {
			return AjaxJson.getError("此id已存在，请更换");
		}
		SpRoleUtil.checkRoleThrow(s);
		int line = spRoleMapper.insert(s);
		AjaxError.throwByLine(line, "添加失败");
		// 返回这个对象 
		long id = s.getId();
		if(id == 0) {
			id = SP.publicMapper.getPrimarykey();
		}
		return AjaxJson.getSuccessData(spRoleMapper.selectById(id));
	}

	// 删  
	@RequestMapping("delete")
	AjaxJson delete(long id, HttpServletRequest request){
		StpUtil.checkPermission(AuthConst.r1);	// 鉴权
		StpUtil.checkPermission(AuthConst.p_role_list);	// 鉴权
		int line = spRoleMapper.deleteById(id);
		return AjaxJson.getByLine(line);
	}

	// 改  
	@RequestMapping("update")
	AjaxJson update(SpRole s){
		StpUtil.checkPermission(AuthConst.r1);	// 鉴权
		StpUtil.checkPermission(AuthConst.p_role_list);	// 鉴权
		SpRoleUtil.checkRoleThrow(s);
		int line = spRoleMapper.updateById(s);
		return AjaxJson.getByLine(line);
	}

	// 查  
	@RequestMapping("getById")
	AjaxJson getById(long id){
		StpUtil.checkPermission(AuthConst.r99);	// 鉴权
		Object data = spRoleMapper.selectById(id);
		return AjaxJson.getSuccess("ok").setData(data);
	}

	// 查 - 集合（参数为null或0时默认忽略此条件）  
	@RequestMapping("getList")
	AjaxJson getList(){
		StpUtil.checkPermission(AuthConst.r99);	// 鉴权
		SoMap so = SoMap.getRequestSoMap();
		List<SpRole> list = spRoleMapper.selectByMap(so);
		return AjaxJson.getSuccessData(list);
	}


	
	

}
