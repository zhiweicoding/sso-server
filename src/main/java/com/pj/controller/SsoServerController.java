package com.pj.controller;

import cn.dev33.satoken.config.SaSsoConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.ejlchina.okhttps.OkHttps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by diaozhiwei on 2022/07/18.
 * @email diaozhiwei2k@163.com
 */
@RestController
public class SsoServerController {
    /*
     * SSO-Server端：处理所有SSO相关请求 (下面的章节我们会详细列出开放的接口)
     */
    @RequestMapping("/sso/*")
    public Object ssoRequest() {
        return SaSsoHandle.serverRequest();
    }

    // 自定义接口：获取userinfo
    @RequestMapping("/sso/userinfo")
    public Object userinfo(String loginId) {
        System.out.println("---------------- 获取userinfo --------");

        // 校验签名，防止敏感信息外泄
        SaSsoUtil.checkSign(SaHolder.getRequest());

        // 自定义返回结果（模拟）
        return SaResult.ok()
                .set("id", loginId)
                .set("name", "linxiaoyu")
                .set("sex", "女")
                .set("age", 18);
    }

    // 获取指定用户的关注列表
    @RequestMapping("/sso/getFollowList")
    public Object ssoRequest(Long loginId) {

        // 校验签名，签名不通过直接抛出异常
        SaSsoUtil.checkSign(SaHolder.getRequest());

        // 查询数据 (此处仅做模拟)
        List<Integer> list = Arrays.asList(10041, 10042, 10043, 10044);
        // 返回
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        return result;
    }

    /**
     * 配置SSO相关参数
     */
    @Autowired
    private void configSso(SaSsoConfig sso) {
        // 配置：未登录时返回的View
        sso.setNotLoginView(() -> {
            String msg = "no login ,please click"
                    + "<a href='/sso/doLogin?name=diaozhiwei&pwd=123456' target='_blank'> doLogin登录 </a>"
                    + "to login ，then refresh page";
            return msg;
//            return new ModelAndView("sa-login.html");
        });

        // 配置：登录处理函数
        sso.setDoLoginHandle((name, pwd) -> {
            // 此处仅做模拟登录，真实环境应该查询数据进行登录
            if ("diaozhiwei".equals(name) && "123456".equals(pwd)) {
                StpUtil.login(10001);
                return SaResult.ok("login ok！").setData(StpUtil.getTokenValue());
            }
            return SaResult.error("login fail！");
        });

        // 配置 Http 请求处理器 （在模式三的单点注销功能下用到，如不需要可以注释掉）
        sso.setSendHttp(url -> {
            try {
                // 发起 http 请求
                System.out.println("发起请求：" + url);
                return OkHttps.sync(url).get().getBody().toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

}
