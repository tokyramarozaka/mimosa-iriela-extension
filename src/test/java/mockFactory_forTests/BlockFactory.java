package mockFactory_forTests;

import logic.Constant;
import logic.Term;
import logic.Variable;

import java.security.InvalidParameterException;

public class BlockFactory {
    public static Constant createConstant(String blockName){
        return new Constant("Block "+blockName);
    }

    public static Variable createVariable(String blockName){
        return new Variable("Block "+blockName);
    }

    public static Term create(String type, String blockName){
        if(type.equals("CONSTANT")){
            return createConstant(blockName);
        }else if (type.equals("VARIABLE")){
            return createVariable(blockName);
        }else{
            throw new InvalidParameterException("Block must either be a variable or a Constant");
        }
    }
}
