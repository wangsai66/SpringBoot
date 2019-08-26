package com.shiro;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.auth.entity.AuthUser;

@Component
public class UserRealm extends AbstractUserRealm{

	
	/**
	 * 这里实现AbstractUserRealm 的接口 
	 * 暂不做  
	 * 目的为 获取用户组的权限信息
	 * Description: 
	 * All Rights Reserved.
	 * @param userInfo
	 * @return
	 * @version 1.0  2018年4月13日 下午12:00:39  by 王赛(wangsai@zhihuihutong.com)
	 */
	@Override
    public UserRolesAndPermissions doGetGroupAuthorizationInfo(AuthUser userInfo) {
        Set<String> userRoles = new HashSet<>();
        Set<String> userPermissions = new HashSet<>();
        //TODO 获取当前用户下拥有的所有角色列表,及权限
        return new UserRolesAndPermissions(userRoles, userPermissions);
    }

	
	/**
	 * 这里实现AbstractUserRealm 的接口 
	 * 暂不做  
	 * 获取用户角色的权限信息
	 * Description: 
	 * All Rights Reserved.
	 * @param userInfo
	 * @return
	 * @version 1.0  2018年4月13日 下午12:01:49  by 王赛(wangsai@zhihuihutong.com)
	 */
    @Override
    public UserRolesAndPermissions doGetRoleAuthorizationInfo(AuthUser userInfo) {
        Set<String> userRoles = new HashSet<>();
        Set<String> userPermissions = new HashSet<>();
        //TODO 获取当前用户下拥有的所有角色列表,及权限
        return new UserRolesAndPermissions(userRoles, userPermissions);
    }
}
