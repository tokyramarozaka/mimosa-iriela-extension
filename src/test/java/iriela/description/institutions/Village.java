package iriela.description.institutions;

import aStar_planning.pop_with_norms.components.Institution;
import aStar_planning.pop_with_norms.components.Norm;
import aStar_planning.pop_with_norms.components.Role;
import aStar_planning.pop_with_norms.components.RoleActions;
import aStar_planning.pop_with_norms.concepts.ActionName;
import iriela.description.TermsFactory;
import logic.Action;
import logic.Constant;
import logic.Predicate;
import logic.Term;

import java.util.List;

/**
 * A class to provide all the basic roles present within the model
 */
public class Village {
    // CONCEPTS
    public static final Role sacred = new Role("sacred");
    public static final Role _protected = new Role("_protected");
    public static final Role member = new Role("member");
    public static final Role office = new Role("office");
    public static final ActionName reportCut = new ActionName("reportCut");
    public static final ActionName getFishingLicense = new ActionName("getFishingLicense");
    public static final ActionName getFishingNet = new ActionName("getFishingNet");
    public static final ActionName getExploitationLicense = new ActionName(
            "getExploitationLicense"
    );

    // INSTITUTION
    public static Institution get() {
        return new Institution(
                "village",
                7,
                Village.concepts(),
                Village.assertions(),
                Village.roleActions(),
                Village.norms()
        );
    }

    private static List<Norm> norms() {
        return List.of(
                VillageNorms.mandatoryCutReport(TermsFactory.X),
                VillageNorms.locatedInSacred(TermsFactory.X, TermsFactory.Z),
                VillageNorms.fishWithoutLicense(TermsFactory.X, TermsFactory.Z),
                VillageNorms.cutWithLicense(TermsFactory.X, TermsFactory.Z),
                VillageNorms.haveFishingNetWithLicense(TermsFactory.X)
        );
    }


    private static List<Constant> concepts() {
        return List.of(
                sacred,
                _protected,
                member,
                office,
                reportCut,
                getFishingLicense,
                getExploitationLicense,
                getFishingNet
        );
    }

    private static List<Predicate> assertions() {
        return List.of(
                haveFishingLicense(TermsFactory.X),
                haveExploitationLicense(TermsFactory.X),
                filledCutReport(TermsFactory.X),
                haveFishingNet(TermsFactory.X)
        );
    }

    private static List<Action> memberActions() {
        return List.of(
                VillageActions.reportCut(TermsFactory.X),
                VillageActions.getExploitationLicense(TermsFactory.X, TermsFactory.Z),
                VillageActions.getFishingLicense(TermsFactory.X, TermsFactory.Z),
                VillageActions.getFishingNet(TermsFactory.X)
        );
    }

    // CHECK PREDICATES
    public static Predicate sacred(Term subject) {
        return new Predicate("sacred", List.of(subject));
    }

    public static Predicate _protected(Term subject) {
        return new Predicate("_protected", List.of(subject));
    }

    public static Predicate office(Term subject) {
        return new Predicate("office", List.of(subject));
    }

    public static Predicate member(Term subject) {
        return new Predicate("member", List.of(subject));
    }

    public static Predicate haveFishingLicense(Term subject) {
        return new Predicate("haveFishingLicense", List.of(subject));
    }

    public static Predicate haveExploitationLicense(Term subject) {
        return new Predicate("haveExploitationLicense", List.of(subject));
    }

    public static Predicate filledCutReport(Term subject) {
        return new Predicate("filledCutReport", List.of(subject));
    }

    public static Predicate haveFishingNet(Term subject) {
        return new Predicate("haveFishingNet", List.of(subject));
    }

    private static List<RoleActions> roleActions() {
        return List.of(forMember());
    }

    private static RoleActions forMember() {
        return new RoleActions(member, memberActions());
    }
}
