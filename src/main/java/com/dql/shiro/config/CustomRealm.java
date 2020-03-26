package com.dql.shiro.config;

import com.dql.shiro.domain.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

public class CustomRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登录的用户
        User user = (User) principalCollection.getPrimaryPrincipal();
        //通过SimpleAuthenticationInfo做授权
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //添加角色
        simpleAuthorizationInfo.addRole(user.getRole());
        //添加权限
        simpleAuthorizationInfo.addStringPermissions(user.getPermissions());
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取用户输入的账号
        String username = (String)authenticationToken.getPrincipal();
        //2.通过username从数据库中查找到user实体
        User user = getUserByUserName(username);
        if(user == null){
            return null;
        }
        //3.通过SimpleAuthenticationInfo做身份处理
        SimpleAuthenticationInfo simpleAuthenticationInfo =
                new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        //4.用户账号状态验证等其他业务操作
        if(!user.isAvailable()){
            throw new AuthenticationException("该账号已经被禁用");
        }
        //5.返回身份处理对象
        return simpleAuthenticationInfo;
    }

    /**
     * 模拟通过username从数据库中查找到user实体
     * @param username
     * @return
     */
    private User getUserByUserName(String username){
        List<User> users = getUsers();
        for(User user : users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    /**
     * 模拟数据库数据
     * @return
     */
    private List<User> getUsers(){
        List<User> users = new ArrayList<>(2);
        List<String> cat = new ArrayList<>(2);
        cat.add("sing");
        cat.add("rap");
        List<String> dog = new ArrayList<>(2);
        dog.add("jump");
        dog.add("basketball");
        users.add(new User("张小黑的猫","123qwe",true,"cat", cat));
        users.add(new User("张小黑的狗","123qwe",true,"dog", dog));
        return users;
    }
}
