package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * A set of codenotations
 *
 * @see logic.Codenotation
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class CodenotationConstraints {
    private List<Codenotation> codenotations;

    public boolean isCoherent() {

    }
}
