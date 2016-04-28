package com.tesis.annotatorLogger.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCatch;

/**
 * Reports warnings when empty catch blocks are found.
 */
public class CatchProcessor extends AbstractProcessor<CtCatch> {

    public void process(CtCatch element) {
        if (element.getBody().getStatements().size() == 0) {
//            getFactory().getEnvironment().report(this, Severity.WARNING, element, "Something");
            element.getBody().setDocComment("aasdasdasdasd");
            System.out.println("Hello World!1");
        } else {
            element.getBody().setDocComment("aasdasdasdasd");
            System.out.println("Hello World2!");
        }
    }
}