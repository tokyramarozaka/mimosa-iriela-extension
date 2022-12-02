package mock_blocks;

import logic.Constant;
import logic.Variable;

public class Blocks {
    public static Constant A = (Constant) BlockFactory.create("CONSTANT","A");
    public static Constant B = (Constant) BlockFactory.create("CONSTANT","B");
    public static Constant C = (Constant) BlockFactory.create("CONSTANT","C");

    public static Variable X = (Variable) BlockFactory.create("VARIABLE","X");
    public static Variable Y = (Variable) BlockFactory.create("VARIABLE","Y");
    public static Variable Z = (Variable) BlockFactory.create("VARIABLE","Z");
}
