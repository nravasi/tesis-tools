package com.tesis.annotatorLogger.processors;


import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.support.reflect.code.CtInvocationImpl;

/**
 * Created by Ignacio on 25-Apr-16.
 */


public class IfProcessor extends AbstractProcessor<CtIf> {

    public void process(CtIf ctIf) {
        try {
            System.out.print("estoy entrando al fucking if");
            CtExpression<Boolean> condition = ctIf.getCondition();
            if (((CtInvocationImpl) condition).getExecutable().getSimpleName().equals("equals")) {
                String varName = ((CtVariableAccess) condition.getElements(
                        el -> el instanceof CtVariableAccess).get(0)).getVariable().getSimpleName();

                String value = ((CtLiteral) condition.getElements(
                        el -> el instanceof CtLiteral).get(0)).getValue().toString();

                CtStatement thenLog = getFactory().Code().createCodeSnippetStatement(String.format(logCodeTemplate, varName, value, "T"));
                ctIf.getThenStatement().insertBefore(thenLog);

                CtStatement elseLog = getFactory().Code().createCodeSnippetStatement(String.format(logCodeTemplate, varName, value, "F"));
                ctIf.getElseStatement().insertBefore(elseLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String logCodeTemplate = "android.util.Log.w(TAG, String.format(\"equals;\" + %s + \";%s;%s\"))";
}