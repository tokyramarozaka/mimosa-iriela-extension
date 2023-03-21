package logic;

import graph.Graph;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public abstract class Graphic {
    private Graph graph;

    public Graphic(){
        this.graph = new Graph();
    }
}
