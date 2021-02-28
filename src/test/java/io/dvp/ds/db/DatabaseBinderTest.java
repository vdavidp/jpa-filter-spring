package io.dvp.ds.db;

import io.dvp.ds.el.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.*;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiFunction;

import static io.dvp.ds.el.ExpressionTree.defaultSymbols;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Mock
    Predicate predicate1, predicate2, predicate3;

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

    @Test
    void bindTwoOperatorsWithDifferentWeight() {
        when(root.<String>get("prop")).thenReturn(path1);
        when(root.<String>get("name")).thenReturn(path2);
        when(cb.equal(eq(path1), eq("something"))).thenReturn(predicate1);
        when(cb.equal(eq(path2), eq("me"))).thenReturn(predicate2);
        when(cb.and(eq(predicate1), eq(predicate2))).thenReturn(predicate3);

        Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers = new HashMap<>();
        mappers.put("=", Mappers.equalTo());
        mappers.put("and", Mappers.and());
        binder.setMappers(mappers);

        String exp = "{prop}='something' and {name}='me'";
        ExpressionTree tree = ExpressionTree.build(exp, defaultSymbols());
        tree.getRoot().visit(binder);

        assertEquals(predicate3, binder.getPredicate());
    }
}
