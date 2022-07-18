package com.pj.service;

/**
 * @author by diaozhiwei on 2022/07/15.
 * @email diaozhiwei2k@163.com
 */

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        if (loginId.equals("diaozhiwei")) {
            list.add("101");
            list.add("user-add");
            list.add("user-delete");
            list.add("user-update");
            list.add("user-get");
            list.add("article-get");
        }else{
            list.add("101");
            list.add("*.json");
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        if (loginId.equals("diaozhiwei")) {
            list.add("admin");
            list.add("super-admin");
        }else{
            list.add("admin");
        }
        return list;
    }

}
