package logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
public class RuleConclusions {
    private List<Atom> conclusions;

    public RuleConclusions build(Context context){
        return new RuleConclusions(
                this.conclusions.stream().map(conclusion -> conclusion.build(context)).toList()
        );
    }

    /**
     * TODO : represent into string a rule for backward planning
     * @return
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.toString();
    }
}