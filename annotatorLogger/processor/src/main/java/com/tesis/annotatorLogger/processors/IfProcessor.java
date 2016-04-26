package com.tesis.annotatorLogger.processors;


import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeParameterReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.AbstractFilter;
/**
 * Created by Ignacio on 25-Apr-16.
 */


public class IfProcessor extends AbstractProcessor<CtIf> {

    public void process(CtIf ctIf) {
        CtExpression<Boolean> condition = ctIf.getCondition();
        CtStatement statementIf = getFactory().Code().createCodeSnippetStatement("System.out.println(\"Hello World!1if\");");
        ctIf.setThenStatement(statementIf);
        System.out.println("Hello World!1if" +statementIf );

    }
}