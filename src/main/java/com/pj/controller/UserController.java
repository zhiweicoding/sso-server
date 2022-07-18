package com.pj.controller;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.LoginMsg;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by diaozhiwei on 2022/07/15.
 * @email diaozhiwei2k@163.com
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    private ObjectMapper objectMapper = new ObjectMapper();

    // 测试登录 http://localhost:8081/user/doLogin?username=diao&password=123456
    @RequestMapping("doLogin")
    public SaResult doLogin(String username, String password) throws JsonProcessingException {
        if ("diao".equals(username) && "123456".equals(password)) {
            StpUtil.login("diaozhiwei", new SaLoginModel()
                    .setTimeout(60 * 60 * 24 * 7)
                    .setDevice("pc")
                    .setIsLastingCookie(true)// 是否为持久Cookie（临时Cookie在浏览器关闭时会自动删除，持久Cookie在重新打开后依然存在）
            );
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            System.out.println(objectMapper.writeValueAsString(tokenInfo));
            String tokenValue = StpUtil.getTokenValue();
            System.out.println(tokenValue);
//            StpUtil.login("diaozhiwei","app");
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 测试登录 http://localhost:8081/user/doLoginAPP?username=diao&password=123456
    @RequestMapping("doLoginAPP")
    public SaResult doLoginAPP(String username, String password) throws JsonProcessingException {
        if ("diao".equals(username) && "123456".equals(password)) {
            StpUtil.login("diaozhiwei", new SaLoginModel()
                    .setDevice("app")
            );
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            System.out.println(objectMapper.writeValueAsString(tokenInfo));
            String tokenValue = StpUtil.getTokenValue();
            System.out.println(tokenValue);
//            StpUtil.login("diaozhiwei","app");
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 查询登录状态 http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "is login:" + StpUtil.isLogin();
    }

    // 检查登录状态 http://localhost:8081/user/checkLogin
    @RequestMapping("checkLogin")
    public String checkLogin() {
        try {
            StpUtil.checkLogin();
            //httpSession和saSession不同
            SaSession session = StpUtil.getSession();
            session.set("model", new LoginMsg());
            return "just for check";
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    // 检查登录状态 http://localhost:8081/user/getLoginId
    @RequestMapping("getLoginId")
    public SaResult getLoginId() {
        try {
            Object loginId = StpUtil.getLoginId();
            String str = objectMapper.writeValueAsString(loginId);
            return SaResult.data(str);
        } catch (Exception e) {
            e.printStackTrace();
            return SaResult.error(e.getMessage());
        }

    }

    // 检查登录状态 http://localhost:8081/user/logout
    @RequestMapping("logout")
    public SaResult logout(String logId) {
        try {
            StpUtil.logout(logId);
            return SaResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SaResult.error(e.getMessage());
        }

    }

    // 检查登录状态 http://localhost:8081/user/kickout?logId=diaozhiwei

    //踢下线后再次检查
    //http://localhost:8081/user/checkLogin
    @RequestMapping("kickout")
    public SaResult kickout(String logId) {
        try {
            StpUtil.kickout(logId);
            return SaResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SaResult.error(e.getMessage());
        }

    }

    //http://localhost:8081/user/disable?logId=diaozhiwei
    @RequestMapping("disable")
    public SaResult disable(String logId) {
        try {
            StpUtil.disable(logId, 60);
            return SaResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SaResult.error(e.getMessage());
        }

    }


    //全局异常拦截（拦截项目中的NotLoginException异常）
    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerNotLoginException(NotLoginException nle) throws Exception {

        // 打印堆栈，以供调试
        nle.printStackTrace();

        // 判断场景值，定制化异常信息
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
        } else {
            message = "当前会话未登录";
        }

        // 返回给前端
        return SaResult.error(message);
    }

    @ExceptionHandler(DisableLoginException.class)
    public SaResult handlerDisableLoginException(DisableLoginException e) throws Exception {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }
}
