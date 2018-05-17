/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;
import com.childcare.entity.Account;
import com.childcare.entity.DemoClass;
import com.childcare.entity.Family;
import com.childcare.entity.PasswdChanger;
import com.childcare.entity.MailandPsw;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.wrapper.NewFamilyWrapper;
import com.childcare.entity.wrapper.ReadWrapper;
import com.childcare.entity.wrapper.UpdateWrapper;
import com.childcare.model.service.serviceAccount;
import com.childcare.model.service.serviceFamily;
import com.childcare.model.support.JsonWebTokenUtil;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author New User
 */
@RestController
@RequestMapping(value = "/account")
public class AccountController {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    @Resource
    private serviceAccount service;
    @Resource
    private serviceFamily serviceF;
    
    public static final int STATUS_MEMBER = 0;
    public static final int STATUS_OWNER = 1;
    
    public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
    
    @ResponseBody
    @RequestMapping(value = "/join_family", method = POST, produces = { APPLICATION_JSON_VALUE})
    public Object joinFamily(@RequestBody Map<String,String> map) {
         if(map.containsKey("uid")&map.containsKey("fid")&map.containsKey("access")&map.containsKey("status")) 
         {
             try{
             int uid = Integer.parseInt(map.get("uid"));
             int fid = Integer.parseInt(map.get("fid"));
             int status = Integer.parseInt(map.get("status"));
             if (status<=0 | status>99)
                 return new Response(Response.ERROR_FAMILY_STATUS,"status is out of boundary");
             return serviceF.addRelationship(fid, uid, status, map.get("access"));
             }
             catch (NumberFormatException e)
             {
                 return new Response(e);
             }
         }
         else
            return new Response(Response.ERROR_NULL_FIELD,"Key 'uid' 'access' 'status' and 'fid' expected. ");
    }
    
    @ResponseBody
    @RequestMapping(value = "/update_status", method = POST, produces = { APPLICATION_JSON_VALUE })
    public Object editRelationship(@RequestBody Map<String,String> map)
    {
         if(map.containsKey("uid")&map.containsKey("fid")&map.containsKey("access")&map.containsKey("status")&map.containsKey("key_uid")) 
         {
             try{
             int uid = Integer.parseInt(map.get("uid"));//target uid
             int fid = Integer.parseInt(map.get("fid"));
             int status = Integer.parseInt(map.get("status"));
             int host = Integer.parseInt(map.get("key_uid"));//who is sending request
             if (status<=0 | status>99)
                 return new Response(Response.ERROR_FAMILY_STATUS,"status is out of boundary");
             return serviceF.editRelationship(fid, uid, status, map.get("access"),host);
             }
             catch (NumberFormatException e)
             {
                 return new Response(e);
             }
         }
         else
            return new Response(Response.ERROR_NULL_FIELD,"Key 'uid' 'access' 'status' and 'fid' expected. ");
    }
    
    @ResponseBody
    @RequestMapping(value = "/create_family", method = POST)
    public Object createFamily(@RequestBody NewFamilyWrapper wrapper) {
        return this.serviceF.createFamily(wrapper.getUid(), wrapper.getFamily(),wrapper.getToken());
     }
    
  @ResponseBody
    @RequestMapping(value = "/my_families", method = POST)
    public Object fetchFamily(@RequestBody Map<String,String> map) {
        if(map.containsKey("uid")&map.containsKey("access")) 
         {
             try{
            int uid = Integer.parseInt(map.get("uid"));
            return this.serviceF.getMyFamily(uid,map.get("access"));
             } catch (NumberFormatException e)
             {
                 return new Response(e);
             }
         }
         else
            return new Response(Response.ERROR_NULL_FIELD,"Key 'uid' and 'access' expected. ");

     }
    
    
    //--------------------------
    
