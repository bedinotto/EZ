package semanalysis;

import symtable.*;
import syntacticTree.*;
import recovery.*;
import java.util.Collections;
import java.util.List;

public class VarCheck extends ClassCheck {

    public VarCheck(){
    super();
    }

    public void VarCheckRoot(ListNode x) throws SemanticException{
        ClassCheckRoot(x);
        VarCheckClassDeclListNode(x);
        if(foundSemanticError != 0)//se houver erro, lança exceção
            throw new SemanticException(foundSemanticError + " erros semanticos encontrados (phase 2)");
    }

    public void VarCheckClassDeclListNode(ListNode x) {
        if(x == null) return;
        try{
            VarCheckClassDeclNode( (ClassDeclNode) x.node);
        }
        catch (SemanticException e){
            //se um erro ocorreu da classe,
            //dá a mensagem, mas faz a análise para próxima classe
            System.out.println(e.getMessage());
            foundSemanticError++; 
        }
        VarCheckClassDeclListNode(x.next);
}

    public void VarCheckClassDeclNode(ClassDeclNode x) throws SemanticException {
        Symtable temphold = Curtable; // salva tabela corrente
        EntryClass c = null;
        EntryClass nc;

        if (x == null)
            return;
        if(x.supername != null) {//verifica se a superclasse foi definida
            c = (EntryClass) Curtable.classFindUp(x.supername.image);
           // if(c == null){ //se não achou superclasse, ERRO
             //   throw new SemanticException(x.position, "Superclass "+ x.supername.image + "not found");
            //}
        }
        nc = (EntryClass) Curtable.classFindUp(x.name.image);
        nc.parent = c;      //coloca na tabela o apontador p/ superclasse
        Curtable = nc.nested; // tabela corrente = tabela de classe
        VarCheckClassBodyNode(x.body);
        Curtable = temphold;    //recupera tabela corrente
    }
    
    public void VarCheckClassBodyNode(ClassBodyNode x) {
        if(x == null) return;

        VarCheckClassDeclListNode(x.clist);
        VarCheckVarDeclListNode(x.vlist);
        VarCheckConstructDeclListNode(x.ctlist);

        //se não existe constructor(), insere um falso
        if(Curtable.methodFindInclass("constructor", null) == null){
            Curtable.add(new EntryMethod("constructor", Curtable.levelup,true));
        }
        VarCheckMethodDeclListNode(x.mlist);
    }
    
    public void VarCheckVarDeclListNode(ListNode x) {
        if(x == null) return;
        try{
            VarCheckVarDeclNode( (VarDeclNode) x.node);
        }
        catch(SemanticException e){
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        VarCheckVarDeclListNode(x.next);
    }

    public void VarCheckVarDeclNode(VarDeclNode x) throws SemanticException {
        EntryTable c;
        ListNode p;
        
        if(x == null) return;

        //acha entrada do tipo da variável
        c = Curtable.classFindUp(x.position.image);
        //se não achou, ERRO
        //if(c == null) throw new SemanticException(x.position, "Class"+ x.position.image + "not found");

        //para cada variável da declaracão, cria uma entrada na tabela
        for(p = x.vars; p != null; p = p.next) {
            VarNode q = (VarNode) p.node;
            Curtable.add(new EntryVar(q.position.image, c, q.dim));
        }
    }

    public void VarCheckConstructDeclListNode(ListNode x) {
        if (x == null) return;

        try {
            VarCheckConstructDeclNode( (ConstructDeclNode) x.node);
        }
        catch (SemanticException e) {
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        VarCheckConstructDeclListNode(x.next);
    }

    public void VarCheckConstructDeclNode(ConstructDeclNode x) throws SemanticException {
        EntryMethod c;
        EntryRec r = null;
        EntryTable e;
        ListNode p;
        VarDeclNode q;
        VarNode u;
        int n;

        if(x == null) return;
        p = x.body.param;
        n = 0;
        while(p != null){ // para cada parametro do construtor
            q = (VarDeclNode) p.node; // q = no com a declaracão do parametro
            u = (VarNode) q.vars.node; //u = no com o nome e dimensão
            n++;
            //acha a entrada do tipo na tabela
            e = Curtable.classFindUp(q.position.image);

            //se não achou : ERRO
            //if(e == null) throw new SemamticException(q.position, "Class " + q.position.image + "not found");

            //constroi a lista com os parametros
            r = new EntryRec(e, u.dim, n, r);
            p = p.next;
        }

        if(r != null)
            r = r.inverte(); //inverte a lista

        // procura construtor com essa assinatura dentro da mesma classe
        c = Curtable.methodFindInclass("constructor", r);

        if(c == null){//se não achou, insere
            c = new EntryMethod("constructor", Curtable.levelup, 0, r);
            Curtable.add(c);
        }
        //else throw new SemanticException(x.position, "Constructor " + Curtable.levelup.name + "(" + (r == null? "" : r.toStr()) + ")" + " already declared");
    }


    public void VarCheckMethodDeclListNode(ListNode x) {
        if(x == null) return;
        try {
            VarCheckMethodDeclNode((MethodDeclNode) x.node);
        }
        catch(SemanticException e) {
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        VarCheckMethodDeclListNode(x.next);
    }

    public void VarCheckMethodDeclNode(MethodDeclNode x) throws SemanticException {//verificar chaves
        EntryMethod c;
        EntryRec r = null;
        EntryTable e;
        ListNode p;
        VarDeclNode q;
        VarNode u;
        int n;

        if(x == null) return;
        p = x.body.param;
        n = 0;
        while(p != null){
            n++;
            q = (VarDeclNode) p.node;
            
            u = (VarNode) q.vars.node;
            e = Curtable.classFindUp(q.position.image);
            //se não achou, ERRO
            //if(e == null) throw new SemanticException(q.position, "Class " + q.position.image + " not found");
            //constroi lista de tipos dos parametros
            r =  new EntryRec(e, u.dim, n, r);
            p = p.next;
        }
        if(r != null)
            r = r.inverte();
        
        e = Curtable.classFindUp(x.position.image);
        //if(e == null) throw new SemanticException(x.position, "Class " + x.position.image + " not found");
        //procura método na tabela, dentro da mesma classe
        c = Curtable.methodFindInclass(x.name.image, r);
        if(c == null){
            //se não achou, insere
            c = new EntryMethod(x.name.image, e, x.dim, r);
            Curtable.add(c);
        }
      //  else //método jaz definido na mesma classe, erro
           // throw new SemanticException(x.position, "Method" + x.name.image + "(" +(r == null? "" : r.toStr()) + ")" + " already declared");
    }
  }
                
        
