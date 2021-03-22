package com.program.moist.others.interceptors;

import com.program.moist.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Date: 2021/3/12
 * Author: SilentSherlock
 * Description: validate weather user session exist
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("UserInterceptor-preHandle");

        String cur = request.getRequestURI();
        for (String uri : PublicUris.uris) {
            if (cur.startsWith(uri)) {
                return true;//不需要验证
            }
        }

        String login_token = request.getHeader(TokenUtil.LOGIN_TOKEN);
        if (login_token != null && !"".equals(login_token)) {
            String userStr = (String) RedisUtil.getMapValue(TokenUtil.LOGIN_TOKEN, login_token);
            if (userStr != null && !"".equals(userStr)) {
                return true;
            }
        }

        //重设返回消息
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        PrintWriter writer = response.getWriter();
        writer.write(JsonUtil.obj2String(new Result(Status.NEED_LOGIN, "用户不存在或token信息过期")));
        writer.flush();
        writer.close();

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("UserInterceptor-postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("UserInterceptor-afterCompletion");
    }
}
