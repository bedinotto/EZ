package syntacticTree;

public class PrintTree {
    int kk;

    public PrintTree() {
        kk = 1; 
    }

    public void printRoot(ListNode node) {
        if (node == null) {
            System.out.println("Empty syntatic tree. Nothing to be printed");
        } else {
            numberClassDeclListNode(node);
            printClassDeclListNode(node);
        }

        System.out.println();
    }

    
    public void numberClassDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberClassDeclNode((ClassDeclNode) node.node);
        numberClassDeclListNode(node.next);
    }

    public void printClassDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ListNode (ClassDeclNode)  ===> " +
            node.node.number + " " +
            ((node.next == null) ? "null" : String.valueOf(node.next.number)));

        printClassDeclNode((ClassDeclNode) node.node);
        printClassDeclListNode(node.next);
    }

    
    public void numberClassDeclNode(ClassDeclNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberClassBodyNode(node.body);
    }

    public void printClassDeclNode(ClassDeclNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ClassDeclNode ===> " + node.name.image +
            " " + ((node.supername == null) ? "null" : node.supername.image) + " " +
            ((node.body == null) ? "null" : String.valueOf(node.body.number)));

        printClassBodyNode(node.body);
    }

    
    public void numberClassBodyNode(ClassBodyNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberClassDeclListNode(node.clist);
        numberVarDeclListNode(node.vlist);
        numberConstructDeclListNode(node.ctlist);
        numberMethodDeclListNode(node.mlist);
    }

    public void printClassBodyNode(ClassBodyNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ClassBodyNode ===> " +
            ((node.clist == null) ? "null" : String.valueOf(node.clist.number)) +
            " " + ((node.vlist == null) ? "null" : String.valueOf(node.vlist.number)) +
            " " +
            ((node.ctlist == null) ? "null" : String.valueOf(node.ctlist.number)) +
            " " + ((node.mlist == null) ? "null" : String.valueOf(node.mlist.number)));

        printClassDeclListNode(node.clist);
        printVarDeclListNode(node.vlist);
        printConstructDeclListNode(node.ctlist);
        printMethodDeclListNode(node.mlist);
    }

    
    public void numberVarDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberVarDeclNode((VarDeclNode) node.node);
        numberVarDeclListNode(node.next);
    }

    public void printVarDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ListNode (VarDeclNode) ===> " +
            node.node.number + " " +
            ((node.next == null) ? "null" : String.valueOf(node.next.number)));

        printVarDeclNode((VarDeclNode) node.node);
        printVarDeclListNode(node.next);
    }

    
    public void numberVarDeclNode(VarDeclNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numbervarListNode(node.vars);
    }

    public void printVarDeclNode(VarDeclNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": VarDeclNode ===> " + node.position.image +
            " " + node.vars.number);
        printvarListNode(node.vars);
    }

    
    public void numbervarListNode(ListNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberVarNode((VarNode) node.node);
        numbervarListNode(node.next);
    }

    public void printvarListNode(ListNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ListNode (VarNode) ===> " +
            node.node.number + " " +
            ((node.next == null) ? "null" : String.valueOf(node.next.number)));

        printVarNode((VarNode) node.node);
        printvarListNode(node.next);
    }

    
    public void numberConstructDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberConstructDeclNode((ConstructDeclNode) node.node);
        numberConstructDeclListNode(node.next);
    }

    public void printConstructDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ListNode (ConstructDeclNode) ===> " +
            node.node.number + " " +
            ((node.next == null) ? "null" : String.valueOf(node.next.number)));

        printConstructDeclNode((ConstructDeclNode) node.node);
        printConstructDeclListNode(node.next);
    }

    
    public void numberConstructDeclNode(ConstructDeclNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberMethodBodyNode(node.body);
    }

    public void printConstructDeclNode(ConstructDeclNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ConstructDeclNode ===> " +
            node.body.number);
        printMethodBodyNode(node.body);
    }

    
    public void numberMethodDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberMethodDeclNode((MethodDeclNode) node.node);
        numberMethodDeclListNode(node.next);
    }

    public void printMethodDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ListNode (MethodDeclNode) ===> " +
            node.node.number + " " +
            ((node.next == null) ? "null" : String.valueOf(node.next.number)));
        printMethodDeclNode((MethodDeclNode) node.node);
        printMethodDeclListNode(node.next);
    }

    
    public void numberMethodDeclNode(MethodDeclNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberMethodBodyNode(node.body);
    }

    public void printMethodDeclNode(MethodDeclNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": MethodDeclNode ===> " +
            node.position.image + " " + ((node.dim == 0) ? "" : ("[" + node.dim + "] ")) +
            node.name.image + " " + node.body.number);
        printMethodBodyNode(node.body);
    }

    
    public void numberMethodBodyNode(MethodBodyNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberVarDeclListNode(node.param);
        numberStatementNode(node.stat);
    }

    public void printMethodBodyNode(MethodBodyNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": MethodBodyNode ===> " +
            ((node.param == null) ? "null" : String.valueOf(node.param.number)) +
            " " + node.stat.number);
        printVarDeclListNode(node.param);
        printStatementNode(node.stat);
    }

    
    public void numberBlockNode(BlockNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberStatementListNode(node.stats);
    }

    public void printBlockNode(BlockNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": BlockNode ===> " + node.stats.number);
        printStatementListNode(node.stats);
    }

    
    public void numberStatementListNode(ListNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberStatementNode((StatementNode) node.node);
        numberStatementListNode(node.next);
    }

    public void printStatementListNode(ListNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ListNode (StatementNode) ===> " +
            node.node.number + " " +
            ((node.next == null) ? "null" : String.valueOf(node.next.number)));

        printStatementNode((StatementNode) node.node);
        printStatementListNode(node.next);
    }

    
    public void numberPrintNode(PrintNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr);
    }

    public void printPrintNode(PrintNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": PrintNode ===> " + node.expr.number);
        printExpreNode(node.expr);
    }

    
    public void numberReadNode(ReadNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr);
    }

    public void printReadNode(ReadNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ReadNode ===> " + node.expr.number);
        printExpreNode(node.expr);
    }

    
    public void numberReturnNode(ReturnNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr);
    }

    public void printReturnNode(ReturnNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ReturnNode ===> " +
            ((node.expr == null) ? "null" : String.valueOf(node.expr.number)));
        printExpreNode(node.expr);
    }

    
    public void numberSuperNode(SuperNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreListNode(node.args);
    }

    public void printSuperNode(SuperNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": SuperNode ===> " +
            ((node.args == null) ? "null" : String.valueOf(node.args.number)));
        printExpreListNode(node.args);
    }

    
    public void numberAtribNode(AtribNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr1);
        numberExpreNode(node.expr2);
    }

    public void printAtribNode(AtribNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": AtribNode ===> " + node.expr1.number + " " +
            node.expr2.number);
        printExpreNode(node.expr1);
        printExpreNode(node.expr2);
    }

    
    public void numberIfNode(IfNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr);
        numberStatementNode(node.stat1);
        numberStatementNode(node.stat2);
    }

    public void printIfNode(IfNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": IfNode ===> " + node.expr.number + " " +
            node.stat1.number + " " +
            ((node.stat2 == null) ? "null" : String.valueOf(node.stat2.number)));

        printExpreNode(node.expr);
        printStatementNode(node.stat1);
        printStatementNode(node.stat2);
    }

    
    public void numberForNode(ForNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberAtribNode(node.init);
        numberExpreNode(node.expr);
        numberAtribNode(node.incr);
        numberStatementNode(node.stat);
    }

    public void printForNode(ForNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ForNode ===> " +
            ((node.init == null) ? "null" : String.valueOf(node.init.number)) + " " +
            ((node.expr == null) ? "null" : String.valueOf(node.expr.number)) + " " +
            ((node.incr == null) ? "null" : String.valueOf(node.incr.number)) + " " +
            node.stat.number);

        printAtribNode(node.init);
        printExpreNode(node.expr);
        printAtribNode(node.incr);
        printStatementNode(node.stat);
    }

    
    public void numberBreakNode(BreakNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
    }

    public void printBreakNode(BreakNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": BreakNode");
    }

    
    public void numberNopNode(NopNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
    }

    public void printNopNode(NopNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": NopNode");
    }

    
    public void numberNewObjectNode(NewObjectNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreListNode(node.args);
    }

    public void printNewObjectNode(NewObjectNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": NewObjectNode ===> " + node.name.image +
            " " + ((node.args == null) ? "null" : String.valueOf(node.args.number)));

        printExpreListNode(node.args);
    }

    
    public void numberNewArrayNode(NewArrayNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreListNode(node.dims);
    }

    public void printNewArrayNode(NewArrayNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": NewArrayNode ===> " + node.name.image +
            " " + ((node.dims == null) ? "null" : String.valueOf(node.dims.number)));

        printExpreListNode(node.dims);
    }

    
    public void numberExpreListNode(ListNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode((ExpreNode) node.node);
        numberExpreListNode(node.next);
    }

    public void printExpreListNode(ListNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": ListNode (ExpreNode) ===> " +
            node.node.number + " " +
            ((node.next == null) ? "null" : String.valueOf(node.next.number)));
        printExpreNode((ExpreNode) node.node);
        printExpreListNode(node.next);
    }

    
    public void numberRelationalNode(RelationalNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr1);
        numberExpreNode(node.expr2);
    }

    public void printRelationalNode(RelationalNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": RelationalNode ===> " + node.expr1.number +
            " " + node.position.image + " " + node.expr2.number);
        printExpreNode(node.expr1);
        printExpreNode(node.expr2);
    }

    
    public void numberAddNode(AddNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr1);
        numberExpreNode(node.expr2);
    }

    public void printAddNode(AddNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": AddNode ===> " + node.expr1.number + " " +
            node.position.image + " " + node.expr2.number);
        printExpreNode(node.expr1);
        printExpreNode(node.expr2);
    }

    
    public void numberMultNode(MultNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr1);
        numberExpreNode(node.expr2);
    }

    public void printMultNode(MultNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": MultNode ===> " + node.expr1.number + " " +
            node.position.image + " " + node.expr2.number);
        printExpreNode(node.expr1);
        printExpreNode(node.expr2);
    }

    
    public void numberUnaryNode(UnaryNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr);
    }

    public void printUnaryNode(UnaryNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": UnaryNode ===> " + node.position.image +
            " " + node.expr.number);
        printExpreNode(node.expr);
    }

    
    public void numberIntConstNode(IntConstNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
    }

    public void printIntConstNode(IntConstNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": IntConstNode ===> " + node.position.image);
    }

    
    public void numberStringConstNode(StringConstNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
    }

    public void printStringConstNode(StringConstNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": StringConstNode ===> " +
            node.position.image);
    }

    
    public void numberNullConstNode(NullConstNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
    }

    public void printNullConstNode(NullConstNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": NullConstNode ===> " + node.position.image);
    }

    
    public void numberVarNode(VarNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
    }

    public void printVarNode(VarNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": VarNode ===> " + node.position.image + " " +
            ((node.dim == 0) ? "" : ("[" + node.dim + "]")));
    }

    
    public void numberCallNode(CallNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr);
        numberExpreListNode(node.args);
    }

    public void printCallNode(CallNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": CallNode ===> " + node.expr.number + " " +
            node.meth.image + " " +
            ((node.args == null) ? "null" : String.valueOf(node.args.number)));
        printExpreNode(node.expr);
        printExpreListNode(node.args);
    }

    
    public void numberIndexNode(IndexNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr1);
        numberExpreNode(node.expr2);
    }

    public void printIndexNode(IndexNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": IndexNode ===> " + node.expr1.number + " " +
            node.expr2.number);
        printExpreNode(node.expr1);
        printExpreNode(node.expr2);
    }

    
    public void numberDotNode(DotNode node) {
        if (node == null) {
            return;
        }

        node.number = kk++;
        numberExpreNode(node.expr);
    }

    public void printDotNode(DotNode node) {
        if (node == null) {
            return;
        }

        System.out.println();
        System.out.print(node.number + ": DotNode ===> " + node.expr.number + " " +
            node.field.image);
        printExpreNode(node.expr);
    }

    
    public void printExpreNode(ExpreNode node) {
        if (node instanceof NewObjectNode) {
            printNewObjectNode((NewObjectNode) node);
        } else if (node instanceof NewArrayNode) {
            printNewArrayNode((NewArrayNode) node);
        } else if (node instanceof RelationalNode) {
            printRelationalNode((RelationalNode) node);
        } else if (node instanceof AddNode) {
            printAddNode((AddNode) node);
        } else if (node instanceof MultNode) {
            printMultNode((MultNode) node);
        } else if (node instanceof UnaryNode) {
            printUnaryNode((UnaryNode) node);
        } else if (node instanceof CallNode) {
            printCallNode((CallNode) node);
        } else if (node instanceof IntConstNode) {
            printIntConstNode((IntConstNode) node);
        } else if (node instanceof StringConstNode) {
            printStringConstNode((StringConstNode) node);
        } else if (node instanceof NullConstNode) {
            printNullConstNode((NullConstNode) node);
        } else if (node instanceof IndexNode) {
            printIndexNode((IndexNode) node);
        } else if (node instanceof DotNode) {
            printDotNode((DotNode) node);
        } else if (node instanceof VarNode) {
            printVarNode((VarNode) node);
        }
    }

    public void numberExpreNode(ExpreNode node) {
        if (node instanceof NewObjectNode) {
            numberNewObjectNode((NewObjectNode) node);
        } else if (node instanceof NewArrayNode) {
            numberNewArrayNode((NewArrayNode) node);
        } else if (node instanceof RelationalNode) {
            numberRelationalNode((RelationalNode) node);
        } else if (node instanceof AddNode) {
            numberAddNode((AddNode) node);
        } else if (node instanceof MultNode) {
            numberMultNode((MultNode) node);
        } else if (node instanceof UnaryNode) {
            numberUnaryNode((UnaryNode) node);
        } else if (node instanceof CallNode) {
            numberCallNode((CallNode) node);
        } else if (node instanceof IntConstNode) {
            numberIntConstNode((IntConstNode) node);
        } else if (node instanceof StringConstNode) {
            numberStringConstNode((StringConstNode) node);
        } else if (node instanceof NullConstNode) {
            numberNullConstNode((NullConstNode) node);
        } else if (node instanceof IndexNode) {
            numberIndexNode((IndexNode) node);
        } else if (node instanceof DotNode) {
            numberDotNode((DotNode) node);
        } else if (node instanceof VarNode) {
            numberVarNode((VarNode) node);
        }
    }

    
    public void printStatementNode(StatementNode node) {
        if (node instanceof BlockNode) {
            printBlockNode((BlockNode) node);
        } else if (node instanceof VarDeclNode) {
            printVarDeclNode((VarDeclNode) node);
        } else if (node instanceof AtribNode) {
            printAtribNode((AtribNode) node);
        } else if (node instanceof IfNode) {
            printIfNode((IfNode) node);
        } else if (node instanceof ForNode) {
            printForNode((ForNode) node);
        } else if (node instanceof PrintNode) {
            printPrintNode((PrintNode) node);
        } else if (node instanceof NopNode) {
            printNopNode((NopNode) node);
        } else if (node instanceof ReadNode) {
            printReadNode((ReadNode) node);
        } else if (node instanceof ReturnNode) {
            printReturnNode((ReturnNode) node);
        } else if (node instanceof SuperNode) {
            printSuperNode((SuperNode) node);
        } else if (node instanceof BreakNode) {
            printBreakNode((BreakNode) node);
        }
    }

    public void numberStatementNode(StatementNode node) {
        if (node instanceof BlockNode) {
            numberBlockNode((BlockNode) node);
        } else if (node instanceof VarDeclNode) {
            numberVarDeclNode((VarDeclNode) node);
        } else if (node instanceof AtribNode) {
            numberAtribNode((AtribNode) node);
        } else if (node instanceof IfNode) {
            numberIfNode((IfNode) node);
        } else if (node instanceof ForNode) {
            numberForNode((ForNode) node);
        } else if (node instanceof PrintNode) {
            numberPrintNode((PrintNode) node);
        } else if (node instanceof NopNode) {
            numberNopNode((NopNode) node);
        } else if (node instanceof ReadNode) {
            numberReadNode((ReadNode) node);
        } else if (node instanceof ReturnNode) {
            numberReturnNode((ReturnNode) node);
        } else if (node instanceof SuperNode) {
            numberSuperNode((SuperNode) node);
        } else if (node instanceof BreakNode) {
            numberBreakNode((BreakNode) node);
        }
    }
}
