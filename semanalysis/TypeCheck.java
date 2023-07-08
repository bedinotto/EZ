package semanalysis;

import symtable.*;
import syntacticTree.*;
import recovery.*;


public class TypeCheck extends VarCheck{

    int nesting;
    protected int Nlocals;
    type Returntype;
    protected final EntrySimple STRING_TYPE, INT_TYPE, NULL_TYPE;
    protected EntryMethod CurMethod;
    boolean cansuper;
    protected boolean store;
    
    public TypeCheck(){
        super();
        nesting = 0;
        Nlocals = 0;
        STRING_TYPE = (EntrySimple) Maintable.classFindUp("string");
        INT_TYPE = (EntrySimple) Maintable.classFindUp("int");
        NULL_TYPE = new EntrySimple ("$NULL$");
        Maintable.add(NULL_TYPE);
    }

    public void TypeCheckRoot(ListNode x) throws SemanticException{
        VarCheckRoot(x);
        TypeCheckClassDeclListNode(x);
        if(foundSemanticError != 0) throw new SemanticException(foundSemanticError + " Semantic Errors found (phase 3)");
    } 

     public void TypeCheckClassDeclListNode(ListNode x) {
        if(x == null) return;
        try{
            TypeCheckClassDeclNode( (ClassDeclNode) x.node);
        }
        catch (SemanticException e){
            //se um erro ocorreu da classe,
            //dá a mensagem, mas faz a análise para próxima classe
            System.out.println(e.getMessage());
            foundSemanticError++; 
        }
        TypeCheckClassDeclListNode(x.next);
    }

    public void TypeCheckClassDeclNode(ClassDeclNode x) throws SemanticException {
        Symtable temphold = Curtable; // salva tabela corrente
        EntryClass nc;

        if (x == null)
            return;
        nc = (EntryClass) Curtable.classFindUp(x.name.image);
        if(circularSuperclass(nc, nc.parent)){
            nc.parent = null;
            //throw new SemanticException(x.postion, "Circular inheritance");
        }
        Curtable = nc.nested; // tabela corrente = tabela de classe
        TypeCheckClassBodyNode(x.body);
        Curtable = temphold;    //recupera tabela corrente
    }
      
    private boolean circularSuperclass(EntryClass orig, EntryClass e){
        if(e == null) return false;
        if(orig == e) return true;
        return circularSuperclass(orig, e.parent);
    }
    
    public void TypeCheckClassBodyNode(ClassBodyNode x){
        if(x == null) return;

        TypeCheckClassDeclListNode(x.clist);
        TypeCheckVarDeclListNode(x.vlist);
        TypeCheckConstructDeclListNode(x.ctlist);
        TypeCheckMethodDeclListNode(x.mlist);
    }

    public void TypeCheckVarDeclListNode(ListNode x) {
        if(x == null) return;
        try{
            TypeCheckVarDeclNode( (VarDeclNode) x.node);
        }
        catch(SemanticException e){
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        TypeCheckVarDeclListNode(x.next);
    }

    public void TypeCheckVarDeclNode(VarDeclNode x) throws SemanticException{
        ListNode p;
        EntryVar l;

        if(x == null) return;
        for(p = x.vars; p != null; p = p.next) {
            VarNode q = (VarNode) p.node;
            l = Curtable.varFind(q.position.image,2);
            
            //if(l != null) throw new SemanticException(q.position, "Variable "+ q.position.image + " already declared");
        }
    }
    
     public void TypeCheckConstructDeclListNode(ListNode x) {
        if (x == null) return;

        try {
            TypeCheckConstructDeclNode( (ConstructDeclNode) x.node);
        }
        catch (SemanticException e) {
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        TypeCheckConstructDeclListNode(x.next);
    }

    public void TypeCheckConstructDeclNode(ConstructDeclNode x) throws SemanticException {
        EntryMethod t;
        EntryRec r = null;
        EntryTable e;
        EntryClass thisclass;
        EntryVar thisvar;
        ListNode p;
        VarDeclNode q;
        VarNode u;
        int n;

        if(x == null) return;
        p = x.body.param;
        n = 0;

        while(p != null) {
            q = (VarDeclNode) p.node;
            u = (VarNode) q.vars.node;
            n++;
            e = Curtable.classFindUp(q.position.image);
            r = new EntryRec(e, u.dim, n, r);
            p = p.next;
        }

        if(r != null)
            r = r.inverte();

        t = Curtable.methodFindInclass("constructor", r);
        CurMethod = t;

        Curtable.beginScope();

        thisclass = (EntryClass) Curtable.levelup;

        thisvar = new EntryVar("this", thisclass, 0);
        Curtable.add(thisvar);
        Returntype = null;
        nesting = 0;
        Nlocals = 1;
        TypeCheckMethodBodyNode(x.body);
        t.totallocals = Nlocals;
        Curtable.endScope();
    }

    public void TypeCheckMethodDeclListNode(ListNode x) {
        if(x == null) return;
        try {
            TypeCheckMethodDeclNode((MethodDeclNode) x.node);
        }
        catch(SemanticException e) {
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        TypeCheckMethodDeclListNode(x.next);
    }    

    public void TypeCheckMethodDeclNode(MethodDeclNode x) throws SemanticException {

       t = Curtable.methodFind(x.name, image, r);
        CurMethod = t;
        
        Returntype = new type(t.type,t.dim);
    }
   
    public void TypeCheckMethodBodyNode(MethodBodyNode x){
        if(x == null) return;
        TypeCheckLocalVarDeclListNode(x.param);

        cansuper = false;
        if(Curtable.levelup.parent != null){

            StatementNode p = x.stat;
            while(p instanceof BlockNode)
                p = (StatementNode) ((BlockNode) p).stats.node;

            cansuper = p instanceof SuperNode;
        }
        try{
            TypeCheckStatementNode(x.stat);
        }
        catch(SemanticException e){
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
    }

    public void TypeCheckLocalVarDeclListNode(ListNode x){
        if(x == null) return;
        try{
            TypeCheckLocalVarDeclNode((VarDeclNode) x.node);
        }
        catch(SemanticException e){
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        TypeCheckLocalVarDeclListNode(x.next);
    }


    public void TypeCheckBlockNode(BlockNode x){
        Curtable.beginScope();
        TypeCheckStatementListNode(x.state);
        Curtable.endScope();
    }

    public void TypeCheckStatementListNode(ListNode x){
        if(x == null) return;

        try{
            TypeCheckStatementNode((StatementNode) x.node);
        }
        catch(SemanticException e){
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        TypeCheckStatementListNode(x.next);
    }

}
    
