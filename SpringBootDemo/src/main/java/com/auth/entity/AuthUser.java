package com.auth.entity;

import java.io.Serializable;

import com.terran4j.commons.api2doc.annotations.ApiComment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 
 * <br>
 * <b>功能：</b>AuthUserEntity<br>
 */
/**
 * Lombok主要注解
	@Getter and @Setter / 自动为属性提供 Set和Get 方法
	@ToString / 该注解的作用是为类自动生成toString()方法
	@EqualsAndHashCode / 为对象字段自动生成hashCode和equals实现
	@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor / 顾名思义，为类自动生成对应参数的constructor
	@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog / 自动为类添加对应的log支持
	@Data / 自动为所有字段添加@ToString, @EqualsAndHashCode, @Getter，为非final字段添加@Setter,和@RequiredArgsConstructor，本质上相当于几个注解的综合效果
	@NonNull / 自动帮助我们避免空指针。作用在方法参数上的注解，用于自动生成空值参数检查
	@Cleanup / 自动帮我们调用close()方法。作用在局部变量上，在作用域结束时会自动调用close方法释放资源
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2018年4月10日 上午9:53:03  by 王赛(wangsai@zhihuihutong.com)
 */
@Setter
@Getter
@Data
@Accessors(chain = true)
public class AuthUser  implements Serializable {
	 @ApiModelProperty(value = "用户ID", required = true)
	private java.lang.String userId;//   用户ID
	
	 @ApiModelProperty(value = "用户编号", required = true)
	private java.lang.String userNumber;//   用户编号
	
	 @ApiModelProperty(value = "登录名", required = true)
	private java.lang.String loginName;//   登录名
	
	 @ApiModelProperty(value = "密码", required = true)
	private java.lang.String userPassword;//   密码
	
	 @ApiModelProperty(value = "用户名", required = true)
	private java.lang.String userName;//   用户名
	
	 @ApiModelProperty(value = "  用户性别  ", required = true)
	private java.lang.Integer userSex;//   用户性别  
	
	 @ApiModelProperty(value = "车场id", required = true)
	private String  parkId;
	
	 @ApiModelProperty(value = "当前页", required = true)
	private Integer count;
	

/*	public AuthUser(String parkId) {
		this.parkId = parkId;
	}*/
}

