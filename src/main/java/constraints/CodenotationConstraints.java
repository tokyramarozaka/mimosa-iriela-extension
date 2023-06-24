package constraints;

import aStar.AStarResolver;
import aStar_planning.graph_planning.GraphForwardPlanningProblem;
import exception.NoPlanFoundException;
import graph.Graph;
import graph.Node;
import logic.Context;
import logic.ContextPair;
import logic.ContextualTerm;
import logic.Graphic;
import logic.Term;
import logic.Variable;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of codenotations constraints to describe variable bindings. They can either be :
 * <ul>
 *     <li>Codenotations forcing the binding between two terms, or,</li>
 *     <li>Non-codenotations forbidding the binding between two terms.</li>
 * </ul>
 *
 * @see Codenotation
 */
@Getter
@ToString
public class CodenotationConstraints extends Graphic {
    private List<Codenotation> codenotations;
    private final static Logger logger = LogManager.getLogger(CodenotationConstraints.class);


    public CodenotationConstraints(List<Codenotation> codenotations){
        super(new Graph(new ArrayList<>()));
        this.codenotations = codenotations;
    }

    public CodenotationConstraints(){
        super(new Graph(new ArrayList<>()));
        this.codenotations = new ArrayList<>();
    }

    private boolean nodeEqualsToCodenotation(Node node, ContextualTerm codenotationPart){
        return node.getName().equals(codenotationPart.toString());
    }
    /**
     * Initialize the list of nodes of the graph representation of this set of constraint.
     * @return
     */
    public List<Node> initializeNodes() {
        List<Node> nodes = new ArrayList<Node>();

        this.codenotations
                .stream()
                .filter(codenotation -> codenotation.isCodenotation())
                .forEach(codenotation -> {
                    addIfNotPresent(nodes, codenotation);
                });

        return nodes;
    }


    /**
     * Initializes the links of the codenotation graph representation. Nodes must be initialized
     * before linking them.
     */
    public void initializeLinks() {
        List<Codenotation> codenotations = this.getCodenotations()
                .stream()
                .filter(c -> c.isCodenotation())
                .collect(Collectors.toList());

        for(Codenotation codenotation : codenotations) {
            this.getGraph().getContainingNode(codenotation.getLeft())
                    .link(this.getGraph().getContainingNode(codenotation.getRight()));
        }
    }

    /**
     * Checks if the current codenotation constraints are coherent : meaning that there is no
     * contradiction, and that there is no auto-binding which would create an infinite cycle.
     *
     * @return true, if the current codenotation constraints are coherent and totally okay, false
     * otherwise.
     */
    public boolean isCoherent() {
        return this.hasNoContradictions() && this.hasNoCycles();
    }

    /**
     * <p>Checks if the set of codenotations are not cyclic, where the variable is linked to itself
     * creating an infinite cycle. This cyclic binding might be direct <i>(ex: x = y)</i> or
     * indirect <i>(x = y; y = x).</i></p>
     * <br>
     *
     * <p> A cycle exists if, given <i>(x = y)</i>, a planning from <i>y</i> to <i>x</i> succeeds in
     * the codenotation graph. </p>
     *
     * @return true if the current codenotation constraints have no cycles, false otherwise.
     */
    private boolean hasNoCycles() {
        this.updateGraph();
        List<Codenotation> codenotationsOnly = this.allCodenotations();

        for (Codenotation codenotation : codenotationsOnly) {
            AStarResolver loopPlanningProblem = new AStarResolver(new GraphForwardPlanningProblem(
                    this.getGraph().getContainingNode(codenotation.getRight()),
                    this.getGraph().getContainingNode(codenotation.getLeft())
            ));

            try{
                loopPlanningProblem.findSolution();
                return false;
            }catch(NoPlanFoundException exception){
                return true;
            }
        }

        return true;
    }

    private void updateGraph() {
        this.setGraph(new Graph(this.initializeNodes()));
        this.initializeLinks();
    }

    /**
     * Check if there are any contradictions in the current codenotation constraints. Formally,
     * a contradiction when you have for both terms A and B : A == B and A != B at the same time.
     * Codenotation being a commutative relation, order does not matter, as A == B is equal to
     * B == A.
     * @return true if there are any contradictions, false otherwise.
     */
    private boolean hasNoContradictions() {
        List<Codenotation> allCodenotations = this.allCodenotations();
        List<Codenotation> allNonCodenotations = this.allNonCodenotations();

        return allNonCodenotations
                .stream()
                .noneMatch(nonCodenotation -> nonCodenotation.matchesAny(allCodenotations));
    }

    /**
     * Returns the sublist of all codenotations only from the current codenotation constraint
     * @return all codenotations from the current codenotation constraint
     */
    private List<Codenotation> allCodenotations(){
        return this.codenotations
                .stream()
                .filter(Codenotation::isCodenotation)
                .collect(Collectors.toList());
    }

