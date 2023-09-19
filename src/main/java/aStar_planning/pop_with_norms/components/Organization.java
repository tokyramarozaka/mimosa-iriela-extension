package aStar_planning.pop_with_norms.components;

import logic.Predicate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Organization {
    private Institution institution;
    private List<Norm> norms;
    private List<Predicate> assertions;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getInstitution().getName()).append("Org");
//
//        stringBuilder.append("- NORMS\n");
//
//        for (Norm norm : this.getNorms()) {
//            stringBuilder.append("\t").append(norm).append("\n");
//        }
//
//        stringBuilder.append("- ASSERTIONS\n");
//        for (Predicate assertion : this.getAssertions()) {
//            stringBuilder.append("\t").append(assertion).append("\n");
//        }

        return stringBuilder.toString();
    }
}