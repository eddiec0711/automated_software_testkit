package TestFileGenerator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.IfStmt;

import java.io.IOException;
import java.util.List;

/**
 * @author eddie
 * TestFileGenerator.MethodModifier.java
 *
 * take in a method as parameter
 * read all condition statements within method
 * pass them to TestFileGenerator.Logger for instrumentation
 */

public class MethodModifier {

    private static Logger logger = new Logger();

    // search for "if" statements in given method
    public static void analyseMethod(MethodDeclaration method) throws IOException {

        // find all if statements to instrument
        List<IfStmt> ifStatements = method.getChildNodesByType(IfStmt.class);
        for (IfStmt ifstmt: ifStatements) {
            modifyClause(ifstmt);
        }
    }

    // instrument conditions and branch
    private static void modifyClause(IfStmt stmt) {

        // collect conditions and instrument them
        stmt.setCondition(logger.loggerLoop(stmt.getCondition()));

        // instrument if/else-if branch
        logger.instrumentBranch(stmt, false);

        // instrument else branch
        if (!stmt.hasCascadingIfStmt() && stmt.hasElseBranch()) {
            logger.instrumentBranch(stmt, true);
        }
    }

}