    @ResponseBody
    @RequestMapping(value = "/demo/{id}", method = GET, produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Account reply(@PathVariable(value = "id")long id) {
        return jdbcDataDAO.getDaoAccount().getAccountInstance(id);
     }
    
    /*
    {
    "pin": "",
    "firstName": "john",
    "lastName": "LastName",
    "accountFamilyCollection": null,
    "familyCollection": null,
    "uid": 21,
    "password": "123",
    "email": "asd@asd",
    "phone": "123456",
    "discard": false,
    "anchorgroupCollection": null
    }
    */
    
   /**
    * Register a new account
    * @param account Auto injected from request body
    * @return a string as UID for created account, or error message starting with "FAIL: " if there is an error during transaction.
    */
    @ResponseBody
    @RequestMapping(value = "/register", method = POST)
    public Object register(@RequestBody Account account) {
        return this.service.createAccount(account);
     }
    
    /**
     * 登陆
     * @param account
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/signin", method = POST)
    public Object signIn(@RequestBody MailandPsw account) {
        return this.service.signIn(account);
     }
    
    /**
     * 使用PIN重置密码
     * @param pc
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/reset", method = POST)
    public Object reset(@RequestBody PasswdChanger pc) {
        return this.service.forgetPassword(pc);
    }
    
    /**
     * 通过邮箱重置密码，用户提交新密码和邮箱，服务器返回一个token到对应邮箱
     * @param map
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/resetByMail", method = POST)
    public Object resetByMail(@RequestBody Map<String,String> map) {
        return this.service.forgetPasswordViaMail(map);
    }
    
    /**
     * (邮箱重置密码)返回重置密码页面，用户在页面内提交拿到的token
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/portal", method = GET)
    public ModelAndView portal() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("upload");
        return mav;
         //reply with a jsp
    }
    
    /**
     * 验证提交的token，返回重置结果
     * @param token
     * @return 
     */
    @RequestMapping(value = "/auth", method = POST)
    public ModelAndView auth(String token) {
        return this.service.react2Mail(token);
        //response to jsp
    }
    
    /**
     * 刷新refresh token，返回新刷新令牌和新access token
     * @param rt
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/refresh", method = POST)
    public Object refresh(@RequestBody Map<String,String> rt) {
        return this.service.refreshToken(rt);
    }
    
    @ResponseBody
    @RequestMapping(value = "/Update", method = POST , produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Object updateAccount(@RequestBody UpdateWrapper wrapper)
    { 
            try{
          //  Account account = (Account)map.get("account");
           // String token = (String)map.get("accessToken");
            return this.service.editAccount(wrapper.getAccount(),wrapper.getAccessToken());
            }
            catch (ClassCastException e)
            {
                return new Response(Response.EXCEPTION_CLASSCAST,"Cannot cast input account data or token into corresponding instance. Please check your syntax.");
            }
        }

    
   
    @ResponseBody
    @RequestMapping(value = "/read", method = POST , produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Object getAccount(@RequestBody ReadWrapper wrapper)
    {    
            return this.service.getAccountDetail(wrapper.getUID(), wrapper.getAccess());
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/Password", method = POST , produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Object ChangePassword(@RequestBody PasswdChanger psd)
    {
        return this.service.changePassword(psd);
    }
    
    
      @ResponseBody
    @RequestMapping(value = "/d", method = GET , produces = { APPLICATION_JSON_VALUE})
    public Object PSDDemo()
    {
        PasswdChanger psd = new PasswdChanger();
        psd.setUid(2);
        psd.setOldPasswd("oldpsd");
        psd.setNewPasswd("newPsd");
        Map<String,Object> map = new HashMap();
         Account account = new Account();
        map.put("account", account);
        map.put("accessToken","123456");
        Family f = new Family();
        f.setMaxCluster(4);
        f.setFamilyName("Dire");
        f.setFamilyPassword("fpasswd");
        //return new NewFamilyWrapper(f,23,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVSUQiOjIzLCJUeXBlIjoiQWNjZXNzIiwiZXhwIjoxNTIzNjE2Njk3LCJUVEwiOjI0LCJpYXQiOjE1MjM1MzAyOTd9.wHwjU0SLq_j_Mg_meJx0yT7ncau59bRTL1WU3uL7lfE");
        Map<String,String> m = new HashMap();
        m.put("uid","23");
        m.put("access","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVSUQiOjIzLCJUeXBlIjoiQWNjZXNzIiwiZXhwIjoxNTIzNjE2Njk3LCJUVEwiOjI0LCJpYXQiOjE1MjM1MzAyOTd9.wHwjU0SLq_j_Mg_meJx0yT7ncau59bRTL1WU3uL7lfE");
        return m;
    
    
    }
         
       
     //-----------
    /**
     * Try to move one account out of effect by setting discard field as true. If it is already true, or password do not match, return an ERR msg. If other exception happens, return a Fail msg. Return SUCC if successful.
     * @param account
     * @return 
     */
     @ResponseBody
    @RequestMapping(value = "/delete", method = POST , produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Object deleteUser(@RequestBody Account account)
    {
        Account a;
        try{
        a = jdbcDataDAO.getDaoAccount().getAccountInstance(account.getUid());
        }
        catch (DataAccessException e)
        {
            return "ERID: NO UID in database match given one. -->"+e.getMessage();
        }
        if(a.equals(account) && !a.getDiscard() && a.getPassword().equals(account.getPassword()))
          //a.uid = account.uid     &&  a is not discarded already  && password string of a equals to account.password
        {
        try{
            jdbcDataDAO.getDaoAccount().deleteAccountByUID(account.getUid());
            return "SUCC";
        }
        catch (DataAccessException e)
        {
            return "FAIL: "+e.getMessage();
        }
        }
        else return "ERR: Password does not match or Account already discared.";
    }
     
}
     
     


