package com.tesis.annotatorLogger;

import javassist.*;
import javassist.bytecode.*;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, CannotCompileException, NotFoundException, BadBytecode {
        System.out.println( "Hello World!" );

        //levantamos el .class
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(new FileInputStream("C:/Users\\Ignacio\\Desktop\\HelloWorldApp.class"));

        CtMethod[] methos = ctClass.getMethods();
    
        if(methos.length == 0){
            return;            
        }

        for (CtMethod method : methos) {
            MethodInfo info = method.getMethodInfo();
            CodeAttribute ca = method.getMethodInfo().getCodeAttribute();
            if(ca.equals(null)){
                System.out.println("termino1");
                return;
            }
            CodeIterator ci = ca.iterator();
            while (ci.hasNext()) {
                int index = ci.next();
                int op = ci.byteAt(index);
                System.out.println(Mnemonic.OPCODE[op]+ ": " + info.getLineNumber(index));
            }
            System.out.println("termino metodo");

        }
        



        //Escribimos el archivo final.
        ctClass.writeFile();
        System.out.println( "Hello World!" );
    }

}

