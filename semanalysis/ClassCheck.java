package semanalysis;

import symtable.*;
import syntacticTree.*;

public class ClassCheck {
    Symtable main_table;
    protected Symtable current_table;
    int found_semantic_error;

    public ClassCheck(){
        EntrySimple k;
        found_semantic_error = 0;
        main_table = new Symtable(); // create the main symbol table
        k = new EntrySimple("int"); // insert basic type int
        main_table.add(k);
        k = new EntrySimple("string"); // insert basic type string
        main_table.add(k);
    }

    // check if the root node is a class declaration list node
    public void ClassCheckRoot(ListNode x) throws SemanticException {
        this.current_table = main_table; // set the current symbol table to the main symbol table
        ClassCheckClassDeclListNode(x); // check the class declaration list node
        if (found_semantic_error != 0) { 
            throw new SemanticException(found_semantic_error + " Semantic Errors found (phase 1)");
        }
    }

    // check if the node is a class declaration list node
    public void ClassCheckClassDeclListNode(ListNode x){
        if (x == null) {
            return;
        }
        try {
            ClassCheckClassDeclNode((ClassDeclNode) x.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }
        ClassCheckClassDeclListNode(x.next);
    }

    // check if the node is a class declaration node
    public void ClassCheckClassDeclNode(ClassDeclNode x) throws SemanticException{
        Symtable temp_hold = this.current_table;
        EntryClass new_class; // entry for the class
        if (x == null) {
            return;
        }
        // check if the class is already declared
        new_class = (EntryClass) this.current_table.classFindUp(x.name.image);
        
        if (new_class != null) { // if class already declared
            throw new SemanticException(x.name, "Class " + x.name.image + " already declared");
        }

        // insert the class in the symbol table
        this.current_table.add(new_class = new EntryClass(x.name.image, this.current_table));
        this.current_table = new_class.nested; // set the current symbol table to the class symbol table
        ClassCheckClassBodyNode(x.body); // check the class body node
        this.current_table = temp_hold; // restore the current symbol table
    }

    public void ClassCheckClassBodyNode(ClassBodyNode x){
        if (x == null){
            return;
        }
        ClassCheckClassDeclListNode(x.clist);
    }
}
