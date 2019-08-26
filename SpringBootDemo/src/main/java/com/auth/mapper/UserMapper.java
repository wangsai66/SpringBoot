package com.auth.mapper;

import com.auth.entity.AuthUser;
import com.datastax.driver.mapping.annotations.Param;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Application中添加了@MapperScan注解,可以扫描指定包下的所有Mapper接口
 * 相当于在所有的Mapper接口中添加了@Mapper注解,因此在Dao中是不需要任何注解就可以在Service层依赖注入的,
	这种情况实现依赖注入有二种方式
	1: 在Application中使用@MapperScan注解
	2: 在Mapper接口中使用@Mapper注解
	如果不使用第一种方式的话,只能使用@Mapper注解,
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2018年4月9日 下午4:56:59  by 王赛(wangsai@zhihuihutong.com)
 */
@Mapper  //声明一个mybatis的dao接口，会被spring boot扫描到
public interface  UserMapper {
	
	@Results({ 
		 @Result(property="userId",column="USER_ID"),
		 @Result(property="userName",column="USER_NAME"),
		 @Result(property="userSex",column="USER_SEX"),
		  @Result(property="userNumber",column="USER_NUMBER"),
		  @Result(property="userPassword",column="USER_PASSWORD")
	}) 
	@Select("SELECT * FROM tob_auth_user WHERE USER_NUMBER =#{userNumber}")
	public  List<AuthUser> findUserByPhone(@Param("userNumber") String userNumber);

	
	 @Insert("insert into tob_auth_user(USER_ID,USER_NUMBER) values(UUID(),#{userNumber})")
	 void insert(AuthUser user);
	 
	 @Delete("delete from  tob_auth_user WHERE USER_NUMBER = #{userNumber}")
	 void delete(@Param("userNumber") String userNumber);
	

	 @Update("update tob_auth_user set USER_NUMBER=#{userNumber}  where LOGIN_NAME=#{loginName}")
	 public void updateUser(AuthUser userr);

	 
	 @Results({ 
		 @Result(property="userId",column="USER_ID"),
		 @Result(property="userName",column="USER_NAME"),
		 @Result(property="loginName",column="LOGIN_NAME"),
		  @Result(property="userPassword",column="USER_PASSWORD")
	}) 
	@Select("SELECT * FROM tob_auth_user WHERE D=0 and  LOGIN_NAME =#{loginName}")
	public AuthUser findByLoginName(@Param("loginName") String loginName);

}
