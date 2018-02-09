/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;
import com.childcare.entity.Account;
import com.childcare.entity.DemoClass;
import com.childcare.entity.PasswdChanger;
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

/**
 *
 * @author New User
 */
@RestController
@RequestMapping(value = "/account")
public class AccountController {
    
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
    
    @ResponseBody
    @RequestMapping(value = "/demo/{id}", method = GET, produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Account reply(@PathVariable(value = "id")long id) {
        return jdbcDataDAO.getAccountInstance(id);
     }
    
   /**
    * Register a new account
    * @param account Auto injected from request body
    * @return a string as UID for created account, or error message starting with "FAIL: " if there is an error during transaction.
    */
    @ResponseBody
    @RequestMapping(value = "/register", method = POST)
    public Object register(@RequestBody Account account) {
        try{
        jdbcDataDAO.createAccount(account);
        }
        catch (DataAccessException e)
        {
            return "FAIL: "+e.getMessage();
        }
        try{
            return jdbcDataDAO.findUID(account.getUserName());
        }
        catch (DataAccessException e)
        {
            return "FAIL: "+e.getMessage();
        }
     }
    
    /**
     * Find a user's UID with Username
     * @param username
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/findUID/{username}", method = GET , produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
     public Object findUID(@PathVariable(value = "username")String username) {
         System.out.println("find UID by UserName: "+username);
                 try{
            return jdbcDataDAO.findUID(username);
           //return jdbcDataDAO.QueryAllUser();
        }
        catch (DataAccessException e)
        {
            return "FAIL: Cannot find Matching result.";
        }
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
        a = jdbcDataDAO.getAccountInstance(account.getUid());
        }
        catch (DataAccessException e)
        {
            return "ERID: NO UID in database match given one. -->"+e.getMessage();
        }
        if(a.equals(account) && !a.getDiscard() && a.getPassword().equals(account.getPassword()))
          //a.uid = account.uid     &&  a is not discarded already  && password string of a equals to account.password
        {
        try{
            jdbcDataDAO.deleteAccountByUID(account.getUid());
            return "SUCC";
        }
        catch (DataAccessException e)
        {
            return "FAIL: "+e.getMessage();
        }
        }
        else return "ERR: Password does not match or Account already discared.";
    }
    
    @ResponseBody
    @RequestMapping(value = "/Update", method = POST , produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Object UpdateAccount(@RequestBody Account account)
    { 
        Account a;
        try{
        a = jdbcDataDAO.getAccountInstance(account.getUid());
        }
        catch (DataAccessException e)
        {
            return "ERID: NO UID in database match given one. -->"+e.getMessage();
        }
        System.out.println(""+a.equals(account)+"   "+a.getPassword().equals(account.getPassword()));
        if(a.equals(account) && !a.getDiscard()/*Not discarded*/ && a.getPassword().equals(account.getPassword()))
        {
           try
           {this.jdbcDataDAO.mergeAccount(account);
            return "SUCC";}
           catch (DataAccessException e)
           {
               return "FAIL: "+ e.getMessage();
           }
        } else
            if (!a.getDiscard())
                return "ERR: this account is already discarded.";
        else
             return "ERR: Password in database does not match given one.";
    }
    
    @ResponseBody
    @RequestMapping(value = "/Password", method = POST , produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Object ChangePassword(@RequestBody PasswdChanger psd)
    {
        try{
        if (this.jdbcDataDAO.updatePassword(psd))
                return "SUCC";
        else
                return "ERRP: Orgiginal Password does not match record in DataBase.";
        }
        catch (DataAccessException e)
        {
            return "FAIL: error during transcation."+e.getMessage();
        }
    }
    
    
      @ResponseBody
    @RequestMapping(value = "/psdChangerDemo", method = GET , produces = { APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE })
    public Object PSDDemo()
    {
        PasswdChanger psd = new PasswdChanger();
        psd.setUID(2);
        psd.setOrgPasswd("oldpsd");
        psd.setNewPasswd("newPsd");
        return psd;
    }
         
     
     
}
     
     


