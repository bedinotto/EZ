package codegen;

import parser.*;

public class GenCodeException extends Exception{
    GenCodeException(Token token, String msg){
        super("Code Generation Error: " + msg + " at line " + token.beginLine + ", column " + token.beginColumn);
    }

    GenCodeException(String msg){
        super("Code Generation Error: " + msg);
    }
}
