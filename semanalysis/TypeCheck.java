
// public class TypeCheck extends VarCheck {
//     int nesting; // controll the nesting level of the blocks
//     protected int n_local_vars; // number of local variables in a method
//     type return_type; // type of return from a method
//     protected final EntrySimple STRING_TYPE; // string type
//     protected final EntrySimple INT_TYPE; // int type
//     protected final EntrySimple NULL_TYPE; // null type
//     protected EntryMethod current_method; // current method
//     boolean can_call_super; // control if a super() call is allowed

//     public TypeCheck() {
//         super();
//         this.nesting = 0;
//         this.n_local_vars = 0;
//         this.STRING_TYPE = (EntrySimple) main_table.classFindUp("string");
//         this.INT_TYPE = (EntrySimple) main_table.classFindUp("int");
//         this.NULL_TYPE = new EntrySimple("$NULL$");
//         main_table.add(NULL_TYPE);
//     }

//     // Verify the root of the tree
//     public void TypeCheckRoot(ListNode node) throws SemanticException{
//         VarCheckRoot(node); // make the semantic analysis of the class(methods and attributes)
//         TypeCheckClassDeclListNode(node); // make the semantic analysis of methods bodies

//         if (this.found_semantic_error != 0) {
//             throw new SemanticException(this.found_semantic_error + " semantic errors found (phase 3)");
//         }
//     }

//     // Verify a list of class declarations
//     public void TypeCheckClassDeclListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         try {
//             TypeCheckClassDeclNode((ClassDeclNode) node.node);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         TypeCheckClassDeclListNode(node.next);
//     }

//         // Verify if exists a circular reference
//     private boolean circularSuperClass(EntryClass origin, EntryClass dest) {
//         if (dest == null) {
//             return false;
//         }

//         if (origin == dest) {
//             return true;
//         }

//         return circularSuperClass(origin, dest.parent);
//     }

//     // Verify a class declaration
//     public void TypeCheckClassDeclNode(ClassDeclNode node) throws SemanticException {
//         Symtable tempHold = this.current_table; // save the current table
//         EntryClass new_class;

//         if (node == null) {
//             return;
//         }

//         new_class = (EntryClass) this.current_table.classFindUp(node.name.image);

//         if (circularSuperClass(new_class, new_class.parent)){
//             new_class.parent = null;
//             throw new SemanticException(node.position, "Circular inheritance in class " + new_class.name);
//         }
//         this.current_table = new_class.nested;
//         TypeCheckClassBodyNode(node.body);
//         this.current_table = tempHold; // restore the current table
//     } 

//     // Verify the body of a class body
//     public void TypeCheckClassBodyNode(ClassBodyNode node) {
//         if (node == null) {
//             return;
//         }

//         TypeCheckClassDeclListNode(node.clist);
//         TypeCheckVarDeclListNode(node.vlist);
//         TypeCheckConstructDeclListNode(node.ctlist);
//         TypeCheckMethodDeclListNode(node.mlist);
//     }

//     // Verify a list of declarations of variables
//     public void TypeCheckVarDeclListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         try {
//             TypeCheckVarDeclNode((VarDeclNode) node.node);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         TypeCheckVarDeclListNode(node.next);
//     }

//     // Check if a variable is already declared
//     public void TypeCheckVarDeclNode(VarDeclNode node) throws SemanticException {
//         ListNode p;
//         EntryVar l;

//         if (node == null) {
//             return;
//         }

//         for (p = node.vars; p != null; p = p.next) {
//             VarNode q = (VarNode) p.node;

//             l = this.current_table.varFind(q.position.image, 2);

//             if (l != null) {
//                 throw new SemanticException(q.position, "Variable " + q.position.image + " already declared");
//             }
//         }
//     }

//     // Verify a list of declarations of constructors
//     public void TypeCheckConstructDeclListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         try {
//             TypeCheckConstructDeclNode((ConstructDeclNode) node.node);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         TypeCheckConstructDeclListNode(node.next);
//     }

//     // Verify a declaration of a constructor
//     public void TypeCheckConstructDeclNode(ConstructDeclNode node) throws SemanticException{
//         EntryMethod the_method; // method entry
//         EntryRec the_paramethers = null; // list of parameters
//         EntryTable the_table; // current table
//         EntryClass the_class; // class entry
//         EntryVar the_var; // variable entry
//         ListNode list_nodes; // list of nodes
//         VarDeclNode var_decl; // variable declaration node
//         VarNode var_node; // variable node
//         int n;

//         if (node == null) {
//             return;
//         }

//         // get the list of parameters
//         list_nodes = node.body.param;
//         n = 0;

//         // insert in the list the parameters types
//         while (list_nodes != null) {
//             var_decl = (VarDeclNode) list_nodes.node; // get the variable declaration node
//             var_node = (VarNode) var_decl.vars.node; // get the variable node
//             n++;// count the number of parameters

//             the_table = this.current_table.classFindUp(var_node.position.image); // try to find the if the var was declared in the super class

//             the_paramethers = new EntryRec(the_table, var_node.dim, n, the_paramethers); 
//             list_nodes = list_nodes.next; 
//         }

//         if (the_paramethers != null) { 
//             the_paramethers = the_paramethers.inverte(); 
//         }

//         the_method = this.current_table.methodFind("constructor", the_paramethers); // try to find the constructor in the current class
//         System.out.println(the_method);
//         this.current_method = the_method; // save the current method (constructor)

//         this.current_table.beginScope(); // begin the scope of the method

//         the_class = (EntryClass) this.current_table.levelup; 

//         the_var = new EntryVar("this", the_class, 0, 0); // create a new variable entry
//         this.current_table.add(the_var); // add the variable entry to the current table

//         return_type = null; // set the return type to null
//         nesting = 0; // set the nesting level to 0
//         this.n_local_vars = 1; // set the number of local variables to 1
//         TypeCheckMethodBodyNode(node.body); // make the semantic analysis of the method body
//         the_method.local_count_total = this.n_local_vars; // set the number of local variables
//         this.current_table.endScope(); // end the scope of the method
//     }

//     // Verify a list of declarations of methods
//     public void TypeCheckMethodDeclListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         try {
//             TypeCheckMethodDeclNode((MethodDeclNode) node.node);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         TypeCheckMethodDeclListNode(node.next);
//     }

//     // Verify a declaration of a method
//     public void TypeCheckMethodDeclNode(MethodDeclNode node) throws SemanticException{
//         EntryMethod the_method; // method entry
//         EntryRec the_paramethers = null; // list of parameters
//         EntryTable the_table; // current table
//         EntryClass the_class; // class entry
//         EntryVar the_var; // variable entry
//         ListNode list_nodes; // list of nodes
//         VarDeclNode var_decl; // variable declaration node
//         VarNode var_node; // variable node
//         int n;

//         if (node == null) {
//             return;
//         }

//         list_nodes = node.body.param; // get the list of parameters
//         n = 0;

//         // insert in the list the parameters types
//         while (list_nodes != null) {
//             var_decl = (VarDeclNode) list_nodes.node; // get the variable declaration node
//             var_node = (VarNode) var_decl.vars.node; // get the variable node
//             n++; // count the number of parameters

//             the_table = this.current_table.classFindUp(var_node.position.image); // try to find the if the var was declared in the super class

