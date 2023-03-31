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

    @Override
    public String toString() {
        return String.valueOf(this.getConclusions());
    }
}