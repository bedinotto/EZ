package semanalysis;

import symtable.EntryClass;
import symtable.EntryMethod;
import symtable.EntryRec;
import symtable.EntryTable;
import symtable.EntryVar;
import symtable.Symtable;
import syntacticTree.ClassBodyNode;
import syntacticTree.ClassDeclNode;
import syntacticTree.ConstructDeclNode;
import syntacticTree.ListNode;
import syntacticTree.MethodDeclNode;
import syntacticTree.VarDeclNode;
import syntacticTree.VarNode;

public class VarCheck extends ClassCheck {
    public VarCheck() {
        super();
    }

    public void VarCheckRoot(ListNode node) throws SemanticException {
        ClassCheckRoot(node); // make all the semantic analysis of the class
        VarCheckClassDeclListNode(node);

        if (found_semantic_error != 0) {
            throw new SemanticException(found_semantic_error + " Semantic Errors found (phase 2)");
        }
    }

    public void VarCheckClassDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }
        try {
            VarCheckClassDeclNode((ClassDeclNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        VarCheckClassDeclListNode(node.next);
    }

    public void VarCheckClassDeclNode(ClassDeclNode node)
            throws SemanticException {
        Symtable temphold = this.current_table;
        EntryClass c = null;
        EntryClass nc;

        if (node == null) {
            return;
        }

        if (node.supername != null) {
            c = (EntryClass) this.current_table.classFindUp(node.supername.image);

            if (c == null) {
                throw new SemanticException(node.position, "Superclass " + node.supername.image + " not found");
            }
        }

        nc = (EntryClass) this.current_table.classFindUp(node.name.image);
        nc.parent = c;
        this.current_table = nc.nested;
        VarCheckClassBodyNode(node.body);
        this.current_table = temphold;
    }

    public void VarCheckClassBodyNode(ClassBodyNode node) {
        if (node == null) {
            return;
        }

        VarCheckClassDeclListNode(node.clist);
        VarCheckVarDeclListNode(node.vlist);
        VarCheckConstructDeclListNode(node.ctlist);

        if (this.current_table.methodFindInClass("constructor", null) == null) {
            this.current_table.add(new EntryMethod("constructor", this.current_table.levelup, true));
        }

        VarCheckMethodDeclListNode(node.mlist);
    }

    public void VarCheckVarDeclListNode(ListNode node) {

        if (node == null) {
            return;
        }

        try {
            VarCheckVarDeclNode((VarDeclNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        VarCheckVarDeclListNode(node.next);
    }

    public void VarCheckVarDeclNode(VarDeclNode node) throws SemanticException {
        EntryTable c;
        ListNode p;

        if (node == null) {
            return;
        }

        c = this.current_table.classFindUp(node.position.image);

        if (c == null) {
            throw new SemanticException(node.position, "Class " + node.position.image + " not found");
        }

        for (p = node.vars; p != null; p = p.next) {
            VarNode q = (VarNode) p.node;
            this.current_table.add(new EntryVar(q.position.image, c, q.dim));
        }
    }

    public void VarCheckConstructDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        try {
            VarCheckConstructDeclNode((ConstructDeclNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        VarCheckConstructDeclListNode(node.next);
    }

    public void VarCheckConstructDeclNode(ConstructDeclNode node) throws SemanticException {
        EntryMethod c;
        EntryRec r = null;
        EntryTable e;
        ListNode p;
        VarDeclNode q;
        VarNode u;
        int n;

        if (node == null) {
            return;
        }

        p = node.body.param;
        n = 0;

        while (p != null) {
            q = (VarDeclNode) p.node;
            u = (VarNode) q.vars.node;
            n++;

            e = this.current_table.classFindUp(q.position.image);

            if (e == null) {
                throw new SemanticException(q.position, "Class " + q.position.image + " not found");
            }

            r = new EntryRec(e, u.dim, n, r);
            p = p.next;
        }

        if (r != null) {
            r = r.inverte();
        }

        c = this.current_table.methodFindInClass("constructor", r);

        if (c == null) {
            c = new EntryMethod("constructor", this.current_table.levelup, r, 0);
            this.current_table.add(c);
        } else {
            throw new SemanticException(node.position,
                    "Constructor " + this.current_table.levelup.name + "(" +
                            ((r == null) ? "" : r.toStr()) + ")" + " already declared");
        }
    }

    public void VarCheckMethodDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        try {
            VarCheckMethodDeclNode((MethodDeclNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        VarCheckMethodDeclListNode(node.next);
    }

    public void VarCheckMethodDeclNode(MethodDeclNode node) throws SemanticException {
        EntryMethod c;
        EntryRec r = null;
        EntryTable e;
        ListNode p;
        VarDeclNode q;
        VarNode u;
        int n;

        if (node == null) {
            return;
        }

        p = node.body.param;
        n = 0;

        while (p != null) {
            n++;
            q = (VarDeclNode) p.node;
            u = (VarNode) q.vars.node;

            e = this.current_table.classFindUp(q.position.image);

            if (e == null) {
                throw new SemanticException(q.position, "Class " + q.position.image + " not found");
            }
            r = new EntryRec(e, u.dim, n, r);
            p = p.next;
        }

        if (r != null) {
            r = r.inverte();
        }

        e = this.current_table.classFindUp(node.position.image);

        if (e == null) {
            throw new SemanticException(node.position, "Class " + node.position.image + " not found");
        }

        c = this.current_table.methodFindInClass(node.name.image, r);

        if (c == null) {
            c = new EntryMethod(node.name.image, e, r, node.dim);
            this.current_table.add(c);
        } else {
            throw new SemanticException(node.position,
                    "Method " + node.name.image + "(" + ((r == null) ? "" : r.toStr()) + ")" + " already declared");
        }
    }
}
