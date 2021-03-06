package io.dvp.ds.spring;

import io.dvp.ds.db.Binder;
import io.dvp.ds.db.DatabaseBinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Slf4j
@RequiredArgsConstructor
public class ExpressionTreeArgumentResolver implements HandlerMethodArgumentResolver {

    private final ExpressionTreeConfigurator configurator;
    private final ObjectProvider<Binder> binderProvider;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameter().getType() == Specification.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String exp = request.getParameter("filter");

        if (exp == null || "".equals(exp.trim())) {
            log.info("Null or empty expression in filter query param");
            return null;
        } else {
            exp = URLDecoder.decode(exp, "UTF-8");
            log.info("Detected expression: {}", exp);
            return new ExpressionTreeSpecification(exp, configurator, binderProvider);
        }
    }
}
