package io.github.vdavidp.jpa.filter.spring;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class JpaFilterArgumentResolver implements HandlerMethodArgumentResolver {

  private final SpecificationProvider provider;

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterType() == Specification.class
        && methodParameter.hasParameterAnnotation(Filter.class);
  }

  @Override
  public Object resolveArgument(MethodParameter methodParameter,
      ModelAndViewContainer modelAndViewContainer,
      NativeWebRequest nativeWebRequest,
      WebDataBinderFactory webDataBinderFactory) throws Exception {
    
    Filter filter = methodParameter.getParameterAnnotation(Filter.class);

    HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
    String exp = request.getParameter(filter.queryParam());

    if (exp == null || "".equals(exp.trim())) {
      log.info("Null or empty expression in {} query param", filter.queryParam());
    }
    
    log.info("Detected expression: {}", exp);
    return provider.create(exp);
  }
}
