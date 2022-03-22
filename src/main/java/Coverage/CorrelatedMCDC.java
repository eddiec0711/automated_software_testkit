package Coverage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author eddie
 * Coverage.CorrelatedMCDC.java
 *
 * compute correlated MCDC
 */

public class CorrelatedMCDC {

    private Map<Integer, HashSet<Integer>> loggers;
    private ArrayList<HashMap<Integer, HashMap<Integer, Boolean>>> logHistory;

    // condition : branch
    private Map<Integer, Integer> relations;

    private HashMap<HashMap<Integer, Boolean>, HashMap<Integer, Boolean>> covered = new HashMap<>();

    public CorrelatedMCDC(HashMap<Integer, HashSet<Integer>> loggers,
                          ArrayList<HashMap<Integer, HashMap<Integer, Boolean>>> logHistory) {
        this.loggers = loggers;
        this.logHistory = logHistory;
    }


    public void getMCDCoverage() {

        setRelations();

        computeCovered();

        computeCoverage();

    }

    // record relations between parents branch and child condition
    private void setRelations() {
        relations = new HashMap<>();
        for (int branchId : loggers.keySet()) {
            for (Integer logger : loggers.get(branchId)) {
                relations.put(Integer.valueOf(logger), branchId);
            }
        }
    }

    // compute covered requirements
    private void computeCovered() {
        // branchId : (conditionId : conditionEval)
        for (HashMap<Integer, HashMap<Integer, Boolean>> branchHistory : logHistory) {

            // only contain one element, improvement?
            for (int branchId : branchHistory.keySet()) {

                // conditionId : conditionEval
                HashMap<Integer, Boolean> conditionHistory = branchHistory.get(branchId);
                for (int conditionId : conditionHistory.keySet()) {

                    // initiate parent branch and child condition evaluation
                    HashMap<Integer, Boolean> branchEval = new HashMap<>();
                    HashMap<Integer, Boolean> conditionEval = new HashMap<>();

                    // if branch not a outcome of the combination of child conditions, branch == false
                    Boolean branchRes;
                    if (relations.get(conditionId) != branchId) {
                        branchRes = false;
                    }
                    else {
                        branchRes = true;
                    }

                    // record branch and condition evaluation
                    conditionEval.put(conditionId, conditionHistory.get(conditionId));
                    branchEval.put(relations.get(conditionId), branchRes);

                    if (!covered.containsKey(branchEval)) {
                        covered.put(branchEval, conditionEval);
                    }
                    else {
                        covered.get(branchEval).put(conditionId, conditionHistory.get(conditionId));
                    }
                }
            }
        }
    }

    // compute left out requirements
    private void computeCoverage() {

        Boolean complete = true;

        // for each branch
        for (int branchId : loggers.keySet()) {

            // check if branch has been evaluated as true and false
            HashMap<Integer, Boolean> branchReq1 = new HashMap<>();
            branchReq1.put(branchId, true);

            HashMap<Integer, Boolean> branchReq2 = new HashMap<>();
            branchReq2.put(branchId, false);

            if (covered.containsKey(branchReq1) && covered.containsKey(branchReq2)) {

                // conditions from true branch covered
                HashMap<Integer, Boolean> trueBranch = covered.get(branchReq1);

                // conditions from false branch covered
                HashMap<Integer, Boolean> falseBranch = covered.get(branchReq2);

                // for each child conditions
                for (int conditionId : loggers.get(branchId)) {

                    // collect covered conditions
                    Map<Integer, Boolean> trueConditions = trueBranch.entrySet().stream()
                                    .filter(id -> id.getKey() == conditionId)
                                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
                    Map<Integer, Boolean> falseConditions = falseBranch.entrySet().stream()
                            .filter(id -> id.getKey() == conditionId)
                            .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

                    if ((trueConditions.size() > 1 && falseConditions.size() > 1) ||
                            trueConditions.size() > 1 && falseConditions.size() == 1 ||
                            trueConditions.size() == 1 && falseConditions.size() > 1) {
                        continue;
                    }
                    else if (trueConditions.size() == 1 && falseConditions.size() == 1) {
                        if (trueConditions.equals(falseConditions)) {
                            // conditions overlap
                            System.out.println("Condition " + conditionId + " in branch " + branchId + " not fully evaluated");
                            complete = false;
                        }
                    } else {
                        // one of the condition requirements not covered
                        System.out.println("one of the requirements not covered");
                        complete = false;
                    }
                }
            }
            else {
                System.out.println("branch " + branchId + " never visited/not fully covered");
            }
        }
    }
}
