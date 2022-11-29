package mockFactory_forTests;

import logic.Atom;
import logic.Rule;
import logic.RuleConclusions;
import logic.RuleConditions;
import logic.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RulesFactory {
    public static Rule holdRule(Variable x1, Variable y1, Variable w1, Variable z1){
        RuleConditions holdCondition = new RuleConditions(Arrays.asList(
                new Atom(false, PredicateFactory.hold(x1))
        ));
        RuleConclusions holdRuleConclusions = new RuleConclusions(Arrays.asList(
                new Atom(true, PredicateFactory.emptyArm),
                new Atom(true, PredicateFactory.on(x1,y1)),
                new Atom(true, PredicateFactory.on(w1,x1)),
                new Atom(true, PredicateFactory.hold(z1))
        ));

        return new Rule("hold_rule", holdCondition, holdRuleConclusions);
    }

    public static Rule onRule(Variable x1, Variable y1){
        RuleConditions onCondition = new RuleConditions(Arrays.asList(
                new Atom(false, PredicateFactory.on(x1,y1))
        ));
        RuleConclusions onRuleConclusions = new RuleConclusions(Arrays.asList(
                new Atom(true, PredicateFactory.free(y1)),
                new Atom(true, PredicateFactory.hold(x1)),
                new Atom(true, PredicateFactory.hold(y1))
        ));

        return new Rule("on_rule", onCondition, onRuleConclusions);
    }

    public static Rule onTableRule(Variable x1, Variable y1){
        RuleConditions onTableCondition = new RuleConditions(Arrays.asList(
                new Atom(false, PredicateFactory.onTable(x1))
        ));
        RuleConclusions onTableRuleConclusions = new RuleConclusions(Arrays.asList(
                new Atom(true, PredicateFactory.on(x1,y1))
        ));

        return new Rule("onTable_rule", onTableCondition, onTableRuleConclusions);
    }

    public static Rule freeRule(Variable x1, Variable y1){
        RuleConditions freeRuleConditions = new RuleConditions(Arrays.asList(
                new Atom(false, PredicateFactory.free(x1))
        ));
        RuleConclusions freeRuleConclusions = new RuleConclusions(Arrays.asList(
                new Atom(true, PredicateFactory.on(y1,x1)),
                new Atom(true, PredicateFactory.hold(x1))
        ));

        return new Rule("free_rule",freeRuleConditions, freeRuleConclusions);
    }

    public static List<Rule> allRules__BlocksWorld()
    {
        List<Rule> rules = new ArrayList<>();
        Variable blockW = BlockFactory.createVariable("W");
        Variable blockX = BlockFactory.createVariable("X");
        Variable blockY = BlockFactory.createVariable("Y");
        Variable blockZ = BlockFactory.createVariable("Z");

        rules.add(holdRule(blockX, blockY, blockW, blockZ));
        rules.add(onRule(blockX, blockY));
        rules.add(onTableRule(blockX, blockY));
        rules.add(freeRule(blockX, blockY));

        return rules;
    }

}
