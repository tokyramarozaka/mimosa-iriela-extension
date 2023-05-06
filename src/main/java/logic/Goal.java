package logic;

import aStar.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class Goal implements State {
    private Context goalContext;
    private List<Atom> goalPropositions;
}
