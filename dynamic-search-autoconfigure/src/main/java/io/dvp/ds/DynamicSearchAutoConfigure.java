package io.dvp.ds;

import io.dvp.ds.el.ExpressionTree;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ExpressionTree.class)
public class DynamicSearchAutoConfigure {

}
