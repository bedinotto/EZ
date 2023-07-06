package recovery;

import parser.*;

// import java.util.*;


public class First { //implementa os conjuntos first p/ alguns n.terminais

    static public final RecoverySet methoddecl = new RecoverySet();
    static public final RecoverySet vardecl = new RecoverySet();
    static public final RecoverySet classlist = new RecoverySet();
    static public final RecoverySet constructdecl = new RecoverySet();
    static public final RecoverySet statlist = new RecoverySet();
    static public final RecoverySet program = classlist;

    static {
        methoddecl.add(new Integer(ezConstants.INT));
        methoddecl.add(new Integer(ezConstants.STRING));
        methoddecl.add(new Integer(ezConstants.IDENT));

        vardecl.add(new Integer(ezConstants.INT));
        vardecl.add(new Integer(ezConstants.STRING));
        vardecl.add(new Integer(ezConstants.IDENT));

        classlist.add(new Integer(ezConstants.CLASS));

        constructdecl.add(new Integer(ezConstants.CONSTRUCTOR));

        statlist.addAll(vardecl);
        statlist.add(new Integer(ezConstants.IDENT)); // first do atribstat
        statlist.add(new Integer(ezConstants.PRINT));
        statlist.add(new Integer(ezConstants.READ));
        statlist.add(new Integer(ezConstants.RETURN));
        statlist.add(new Integer(ezConstants.SUPER));
        statlist.add(new Integer(ezConstants.IF));
        statlist.add(new Integer(ezConstants.FOR));
        statlist.add(new Integer(ezConstants.LBRACE));
        statlist.add(new Integer(ezConstants.BREAK));
        statlist.add(new Integer(ezConstants.SEMICOLON));
    }
}
