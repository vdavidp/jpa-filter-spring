package io.dvp.ds.db;

import io.dvp.ds.el.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.*;

import static io.dvp.ds.el.ExpressionTree.defaultSymbols;
import static java.util.Collections.singletonMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseBinderTest {

    @Mock
    Root<Article> root;
    @Mock
    CriteriaBuilder cb;
    @Mock
    Path<String> path1, path2;
    @Mock
    Path<Boolean> pathBool1, pathBool2;

    DatabaseBinder<Article> binder;

    @BeforeEach
    void setup() {
        binder = new DatabaseBinder<>(root, cb);
    }

    @Test
    void bindEqualToOperatorWithOneExpression() {
        when(root.<String>get("title")).thenReturn(path1);

        binder.setMappers(singletonMap("=", Mappers.equalTo()));

        ExpressionTree tree = ExpressionTree.build("{title}='Title1'", defaultSymbols());
        tree.getRoot().visit(binder);

        verify(cb).equal(eq(path1), eq("Title1"));
    }

    @Test
    void bindEqualToOperatorWithTwoExpression() {
        when(root.<String>get("title")).thenReturn(path1);
        when(root.<String>get("other")).thenReturn(path2);

        binder.setMappers(singletonMap("=", Mappers.equalTo()));

        ExpressionTree tree = ExpressionTree.build("{title}={other}", defaultSymbols());
        tree.getRoot().visit(binder);

        verify(cb).equal(eq(path1), eq(path2));
    }

    @Test
    void bindAndOperator() {
        when(root.<Boolean>get("active1")).thenReturn(pathBool1);
        when(root.<Boolean>get("active2")).thenReturn(pathBool2);

        binder.setMappers(singletonMap("and", Mappers.and()));

        ExpressionTree tree = ExpressionTree.build("{active1}and{active2}", defaultSymbols());
        tree.getRoot().visit(binder);

        verify(cb).and(eq(pathBool1), eq(pathBool2));
    }

    @Test
    void bindOrOperator() {
        when(root.<Boolean>get("active1")).thenReturn(pathBool1);
        when(root.<Boolean>get("active2")).thenReturn(pathBool2);

        binder.setMappers(singletonMap("or", Mappers.or()));

        ExpressionTree tree = ExpressionTree.build("{active1}or{active2}", defaultSymbols());
        tree.getRoot().visit(binder);

        verify(cb).or(eq(pathBool1), eq(pathBool2));
    }
}
