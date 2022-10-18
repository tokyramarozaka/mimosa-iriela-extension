package logic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@EqualsAndHashCode
@ToString
public abstract class Term implements Unifiable{
    private String name;
}
