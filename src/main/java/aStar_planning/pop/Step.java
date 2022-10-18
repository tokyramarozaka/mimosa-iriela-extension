package aStar_planning.pop;

import logic.LogicalInstance;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Step implements PartiallyOrderedElement{
    private LogicalInstance actionInstance;
}
