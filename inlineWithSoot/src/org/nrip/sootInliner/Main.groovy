package org.nrip.sootInliner;

import soot.*;
import soot.jimple.*
import soot.jimple.internal.JAssignStmt
import soot.jimple.internal.JEqExpr
import soot.jimple.internal.JimpleLocal;
import soot.options.Options;

import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        //prefer Android APK files// -src-prec apk
        Options.v().set_src_prec(Options.src_prec_apk);

        //output as APK, too//-f J
        Options.v().set_output_format(Options.output_format_dex);

        // resolve the PrintStream and System soot-classes
        Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);

        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {

            @Override
            protected void internalTransform(Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {
                final PatchingChain<Unit> units = body.getUnits();

                //important to use snapshotIterator here
                for (Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext(); ) {
                    final Unit u = iter.next();
                    u.apply(new AbstractStmtSwitch() {

//                        public void caseInvokeStmt(InvokeStmt stmt) {
//                            InvokeExpr invokeExpr = stmt.getInvokeExpr();
//                            if (invokeExpr.getMethod().getName().equals("setText")) {
//
//                                Local tmpRef = addTmpRef(b);
//                                Local tmpString = addTmpString(b);
//
//                                // insert "tmpRef = java.lang.System.out;"
//                                units.insertBefore(Jimple.v().newAssignStmt(
//                                        tmpRef, Jimple.v().newStaticFieldRef(
//                                                Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), u);
//
//                                // insert "tmpLong = 'HELLO';"
//                                units.insertBefore(Jimple.v().newAssignStmt(tmpString,
//                                        StringConstant.v("Hola soy un equals")), u);
//
//                                // insert "tmpRef.println(tmpString);"
//                                SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
//                                units.insertBefore(Jimple.v().newInvokeStmt(
//                                        Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpString)), u);
//
//                                //check that we did not mess up the Jimple
//                                b.validate();
//                            }
//                        }


                        @Override
                        public void caseIfStmt(IfStmt stmt) {
                            def temp = u;
                            def tempb = body;
                            def condValue = stmt.conditionBox.value
                            if(condValue instanceof JEqExpr){
//                                condValue.getOp1().

                                def firstOp = condValue.op1
                                if(firstOp instanceof JimpleLocal){

                                    def localAssign = body.units.find {
                                        it instanceof JAssignStmt && it.leftBox.value.number == firstOp.number
                                    }

                                    if(localAssign){
                                        
                                    }
                                }

                            }
                        }
                    });

                }
            }


        }));

        soot.Main.main(args);
    }

    private static Local addTmpRef(Body body) {
        Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
        body.getLocals().add(tmpRef);
        return tmpRef;
    }

    private static Local addTmpString(Body body) {
        Local tmpString = Jimple.v().newLocal("tmpString", RefType.v("java.lang.String"));
        body.getLocals().add(tmpString);
        return tmpString;
    }
}
