package TestFileGenerator;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;

/**
 * @author eddie
 * TestFileGenerator.Logger.java
 *
 * instrument individual branches/conditions
 */

public class Logger {

    private Integer conditionCount = 0;
    private Integer branchCount = 0;

    public String loggerLoop(Expression expr, Boolean isRetriever) {
        return this.loggerLoop(expr).toString();
    }

    // main (recursive) function to instrument conditions
    public Expression loggerLoop(Expression expr) {

        if (expr instanceof BinaryExpr) { // handle || or && in clause
            BinaryExpr binExpr = (BinaryExpr) expr;
            if (binExpr.getOperator() == BinaryExpr.Operator.AND
                    || binExpr.getOperator() == BinaryExpr.Operator.OR
                    || binExpr.getOperator() == BinaryExpr.Operator.XOR) {
                // recurse through child expressions
                binExpr.setLeft(loggerLoop(binExpr.getLeft()));
                binExpr.setRight(loggerLoop(binExpr.getRight()));
                binExpr.setOperator(binExpr.getOperator());
                expr = binExpr;
            }
            else { // instrument condition
                expr = instrumentCondition(expr);
            }
        }
        else if (expr instanceof EnclosedExpr) { // extract statements enclosed in brackets
            expr = loggerLoop(((EnclosedExpr) expr).getInner());
        }
        else { // handling other types of expression
            expr = instrumentCondition(expr);
        }

        return expr;
    }

    // instrument branch
    public void instrumentBranch(IfStmt stmt, Boolean isElse) {
        // initiate statements holder
        BlockStmt block = new BlockStmt();
        block.addStatement(createMethod("logBranch", branchCount));

        // merge existing statements and logger
        if (isElse) { // for else branch
            addChildStmts(block, stmt.getElseStmt().get().getChildNodes());
            stmt.setElseStmt(block);
        }
        else { // for else-if branch
            addChildStmts(block, stmt.getThenStmt().getChildNodes());
            stmt.setThenStmt(block);
        }

        branchCount++;
    }

    // return instrumented condition
    private MethodCallExpr instrumentCondition(Expression expr) {
        MethodCallExpr method = createMethod("logCondition", conditionCount, expr);

        conditionCount++;
        return method;
    }

    // generate method logBranch(id)
    private MethodCallExpr createMethod(String name, int count) {
        MethodCallExpr method = new MethodCallExpr(new NameExpr("autoTester"), name);
        return method.addArgument(new IntegerLiteralExpr(count));
    }

    // generate method logCondition(id, condition)
    private MethodCallExpr createMethod(String name, int count, Expression expr) {
        return createMethod(name, count).addArgument(expr);
    }

    // add child nodes to compose "then" statement
    private void addChildStmts(BlockStmt block, List<Node> childNodes) {
        // use iterator/cloning to avoid concurrent errors
        Iterator<Node> nodes = (new ArrayList<>(childNodes)).iterator();
        while (nodes.hasNext()) {
            block.addStatement((Statement) nodes.next());
        }
    }
}
