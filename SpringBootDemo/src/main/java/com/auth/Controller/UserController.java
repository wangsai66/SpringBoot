package com.auth.Controller;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tools.ant.taskdefs.Length;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aliyun.openservices.shade.io.netty.util.internal.StringUtil;
import com.auth.entity.AuthUser;
import com.auth.entity.FileVo;
import com.auth.entity.MonitiorYdpVo;
import com.auth.service.UserService;
import com.terran4j.commons.api2doc.annotations.Api2Doc;
import com.terran4j.commons.api2doc.annotations.ApiComment;
import com.terran4j.commons.api2doc.annotations.ApiError;
import com.test.CpuTets;
import com.test.CsvTest;
import com.test.ExcelTest;
import com.util.CsvExportUtil;
import com.util.HttpClientUtils;
import com.util.ResultUtil;
import com.util.StringUtils;
import com.util.TransformForYdp;
import com.util.YdpCode;

import lombok.extern.log4j.Log4j;
import net.sf.json.JSONArray;

@Api2Doc(id = "user", name = "用户接口", order = 1)
@ApiComment(seeClass = AuthUser.class)
@Log4j
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired  
	private UserService userService;  
	
	/**
	 * 查询方法
	 * Description: 
	 * All Rights Reserved.
	 * @param model
	 * @param 
	 * 
	 * 因为配置文件继承了CachingConfigurerSupportCachingConfigurerSupport，所以没有指定key的话就是用默认KEY生成策略生成,这里指定了KEY
	 * value属性指定Cache名称
	 * 使用key属性自定义key
	 * condition属性指定发生的条件(如上示例表示只有当user的id为偶数时才会进行缓存)
	 * @return
	 * @version 1.0  2018年4月9日 下午4:47:02  by 王赛(wangsai@zhihuihutong.com)
	 */
	@Api2Doc(order = 1)
    @ApiComment("根据用户编号查询该用户")
	@RequestMapping(value={"/selectUserByName"}, method=RequestMethod.POST)
    public List<AuthUser>  selectUserByName(Model model,  @ApiComment(value="用户编号") @RequestParam String Number){
        ResultUtil r = new ResultUtil();
		String result = "";
		List<AuthUser>  userList =new ArrayList<>();
		try {
			log.info("========开始时间"+new Date());
	    	userList= userService.findUserByPhone(Number);
	    /*	if(CollectionUtils.isNotEmpty(userList)){
	    		 model.addAttribute("userList",userList); 
	    		 return "list";
	    	}*/
	        log.info("========结束时间"+new Date());
			result = r.success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = r.error(e.getMessage());
		}
       return userList;
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/ydpInterFace",method=RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ydpInterFace(@RequestParam String param) {
		ResultUtil r = new ResultUtil();
		String result = "";
		try {
			TransformForYdp<Map>  p = TransformForYdp.fromJson(param, Map.class);
			Map<String, Object> map= (Map<String, Object>) p.getData();
			JSONArray json = JSONArray.fromObject(map.get("params"));
			String string = json.toString();
			String xin =string.replace("[", "");
			String s = xin.substring(0, xin.length()-1);
			String string2 = map.get("id").toString();
			log.info("====="+string);
			log.info("====="+s);
			log.info("====="+string2);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = r.error(e.getMessage());
		}
		return result;
	}
	
	
	
    /**
     * 
     * 下述操作为初始操作 ，仍有可能数据库和缓存存在不一致现象，还有待优化
     * 具体解决方案
     * redis系列之数据库与缓存数据一致性解决方案
     * https://blog.csdn.net/simba_1986/article/details/77823309
     * Description: 
     * All Rights Reserved.
     * @param userNumber
     * @version 1.0  2018年4月11日 下午3:07:44  by 王赛(wangsai@zhihuihutong.com)
     */
	@RequestMapping(value={"/updateByName"}, method=RequestMethod.GET)
    public void updateByName(String userNumber){
	/*	AuthUser auth=new AuthUser();
		userService.updateByName(auth);*/
    }
	
    

    
	@RequestMapping(value="/create", method = RequestMethod.POST)
    public void send(){
    	try {  
            Map<String,String> uploadParams = new LinkedHashMap<String, String>();  
            uploadParams.put("jhdas", "image");  
            String tempFileName = "C:/Users/wangsai/Desktop/play001.lst";  
            HttpClientUtils.getInstance().uploadFileImpl(  
                    "http://10.100.16.90:8080/nuowa/sendFile",tempFileName,  
                    "userImage", uploadParams);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
	
	@RequestMapping(value={"/test"}, method=RequestMethod.GET)
	public void test (HttpServletResponse response){
		// 查询需要导出的数据
	    List<FileVo> dataList = new ArrayList<>();
	    // 构造导出数据结构
	    String titles = "组号,组名";  // 设置表头
	    String keys = "no,name";  // 设置每列字段
	    // 构造导出数据
	    List<Map<String, Object>> datas = new ArrayList<>();
	    Map<String, Object> map = null;
	    for (int i = 0; i < 100000; i++) {
	    	   map = new HashMap<>();
	    	   map.put("no", i);
		       map.put("name", UUID.randomUUID().toString());
		       datas.add(map);
		}
	    // 设置导出文件前缀
	    String fName = "data_";
	    // 文件导出
	    try {
	        OutputStream os = response.getOutputStream();
	        CsvExportUtil.responseSetProperties(fName, response);
	        CsvExportUtil.doExport(datas, titles, keys, os);
	        os.close();
	    } catch (Exception e) {
	    	Log.info("csv导出失败====="+e);
	    }
	}
	
	
	@RequestMapping(value="/cvsTest", method = RequestMethod.GET)
    public void cvsTest(HttpServletResponse response){
		 //拼接数据start
        List<List<String>> datas = new ArrayList<List<String>>();
        String rows[] = {"year","month"};
        List<String> rowsTitle  = Arrays.asList(rows);
        datas.add(rowsTitle);
        for(int i = 0; i< 1000000 ;i++){
            String [] rowss = {"1","2"};
            List<String> rowssList = Arrays.asList(rowss);
            datas.add(rowssList);
        }
		/*List<Map<String, Object>> datas = new ArrayList<>();
	    Map<String, Object> map = null;
	    for (int i = 0; i < 1000000; i++) {
	    	   map = new HashMap<>();
	    	   map.put("no", i);
		       map.put("name", UUID.randomUUID().toString());
		       datas.add(map);
		}*/
		ExecutorService exec = Executors.newFixedThreadPool(1);
	    exec.execute(new ExcelTest(response,datas));
	    exec.shutdown();
		long start =System.currentTimeMillis();
	        while (true) {
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            System.out.println(CpuTets.getInstance().getProcessCpu());
	            if(exec.isTerminated()){
	            	 long end=System.currentTimeMillis();
	            	 long time = end-start;
	            	log.info("所用时间===="+time);
	            	break;
	            }
	        }
    }
}
