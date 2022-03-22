package Coverage;

import java.util.*;

/**
 * @author eddie
 * Coverage.BranchCoverage.java
 *
 * log and compute branch coverage
 *
 */

public class BranchCoverage {

    private HashMap<Integer, HashSet<Integer>> loggers;
    private HashSet<Integer> logHistory;

    public BranchCoverage(HashMap<Integer, HashSet<Integer>> loggers) {
        this.loggers = loggers;
        logHistory = new HashSet<>();
    }

    // record visited branch
    public void logBranch(int id) {
        if (!logHistory.contains(id)) {
            logHistory.add(id);
        }
    }

    // discover uncovered branches
    public void getCoverage() {
        Boolean complete = true;

        // for each branch
        for (int branchId : loggers.keySet()) {

            // if not recorded
            if (!logHistory.contains(branchId)){
                complete = false;
                System.out.println("Branch " + branchId + " never visited");
            }
        }

        if (complete) {
            System.out.println("All branches has been covered");
        }
    }
}
