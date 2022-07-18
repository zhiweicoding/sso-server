package com.pj.controller;

import cn.dev33.satoken.annotation.SaCheckBasic;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by diaozhiwei on 2022/07/15.
 * @email diaozhiwei2k@163.com
 */
@SaCheckLogin
@RequestMapping("anno")
@RestController
public class AnnotationController {

    // http://localhost:8081/anno/index
    @SaCheckLogin
    @RequestMapping("index")
    public String index() {
        return "ok";
    }

    // http://localhost:8081/anno/dzw
    @SaCheckRole("super-admin")
    @RequestMapping("dzw")
    public String dzw() {
        return "ok-dzw";
    }

    // http://localhost:8081/anno/other
    @SaCheckPermission("*.json")
    @RequestMapping("other")
    public String other() {
        return "ok-dzw";
    }

    // Http Basic 认证：只有通过 Basic 认证后才能进入该方法
    @SaCheckBasic(account = "sa:123456")
    @RequestMapping("httpBasic")
    public String httpBasic() {
        return "httpBasic";
    }

    @ExceptionHandler(NotPermissionException.class)
    public SaResult handlerNotPermissionException(NotPermissionException e) throws Exception {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }
}
