package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 身份校验过滤器
 */
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    AuthService authService;

    /**
     * 请求在被路由之前执行
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤序号，越小越被优先执行
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 返回true表示要执行此过滤器
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的内容
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //得到request
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        //取cookie中的身份令牌
        String cookie = authService.getTokenFromCookie(request);
        if (StringUtils.isEmpty(cookie)) {
            //拒绝访问
            access_denied();
            return null;
        }
        //从header中取jwt
        String jwtFromHeard = authService.getJwtFromHeard(request);
        if (StringUtils.isEmpty(jwtFromHeard)) {
            access_denied();
            return null;
        }
        //从redis中取jwt的过期时间
        long expire = authService.getExpire(cookie);
        if (expire < 0) {
            access_denied();
            return null;
        }
        return null;
    }


    /**
     * 拒绝访问
     */
    public void access_denied() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        //拒绝访问
        requestContext.setSendZuulResponse(false);
        //设置响应代码
        requestContext.setResponseStatusCode(200);
        //构建响应信息
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        //转成json
        String s = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(s);
        //转成json，设置contentType
        response.setContentType("application/json;charset=UTF-8");
    }
}
