package com.shiro;

import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.auth.entity.AuthUser;
import com.auth.service.UserService;

import lombok.extern.log4j.Log4j;

/**
 * 注解释义
 * 
 * 
 *  @RequiresAuthentication  
	表示当前Subject已经通过login进行了身份验证；即Subject. isAuthenticated()返回true。 
	
	@RequiresUser  
	表示当前Subject已经身份验证或者通过记住我登录的。
	
	@RequiresGuest  
	表示当前Subject没有身份验证或通过记住我登录过，即是游客身份。
	  
	@RequiresRoles(value={“admin”, “user”}, logical= Logical.AND)  
	表示当前Subject需要角色admin和user。
	
	@RequiresPermissions (value={“user:a”, “user:b”}, logical= Logical.OR)  
	表示当前Subject需要权限user:a或user:b。  
	
	配置完shiro后可根据具体的业务需求在方法上添加该注解，以实现具体的业务需求
 * 
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2018年4月13日 上午11:39:03  by 王赛(wangsai@zhihuihutong.com)
 */
/*@Log4j
@Controller
@RequestMapping("/login")*/
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    
    /**
     * 登录方法
     * Description: 
     * All Rights Reserved.
     * @param user
     * @param bindingResult
     * @param redirectAttributes
     * @return
     * @version 1.0  2018年4月13日 下午4:52:44  by 王赛(wangsai@zhihuihutong.com)
     */
	@RequestMapping(value={"/loginUser"}, method=RequestMethod.POST)
    public String loginUser(@Valid AuthUser user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		 // 获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        // 测试当前的用户是否已经被认证，即是否已经登陆
        // 调用Subject的isAuthenticated
        if (!currentUser.isAuthenticated()) {
            // 把用户名和密码封装为UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getUserPassword());
            token.setRememberMe(true);
            try {
                // 执行登陆
                currentUser.login(token);
            } catch (AuthenticationException ae) {
                System.out.println("登录失败--->" + ae.getMessage());
                return "error";
            }
        }
        return "success";
    }

	
	/**
	 * 退出登录
	 * Description: 
	 * All Rights Reserved.
	 * @param redirectAttributes
	 * @return
	 * @version 1.0  2018年4月13日 下午5:01:26  by 王赛(wangsai@zhihuihutong.com)
	 */
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/login";
    }


   /* @GetMapping("/reg")
    @ResponseBody
    public Result<String> reg(@Valid AuthUser user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error("用户信息填写不完整");
        }
        userService.save(user);
        return Result.ok();
    }
	*/
	
	
}
