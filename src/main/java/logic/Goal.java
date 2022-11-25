package logic;

import aStar.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Goal implements State {
    private Context goalContext;
    private List<Atom> goalPropositions;
}
