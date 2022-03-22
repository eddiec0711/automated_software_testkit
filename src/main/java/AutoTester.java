import Coverage.BranchCoverage;
import Coverage.ConditionCoverage;
import Coverage.CorrelatedMCDC;
import TestDataGenerator.TestGeneration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.IfStmt;

import java.io.*;
import java.util.*;

/**
 * @author eddie
 * AutoTester.java
 *
 * class that read in all the loggers
 * and records output history
 *
 */

public class AutoTester {

    // record log history when running test method
    private ArrayList<HashMap<Integer, HashMap<Integer, Boolean>>> logHistory;
    private HashMap<Integer, HashMap<Integer, Boolean>> branchHistory = new HashMap<>();
    private HashMap<Integer, Boolean> conditionHistory = new HashMap<>();

    // record loggers inserted during setup
    private HashMap<Integer, HashSet<Integer>> loggers = new HashMap<>();

    // compute coverage
    private ConditionCoverage cc;
    private BranchCoverage bc;
    private CorrelatedMCDC mcdc;

    public AutoTester(String testFile) {

        logHistory = new ArrayList<>();

        // use java parser to read in loggers
        CompilationUnit cu = new CompilationUnit();
        try {
            cu = StaticJavaParser.parse(new File(testFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        retrieveLoggers(cu);

        cc = new Coverage.ConditionCoverage(loggers);
        bc = new Coverage.BranchCoverage(loggers);
        mcdc = new Coverage.CorrelatedMCDC(loggers, logHistory);

    }

    // used in test file to record condition results
    public Boolean logCondition(int id, Boolean condition) {
        conditionHistory.put(id, condition);
        cc.logCondition(id, condition);

        return condition;
    }

    // used in test file to record branches visited
    // each branch visited mark condition visited up to the point
    public void logBranch(int id){
        branchHistory.put(id, conditionHistory);
        logHistory.add(branchHistory);

        branchHistory = new HashMap<>();
        conditionHistory = new HashMap<>();

        bc.logBranch(id);
    }

    public void getConditionCoverage() {
        cc.getCoverage();
    }

    public void getBranchCoverage() {
        bc.getCoverage();
    }

    public void getMCDCoverage() {
        mcdc.getMCDCoverage();
    }

    // retrieve all loggers to compare with logHistory and compute coverage
    private void retrieveLoggers(CompilationUnit cu) {

        for (IfStmt ifstmt : cu.findAll(IfStmt.class)) {

            // use java parser to find first branch logger in a statement
            MethodCallExpr branchLogger = ifstmt.getThenStmt()
                                            .findAll(MethodCallExpr.class)
                                            .stream()
                                            .filter(method -> method.getNameAsString().equals("logBranch"))
                                            .findFirst().get();
            int branch = Integer.valueOf(branchLogger.getArgument(0).toString());

            // record all condition loggers and corresponding operator in sequence
            HashSet<Integer> conLogger = new LinkedHashSet<>();
            readCondition(ifstmt.getCondition(), conLogger);
            loggers.put(branch, conLogger);
        }
    }

    private void readCondition(Expression expr, HashSet<Integer> logger) {
        if (expr instanceof BinaryExpr) {
            BinaryExpr binExpr = (BinaryExpr) expr;
            readCondition(binExpr.getLeft(), logger);
            readCondition(binExpr.getRight(), logger);
        }
        else {
            MethodCallExpr conLogger = (MethodCallExpr) expr;
            int condition = Integer.valueOf(conLogger.getArgument(0).toString());
            logger.add(condition);
        }
    }

}

