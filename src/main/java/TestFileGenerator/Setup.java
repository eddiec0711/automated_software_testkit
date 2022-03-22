package TestFileGenerator;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author eddie
 * TestFileGenerator.Setup.java
 *
 * run by user to generate new test file with instrumented method
 * to compute coverage
 * by using java parser
 *
 * @param1 targetFile
 * @param2 testFile
 * @param3 targetMethod
 */

public class Setup {

    public static void main(String[] args) throws IOException {

        // Input variables
        String targetFilename = args[0];
        String testFilename = args[1];
        String targetMethod = args[2];

        // read in target file
        File targetFile = new File(targetFilename);
        CompilationUnit cu = StaticJavaParser.parse(targetFile);

        // search for target method to instrument
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            if (method.getName().asString().equals(targetMethod)) { // find target method
                MethodModifier.analyseMethod(method);
            }
        }

        // generate test file
        File testFile = new File(testFilename);
        generateTestClassName(cu,
                                targetFile.getName().split("\\.")[0],
                                testFile.getName().split("\\.")[0],
                                targetMethod);
        Files.write(testFile.toPath(), Collections.singleton(cu.toString()), StandardCharsets.UTF_8);
    }

    // generate test class with AutoTester instance
    private static void generateTestClassName(CompilationUnit cu, String targetClass, String testClass, String method){

        // get main class
        ClassOrInterfaceDeclaration clazz = cu.getClassByName(targetClass).get();

        // declare new autoTester instance
        // e.g. AutoTester autoTester = new AutoTester(testFile, targetMethod)
        VariableDeclarator variable = new VariableDeclarator();
        variable.setName("autoTester");
        variable.setType("AutoTester");
        variable.setInitializer("new AutoTester(" + "\"" + testClass + ".java\"" + ")");

        // and add to class
        FieldDeclaration fieldDeclaration = new FieldDeclaration().addVariable(variable);
        fieldDeclaration.setModifiers(new NodeList<>(Arrays.asList(Modifier.publicModifier(), Modifier.staticModifier())));
        clazz.getMembers().add(0, fieldDeclaration);

        // set test class name
        clazz.setName(testClass);

        cu.accept(new VoidVisitorAdapter<>() {
            @Override
            public void visit(ConstructorDeclaration n, Object arg) {
                if (n.getNameAsString().equals(targetClass)) {
                    n.setName(testClass);
                }
                super.visit(n, arg);
            }
        }, null);
    }
}
