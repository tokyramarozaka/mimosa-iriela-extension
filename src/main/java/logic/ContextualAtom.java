package logic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ContextualAtom {
    private Context context;
    private Atom atom;

    public boolean isVerified(Situation toTest) {
        if(this.getAtom().isNegation()){
            return !this.toContextualPredicate().isVerified(toTest);
        }
        return this.toContextualPredicate().isVerified(toTest);
    }

    public ContextualPredicate toContextualPredicate(){
        return new ContextualPredicate(this.getContext(),this.getAtom().getPredicate());
    }
}