    /**
     * Returns the sublist of all non-codenotations only from the current codenotation constraint
     * @return all non-codenotations from the current codenotations constraint
     */
    private List<Codenotation> allNonCodenotations(){
        return this.codenotations
                .stream()
                .filter(codenotation -> !codenotation.isCodenotation())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the link of a Variable inside of the codenotation constraints
     * @param variable : the source variable
     * @param context : the source variable's context
     * @return the term linked to the source
     */
    public ContextualTerm getLink(Term variable, Context context) {
        ContextualTerm source = new ContextualTerm(context,variable);

        if(!isLinked(variable, context)){
            return source;
        }

        return this.codenotations
                .stream()
                .filter(codenotation -> codenotation.getLeft().equals(source))
                .findFirst()
                .get()
                .getRight();
    }

    private void addIfNotPresent(List<Node> nodes, Codenotation codenotation){
        if (nodes.stream().noneMatch(node -> nodeEqualsToCodenotation(node,codenotation.getLeft())))
        {
            nodes.add(codenotation.toNodes().get(0));
        }

        if (nodes.stream().noneMatch(node -> nodeEqualsToCodenotation(node,codenotation.getRight()))
        ){
            nodes.add(codenotation.toNodes().get(1));
        }
    }

    /**
     * Checks if a variable is linked according to the currentCodenotations. Note that
     * non-codenotation constraints does not count as a link.
     * @param variable : the source variable we want to check
     * @param context : the context of the source variable
     * @return true if the variable has is codenotated to another variable, false otherwise.
     */
    public boolean isLinked(Term variable, Context context) {
        ContextualTerm toTest = new ContextualTerm(context, variable);

        return this.codenotations
                .stream().anyMatch(codenotation -> codenotation.getLeft().equals(toTest)
                        && codenotation.isCodenotation());
    }

    /**
     * Adds a new codenotation constraint to the set of already existing codenotation constraints.
     * @param left : the left part of the new codenotation
     * @param leftContext : the context of the left part of the new codenotation
     * @param right : the right part of the new codenotation
     * @param rightContext : the context of the right part of the new codenotation
     */
    public void link(Variable left, Context leftContext, Term right, Context rightContext) {
        this.codenotations.add(new Codenotation(
                true,
                new ContextualTerm(leftContext, left),
                new ContextualTerm(rightContext, right)
        ));
    }

    /**
     * Removes all bindings of the given term within the current codenotation constraints
     * @param toRemove : the term to remove
     * @return true, if any elements were removed, and false otherwise.
     */
    public void unlink(ContextualTerm toRemove){
        this.getCodenotations().removeIf(
            codenotation -> codenotation.getLeft().equals(toRemove)
        );
    }

    /**
     * Get the non-codenoted term for a given term, if any
     * @param term
     * @param context
     * @return all non codenotation constraints amongst the current set
     */
    public List<ContextualTerm> getNegativeLinks(Term term, Context context){
        ContextualTerm contextualTerm = new ContextualTerm(context, term);
        return codenotations.stream()
                .filter(codenotation ->
                        codenotation.getLeft().getTerm().equals(contextualTerm.getTerm())
                        && codenotation.getLeft().getContext().equals(contextualTerm.getContext())
                        && !codenotation.isCodenotation())
                .map(Codenotation::getRight)
                .collect(Collectors.toList());
    }

    public boolean isNegativelyLinked(ContextualTerm contextualTerm){
        return this.getCodenotations().stream()
                .anyMatch(codenotation -> codenotation.getLeft().toString().equals(contextualTerm.toString())
                && !codenotation.isCodenotation());
    }

    /**
     * Checks if a given (non)codenotation would make the current codenotations contradict itself.
     * @param toAdd : the codenotation we want to check without adding
     * @return true, if the codenotation does not break the current constraint, false otherwise.
     */
    public boolean isAllowed(Codenotation toAdd) {
        if(this.getCodenotations().stream()
                .anyMatch(codenotation -> codenotation.toString().equals(toAdd.toString()))
        ){
            return true;
        }

        Term term = toAdd.getLeft().getTerm();
        Context context = toAdd.getLeft().getContext();

        if(isLinked(term, context)) {
            ContextualTerm link = getLink(term, context);
            if(toAdd.isCodenotation()) {
                if (link.getTerm() instanceof Variable) {
                    return link.getTerm().testEqual(
                            link.getContext(),
                            toAdd.getRight().getTerm(),
                            toAdd.getRight().getContext(),
                            this
                    );
                } else {
                    return link.getTerm().sameName(term);
                }
            }

            return !toAdd.getRight().getTerm().testEqual(
                    toAdd.getRight().getContext(),
                    link.getTerm(),
                    link.getContext(),
                    this
            );
        }

        if(this.isNegativelyLinked(toAdd.getLeft())) {
            for (ContextualTerm nonCodenotation : getNegativeLinks(term, context)) {
                if (toAdd.getRight().getTerm().testEqual(
                        toAdd.getRight().getContext(),
                        nonCodenotation.getTerm(),
                        nonCodenotation.getContext())
                ){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fuses two codenotation constraints into a new codenotations constraint, using shallow copies
     * of their values (meaning that they copy the original codenotations)
     * @param other : the codenotation constraints that we want to fuse with
     * @return codenotation constraints combining both codenotations from the current and the other
     * codenotation constraints
     */
    public CodenotationConstraints fuseWith(CodenotationConstraints other){
        List<Codenotation> codenotations = new ArrayList<>();

        codenotations.addAll(this.getCodenotations().stream().toList());
        codenotations.addAll(other.getCodenotations().stream().toList());

        return new CodenotationConstraints(codenotations);
    }

    public CodenotationConstraints copy(){
        return new CodenotationConstraints(this.getCodenotations().stream()
                .collect(Collectors.toList()));
    }
}
