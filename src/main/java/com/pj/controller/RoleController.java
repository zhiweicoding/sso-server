package com.pj.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author by diaozhiwei on 2022/07/15.
 * @email diaozhiwei2k@163.com
 */
@RestController
@RequestMapping("/role/")
public class RoleController {

    // 测试登录 http://localhost:8081/role/doLogin?username=diao&password=123456
    @RequestMapping("doLogin")
    public SaResult doLogin(String username, String password) {
        if ("diao".equals(username) && "123456".equals(password)) {
            StpUtil.login("diaozhiwei");
            List<String> list = StpUtil.getPermissionList("diaozhiwei");
            //NotRoleException
            StpUtil.checkRole("admin");
            SaResult ok = SaResult.ok("登陆成功");
            ok.setData(list);
            return ok;
        }
        return SaResult.error("登录失败");
    }

    // 测试登录 http://localhost:8081/role/doLoginOther?username=other&password=123456
    @RequestMapping("doLoginOtherSuccess")
    public SaResult doLoginOtherSuccess(String username, String password) {
        if ("other".equals(username) && "123456".equals(password)) {
            StpUtil.login("other");
            List<String> list = StpUtil.getPermissionList("other");
            StpUtil.checkRole("admin");
            SaResult ok = SaResult.ok("登陆成功");
            ok.setData(list);
            return ok;
        }
        return SaResult.error("登录失败");
    }

    // 测试登录 http://localhost:8081/role/doLoginOther?username=other&password=123456
    @RequestMapping("doLoginOther")
    public SaResult doLoginOther(String username, String password) {
        if ("other".equals(username) && "123456".equals(password)) {
            StpUtil.login("other");
            List<String> list = StpUtil.getPermissionList("other");
            //NotRoleException
            StpUtil.checkRole("super-admin");
            SaResult ok = SaResult.ok("登陆成功");
            ok.setData(list);
            return ok;
        }
        return SaResult.error("登录失败");
    }

    // 如果是前后项目 thymeleaf 做好了标签
    // 如果分离项目，交由前端处理即可
    // <span sa:login>value</span>
    // <span sa:notLogin>value</span>
    // 测试登录 http://localhost:8081/role/doLoginOther?username=other&password=123456
    @RequestMapping("doLoginOtherJson")
    public SaResult doLoginOtherJson(String username, String password) {
        if ("other".equals(username) && "123456".equals(password)) {
            StpUtil.login("other");
            List<String> list = StpUtil.getPermissionList("other");
            StpUtil.hasPermission("test.json");
            SaResult ok = SaResult.ok("登陆成功");
            ok.setData(list);
            return ok;
        }
        return SaResult.error("登录失败");
    }

    @ExceptionHandler(NotRoleException.class)
    public SaResult handlerNotRoleException(NotRoleException nle) throws Exception {
        nle.printStackTrace();
        return SaResult.error(nle.getMessage());
    }
}
