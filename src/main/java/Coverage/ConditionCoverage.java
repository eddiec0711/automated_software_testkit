package Coverage;

import java.util.*;

/**
 * @author eddie
 * Coverage.ConditionCoverage.java
 *
 * log and compute condition coverage
 *
 */

public class ConditionCoverage {

    private HashMap<Integer, HashSet<Integer>> loggers;
    private HashMap<Integer, Set<Boolean>> logHistory;

    public ConditionCoverage(HashMap<Integer, HashSet<Integer>> loggers) {
        this.loggers = loggers;
        logHistory = new HashMap<>();
    }

    // record visited condition and outcome
    public void logCondition(int id, Boolean condition) {
        if (!logHistory.containsKey(id)) {
            logHistory.put(id, new HashSet<>());
        }
        logHistory.get(id).add(condition);
    }

    // discover conditions which are not visited/fully evaluated
    public void getCoverage() {
        Boolean complete = true;
        int uncovered = 0;
        int total = 0;

        // for each branch logger
        for (int branchId : loggers.keySet()) {

            // for each condition logger
            for (int conditionId : loggers.get(branchId)) {
                total+=2;

                Set<Boolean> req = new HashSet<>(Arrays.asList(true, false));

                if (logHistory.containsKey(conditionId)) {
                    req.removeAll(logHistory.get(conditionId));

                    // requirements not fulfilled
                    if (!req.isEmpty()) {
                        complete = false;
                        System.out.println("Condition " + conditionId + " never evaluated as " + req);
                        uncovered++;
                    }
                }
                else {
                    complete = false;
                    System.out.println("Condition " + conditionId + " never visited");
                    uncovered+=2;
                }
            }
        }

        if (complete) {
            System.out.println("All conditions has been covered");
        }
        else {
            System.out.println(uncovered + " out of " + total + " requirements uncovered");
        }
    }

}
