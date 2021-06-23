package com.evision.dosage.interceptor;

import com.evision.dosage.annotation.PassToken;
import com.evision.dosage.exception.DosageException;
import com.evision.dosage.mapper.UserMapper;
import com.evision.dosage.pojo.entity.user.UserEntity;
import com.evision.dosage.utils.CookieUtils;
import com.evision.dosage.utils.TokenUtils;
import com.evision.dosage.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/17 19:40
 */
public class AuthenticationInterceptor implements AsyncHandlerInterceptor {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object object) throws JsonProcessingException {

        // TODO 本地测试，注释
//        if (true){
//            return true;
//        }
        //设置允许哪些域名应用进行ajax访问
        String allowOrigin = httpServletRequest.getHeader("Origin");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", allowOrigin);
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", " Origin, X-Requested-With, content-Type, Accept, Authorization");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");

        //如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passToken注释，有则跳过验证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        String cookieToken = TokenUtils.getToken(httpServletRequest);
        //检查是否有需要用户权限的注解
        //执行认证
        if (cookieToken == null) {
            throw new DosageException("无token，请重新登录");
        } else {
            //从缓存中查看token是否过期
            Claims claims;
            try {
                claims = TokenUtils.parseJWT(cookieToken);
            } catch (ExpiredJwtException e) {
                httpServletResponse.setStatus(500);
                String new_token = TokenUtils.RefreshToken(cookieToken);
                // 如果登录成功，将token设置到cookie中
                Cookie cookie = new Cookie("user", new_token);
                cookie.setMaxAge(3600);
                httpServletResponse.addCookie(cookie);
                throw new DosageException("410");
            } catch (SignatureException e) {
                httpServletResponse.setStatus(500);
                throw new DosageException("token错误，请重新登录");
            }
            String userId = claims.getId();
            UserEntity userEntity = userMapper.selectById(Integer.parseInt(userId));
            if (userEntity == null) {
                httpServletResponse.setStatus(500);
                throw new DosageException("用户不存在，请重新登录");
            }

            // 设置当前登录用户，全局上下文
            UserUtils.setCurrentUser(userEntity);
        }
        return true;
    }

    // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
