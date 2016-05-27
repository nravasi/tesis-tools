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
        Scene.v().addBasicClass("android.util.Log", SootClass.SIGNATURES);

        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new IfLoggerTransformer()));

        soot.Main.main(args);
    }
}