//             the_paramethers = new EntryRec(the_table, var_node.dim, n, the_paramethers); 
//             list_nodes = list_nodes.next; 
//         }

//         if (the_paramethers != null) { 
//             the_paramethers = the_paramethers.inverte(); 
//         }

//         the_method = this.current_table.methodFind(node.name.image, the_paramethers); // try to find the method in the current class
//         this.current_method = the_method; // save the current method
//         this.return_type = new type(the_method.type, the_method.dim); // set the return type

//         this.current_table.beginScope(); // begin the scope of the method

//         the_class = (EntryClass) this.current_table.levelup;

//         the_var = new EntryVar("this", the_class, 0, 0); // create a new variable entry
//         this.current_table.add(the_var); // add the variable entry to the current table

//         this.nesting = 0; // set the nesting level to 0
//         this.n_local_vars = 1; // set the number of local variables to 1
//         TypeCheckMethodBodyNode(node.body); // make the semantic analysis of the method body
//         the_method.local_count_total = this.n_local_vars; // set the number of local variables
//         this.current_table.endScope(); // end the scope of the method
//     }

//     // Verify the body of a method
//     public void TypeCheckMethodBodyNode(MethodBodyNode node){
//         if (node == null) {
//             return;
//         }

//         TypeCheckLocalVarDeclListNode(node.param);
        
//         this.can_call_super = false;

//         if (this.current_table.levelup.parent != null) {
            
//             StatementNode p = node.stat;

//             while (p instanceof BlockNode){
//                 p = (StatementNode) ((BlockNode) p).stats.node;
//             }
//             this.can_call_super = p instanceof SuperNode;
//         }

//         try {
//             TypeCheckStatementNode(node.stat);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }
//     }

//     // Verify a list of local variables declarations
//     public void TypeCheckLocalVarDeclListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         try {
//             TypeCheckLocalVarDeclNode((VarDeclNode) node.node);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         TypeCheckLocalVarDeclListNode(node.next);
//     }

//     // Verify a local variable declaration
//     public void TypeCheckLocalVarDeclNode(VarDeclNode node) throws SemanticException {
//         ListNode list_node;
//         VarNode var_node;
//         EntryVar var1;
//         EntryVar var2;
//         EntryTable the_table;

//         if (node == null) {
//             return;
//         }

//         the_table = this.current_table.classFindUp(node.position.image);

//         if (the_table == null) {
//             throw new SemanticException(node.position, "Class " + node.position.image + " not found");
//         }

//         for (list_node = node.vars; list_node != null; list_node = list_node.next) {
//             var_node = (VarNode) list_node.node;

//             var1 = this.current_table.varFind(var_node.position.image);

//             if (var1 != null) {
//                 if (var1.scope == this.current_table.scptr) {
//                     throw new SemanticException(var_node.position, "Variable " + var_node.position.image + " already declared");
//                 }

//                 if (var1.local_count < 0) {
//                     System.out.println("Line " + var_node.position.beginLine + 
//                         " Column " + var_node.position.beginColumn + 
//                         " Warning: Variable " + var_node.position.image + 
//                         " hides a class attribute");
//                 } else {
//                     System.out.println("Line " + var_node.position.beginLine + 
//                         " Column " + var_node.position.beginColumn + 
//                         " Warning: Variable " + var_node.position.image + 
//                         " hides a parameter or a local variable");
//                 }
//             }
//             this.current_table.add(new EntryVar(var_node.position.image, the_table, var_node.dim, this.n_local_vars++));
//         }
//     }

//     public void TypeCheckBlockNode (BlockNode node) {
//         this.current_table.beginScope();
//         TypeCheckStatementListNode(node.stats);
//         this.current_table.endScope();
//     }

//     // Verify a list of statements
//     public void TypeCheckStatementListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         try {
//             TypeCheckStatementNode((StatementNode) node.node);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         TypeCheckStatementListNode(node.next);
//     }

//     // Print command
//     public void TypeCheckPrintNode(PrintNode node) throws SemanticException {
//         type t;

//         if (node == null) {
//             return;
//         }

//         // t is the type and dimension of the expression
//         t = TypeCheckExpreNode(node.expr);

//         // verify if the type is a string
//         if ((t.ty != STRING_TYPE) || (t.dim != 0)) {
//             throw new SemanticException(node.position, "String expression required");
//         }
//     }

//     // Read command
//     public void TypeCheckReadNode(ReadNode node) throws SemanticException {
//         type t;

//         if (node == null) {
//             return;
//         }

//         if (!(node.expr instanceof DotNode || node.expr instanceof IndexNode || node.expr instanceof VarNode)) {
//             throw new SemanticException(node.position, "Invalid expression in read statement");
//         }

//         // verify if is a atribuition to "this"
//         if (node.expr instanceof VarNode) {
//             EntryVar v = this.current_table.varFind(node.expr.position.image);
//             if ((v != null) && (v.local_count == 0)){  // is a variable local 0?
//                 throw new SemanticException(node.position, "Reading into variable " + " \"this\" is not legal");
//             }
//         }

//         // Verify if type is string or int
//         t = TypeCheckExpreNode(node.expr);

//         if ((t.ty != STRING_TYPE) && (t.ty != INT_TYPE)) {
//             throw new SemanticException(node.position, "Invalid type. String or int required");
//         }

//         // Verify if the dimension is 0
//         if (t.dim != 0) {
//             throw new SemanticException(node.position, "Can not read into an array");
//         }
//     }

//     // Return command
//     public void TypeCheckReturnNode(ReturnNode node) throws SemanticException {
//         type t;

//         if (node == null) {
//             return;
//         }

//         // verify if the return type is the same of the method
//         t = TypeCheckExpreNode(node.expr);

//         if (t == null) {
//             if (this.return_type == null) {
//                 return;
//             } else {
//                 throw new SemanticException(node.position, "Return expression required");
//             }
//         } else {
//             if (this.return_type == null) {
//                 throw new SemanticException(node.position, "Contructor can not return a value");
//             }
//         }

//         if ((t.ty != this.return_type.ty) || (t.dim != this.return_type.dim)) {
//             throw new SemanticException(node.position, "Invalid return type");
//         }
//     }

//     // Super command
//     public void TypeCheckSuperNode(SuperNode node) throws SemanticException {
//         type t;

//         if (node == null) {
//             return;
//         }

//         if (this.return_type != null) {
//             throw new SemanticException(node.position, "Super is only allowed in constructors");
//         }

//         if (!this.can_call_super) {
//             throw new SemanticException(node.position, "Super must be the first statement in the constructor");
//         }

//         this.can_call_super = false;

//         EntryClass c = this.current_table.levelup.parent;

//         if (c == null) {
//             throw new SemanticException(node.position, "Class " + this.current_table.levelup.name + " does not have a superclass");
//         }

//         t = TypeCheckExpreListNode(node.args);

//         EntryMethod m = c.nested.methodFindInClass("constructor", (EntryRec) t.ty);

//         if (m == null) {
//             throw new SemanticException(node.position, "Constructor " + c.name + "(" + ((t.ty == null) ? "" : ((EntryRec) t.ty).toStr()) + ") not found");
//         }

//         this.current_method.has_super = true;
//     }

//     // Atribuition command
//     public void TypeCheckAtribNode(AtribNode node) throws SemanticException {
//         type t1, t2;
//         EntryVar v;

//         if (node == null) {
//             return;
//         }

