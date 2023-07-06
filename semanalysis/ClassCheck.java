package semanalysis;

import symtable.*;
import syntacticTree.*;
import recovery.*;

public class ClassCheck {
    Symtable Maintable; // tabela de mais alto nível
    protected Symtable Curtable; // apontador para a tabela corrente
    int foundSemanticError;

    public ClassCheck() {
        EntrySimple k;

        foundSemanticError = 0;
        Maintable = new Symtable(); // cria tabela principal
        k = new EntrySimple("int"); // insere tipos básicos da linguagem
        Maintable.add(k);
        k = new EntrySimple("string");
        Maintable.add(k);
    }

    public void ClassCheckRoot(ListNode x) throws SemanticException {
        Curtable = Maintable; // tabela corrente = principal
        ClassCheckDeclListNode(x); // chama análise para raiz da árvore
        if (foundSemanticError != 0) // se houve erro, lança exceção
            throw new SemanticException(foundSemanticError + " erros semanticos encontrados (phase 1) ");
    }

    public void ClassCheckDeclListNode(ListNode x) {
        if (x == null)
            return;
        try {
            ClassCheckClassDeclNode((ClassDeclNode) x.node);
        } catch (SemanticException e) { // se um erro ocorreu na análise da classe,
                                       // dá a mensagem, mas faz a análise para próxima classe
            System.out.println(e.getMessage());
            foundSemanticError++;
        }
        ClassCheckDeclListNode(x.next);
    }
    
    public void ClassCheckClassDeclNode(ClassDeclNode x) throws SemanticException {
        Symtable temphold = Curtable; // salva apontador p/ tabela corrente
        EntryClass nc;

        if (x == null)
            return;

        // procura classe na tabela
        nc = (EntryClass) Curtable.classFindUp(x.name.image);

        //if (nc != null) { // já declarada, ERRO
            //throw new SemanticException(x.name, "Class " + x.name.image + " already declared");
        //}

        // inclui classe na tabela corrente
        Curtable.add(nc = new EntryClass(x.name.image, Curtable));
        Curtable = nc.nested; // tabela corrente = tabela da classe
        ClassCheckClassBodyNode(x.body);
        Curtable = temphold; // recupera apontador p/ tabela corrente
    }
    
    public void ClassCheckClassBodyNode(ClassBodyNode x) throws SemanticException {
        if (x == null)
            return;
        ClassCheckDeclListNode(x.clist);
    }   
}
