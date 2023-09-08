package org.csq.expres;

import org.csq.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component("customExpression")
public class CustomExpression {
    public boolean generalRequestProcessing(HttpServletRequest request, Authentication authentication){
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermission();
        if (ObjectUtils.isEmpty(request.getRequestURI())){
            return false;
        }
        return permissions.contains(request.getRequestURI());
    }
}
