package com.swagger2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth.entity.AuthUser;
import com.auth.service.UserService;
import com.base.CommonResult;
import com.base.base.param.BaseRequestParam;
import com.base.base.param.DataType;
import com.terran4j.commons.api2doc.annotations.ApiComment;
import com.util.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/swagger2")
@Api(value = "video API", tags = "video", description = "视频相关接口")
public class Swagger2Test {
	
	
	@Autowired  
	private UserService userService; 
	
	@ApiOperation(value="查询用户信息", notes="根据编号查询用户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Number", value = "用户编号", required = true, dataType = "String", paramType = "path")
	})
	@RequestMapping(value={"/selectUserByName"}, method=RequestMethod.POST)
    public CommonResult<AuthUser> selectUserByName(@RequestParam(name = "Number")  String Number){
		CommonResult<AuthUser> r = new CommonResult();
		List<AuthUser>  userList =new ArrayList<>();
		try {
			log.info("========开始时间"+new Date());
	    	userList= userService.findUserByPhone(Number);
	        log.info("========结束时间"+new Date());
			r.ok(userList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			r.error();
		}
       return r;
    }
	
	
	
   @ApiOperation("用户查看")
    @RequestMapping(value="/get",method=RequestMethod.POST)
    @ResponseBody
    public CommonResult<AuthUser> userGet(@RequestBody @DataType(AuthUser.class) BaseRequestParam<AuthUser> param){
        return CommonResult.ok(new AuthUser());
    }

}
