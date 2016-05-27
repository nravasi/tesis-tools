package org.nrip.sootInliner

import soot.*
import soot.jimple.*
import soot.jimple.internal.JAssignStmt
import soot.jimple.internal.JEqExpr
import soot.jimple.internal.JimpleLocal
import soot.util.Chain
import soot.util.HashChain

/**
 * Created by nmravasi on 5/25/16.
 */
public class IfLoggerTransformer extends BodyTransformer {

    private Local logRef;
    private StringConstant tag = StringConstant.v("SootInlineTag")

    private Local addAndroidLogRef(Body body) {
        Local logRef = Jimple.v().newLocal("logRef", RefType.v("android.util.Log"));
        body.getLocals().add(logRef);
        return logRef;
    }

    private SootMethod logMethod() {
        return Scene.v().getSootClass("android.util.Log").getMethod("int i(java.lang.String,java.lang.String)");
    }


    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        logRef = addAndroidLogRef(b);

        final PatchingChain<Unit> units = b.getUnits();

        for (Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
            final Unit u = iter.next();
            u.apply(new AbstractStmtSwitch() {
                @Override
                public void caseIfStmt(IfStmt stmt) {
                    Unit temp = u;
                    def tempb = b;
                    def condValue = stmt.conditionBox.value
                    if (condValue instanceof JEqExpr) {

                        def firstOp = condValue.op1
                        if (firstOp instanceof JimpleLocal) {

                            def localAssign = b.units.find {
                                it instanceof JAssignStmt && it.leftBox.value instanceof Local &&
                                        it.leftBox.value.number == firstOp.number
                            }

                            InstanceInvokeExpr call = localAssign?.rightBox?.value

                            if (call) {
                                Chain<Unit> chain = new HashChain<Unit>();

                                def localC = addLocal(b, "concat", "java.lang.String");
                                def arrFalse = createArray(chain, b, "arrF", "java.lang.String",
                                        call.base, StringConstant.v("1"), StringConstant.v("F"))

                                chain.add(Jimple.v().newAssignStmt(localC, Jimple.v().newStaticInvokeExpr(
                                        Scene.v().getSootClass("java.lang.String").
                                                getMethod("java.lang.String format(java.lang.String,java.lang.Object[])")
                                                .makeRef(),
                                        StringConstant.v("Executing. Value: %s, Compare: %s, Branch: %s"),
                                        arrFalse
                                )))

                                chain.add(Jimple.v().newInvokeStmt(Jimple.v().
                                        newStaticInvokeExpr(logMethod().makeRef(), tag, localC)))
                                units.insertAfter(chain, stmt.targetBox.unit)

                                Chain<Unit> chainT = new HashChain<Unit>();


                                def localC2= addLocal(b, "concat2", "java.lang.String");
                                def arrTrue = createArray(chainT, b, "arrT", "java.lang.String",
                                        call.base, StringConstant.v("1"), StringConstant.v("T"))

                                chainT.add(Jimple.v().newAssignStmt(localC2, Jimple.v().newStaticInvokeExpr(
                                        Scene.v().getSootClass("java.lang.String").
                                                getMethod("java.lang.String format(java.lang.String,java.lang.Object[])")
                                                .makeRef(),
                                        StringConstant.v("Executing. Value: %s, Compare: %s, Branch: %s"),
                                        arrTrue
                                )))

                                chainT.add(Jimple.v().newInvokeStmt(Jimple.v().
                                        newStaticInvokeExpr(logMethod().makeRef(), tag, localC2)))

                                units.insertAfter(chainT,u)

                                b.validate();

                            }
                        }
                    }
                }
            });

        }
    }

    private addLocal(Body body, String name, String clazz, boolean isArray = false) {
        def type = RefType.v(clazz)
        if (isArray) type = type.makeArrayType()
        Local ref = Jimple.v().newLocal(name, type);
        body.getLocals().add(ref);
        return ref;
    }

    private InvokeStmt getStaticInvoke(SootMethod method, Value... args) {
        Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(method.makeRef(), args));
    }

    private Local createArray(Chain chain, Body b, String varName, String type, Value... values) {
        def localArr = addLocal(b, varName, type, true);

        chain.add(Jimple.v().newAssignStmt(localArr, Jimple.v().newNewArrayExpr(
                RefType.v(type), IntConstant.v(values.length))));

        for (int i = 0; i < values.length; i++) {
            chain.add(Jimple.v().newAssignStmt(Jimple.v().newArrayRef(localArr, IntConstant.v(i)), values[i]))
        }

        return localArr;
    }
}