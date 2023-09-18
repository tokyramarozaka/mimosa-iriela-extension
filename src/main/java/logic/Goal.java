package logic;

import aStar.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Goal implements State {
    private Context goalContext;
    private List<Atom> goalPropositions;

    public Goal(List<Atom> goalPropositions){
        this.goalPropositions = goalPropositions;
        this.goalContext = new Context();
    }
}