//         // verify if the son has a valid type
//         if (!(node.expr1 instanceof DotNode || node.expr1 instanceof IndexNode || node.expr1 instanceof VarNode)) {
//             throw new SemanticException(node.position, "Invalid left side of assignment");
//         }

//         // verify if is an atribuition to this
//         if (node.expr1 instanceof VarNode) {
//             v = this.current_table.varFind(node.expr1.position.image);
//             if ((v != null) && (v.local_count == 0)){  // is a variable local 0?
//                 throw new SemanticException(node.position, "Assignment to variable " + " \"this\" is not legal");
//             }
//         }

//         t1 = TypeCheckExpreNode(node.expr1); // get the type of the left side of the atribuition
//         t2 = TypeCheckExpreNode(node.expr2); // get the type of the right side of the atribuition

//         // verify if the dimensions are the same
//         if (t1.dim != t2.dim) {
//             throw new SemanticException(node.position, "Incompatible dimensions in assignment");
//         }

//         // verify if left side is a class and right side is null
//         if (t1.ty instanceof EntryClass && (t2.ty == NULL_TYPE)) {
//             return;
//         }

//         // Veriy if t2 is a subclass of t1
//         if (!(isSubClass(t2.ty, t1.ty) || isSubClass(t1.ty, t2.ty))) {
//             throw new SemanticException(node.position, "Incompatible types in assignment");
//         }
//     }

//     protected boolean isSubClass(EntryTable t1, EntryTable t2) {
//         // Verify if are the same type
//         if (t1 == t2) {
//             return true;
//         }

//         // Verify if are classes
//         if (!(t1 instanceof EntryClass && t2 instanceof EntryClass)) {
//             return false;
//         }

//         // Search t2 in the superclasses of t1
//         for (EntryClass p = ((EntryClass) t1).parent; p != null; p = p.parent) {
//             if (p == t2) {
//                 return true;
//             }
//         }
//         return false;
//     }

//     // If command
//     public void TypeCheckIfNode(IfNode node) {
//         type t;

//         if (node == null) {
//             return;
//         }

