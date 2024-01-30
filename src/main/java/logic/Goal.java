package logic;

import aStar.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class Goal implements State {
    private Context goalContext;
    private List<Atom> goalPropositions;

    public Goal(List<Atom> goalPropositions){
        this.goalPropositions = goalPropositions;
        this.goalContext = new Context();
    }

    public Goal(){
        this.goalPropositions = new ArrayList<>();
        this.goalContext = new Context();
    }

    @Override
    public String toGraphNode() {
        return this.toString();
    }
}