//         try {
//             t = TypeCheckExpreNode(node.expr);
//             if ((t.ty != INT_TYPE) || (t.dim != 0)) {
//                 throw new SemanticException(node.expr.position, "Integer expression expected");
//             }
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         try {
//             TypeCheckStatementNode(node.stat1);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         try {
//             TypeCheckStatementNode(node.stat2);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }
//     }

//     // For command
//     public void TypeCheckForNode(ForNode node) {
//         type t;

//         if (node == null) {
//             return;
//         }

//         // Analise the initialization
//         try{
//             TypeCheckStatementNode(node.init);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         // Analise the condition
//         try{
//             t = TypeCheckExpreNode(node.expr);
//             if ((t.ty != INT_TYPE) || (t.dim != 0)) {
//                 throw new SemanticException(node.expr.position, "Integer expression required");
//             }
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         // Analise the increment
//         try{
//             TypeCheckStatementNode(node.incr);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }

//         // Analise the body
//         try {
//             nesting++;
//             TypeCheckStatementNode(node.stat);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//         }
//         nesting--;
//     }

//     // Break command
//     public void TypeCheckBreakNode(BreakNode node) throws SemanticException {
//         if (node == null) {
//             return;
//         }

//         if (nesting <= 0) {
//             throw new SemanticException(node.position, "Break command outside a loop");
//         }
//     }

//     // Empty command
//     public void TypeCheckNopNode(NopNode node) {
//     }

//     // Expression

//     // Allocating a new object
//     public type TypeCheckNewObjectNode(NewObjectNode node) throws SemanticException {
//         EntryTable table;
//         EntryMethod m;
//         type t;

//         if (node == null) {
//             return null;
//         }

//         // verify if the class exists
//         table = this.current_table.classFindUp(node.name.image);

//         if (table == null) {
//             throw new SemanticException(node.position, "Class " + node.name.image + " not found");
//         }

//         t = TypeCheckExpreListNode(node.args); // t.ty gets the list of parameters types

//         Symtable s = ((EntryClass) table).nested; 
//         m = s.methodFindInClass("constructor", (EntryRec) t.ty); // m gets the constructor entry

//         if (m == null) {
//             throw new SemanticException(node.position, 
//                 "Constructor " + node.name.image + 
//                 "(" + ((t.ty == null) ? "" : ((EntryRec) t.ty).toStr()) + ") not found");
//         }

//         t = new type(table, 0); // t gets the type of the object
//         return t;
//     }

//     // Allocating a new array
//     public type TypeCheckNewArrayNode(NewArrayNode node) throws SemanticException {
//         type t;
//         EntryTable table;
//         ListNode list_node;
//         ExpreNode expre_node;
//         int dimensions_counter;

//         if (node == null) {
//             return null;
//         }

//         table = this.current_table.classFindUp(node.name.image); // try to find the class

//         if (table == null) {
//             throw new SemanticException(node.position, "Type " + node.name.image + " not found");
//         }

//         // for each dimension verify if the expression is an integer
//         for (dimensions_counter = 0, list_node = node.dims; list_node != null; list_node = list_node.next) {
//             t = TypeCheckExpreNode((ExpreNode) list_node.node); // get the type of the expression

//             if ((t.ty != INT_TYPE) || (t.dim != 0)) {
//                 throw new SemanticException(node.position, "Invalid type in array");
//             }
//             dimensions_counter++;
//         }

//         return new type(table, dimensions_counter);
//     }

//     // Expression list
//     public type TypeCheckExpreListNode(ListNode node) {
//         type t1, t2;
//         EntryRec r;
//         int lenght_list_t2;

//         if (node == null) {
//             return null;
//         }

//         try {
//             t1 = TypeCheckExpreNode((ExpreNode) node.node);
//         } catch (SemanticException e) {
//             System.out.println(e.getMessage());
//             found_semantic_error++;
//             t1 = new type(NULL_TYPE, 0);
//         }

//         t2 = TypeCheckExpreListNode(node.next); // get the type of the rest of the list

//         lenght_list_t2 = (t2.ty == null) ? 0 : ((EntryRec) t2.ty).count; // length of the list in t2

//         r = new EntryRec(t1.ty, t1.dim, lenght_list_t2+1, (EntryRec) t2.ty); // create a new list of types

//         t1 = new type(r, 0); // t gets the type of the list

//         return t1;
//     }

//     // Relational expression
//     public type TypeCheckRelationalNode(RelationalNode node) throws SemanticException {
//         type t1, t2;
//         int operation;

//         if (node == null) {
//             return null;
//         }

//         operation = node.position.kind; // get the operation
//         t1 = TypeCheckExpreNode(node.expr1); // get the type of the left side of the expression
//         t2 = TypeCheckExpreNode(node.expr2); // get the type of the right side of the expression

//         // verify if both are int 
//         if ((t1.ty == INT_TYPE) && (t2.ty == INT_TYPE)) {
//             return new type(INT_TYPE, 0);
//         }

//         // verify if the dimensions are the same
//         if (t1.dim != t2.dim) {
//             throw new SemanticException(node.position, "Can not compare objects of different dimensions");
//         }

//         // if dimensions are > 0 just can compare if are equal or not equal
//         if ((operation != ezConstants.EQ) && (operation != ezConstants.NEQ) && (t1.dim > 0)) {
//             throw new SemanticException(node.position, "Can not use " + node.position.image + " for arrays");
//         }

//         // verify if the objects are the same type (including string)
//         if ((isSubClass(t2.ty, t1.ty) || isSubClass(t1.ty, t2.ty)) && ((operation == ezConstants.NEQ) || (operation == ezConstants.EQ))) {
//             return new type(INT_TYPE, 0);
//         }

//         // verify if a object is null
//         if (((t1.ty instanceof EntryClass && (t2.ty == NULL_TYPE)) || (t2.ty instanceof EntryClass && (t1.ty == NULL_TYPE))) && ((operation == ezConstants.NEQ) || (operation == ezConstants.EQ))) {
//             return new type(INT_TYPE, 0);
//         }

//         throw new SemanticException(node.position, "Invalid types for " + node.position.image);
//     }

//     // Add or sub expression
//     public type TypeCheckAddNode(AddNode node) throws SemanticException {
//         type t1, t2;
//         int operation;
//         int i, j;

//         if (node == null) {
//             return null;
//         }

//         operation = node.position.kind; // get the operation
//         t1 = TypeCheckExpreNode(node.expr1); // get the type of the left side of the expression
//         t2 = TypeCheckExpreNode(node.expr2); // get the type of the right side of the expression

//         // if dimensions are > 0 just can add/sub if are equal
//         if ((t1.dim > 0) || (t2.dim > 0)) {
//             throw new SemanticException(node.position, "Can not use " + node.position.image + " for arrays");
//         }

//         i = j = 0; // i is the counter of int and j is the counter of string

//         if (t1.ty == INT_TYPE) {
//             i += 1;
//         } else if (t1.ty == STRING_TYPE) {
//             j += 1;
//         }

//         if (t2.ty == INT_TYPE) {
//             i += 1;
//         } else if (t2.ty == STRING_TYPE) {
//             j += 1;
//         }

//         // both are int
//         if (i == 2) {
//             return new type(INT_TYPE, 0);
//         }

//         // a int only can be added to a string
//         if ((operation == ezConstants.PLUS) && ((i + j) == 2)) {
//             return new type(STRING_TYPE, 0);
//         }

//         throw new SemanticException(node.position, "Invalid types for " + node.position.image);
//     }

//     // Mul or div expression
//     public type TypeCheckMultNode(MultNode node) throws SemanticException {
//         type t1, t2;
//         int operation;
//         int i, j;

//         if (node == null) {
//             return null;
//         }

//         operation = node.position.kind; // get the operation
//         t1 = TypeCheckExpreNode(node.expr1); // get the type of the left side of the expression
//         t2 = TypeCheckExpreNode(node.expr2); // get the type of the right side of the expression

//         // if dimensions are > 0 just can add/sub if are equal
//         if ((t1.dim > 0) || (t2.dim > 0)) {
//             throw new SemanticException(node.position, "Can not use " + node.position.image + " for arrays");
//         }

//         // only can multiply/divide int
//         if ((t1.ty != INT_TYPE) || (t2.ty != INT_TYPE)) {
//             throw new SemanticException(node.position, "Invalid types for " + node.position.image);
//         }

//         return new type(INT_TYPE, 0);
//     }

//     // Unary expression
//     public type TypeCheckUnaryNode(UnaryNode node) throws SemanticException {
//         type t;

//         if (node == null) {
//             return null;
//         }

//         t = TypeCheckExpreNode(node.expr); // get the type of the expression

//         // verify if the dimension is > 0
//         if (t.dim > 0) {
//             throw new SemanticException(node.position, "Can not use unary " + node.position.image + " for arrays");
//         }

//         // verify if the type is int
//         if (t.ty != INT_TYPE) {
//             throw new SemanticException(node.position, "Incompatible type for unary " + node.position.image);
//         }

//         return new type(INT_TYPE, 0);
//     }

//     // Int constant
//     public type TypeCheckIntConstNode(IntConstNode node) throws SemanticException {
//         int k;

//         if (node == null) {
//             return null;
//         }

//         try {
//             k = Integer.parseInt(node.position.image);
//         } catch (NumberFormatException e) {
//             throw new SemanticException(node.position, "Invalid int constant");
//         }

//         return new type(INT_TYPE, 0);
//     }

//     // String constant
//     public type TypeCheckStringConstNode(StringConstNode node) {
//         if (node == null) {
//             return null;
//         }

//         return new type(STRING_TYPE, 0);
//     }

//     // Null constant
//     public type TypeCheckNullConstNode(NullConstNode node) {
//         if (node == null) {
//             return null;
//         }

//         return new type(NULL_TYPE, 0);
//     }

//     // Var name
//     public type TypeCheckVarNode(VarNode node) throws SemanticException {
//         EntryVar v;

//         if (node == null) {
//             return null;
//         }

//         v = this.current_table.varFind(node.position.image); // try to find the variable

//         if (v == null) {
//             throw new SemanticException(node.position, "Variable " + node.position.image + " not found");
//         }

//         return new type(v.type, v.dim); // return the type of the variable
//     }

//     // Method call
//     public type TypeCheckCallNode(CallNode node) throws SemanticException {
//         EntryClass c;
//         EntryMethod m;
//         type t1, t2;

//         if (node == null) {
//             return null;
//         }

//         t1 = TypeCheckExpreNode(node.expr); // get the type of the expression

//         if (t1.dim > 0) {
//             throw new SemanticException(node.position, "Arrays do not have methods");
//         }

//         // verify if the type is a class
//         if (!(t1.ty instanceof EntryClass)) {
//             throw new SemanticException(node.position, "Type " + t1.ty.name + " does not have methods");
//         }

//         t2 = TypeCheckExpreListNode(node.args); // get the type of the list of parameters

//         c = (EntryClass) t1.ty; // get the class entry
//         m = c.nested.methodFind(node.meth.image, (EntryRec) t2.ty); // try to find the method

//         if (m == null) {
//             throw new SemanticException(node.position, 
//                 "Method " + node.meth.image + 
//                 "(" + ((t2.ty == null) ? "" : ((EntryRec) t2.ty).toStr()) + ") not found in class " + c.name);
//         }

//         return new type(m.type, m.dim); // return the type of the method
//     }

//     // Var indexation
//     public type TypeCheckIndexNode(IndexNode node) throws SemanticException {
//         EntryClass c;
//         type t1, t2;

//         if (node == null) {
//             return null;
//         }

//         t1 = TypeCheckExpreNode(node.expr1); // get the type of the expression

//         // verify if the expression is an array
//         if (t1.dim <= 0) {
//             throw new SemanticException(node.position, "Type " + t1.ty.name + " is not an array, can not be indexed");
//         }
//         t2 = TypeCheckExpreNode(node.expr2); // get the type of the index

//         // verify if the index is an int
//         if ((t2.ty != INT_TYPE) || (t2.dim > 0)) {
//             throw new SemanticException(node.position, "Invalid type in array index. It must be an integer");
//         }

//         return new type(t1.ty, t1.dim - 1); // return the type of the array
//     }

//     // Access to a class attribute
//     public type TypeCheckDotNode(DotNode node) throws SemanticException {
//         EntryClass c;
//         EntryVar v;
//         type t;

//         if (node == null) {
//             return null;
//         }

//         t = TypeCheckExpreNode(node.expr); // get the type of the expression

//         if (t.dim > 0) {
//             throw new SemanticException(node.position, "Arrays do not have fields");
//         }

//         // verify if the expression is a class
//         if (!(t.ty instanceof EntryClass)) {
//             throw new SemanticException(node.position, "Type " + t.ty.name + " does not have fields");
//         }

//         c = (EntryClass) t.ty; // get the class entry
//         v = c.nested.varFind(node.field.image); // try to find the attribute
//         // verify if the class has the attribute
//         if (v == null) {
//             throw new SemanticException(node.position, "Attribute " + node.field.image + " not found in class " + c.name);
//         }

//         return new type(v.type, v.dim); // return the type of the attribute
//     }

//     // Geral expressions
//     public type TypeCheckExpreNode(ExpreNode node) throws SemanticException {
//         if (node instanceof NewObjectNode) {
//             return TypeCheckNewObjectNode((NewObjectNode) node);
//         } else if (node instanceof NewArrayNode) {
//             return TypeCheckNewArrayNode((NewArrayNode) node);
//         } else if (node instanceof RelationalNode) {
//             return TypeCheckRelationalNode((RelationalNode) node);
//         } else if (node instanceof AddNode) {
//             return TypeCheckAddNode((AddNode) node);
//         } else if (node instanceof MultNode) {
//             return TypeCheckMultNode((MultNode) node);
//         } else if (node instanceof UnaryNode) {
//             return TypeCheckUnaryNode((UnaryNode) node);
//         } else if (node instanceof IntConstNode) {
//             return TypeCheckIntConstNode((IntConstNode) node);
//         } else if (node instanceof StringConstNode) {
//             return TypeCheckStringConstNode((StringConstNode) node);
//         } else if (node instanceof NullConstNode) {
//             return TypeCheckNullConstNode((NullConstNode) node);
//         } else if (node instanceof VarNode) {
//             return TypeCheckVarNode((VarNode) node);
//         } else if (node instanceof CallNode) {
//             return TypeCheckCallNode((CallNode) node);
//         } else if (node instanceof IndexNode) {
//             return TypeCheckIndexNode((IndexNode) node);
//         } else if (node instanceof DotNode) {
//             return TypeCheckDotNode((DotNode) node);
//         } else {
//             return null;
//         }
//     }

//     // Geral commands
//     public void TypeCheckStatementNode(StatementNode node) throws SemanticException {
//         if (node instanceof BlockNode) {
//             TypeCheckBlockNode((BlockNode) node);
//         } else if (node instanceof PrintNode) {
//             TypeCheckPrintNode((PrintNode) node);
//         } else if (node instanceof ReadNode) {
//             TypeCheckReadNode((ReadNode) node);
//         } else if (node instanceof ReturnNode) {
//             TypeCheckReturnNode((ReturnNode) node);
//         } else if (node instanceof SuperNode) {
//             TypeCheckSuperNode((SuperNode) node);
//         } else if (node instanceof AtribNode) {
//             TypeCheckAtribNode((AtribNode) node);
//         } else if (node instanceof IfNode) {
//             TypeCheckIfNode((IfNode) node);
//         } else if (node instanceof ForNode) {
//             TypeCheckForNode((ForNode) node);
//         } else if (node instanceof BreakNode) {
//             TypeCheckBreakNode((BreakNode) node);
//         } else if (node instanceof NopNode) {
//             TypeCheckNopNode((NopNode) node);
//         }
//     }
// }


package semanalysis;

import parser.*;

import symtable.*;

import syntacticTree.*;


public class TypeCheck extends VarCheck {
    int nesting; 
    protected int n_locals; 
    type return_type; 
    protected final EntrySimple STRING_TYPE;
    protected final EntrySimple INT_TYPE;
    protected final EntrySimple NULL_TYPE;
    protected EntryMethod current_method; 
    boolean can_call_super; 

    public TypeCheck() {
        super();
        nesting = 0;
        n_locals = 0;
        STRING_TYPE = (EntrySimple) main_table.classFindUp("string");
        INT_TYPE = (EntrySimple) main_table.classFindUp("int");
        NULL_TYPE = new EntrySimple("$NULL$");
        main_table.add(NULL_TYPE);
    }

    public void TypeCheckRoot(ListNode node) throws SemanticException {
        VarCheckRoot(node); 
        TypeCheckClassDeclListNode(node); 

        if (found_semantic_error != 0) { 
            throw new SemanticException(found_semantic_error +
                " Semantic Errors found (phase 3)");
        }
    }

    
    public void TypeCheckClassDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        try {
            TypeCheckClassDeclNode((ClassDeclNode) node.node);
        } catch (SemanticException e) { 
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        TypeCheckClassDeclListNode(node.next);
    }

    
    private boolean circularSuperclass(EntryClass orig, EntryClass e) {
        if (e == null) {
            return false;
        }

        if (orig == e) {
            return true;
        }

        return circularSuperclass(orig, e.parent);
    }

    
    public void TypeCheckClassDeclNode(ClassDeclNode node)
        throws SemanticException {
        Symtable temp_hold = current_table; 
        EntryClass new_class;

        if (node == null) {
            return;
        }

        new_class = (EntryClass) current_table.classFindUp(node.name.image);

        if (circularSuperclass(new_class, new_class.parent)) { 
            new_class.parent = null;
            throw new SemanticException(node.position, "Circular inheritance");
        }

        current_table = new_class.nested; 
        TypeCheckClassBodyNode(node.body);
        current_table = temp_hold; 
    }

    
    public void TypeCheckClassBodyNode(ClassBodyNode node) {
        if (node == null) {
            return;
        }

        TypeCheckClassDeclListNode(node.clist);
        TypeCheckVarDeclListNode(node.vlist);
        TypeCheckConstructDeclListNode(node.ctlist);
        TypeCheckMethodDeclListNode(node.mlist);
    }

    
    public void TypeCheckVarDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        try {
            TypeCheckVarDeclNode((VarDeclNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        TypeCheckVarDeclListNode(node.next);
    }

    
    public void TypeCheckVarDeclNode(VarDeclNode node) throws SemanticException {
        ListNode p;
        EntryVar l;

        if (node == null) {
            return;
        }

        for (p = node.vars; p != null; p = p.next) {
            VarNode q = (VarNode) p.node;

            
            l = current_table.varFind(q.position.image, 2);

            
            if (l != null) {
                throw new SemanticException(q.position,
                    "Variable " + q.position.image + " already declared");
            }
        }
    }

    
    public void TypeCheckConstructDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        try {
            TypeCheckConstructDeclNode((ConstructDeclNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        TypeCheckConstructDeclListNode(node.next);
    }

    
    public void TypeCheckConstructDeclNode(ConstructDeclNode node)
        throws SemanticException {
        EntryMethod t;
        EntryRec r = null;
        EntryTable e;
        EntryClass the_class;
        EntryVar the_var;
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

            
            e = current_table.classFindUp(q.position.image);

            
            r = new EntryRec(e, u.dim, n, r);
            p = p.next;
        }

        if (r != null) {
            r = r.inverte(); 
        }

        
        t = current_table.methodFind("constructor", r);
        current_method = t; 

        
        current_table.beginScope();

        
        the_class = (EntryClass) current_table.levelup;

        the_var = new EntryVar("this", the_class, 0, 0);
        current_table.add(the_var); 
        return_type = null; 
        nesting = 0; 
        n_locals = 1; 
        TypeCheckMethodBodyNode(node.body);
        t.local_count_total = n_locals; 
        current_table.endScope(); 
    }

    
    public void TypeCheckMethodDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        try {
            TypeCheckMethodDeclNode((MethodDeclNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        TypeCheckMethodDeclListNode(node.next);
    }

    
    public void TypeCheckMethodDeclNode(MethodDeclNode node)
        throws SemanticException {
        EntryMethod t;
        EntryRec r = null;
        EntryTable e;
        EntryClass the_class;
        EntryVar the_var;
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

            
            e = current_table.classFindUp(q.position.image);

            
            r = new EntryRec(e, u.dim, n, r);
            p = p.next;
        }

        if (r != null) {
            r = r.inverte();
        }

        
        t = current_table.methodFind(node.name.image, r);
        current_method = t; 

        
        return_type = new type(t.type, t.dim);

        
        current_table.beginScope();

        
        the_class = (EntryClass) current_table.levelup;

        the_var = new EntryVar("this", the_class, 0, 0);
        current_table.add(the_var); 

        nesting = 0; 
        n_locals = 1; 
        TypeCheckMethodBodyNode(node.body);
        t.local_count_total = n_locals; 
        current_table.endScope(); 
    }

    
    public void TypeCheckMethodBodyNode(MethodBodyNode node) {
        if (node == null) {
            return;
        }

        TypeCheckLocalVarDeclListNode(node.param); 

        can_call_super = false;

        if (current_table.levelup.parent != null) { 
                                               

            StatementNode p = node.stat;

            while (p instanceof BlockNode)
                p = (StatementNode) ((BlockNode) p).stats.node;

            can_call_super = p instanceof SuperNode; 
        }

        try {
            TypeCheckStatementNode(node.stat);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }
    }

    
    public void TypeCheckLocalVarDeclListNode(ListNode node) {
        if (node == null) {
            return;
        }

        try {
            TypeCheckLocalVarDeclNode((VarDeclNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        TypeCheckLocalVarDeclListNode(node.next);
    }

    
    public void TypeCheckLocalVarDeclNode(VarDeclNode node)
        throws SemanticException {
        ListNode p;
        VarNode q;
        EntryVar l;
        EntryVar u;
        EntryTable c;

        if (node == null) {
            return;
        }

        
        c = current_table.classFindUp(node.position.image);

        
        if (c == null) {
            throw new SemanticException(node.position,
                "Class " + node.position.image + " not found.");
        }

        for (p = node.vars; p != null; p = p.next) {
            q = (VarNode) p.node;
            l = current_table.varFind(q.position.image);

            
            if (l != null) {
                
                if (l.scope == current_table.scptr) { 
                    throw new SemanticException(q.position,
                        "Variable " + p.position.image + " already declared");
                }

                
                if (l.local_count < 0) { 
                    System.out.println("Line " + q.position.beginLine +
                        " Column " + q.position.beginColumn +
                        " Warning: Variable " + q.position.image +
                        " hides a class variable");
                } else { 
                    System.out.println("Line " + q.position.beginLine +
                        " Column " + q.position.beginColumn +
                        " Warning: Variable " + q.position.image +
                        " hides a parameter or a local variable");
                }
            }

            
            current_table.add(new EntryVar(q.position.image, c, q.dim, n_locals++));
        }
    }

    
    public void TypeCheckBlockNode(BlockNode node) {
        current_table.beginScope(); 
        TypeCheckStatementListNode(node.stats);
        current_table.endScope(); 
    }

    
    public void TypeCheckStatementListNode(ListNode node) {
        if (node == null) {
            return;
        }

        try {
            TypeCheckStatementNode((StatementNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        TypeCheckStatementListNode(node.next);
    }

    
    public void TypeCheckPrintNode(PrintNode node) throws SemanticException {
        type t;

        if (node == null) {
            return;
        }

        
        t = TypeCheckExpreNode(node.expr);

        
        if ((t.ty != STRING_TYPE) || (t.dim != 0)) {
            throw new SemanticException(node.position, "string expression required");
        }
    }

    
    public void TypeCheckReadNode(ReadNode node) throws SemanticException {
        type t;

        if (node == null) {
            return;
        }

        
        if (!(node.expr instanceof DotNode || node.expr instanceof IndexNode ||
                node.expr instanceof VarNode)) {
            throw new SemanticException(node.position,
                "Invalid expression in read statement");
        }

        
        if (node.expr instanceof VarNode) {
            EntryVar v = current_table.varFind(node.expr.position.image);

            if ((v != null) && (v.local_count == 0)) 
             {
                throw new SemanticException(node.position,
                    "Reading into variable " + " \"this\" is not legal");
            }
        }

        
        t = TypeCheckExpreNode(node.expr);

        if ((t.ty != STRING_TYPE) && (t.ty != INT_TYPE)) {
            throw new SemanticException(node.position,
                "Invalid type. Must be int or string");
        }

        
        if (t.dim != 0) {
            throw new SemanticException(node.position, "Cannot read array");
        }
    }

    
    public void TypeCheckReturnNode(ReturnNode node) throws SemanticException {
        type t;

        if (node == null) {
            return;
        }

        
        t = TypeCheckExpreNode(node.expr);

        
        if (t == null) { 

            if (return_type == null) {
                return;
            } else { 
                throw new SemanticException(node.position,
                    "Return expression required");
            }
        } else {
            if (return_type == null) { 
                throw new SemanticException(node.position,
                    "Constructor cannot return a value");
            }
        }

        
        if ((t.ty != return_type.ty) || (t.dim != return_type.dim)) {
            throw new SemanticException(node.position, "Invalid return type");
        }
    }

    
    public void TypeCheckSuperNode(SuperNode node) throws SemanticException {
        type t;

        if (node == null) {
            return;
        }

        if (return_type != null) {
            throw new SemanticException(node.position,
                "super is only allowed in constructors");
        }

        if (!can_call_super) {
            throw new SemanticException(node.position,
                "super must be first statement in the constructor");
        }

        can_call_super = false; 

        
        EntryClass p = current_table.levelup.parent;

        if (p == null) {
            throw new SemanticException(node.position,
                "No superclass for this class");
        }

        
        t = TypeCheckExpreListNode(node.args);

        
        EntryMethod m = p.nested.methodFindInClass("constructor",
                (EntryRec) t.ty);

        
        if (m == null) {
            throw new SemanticException(node.position,
                "Constructor " + p.name + "(" +
                ((t.ty == null) ? "" : ((EntryRec) t.ty).toStr()) +
                ") not found");
        }

        current_method.has_super = true; 
    }

    
    public void TypeCheckAtribNode(AtribNode node) throws SemanticException {
        type t1;
        type t2;
        EntryVar v;

        if (node == null) {
            return;
        }

        
        if (!(node.expr1 instanceof DotNode || node.expr1 instanceof IndexNode ||
                node.expr1 instanceof VarNode)) {
            throw new SemanticException(node.position,
                "Invalid left side of assignment");
        }

        
        if (node.expr1 instanceof VarNode) {
            v = current_table.varFind(node.expr1.position.image);

            if ((v != null) && (v.local_count == 0)) 
             {
                throw new SemanticException(node.position,
                    "Assigning to variable " + " \"this\" is not legal");
            }
        }

        t1 = TypeCheckExpreNode(node.expr1);
        t2 = TypeCheckExpreNode(node.expr2);

        
        
        if (t1.dim != t2.dim) {
            throw new SemanticException(node.position,
                "Invalid dimensions in assignment");
        }

        
        if (t1.ty instanceof EntryClass && (t2.ty == NULL_TYPE)) {
            return;
        }

        
        if (!(isSubClass(t2.ty, t1.ty) || isSubClass(t1.ty, t2.ty))) {
            throw new SemanticException(node.position,
                "Incompatible types for assignment ");
        }
    }

    protected boolean isSubClass(EntryTable t1, EntryTable t2) {
        
        if (t1 == t2) {
            return true;
        }

        
        if (!(t1 instanceof EntryClass && t2 instanceof EntryClass)) {
            return false;
        }

        
        for (EntryClass p = ((EntryClass) t1).parent; p != null;
                p = p.parent)
            if (p == t2) {
                return true;
            }

        return false;
    }

    
    public void TypeCheckIfNode(IfNode node) {
        type t;

        if (node == null) {
            return;
        }

        try {
            t = TypeCheckExpreNode(node.expr);

            if ((t.ty != INT_TYPE) || (t.dim != 0)) {
                throw new SemanticException(node.expr.position,
                    "Integer expression expected");
            }
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        try {
            TypeCheckStatementNode(node.stat1);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        try {
            TypeCheckStatementNode(node.stat2);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }
    }

    //
    public void TypeCheckForNode(ForNode node) {
        type t;

        if (node == null) {
            return;
        }

        
        try {
            TypeCheckStatementNode(node.init);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        
        try {
            t = TypeCheckExpreNode(node.expr);

            if ((t.ty != INT_TYPE) || (t.dim != 0)) {
                throw new SemanticException(node.expr.position,
                    "Integer expression expected");
            }
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        
        try {
            TypeCheckStatementNode(node.incr);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        
        try {
            nesting++; 
            TypeCheckStatementNode(node.stat);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
        }

        nesting--; 
    }

    
    public void TypeCheckBreakNode(BreakNode node) throws SemanticException {
        if (node == null) {
            return;
        }

        
        if (nesting <= 0) {
            throw new SemanticException(node.position,
                "break not in a for statement");
        }
    }

    
    public void TypeCheckNopNode(NopNode node) {
        
    }

    
    
    public type TypeCheckNewObjectNode(NewObjectNode node)
        throws SemanticException {
        type t;
        EntryMethod p;
        EntryTable c;

        if (node == null) {
            return null;
        }

        
        c = current_table.classFindUp(node.name.image);

        
        if (c == null) {
            throw new SemanticException(node.position,
                "Class " + node.name.image + " not found");
        }

        
        t = TypeCheckExpreListNode(node.args);

        
        Symtable s = ((EntryClass) c).nested;
        p = s.methodFindInClass("constructor", (EntryRec) t.ty);

        
        if (p == null) {
            throw new SemanticException(node.position,
                "Constructor " + node.name.image + "(" +
                ((t.ty == null) ? "" : ((EntryRec) t.ty).toStr()) +
                ") not found");
        }

        
        t = new type(c, 0);

        return t;
    }

    
    public type TypeCheckNewArrayNode(NewArrayNode node) throws SemanticException {
        type t;
        EntryTable c;
        ListNode p;
        ExpreNode q;
        int k;

        if (node == null) {
            return null;
        }

        
        c = current_table.classFindUp(node.name.image);

        
        if (c == null) {
            throw new SemanticException(node.position,
                "Type " + node.name.image + " not found");
        }

        
        for (k = 0, p = node.dims; p != null; p = p.next) {
            t = TypeCheckExpreNode((ExpreNode) p.node);

            if ((t.ty != INT_TYPE) || (t.dim != 0)) {
                throw new SemanticException(p.position,
                    "Invalid expression for an array dimension");
            }

            k++;
        }

        return new type(c, k);
    }

    
    public type TypeCheckExpreListNode(ListNode node) {
        type t;
        type t1;
        EntryRec r;
        int n;

        if (node == null) {
            return new type(null, 0);
        }

        try {
            
            t = TypeCheckExpreNode((ExpreNode) node.node);
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            found_semantic_error++;
            t = new type(NULL_TYPE, 0);
        }

        
        t1 = TypeCheckExpreListNode(node.next);

        
        n = (t1.ty == null) ? 0 : ((EntryRec) t1.ty).count;

        
        r = new EntryRec(t.ty, t.dim, n + 1, (EntryRec) t1.ty);

        
        t = new type(r, 0);

        return t;
    }

    
    public type TypeCheckRelationalNode(RelationalNode node)
        throws SemanticException {
        type t1;
        type t2;
        int op; 

        if (node == null) {
            return null;
        }

        op = node.position.kind;
        t1 = TypeCheckExpreNode(node.expr1);
        t2 = TypeCheckExpreNode(node.expr2);

        
        if ((t1.ty == INT_TYPE) && (t2.ty == INT_TYPE)) {
            return new type(INT_TYPE, 0);
        }

        
        if (t1.dim != t2.dim) {
            throw new SemanticException(node.position,
                "Can not compare objects with different dimensions");
        }

        
        if ((op != ezConstants.EQ) && (op != ezConstants.NEQ) &&
                (t1.dim > 0)) {
            throw new SemanticException(node.position,
                "Can not use " + node.position.image + " for arrays");
        }

        
        
        if ((isSubClass(t2.ty, t1.ty) || isSubClass(t1.ty, t2.ty)) &&
                ((op == ezConstants.NEQ) || (op == ezConstants.EQ))) {
            return new type(INT_TYPE, 0);
        }

        
        if (((t1.ty instanceof EntryClass && (t2.ty == NULL_TYPE)) ||
                (t2.ty instanceof EntryClass && (t1.ty == NULL_TYPE))) &&
                ((op == ezConstants.NEQ) || (op == ezConstants.EQ))) {
            return new type(INT_TYPE, 0);
        }

        throw new SemanticException(node.position,
            "Invalid types for " + node.position.image);
    }

    
    public type TypeCheckAddNode(AddNode node) throws SemanticException {
        type t1;
        type t2;
        int op; 
        int i;
        int j;

        if (node == null) {
            return null;
        }

        op = node.position.kind;
        t1 = TypeCheckExpreNode(node.expr1);
        t2 = TypeCheckExpreNode(node.expr2);

        
        if ((t1.dim > 0) || (t2.dim > 0)) {
            throw new SemanticException(node.position,
                "Can not use " + node.position.image + " for arrays");
        }

        i = j = 0;

        if (t1.ty == INT_TYPE) {
            i++;
        } else if (t1.ty == STRING_TYPE) {
            j++;
        }

        if (t2.ty == INT_TYPE) {
            i++;
        } else if (t2.ty == STRING_TYPE) {
            j++;
        }

        
        if (i == 2) {
            return new type(INT_TYPE, 0);
        }

        
        if ((op == ezConstants.PLUS) && ((i + j) == 2)) {
            return new type(STRING_TYPE, 0);
        }

        throw new SemanticException(node.position,
            "Invalid types for " + node.position.image);
    }

    
    public type TypeCheckMultNode(MultNode node) throws SemanticException {
        type t1;
        type t2;
        int op; 
        int i;
        int j;

        if (node == null) {
            return null;
        }

        op = node.position.kind;
        t1 = TypeCheckExpreNode(node.expr1);
        t2 = TypeCheckExpreNode(node.expr2);

        
        if ((t1.dim > 0) || (t2.dim > 0)) {
            throw new SemanticException(node.position,
                "Can not use " + node.position.image + " for arrays");
        }

        
        if ((t1.ty != INT_TYPE) || (t2.ty != INT_TYPE)) {
            throw new SemanticException(node.position,
                "Invalid types for " + node.position.image);
        }

        return new type(INT_TYPE, 0);
    }

    
    public type TypeCheckUnaryNode(UnaryNode node) throws SemanticException {
        type t;

        if (node == null) {
            return null;
        }

        t = TypeCheckExpreNode(node.expr);

        
        if (t.dim > 0) {
            throw new SemanticException(node.position,
                "Can not use unary " + node.position.image + " for arrays");
        }

        
        if (t.ty != INT_TYPE) {
            throw new SemanticException(node.position,
                "Incompatible type for unary " + node.position.image);
        }

        return new type(INT_TYPE, 0);
    }

    
    public type TypeCheckIntConstNode(IntConstNode node) throws SemanticException {
        int k;

        if (node == null) {
            return null;
        }

        
        try {
            k = Integer.parseInt(node.position.image);
        } catch (NumberFormatException e) { 
            throw new SemanticException(node.position, "Invalid int constant");
        }

        return new type(INT_TYPE, 0);
    }

    
    public type TypeCheckStringConstNode(StringConstNode node) {
        if (node == null) {
            return null;
        }

        return new type(STRING_TYPE, 0);
    }

    
    public type TypeCheckNullConstNode(NullConstNode node) {
        if (node == null) {
            return null;
        }

        return new type(NULL_TYPE, 0);
    }

    
    public type TypeCheckVarNode(VarNode node) throws SemanticException {
        EntryVar p;

        if (node == null) {
            return null;
        }

        
        p = current_table.varFind(node.position.image);

        
        if (p == null) {
            throw new SemanticException(node.position,
                "Variable " + node.position.image + " not found");
        }

        return new type(p.type, p.dim);
    }

    
    public type TypeCheckCallNode(CallNode node) throws SemanticException {
        EntryClass c;
        EntryMethod m;
        type t1;
        type t2;

        if (node == null) {
            return null;
        }

        
        t1 = TypeCheckExpreNode(node.expr);

        
        if (t1.dim > 0) {
            throw new SemanticException(node.position, "Arrays do not have methods");
        }

        
        if (!(t1.ty instanceof EntryClass)) {
            throw new SemanticException(node.position,
                "Type " + t1.ty.name + " does not have methods");
        }

        
        t2 = TypeCheckExpreListNode(node.args);

        
        c = (EntryClass) t1.ty;
        m = c.nested.methodFind(node.meth.image, (EntryRec) t2.ty);

        
        if (m == null) {
            throw new SemanticException(node.position,
                "Method " + node.meth.image + "(" +
                ((t2.ty == null) ? "" : ((EntryRec) t2.ty).toStr()) +
                ") not found in class " + c.name);
        }

        return new type(m.type, m.dim);
    }

    
    public type TypeCheckIndexNode(IndexNode node) throws SemanticException {
        EntryClass c;
        type t1;
        type t2;

        if (node == null) {
            return null;
        }

        
        t1 = TypeCheckExpreNode(node.expr1);

        
        if (t1.dim <= 0) {
            throw new SemanticException(node.position,
                "Can not index non array variables");
        }

        
        t2 = TypeCheckExpreNode(node.expr2);

        
        if ((t2.ty != INT_TYPE) || (t2.dim > 0)) {
            throw new SemanticException(node.position,
                "Invalid type. Index must be int");
        }

        return new type(t1.ty, t1.dim - 1);
    }

    
    public type TypeCheckDotNode(DotNode node) throws SemanticException {
        EntryClass c;
        EntryVar v;
        type t;

        if (node == null) {
            return null;
        }

        
        t = TypeCheckExpreNode(node.expr);

        
        if (t.dim > 0) {
            throw new SemanticException(node.position, "Arrays do not have fields");
        }

        
        if (!(t.ty instanceof EntryClass)) {
            throw new SemanticException(node.position,
                "Type " + t.ty.name + " does not have fields");
        }

        
        c = (EntryClass) t.ty;
        v = c.nested.varFind(node.field.image);

        
        if (v == null) {
            throw new SemanticException(node.position,
                "Variable " + node.field.image + " not found in class " + c.name);
        }

        return new type(v.type, v.dim);
    }

    
    public type TypeCheckExpreNode(ExpreNode node) throws SemanticException {
        if (node instanceof NewObjectNode) {
            return TypeCheckNewObjectNode((NewObjectNode) node);
        } else if (node instanceof NewArrayNode) {
            return TypeCheckNewArrayNode((NewArrayNode) node);
        } else if (node instanceof RelationalNode) {
            return TypeCheckRelationalNode((RelationalNode) node);
        } else if (node instanceof AddNode) {
            return TypeCheckAddNode((AddNode) node);
        } else if (node instanceof MultNode) {
            return TypeCheckMultNode((MultNode) node);
        } else if (node instanceof UnaryNode) {
            return TypeCheckUnaryNode((UnaryNode) node);
        } else if (node instanceof CallNode) {
            return TypeCheckCallNode((CallNode) node);
        } else if (node instanceof IntConstNode) {
            return TypeCheckIntConstNode((IntConstNode) node);
        } else if (node instanceof StringConstNode) {
            return TypeCheckStringConstNode((StringConstNode) node);
        } else if (node instanceof NullConstNode) {
            return TypeCheckNullConstNode((NullConstNode) node);
        } else if (node instanceof IndexNode) {
            return TypeCheckIndexNode((IndexNode) node);
        } else if (node instanceof DotNode) {
            return TypeCheckDotNode((DotNode) node);
        } else if (node instanceof VarNode) {
            return TypeCheckVarNode((VarNode) node);
        } else {
            return null;
        }
    }

    
    public void TypeCheckStatementNode(StatementNode node)
        throws SemanticException {
        if (node instanceof BlockNode) {
            TypeCheckBlockNode((BlockNode) node);
        } else if (node instanceof VarDeclNode) {
            TypeCheckLocalVarDeclNode((VarDeclNode) node);
        } else if (node instanceof AtribNode) {
            TypeCheckAtribNode((AtribNode) node);
        } else if (node instanceof IfNode) {
            TypeCheckIfNode((IfNode) node);
        } else if (node instanceof ForNode) {
            TypeCheckForNode((ForNode) node);
        } else if (node instanceof PrintNode) {
            TypeCheckPrintNode((PrintNode) node);
        } else if (node instanceof NopNode) {
            TypeCheckNopNode((NopNode) node);
        } else if (node instanceof ReadNode) {
            TypeCheckReadNode((ReadNode) node);
        } else if (node instanceof ReturnNode) {
            TypeCheckReturnNode((ReturnNode) node);
        } else if (node instanceof SuperNode) {
            TypeCheckSuperNode((SuperNode) node);
        } else if (node instanceof BreakNode) {
            TypeCheckBreakNode((BreakNode) node);
        }
    }
}
