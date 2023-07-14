// packege codegen;

// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.FileOutputStream;
// import java.io.PrintWriter;

// import parser.ezConstants;
// import semanalysis.SemanticException;
// import semanalysis.TypeCheck;
// import semanalysis.type;
// import symtable.EntryClass;
// import symtable.EntryMethod;
// import symtable.EntryRec;
// import symtable.EntryTable;
// import symtable.EntryVar;
// import symtable.Symtable;
// import syntacticTree.AddNode;
// import syntacticTree.AtribNode;
// import syntacticTree.BlockNode;
// import syntacticTree.BreakNode;
// import syntacticTree.CallNode;
// import syntacticTree.ClassBodyNode;
// import syntacticTree.ClassDeclNode;
// import syntacticTree.ConstructDeclNode;
// import syntacticTree.DotNode;
// import syntacticTree.ExpreNode;
// import syntacticTree.ForNode;
// import syntacticTree.IfNode;
// import syntacticTree.IndexNode;
// import syntacticTree.IntConstNode;
// import syntacticTree.ListNode;
// import syntacticTree.MethodBodyNode;
// import syntacticTree.MethodDeclNode;
// import syntacticTree.MultNode;
// import syntacticTree.NewArrayNode;
// import syntacticTree.NewObjectNode;
// import syntacticTree.NopNode;
// import syntacticTree.NullConstNode;
// import syntacticTree.PrintNode;
// import syntacticTree.ReadNode;
// import syntacticTree.RelationalNode;
// import syntacticTree.ReturnNode;
// import syntacticTree.StatementNode;
// import syntacticTree.StringConstNode;
// import syntacticTree.SuperNode;
// import syntacticTree.UnaryNode;
// import syntacticTree.VarDeclNode;
// import syntacticTree.VarNode;

// public class CodeGen extends TypeCheck {
//     static final String JasExtension = ".j"; // extension of the generated file (Jasmin)
//     String source_file; // source file name
//     String source_path; // path of source file
//     String source_abs; // sourcePath + sourceFile
//     PrintWriter fp = null; // file pointer for output (where things are printed)
//     String break_label = null; // label for break
//     int current_label = 0; // label counter
//     boolean has_start = false; // true if the class has a start method
//     boolean has_return = false; // true if the method has a return
//     int stack_size = 0; // stack size
//     int stack_high = 0; // stack high mark (copilot called it "water mark")

//     public CodeGen() {
//         super(); // call the constructor of the superclass(TypeCheck)
//     }

//     public void CodeGenRoot(ListNode node, String filename) throws SemanticException, GenCodeException {
//         TypeCheckRoot(node); // semantic analysis of the root
//         System.out.println("0 Semantic error found");

//         int i = filename.lastIndexOf(File.separator); // get just the name of the file
//         this.source_file = filename.substring(i + 1);

//         if (i < 0) {
//             this.source_path = "";
//         } else {
//             this.source_path = filename.substring(0, i);
//         }

//         this.source_abs = filename;
//         CodeGenClassDeclListNode(node); // generate code for the root
//     }

//     // Class list
//     public void CodeGenClassDeclListNode(ListNode node) throws GenCodeException {
//         if (node == null) {
//             return;
//         }

//         CodeGenClassDeclNode((ClassDeclNode) node.node);
//         CodeGenClassDeclListNode(node.next);
//     }

//     // Class declaration
//     public void CodeGenClassDeclNode(ClassDeclNode node) throws GenCodeException {
//         Symtable temp_table = this.current_table; // save the current symbol table
//         boolean temp_has_start = this.has_start; // save the current value of hasStart
//         EntryClass the_class = null;
//         EntryClass new_class; // new class entry
//         EntryClass new_superclass = null; // new superclass entry
//         String the_filename = null; // filename for the class
//         String sname; // superclass name
//         FileOutputStream j_file; // .jas file
//         PrintWriter fp_old = this.fp; // save the current file pointer

//         if (node == null) {
//             return;
//         }

//         new_class = (EntryClass) this.current_table.classFindUp(node.name.image);
//         new_superclass = new_class.parent;

//         if (new_superclass == null) {
//             sname = "java/lang/Object"; // if there is no superclass, use Object
//         } else {
//             sname = new_superclass.completeName();
//         }

//         the_filename = this.source_abs + "." + new_class.completeName() + JasExtension;

//         try {
//             j_file = new FileOutputStream(the_filename);
//         } catch (FileNotFoundException e) {
//             throw new GenCodeException("Can not create output file " + the_filename);
//         }

//         this.fp = new PrintWriter(j_file);
//         System.out.println("Generating " + the_filename);

//         writeCode(";---------------------------------------");
//         writeCode(";   Code generated by EZ compiler  :D   ");
//         writeCode(";  Credits: Felipe Fava e Lucas Santos  ");
//         writeCode(";---------------------------------------");
//         writeCode(".source " + this.source_file);
//         writeCode(".class public " + new_class.completeName());
//         writeCode(".super " + sname);

//         this.current_table = new_class.nested; // change the current symbol table
//         has_start = false; // reset hasStart

//         CodeGenClassBodyNode(node.body); // generate code for the class body

//         if (has_start) {
//             createMain();
//         }

//         this.fp.close(); // close the file
//         this.fp = fp_old; // restore the old file pointer
//         this.current_table = temp_table; // restore the old symbol table
//         this.has_start = temp_has_start; // restore the old value of hasStart

//     }

//     // Class body
//     public void CodeGenClassBodyNode(ClassBodyNode node) throws GenCodeException {
//         EntryMethod m;

//         if (node == null) {
//             return;
//         }
//         CodeGenClassDeclListNode(node.clist);
//         CodeGenVarDeclListNode(node.vlist);

//         m = this.current_table.methodFindInClass("constructor", null);

//         if (m.fake) {
//             GeraConstructorDefault(); // if there is no constructor, generate a default one
//         }

//         CodeGenConstructDeclListNode(node.ctlist);
//         CodeGenMethodDeclListNode(node.mlist);
//     }

//     // generate a default constructor
//     public void GeraConstructorDefault() {
//         String sup;
//         EntryClass curr_class = (EntryClass) this.current_table.levelup; // get the current class

//         if (curr_class.parent != null) {
//             sup = curr_class.parent.completeName();
//         } else {
//             sup = "java/lang/Object";
//         }

//         writeCode();
//         writeCode(";Default constructor. Just call super.");
//         writeCode(".method public <init>()V");
//         writeCode(".limit locals 1");
//         writeCode(".limit stack 1");
//         writeCode("aload_0");
//         writeCode("invokespecial " + sup + "/<init>()V");
//         writeCode("return");
//         writeCode(".end method");
//     }

//     // Variable declaration list
//     public void CodeGenVarDeclListNode(ListNode node){
//         if (node == null) {
//             return;
//         }

//         CodeGenVarDeclNode((VarDeclNode) node.node);
//         CodeGenVarDeclListNode(node.next);
//     }

//     // Variable declaration
//     public void CodeGenVarDeclNode(VarDeclNode node) {
//         ListNode aux;
//         EntryVar var;
//         EntryTable table = null;

//         if (node == null) {
//             return;
//         }

//         for (aux = node.vars; aux != null; aux = aux.next) {
//             VarNode var_node = (VarNode) aux.node;

//             var = this.current_table.varFind(var_node.position.image);

//             writeCode(".field public " + var.name + " " + var.dscJava());
//         }
//     }

//     // Constructor declaration list
//     public void CodeGenConstructDeclListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         CodeGenConstructDeclNode((ConstructDeclNode) node.node);
//         CodeGenConstructDeclListNode(node.next);
//     }

//     // Constructor declaration
//     public void CodeGenConstructDeclNode(ConstructDeclNode node) {
//         EntryMethod m;
//         EntryRec r = null;
//         EntryTable the_table;
//         EntryClass the_class;
//         EntryVar the_var;
//         ListNode aux;
//         VarDeclNode var_decl_node;
//         VarNode var_node;
//         int i;
//         String sup = "";

//         if (node == null) {
//             return;
//         }

//         aux = node.body.param;
//         i = 0;

//         while (aux != null) {
//             var_decl_node = (VarDeclNode) aux.node; // get the variable declaration node(param)
//             var_node = (VarNode) var_decl_node.vars.node; // get name and dimension of the variable
//             i++;

//             the_table = this.current_table.classFindUp(var_node.position.image); // find the variable in the symbol table

//             r = new EntryRec(the_table, var_node.dim, i, r); // create a new parameter
//             aux = aux.next;
//         }

//         String param_list = "";

//         if (r != null) {
//             r = r.inverte(); // invert the order of the parameter list
//             param_list = r.dscJava(); // get the parameter list as a string
//         }

//         m = this.current_table.methodFind("constructor", r); // find the constructor in the symbol table
//         this.current_method = m; // set the current method

//         writeCode();
//         writeCode(".method public <init>(" + param_list + ")V");
//         writeCode(".limit locals " + m.local_count_total);

//         the_class = (EntryClass) this.current_table.levelup; // get the current class

//         if (the_class.parent != null) {
//             sup = the_class.parent.completeName();
//         } else {
//             sup = "java/lang/Object";
//         }

//         if (!this.current_method.has_super) {
//             writeCode("aload_0", 1);
//             writeCode("invokespecial " + sup + "/<init>()V", -1);
//         }

//         this.current_table.beginScope();
//         the_var = new EntryVar("this", the_class, 0, 0); // create a new variable
//         this.current_table.add(the_var); // add the variable to the symbol table
//         this.n_locals = 1; // set the number of local variables to 1
//         stack_size = stack_high = 0; // reset the stack size and the stack high mark
//         CodeGenMethodBodyNode(node.body); // generate code for the method body
//         this.current_table.endScope();
//         writeCode("return");
//         writeCode(".limit stack " + (m.total_stack_count = stack_size));
//         writeCode(".end method");
//     }

//     // Method declaration list
//     public void CodeGenMethodDeclListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         CodeGenMethodDeclNode((MethodDeclNode) node.node);
//         CodeGenMethodDeclListNode(node.next);
//     }

//     // Method declaration
//     public void CodeGenMethodDeclNode(MethodDeclNode node) {
//         EntryMethod m;
//         EntryRec r = null;
//         EntryTable the_table;
//         EntryVar the_var;
//         ListNode aux;
//         VarDeclNode var_decl_node;
//         VarNode var_node;
//         int i;

//         if (node == null) {
//             return;
//         }

//         aux = node.body.param;
//         i = 0;

//         while (aux != null) {
//             var_decl_node = (VarDeclNode) aux.node; // get the variable declaration node(param)
//             var_node = (VarNode) var_decl_node.vars.node; // get name and dimension of the variable
//             i++;

//             the_table = this.current_table.classFindUp(var_node.position.image); // find the variable in the symbol
//                                                                                  // table

//             r = new EntryRec(the_table, var_node.dim, i, r); // create a new parameter
//             aux = aux.next;
//         }

//         String param_list = "";

//         if (r != null) {
//             r = r.inverte(); // invert the order of the parameter list
//             param_list = r.dscJava(); // get the parameter list as a string
//         }

//         m = this.current_table.methodFind(node.name.image, r); // find the method in the symbol table
//         this.current_method = m; // set the current method

//         String statc = "";
//         writeCode();

//         if (m.name.equals("start") && (r == null)) {
//             statc = "static ";
//             has_start = true;
//         }

//         writeCode(".method public " + statc + m.name + "(" + param_list + ")" + m.dscJava());
//         writeCode(".limit locals " + m.local_count_total);

//         this.current_table.beginScope();
//         the_var = new EntryVar("this", this.current_table.levelup, 0, 0); // create a new variable
//         this.current_table.add(the_var); // add the variable to the symbol table
//         this.n_locals = 1; // set the number of local variables to 0
//         stack_size = stack_high = 0; // reset the stack size and the stack high mark
//         CodeGenMethodBodyNode(node.body); // generate code for the method body
//         this.current_table.endScope();
//         if ((this.current_method.type == INT_TYPE) && (this.current_method.dim == 0)) {
//             writeCode("bipush 0", 1);
//             writeCode("ireturn", -1);
//         } else {
//             writeCode("aconst_null", 1);
//             writeCode("areturn", -1);
//         }
//         writeCode(".limit stack " + (m.total_stack_count = stack_size));
//         writeCode(".end method");
//     }

//     // Method body
//     public void CodeGenMethodBodyNode(MethodBodyNode node) {
//         if (node == null) {
//             return;
//         }

//         CodeGenLocalVarDeclListNode(node.param);
//         CodeGenStatementNode(node.stat);
//     }

//     // Local variable declaration list
//     public void CodeGenLocalVarDeclListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         CodeGenLocalVarDeclNode((VarDeclNode) node.node);
//         CodeGenLocalVarDeclListNode(node.next);
//     }

//     // Local variable declaration
//     public void CodeGenLocalVarDeclNode(VarDeclNode node) {
//         ListNode aux;
//         VarNode var;
//         EntryTable table;

//         if (node == null) {
//             return;
//         }

//         table = this.current_table.classFindUp(node.position.image); // find the variable in the symbol table

//         for (aux = node.vars; aux != null; aux = aux.next) {
//             var = (VarNode) aux.node; // get name and dimension of the variable

//             this.current_table.add(new EntryVar(var.position.image, table, var.dim, this.n_locals++)); // add the
//                                                                                                          // variable to
//                                                                                                          // the
//                                                                                                          // symbol table
//         }
//     }

//     // Compost command
//     public void CodeGenBlockNode(BlockNode node) {
//         this.current_table.beginScope();
//         CodeGenStatementListNode(node.stats);
//         this.current_table.endScope();
//     }

//     // Statement list
//     public void CodeGenStatementListNode(ListNode node) {
//         if (node == null) {
//             return;
//         }

//         CodeGenStatementNode((StatementNode) node.node);
//         CodeGenStatementListNode(node.next);
//     }

//     // Print command
//     public void CodeGenPrintNode(PrintNode node) {
//         type t;

//         if (node == null) {
//             return;
//         }
//         writeCode();
//         writeCode(";begins print ");

//         writeCode("getstatic java/lang/System/out Ljava/io/PrintStream;", 1);

//         this.has_return = false;
//         t = CodeGenExpreNode(node.expr); // generate code for the expression

//         writeCode("invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V", -2);
//     }

//     // Read command
//     public void CodeGenReadNode(ReadNode node) {
//         type t = null;

//         if (node == null) {
//             return;
//         }

//         try {
//             t = TypeCheckExpreNode(node.expr); // semantic analysis of the expression
//         } catch (Exception e) {
//         }

//         if (t.ty == INT_TYPE) {
//             writeCode("invokestatic EZrt/Runtime/readInt()I", 1);
//         } else {
//             writeCode("invokestatic EZrt/Runtime/readString()Ljava/lang/String;", 1);
//         }

//         this.has_return = true;
//         CodeGenExpreNode(node.expr); // generate code for the expression
//     }

//     // Return command
//     public void CodeGenReturnNode(ReturnNode node) {
//         type t;

//         if (node == null) {
//             return;
//         }

//         writeCode();
//         writeCode(";begins return ");

//         this.has_return = false;
//         t = CodeGenExpreNode(node.expr); // generate code for the expression

//         if ((t.ty == INT_TYPE) && (t.dim == 0)) {
//             writeCode("ireturn", -1);
//         } else {
//             writeCode("areturn", -1);
//         }
//     }

//     // Super command
//     public void CodeGenSuperNode(SuperNode node) {
//         type t;
//         String sup;

//         if (node == null) {
//             return;
//         }
//         EntryClass super_class = this.current_table.levelup.parent; // get the superclass of the current class
//         sup = super_class.completeName();

//         writeCode();
//         writeCode(";begins super");
//         writeCode("aload_0", 1);

//         this.has_return = false;
//         t = CodeGenExpreListNode(node.args); // generate code for the expression list

//         String arg_list = (t.ty == null) ? "" : t.dscJava();
//         int remove_stack = (t.ty == null) ? 0 : ((EntryRec) t.ty).count;

//         writeCode("invokespecial " + sup + "/<init>(" + arg_list + ")V", -(remove_stack + 1));
//     }

//     // Attribution command
//     public void CodeGenAtribNode(AtribNode node) {
//         type t1 = null;
//         type t2;

//         if (node == null) {
//             return;
//         }

//         try {
//             t1 = TypeCheckExpreNode(node.expr1); // semantic analysis of the expression
//         } catch (Exception e) {
//         }

//         this.has_return = false;
//         t2 = CodeGenExpreNode(node.expr2); // generate code for the expression

//         if ((t1.ty != t2.ty) && isSubClass(t1.ty, t2.ty)) {
//             if ( t1.dim > 0 ) {
//                 writeCode("checkcast " + t1.dscJava());
//             } else {
//                 EntryClass c = (EntryClass) t1.ty;
//                 writeCode("checkcast " + c.completeName());
//             }
//         }

//         this.has_return = true;
//         CodeGenExpreNode(node.expr1); // generate code for the expression
//     }

//     // If command
//     public void CodeGenIfNode(IfNode node) {
//         type t;
//         String label = this.newLabel();

//         if (node == null) {
//             return;
//         }

//         writeCode();
//         writeCode(";begins if " + label);

//         this.has_return = false;
//         t = CodeGenExpreNode(node.expr); // generate code for the expression

//         writeCode("ifeq else" + label, -1);
//         CodeGenStatementNode(node.stat1); // generate code for the statement

//         if (node.stat2 != null) {
//             writeCode("goto fi" + label); // jump to the end of the if
//             writeLabel("else " + label + ":"); // put the else label
//             CodeGenStatementNode(node.stat2); // generate code for the statement
//             writeLabel("fi" + label + ":"); // put the end of the if label
//         } else {
//             writeLabel("else" + label + ":"); // put the else label
//         }
//     }

//     // For command
//     public void CodeGenForNode(ForNode node) {
//         String label = this.newLabel();
//         String old_break = this.break_label;

//         if (node == null) {
//             return;
//         }

//         writeCode();
//         writeCode(";begins for " + label);

//         CodeGenStatementNode(node.init); // generate code for the statement list

//         writeLabel("forloop" + label + ":"); // put the for label

//         this.has_return = false;
//         CodeGenExpreNode(node.expr); // generate code for the expression

//         writeCode("ifeq rof" + label, -1);
//         this.break_label = "rof" + label; // set the break label to the end of the for
//         CodeGenStatementNode(node.stat); // generate code for the statement (for body)

//         CodeGenStatementNode(node.incr); // generate code for the statement (for increment)

//         writeCode("goto forloop" + label); // jump to the beginning of the for

//         writeLabel(label + ":"); // put the end of the for label
//         this.break_label = old_break; // restore the old break label
//     }

//     // Break command
//     public void CodeGenBreakNode(BreakNode node) {
//         if (node == null) {
//             return;
//         }

//         writeCode();
//         writeCode(";begins break ");

//         writeCode("goto " + this.break_label);
//     }

//     // Empty command
//     public void CodeGenNopNode(NopNode node) {
//         writeCode();
//         writeCode(";begins empty statement ");
//         writeCode("nop");
//     }

//     // Object creation
//     public type CodeGenNewObjectNode(NewObjectNode node) {
//         type t;
//         EntryMethod p;
//         EntryClass c;

//         if (node == null) {
//             return null;
//         }

//         c = (EntryClass) this.current_table.classFindUp(node.name.image); // find the class in the symbol table

//         writeCode("new " + ((EntryClass) c).completeName(), 1);

//         writeCode("dup", 1);

//         t = CodeGenExpreListNode(node.args); // generate code for the expression list

//         String arg_list = (t.ty == null) ? "" : t.dscJava();
//         int remove_stack = (t.ty == null) ? 0 : ((EntryRec) t.ty).count;

//         writeCode("invokespecial " + c.completeName() + "/<init>(" + arg_list + ")V", -(remove_stack + 1));

//         t = new type(c, 0); // create a new type
//         return t;
//     }

//     // Array creation
//     public type CodeGenNewArrayNode(NewArrayNode node) {
//         type t;
//         EntryTable table;
//         EntryRec r;

//         if (node == null) {
//             return null;
//         }

//         table = this.current_table.classFindUp(node.name.image); // find the variable in the symbol table
//         t = CodeGenExpreListNode(node.dims); // generate code for the expression list
//         r = (EntryRec) t.ty;

//         if (r.count == 1) {
//             if (table == INT_TYPE) {
//                 writeCode("newarray I", 1);
//             } else {
//                 writeCode("anewarray " + table.dscJava(), 1);
//             }
//         } else {
//             writeCode("multianewarray " + EntryTable.strDim(r.count) + table.dscJava() + " " + r.count, 1);
//         }

//         return new type(table, r.count);
//     }

//     // Expression list
//     public type CodeGenExpreListNode(ListNode node) {
//         type t1;
//         type t2;
//         EntryRec r;
//         int i;

//         if (node == null) {
//             return new type(null, 0);
//         }

//         t1 = CodeGenExpreNode((ExpreNode) node.node); // generate code for the expression

//         t2 = CodeGenExpreListNode(node.next); // generate code for the expression list

//         i = (t2.ty == null) ? 0 : ((EntryRec) t2.ty).count;

//         r = new EntryRec(t1.ty, t1.dim, i + 1, (EntryRec) t2.ty); // create a new record

//         return new type(r, 0); // create a new type
//     }

//     // Relacional expression
//     public type CodeGenRelationalNode(RelationalNode node) {
//         type t1;
//         type t2;
//         int operation;
//         String label = this.newLabel();

//         if (node == null) {
//             return null;
//         }

//         operation = node.position.kind;

//         t1 = CodeGenExpreNode(node.expr1); // generate code for the expression
//         t2 = CodeGenExpreNode(node.expr2); // generate code for the expression

//         if ((t1.dim == 0) && (t1.ty == INT_TYPE)) {
//             String s = this.selectBinInstruction(operation);
//             writeCode(s + "relexpr" + label, -2);
//         } else {
//             if (operation == ezConstants.EQ) {
//                 writeCode("if_acmpeq relexpr" + label, -2);
//             } else {
//                 writeCode("if_acmpne relexpr" + label, -2);
//             }
//         }
//         writeCode("bipush 0", 1);
//         writeCode("goto pxeler" + label);
//         writeLabel("relexpr" + label + ":");
//         writeCode("bipush 1"); // do not increment stack or will be executed twice
//         writeLabel("pxeler" + label + ":");
//         return new type(INT_TYPE, 0);
//     }

//     // Sum and subtraction expression
//     public type CodeGenAddNode(AddNode node) {
//         type t1;
//         type t2;
//         int operation;
//         int i, j;

//         if (node == null) {
//             return null;
//         }

//         operation = node.position.kind;
//         t1 = CodeGenExpreNode(node.expr1); // generate code for the expression
//         t2 = CodeGenExpreNode(node.expr2); // generate code for the expression

//         i = j = 0;

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

//         if (i == 2) {
//             String s = selectBinInstruction(operation);
//             writeCode(s, -1);
//             return new type(INT_TYPE, 0);
//         }

//         if (j == 2) {  // ################### CAN BE REMOVED ###################
//             writeCode("invokevirtual java/lang/String/concat" + "(Ljava/lang/String;)Ljava/lang/String;", -1);
//             return new type(STRING_TYPE, 0);
//         }

//         if (t1.ty == INT_TYPE) { // 
//             writeCode("swap"); // swap the top two values on the stack
//             writeCode("invokestatic java/lang/Integer/toString(I)" + "Ljava/lang/String;");
//             writeCode("swap"); // swap back
//         } else {
//             writeCode("invokestatic java/lang/Integer/toString(I)" + "Ljava/lang/String;");
//         }

//         writeCode("invokevirtual java/lang/String/concat" + "(Ljava/lang/String;)Ljava/lang/String;", -1);

//         return new type(STRING_TYPE, 0);
//     }

//     // Multiplication and division expression
//     public type CodeGenMultNode(MultNode node) {
//         type t1;
//         type t2;
//         int operation;

//         if (node == null) {
//             return null;
//         }

//         operation = node.position.kind;
//         t1 = CodeGenExpreNode(node.expr1); // generate code for the expression
//         t2 = CodeGenExpreNode(node.expr2); // generate code for the expression

//         String s = selectBinInstruction(operation);
//         writeCode(s, -1);

//         return new type(INT_TYPE, 0);
//     }

//     // Unary expression
//     public type CodeGenUnaryNode(UnaryNode node) {
//         type t;
//         int operation;

//         if (node == null) {
//             return null;
//         }

//         operation = node.position.kind;
//         t = CodeGenExpreNode(node.expr); // generate code for the expression

//         if (operation == ezConstants.MINUS) {
//             writeCode("ineg");
//         }

//         return new type(INT_TYPE, 0);
//     }

//     // Integer constant
//     public type CodeGenIntConstNode(IntConstNode node) {
//         if (node == null) {
//             return null;
//         }

//         writeCode("ldc " + node.position.image, 1);
//         return new type(INT_TYPE, 0);
//     }

//     // String constant
//     public type CodeGenStringConstNode(StringConstNode node) {
//         if (node == null) {
//             return null;
//         }

//         writeCode("ldc " + node.position.image, 1);
//         return new type(STRING_TYPE, 0);
//     }

//     // Null constant
//     public type CodeGenNullConstNode(NullConstNode node) {
//         if (node == null) {
//             return null;
//         }

//         writeCode("aconst_null", 1);
//         return new type(NULL_TYPE, 0);
//     }

//     // Variable name
//     public type CodeGenVarNode(VarNode node) {
//         EntryVar var;
//         String s1 = this.has_return ? "store" : "load";
//         String s2 = this.has_return ? "putfield" : "getfield";

//         if (node == null) {
//             return null;
//         }

//         var = this.current_table.varFind(node.position.image); // find the variable in the symbol table

//         if (var.local_count >= 0) {
//             if((var.type == INT_TYPE) && (var.dim == 0)) {
//                 writeCode("i" + s1 + " " + var.local_count, this.has_return ? (-1) : 1);
//             } else {
//                 writeCode("a" + s1 + " " + var.local_count, this.has_return ? (-1) : 1);
//             }
//         } else {
//             EntryClass v = var.my_table.levelup;

//             writeCode("aload_0", 1);

//             if (this.has_return) {
//                 writeCode("swap");
//             }

//             writeCode(s2 + " " + v.completeName() + "/" + var.name + " " + var.dscJava(), this.has_return ? (-2) : 0);
//         }
//         return new type(var.type, var.dim);
//     }

//     // Method call
//     public type CodeGenCallNode(CallNode node) {
//         EntryClass c;
//         EntryMethod m;
//         type t1, t2;

//         if (node == null) {
//             return null;
//         }

//         t1 = CodeGenExpreNode(node.expr); // generate code for the expression
//         t2 = CodeGenExpreListNode(node.args); // generate code for the expression list

//         c = (EntryClass) t1.ty;
//         m = c.nested.methodFind(node.meth.image, (EntryRec) t2.ty); // find the method in the symbol table

//         String arg_list = (t2.ty == null) ? "" : t2.dscJava();
//         int remove_stack = (t2.ty == null) ? 0 : ((EntryRec) t2.ty).count;

//         writeCode("invokevirtual " + c.completeName() + "/" + m.name + "(" + arg_list + ")" + m.dscJava(), -remove_stack);
//         return new type(m.type, m.dim);
//     }

//     // Variable indexation
//     public type CodeGenIndexNode(IndexNode node) {
//         EntryClass c;
//         type t1, t2;
//         boolean b = this.has_return;

//         if (node == null) {
//             return null;
//         }

//         this.has_return = false;
//         t1 = CodeGenExpreNode(node.expr1); // generate code for the expression

//         if (b) {
//             writeCode("swap");
//         }

//         t2 = CodeGenExpreNode(node.expr2); // generate code for the expression

//         if (b) {
//             writeCode("swap");
//             if ((t1.ty == INT_TYPE) && (t1.dim == 1)) {
//                 writeCode("iastore", -3);
//             } else {
//                 writeCode("aastore", -3);
//             }
//         } else {
//             if ((t1.ty == INT_TYPE) && (t1.dim == 1)) {
//                 writeCode("iaload", -1);
//             } else {
//                 writeCode("aaload", -1);
//             }
//         }

//         this.has_return = b;
//         return new type(t1.ty, t1.dim - 1);
//     }

//     // Access to a class field
//     public type CodeGenDotNode(DotNode node) {
//         EntryClass c;
//         EntryVar v;
//         type t;
//         boolean b = this.has_return;

//         if (node == null) {
//             return null;
//         }

//         this.has_return = false;
//         t = CodeGenExpreNode(node.expr); // generate code for the expression

//         c = (EntryClass) t.ty;
//         v = c.nested.varFind(node.field.image); // find the variable in the symbol table

//         if (b) {
//             writeCode("swap");
//             writeCode("putfield " + c.completeName() + "/" + v.name + " " + v.dscJava(), -2);
//         } else {
//             writeCode("getfield " + c.completeName() + "/" + v.name + " " + v.dscJava());
//         }

//         this.has_return = b;

//         return new type(v.type, v.dim);
//     }

//     // Geral Expression
//     public type CodeGenExpreNode(ExpreNode node) {
//         if (node instanceof NewObjectNode) {
//             return CodeGenNewObjectNode((NewObjectNode) node);
//         } else if (node instanceof NewArrayNode) {
//             return CodeGenNewArrayNode((NewArrayNode) node);
//         } else if (node instanceof RelationalNode) {
//             return CodeGenRelationalNode((RelationalNode) node);
//         } else if (node instanceof AddNode) {
//             return CodeGenAddNode((AddNode) node);
//         } else if (node instanceof MultNode) {
//             return CodeGenMultNode((MultNode) node);
//         } else if (node instanceof UnaryNode) {
//             return CodeGenUnaryNode((UnaryNode) node);
//         } else if (node instanceof IntConstNode) {
//             return CodeGenIntConstNode((IntConstNode) node);
//         } else if (node instanceof StringConstNode) {
//             return CodeGenStringConstNode((StringConstNode) node);
//         } else if (node instanceof NullConstNode) {
//             return CodeGenNullConstNode((NullConstNode) node);
//         } else if (node instanceof VarNode) {
//             return CodeGenVarNode((VarNode) node);
//         } else if (node instanceof CallNode) {
//             return CodeGenCallNode((CallNode) node);
//         } else if (node instanceof IndexNode) {
//             return CodeGenIndexNode((IndexNode) node);
//         } else if (node instanceof DotNode) {
//             return CodeGenDotNode((DotNode) node);
//         } else {
//             return null;
//         }
//     }

//     // General statement
//     public void CodeGenStatementNode(StatementNode node) {
//         if (node instanceof BlockNode) {
//             CodeGenBlockNode((BlockNode) node);
//         } else if (node instanceof PrintNode) {
//             CodeGenPrintNode((PrintNode) node);
//         } else if (node instanceof ReadNode) {
//             CodeGenReadNode((ReadNode) node);
//         } else if (node instanceof ReturnNode) {
//             CodeGenReturnNode((ReturnNode) node);
//         } else if (node instanceof SuperNode) {
//             CodeGenSuperNode((SuperNode) node);
//         } else if (node instanceof AtribNode) {
//             CodeGenAtribNode((AtribNode) node);
//         } else if (node instanceof IfNode) {
//             CodeGenIfNode((IfNode) node);
//         } else if (node instanceof ForNode) {
//             CodeGenForNode((ForNode) node);
//         } else if (node instanceof BreakNode) {
//             CodeGenBreakNode((BreakNode) node);
//         } else if (node instanceof NopNode) {
//             CodeGenNopNode((NopNode) node);
//         }
//     }

//     // Select the binary instruction
//     public String selectBinInstruction(int operation) {
//         switch (operation) {
//             case ezConstants.PLUS:
//                 return "iadd";
//             case ezConstants.MINUS:
//                 return "isub";
//             case ezConstants.STAR:
//                 return "imul";
//             case ezConstants.SLASH:
//                 return "idiv";
//             case ezConstants.REM:
//                 return "irem";
//             case ezConstants.EQ:
//                 return "if_icmpeq";
//             case ezConstants.NEQ:
//                 return "if_icmpne";
//             case ezConstants.LT:
//                 return "if_icmplt";
//             case ezConstants.GT:
//                 return "if_icmpgt";
//             case ezConstants.LE:
//                 return "if_icmple";
//             case ezConstants.GE:
//                 return "if_icmpge";
//             }
//             return null;
//     }

//     // Create a new label
//     private String newLabel() {
//         return Integer.toString(this.current_label++);
//     }

//     // Create the main
//     private void createMain() {
//         EntryClass c = (EntryClass) this.current_table.levelup;

//         writeCode();
//         writeCode(";Entry point for the JVM");
//         writeCode(".method static public main([Ljava/lang/String;)V");
//         writeCode(".limit  locals 1");
//         writeCode(".limit  stack 1");
//         writeCode("invokestatic EZrt/Runtime/initialize()I");
//         writeCode("ifne         end");
//         writeCode("invokestatic " + c.completeName() + "/start()I");
//         writeCode("pop");
//         writeLabel("end:");
//         writeCode("invokestatic EZrt/Runtime/finilizy()V");
//         writeCode("return");
//         writeCode(".end method");
//     }

//     // Write code
//     private void writeCode(String s, int increment) {
//         this.stack_high += increment;
        
//         if (this.stack_high > this.stack_size) {
//             this.stack_size = this.stack_high;
//         }
//         this.fp.println("\t\t" + s);
//     }

//     // Write code
//     private void writeCode(String s) {
//         writeCode(s, 0);
//     }

//     private void writeCode() {
//         this.fp.println();
//     }

//     // Write label
//     private void writeLabel(String s) {
//         this.fp.println(s);
//     }
// }

package codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import parser.*;
import semanalysis.*;
import symtable.*;
import syntacticTree.*;
import EZrt.*;



public class CodeGen extends TypeCheck {
    static final String Jasextension = ".j"; 
    String SourceFile; 
    String SourcePath; 
    String SourceAbs; 
    PrintWriter fp = null; 
    String breaklabel = null; 
    int currlabel = 0; 
    boolean hasStart = false; 
    boolean store = false; 
    int stackSize; 
    int stackHigh; 

    public CodeGen() {
        super(); 
    }

    public void CodeGenRoot(ListNode x, String filename)
        throws SemanticException, GenCodeException {
        TypeCheckRoot(x); 
        System.out.println("0 Semantic error found");

        int i;
        i = filename.lastIndexOf(File.separator); 
        SourceFile = filename.substring(i + 1);

        if (i < 0) {
            SourcePath = ""; 
        } else {
            SourcePath = filename.substring(0, i);
        }

        SourceAbs = filename; 
        CodeGenClassDeclListNode(x); 
    }

    
    public void CodeGenClassDeclListNode(ListNode x) throws GenCodeException {
        if (x == null) {
            return;
        }

        CodeGenClassDeclNode((ClassDeclNode) x.node);
        CodeGenClassDeclListNode(x.next);
    }

    
    public void CodeGenClassDeclNode(ClassDeclNode x) throws GenCodeException {
        Symtable temphold = current_table; 
        boolean tempstart = hasStart; 
        EntryClass c = null;
        EntryClass nc;
        EntryClass ns = null;
        String Filename = null;
        String sname;
        FileOutputStream os;
        PrintWriter fpOld = fp; 

        if (x == null) {
            return;
        }
        
        nc = (EntryClass) current_table.classFindUp(x.name.image);
        ns = nc.parent; 
        
        if (ns == null) {
            sname = "java/lang/Object"; 
        } else {
            sname = ns.completeName();
        }

        Filename = SourceAbs + "." + nc.completeName() + Jasextension;
        // Filename =  SourceAbs + Jasextension;

        try {
            //
            os = new FileOutputStream(Filename);
        } catch (FileNotFoundException e) {
            throw new GenCodeException("Cannot create output file: " +
                Filename);
        }

        fp = new PrintWriter(os);
        System.out.println("Generating " + Filename);

        writeCode(";---------------------------------------");
        writeCode(";   Code generated by EZ compiler  :D   ");
        writeCode(";  Credits: Felipe Fava e Lucas Santos  ");
        writeCode(";---------------------------------------");
        writeCode(".source " + SourceFile); 
        writeCode(".class public " + nc.completeName()); 
        writeCode(".super " + sname); 

        current_table = nc.nested; 
        hasStart = false;

        CodeGenClassBodyNode(x.body);

        if (hasStart) { 
            createMain();
        }

        
        fp.close();
        fp = fpOld; 
        current_table = temphold; 
        hasStart = tempstart; 
    }

    
    public void CodeGenClassBodyNode(ClassBodyNode x) throws GenCodeException {
        EntryMethod l;

        if (x == null) {
            return;
        }

        CodeGenClassDeclListNode(x.clist);
        CodeGenVarDeclListNode(x.vlist);
        l = current_table.methodFindInClass("constructor", null);

        if (l.fake) {
            GeraConstructorDefault(); 
        }

        CodeGenConstructDeclListNode(x.ctlist);
        CodeGenMethodDeclListNode(x.mlist);
    }

    
    private void GeraConstructorDefault() {
        String sup;
        EntryClass cc;

        cc = (EntryClass) current_table.levelup; 

        if (cc.parent != null) { 
            sup = cc.parent.completeName();
        } else {
            sup = "java/lang/Object";
        }

        
        writeCode();
        writeCode(";Default constructor. Calls super()");
        writeCode(".method public <init>()V");
        writeCode(".limit locals 1");
        writeCode(".limit stack 1");
        writeCode("aload_0");
        writeCode("invokespecial " + sup + "/<init>()V");
        writeCode("return");
        writeCode(".end method");
    }

    
    public void CodeGenVarDeclListNode(ListNode x) {
        if (x == null) {
            return;
        }

        CodeGenVarDeclNode((VarDeclNode) x.node);
        CodeGenVarDeclListNode(x.next);
    }

    
    public void CodeGenVarDeclNode(VarDeclNode x) {
        ListNode p;
        EntryVar l;
        EntryTable c = null;

        if (x == null) {
            return;
        }

        for (p = x.vars; p != null; p = p.next) {
            VarNode q = (VarNode) p.node;

            
            l = current_table.varFind(q.position.image);

            
            writeCode(".field public " + l.name + " " + l.dscJava());
        }
    }

    
    public void CodeGenConstructDeclListNode(ListNode x) {
        if (x == null) {
            return;
        }

        CodeGenConstructDeclNode((ConstructDeclNode) x.node);

        CodeGenConstructDeclListNode(x.next);
    }

    
    public void CodeGenConstructDeclNode(ConstructDeclNode x) {
        EntryMethod t;
        EntryRec r = null;
        EntryTable e;
        EntryClass thisclass;
        EntryVar thisvar;
        ListNode p;
        VarDeclNode q;
        VarNode u;
        int n;
        String sup = "";

        if (x == null) {
            return;
        }

        p = x.body.param;
        n = 0;

        
        while (p != null) {
            q = (VarDeclNode) p.node; 
            u = (VarNode) q.vars.node; 
            n++;

            
            e = current_table.classFindUp(q.position.image);

            
            r = new EntryRec(e, u.dim, n, r);
            p = p.next;
        }

        String parlist = "";

        if (r != null) {
            r = r.inverte(); 

            
            parlist = r.dscJava();
        }

        
        t = current_table.methodFind("constructor", r);
        current_method = t; 

        
        writeCode();
        writeCode(".method public <init>(" + parlist + ")V");

        
        writeCode(".limit locals " + t.local_count_total);

        
        thisclass = (EntryClass) current_table.levelup;

        
        if (thisclass.parent != null) {
            sup = ((EntryClass) thisclass.parent).completeName();
        } else {
            sup = "java/lang/Object";
        }

        if (!current_method.has_super) 
         { 
            writeCode();
            writeCode("aload_0", 1);
            writeCode("invokespecial " + sup + "/<init>()V", -1);
        }

        
        current_table.beginScope();

        thisvar = new EntryVar("this", thisclass, 0, 0);
        current_table.add(thisvar); 
        n_locals = 1; 
        stackSize = stackHigh = 0; 
        CodeGenMethodBodyNode(x.body);
        current_table.endScope(); 
        writeCode("return");
        writeCode(".limit stack " + (t.total_stack_count = stackSize));
        writeCode(".end method"); 
    }

    
    public void CodeGenMethodDeclListNode(ListNode x) {
        if (x == null) {
            return;
        }

        CodeGenMethodDeclNode((MethodDeclNode) x.node);

        CodeGenMethodDeclListNode(x.next);
    }

    
    public void CodeGenMethodDeclNode(MethodDeclNode x) {
        EntryMethod t;
        EntryRec r = null;
        EntryTable e;
        EntryVar thisvar;
        ListNode p;
        VarDeclNode q;
        VarNode u;
        int n;

        if (x == null) {
            return;
        }

        p = x.body.param;
        n = 0;

        
        while (p != null) {
            q = (VarDeclNode) p.node; 
            u = (VarNode) q.vars.node; 
            n++;

            
            e = current_table.classFindUp(q.position.image);

            
            r = new EntryRec(e, u.dim, n, r);
            p = p.next;
        }

        String parlist = "";

        if (r != null) {
            r = r.inverte(); 

            
            parlist = r.dscJava();
        }

        
        t = current_table.methodFind(x.name.image, r);
        current_method = t; 

        
        String statc = "";
        writeCode();

        if (t.name.equals("start") && (r == null)) 
         { 
            statc = "static ";
            hasStart = true;
        }

        writeCode(".method public " + statc + t.name + "(" + parlist + ")" +
            t.dscJava());
        writeCode(".limit locals " + t.local_count_total);

        
        current_table.beginScope();

        thisvar = new EntryVar("this", current_table.levelup, 0, 0);
        current_table.add(thisvar); 

        n_locals = 1; 
        stackSize = stackHigh = 0; 
        CodeGenMethodBodyNode(x.body);
        current_table.endScope(); 

        if ((current_method.type == INT_TYPE) && (current_method.dim == 0)) {
            writeCode("bipush 0", 1);
            writeCode("ireturn", -1);
        } else {
            writeCode("aconst_null", 1);
            writeCode("areturn", -1);
        }

        writeCode(".limit stack " + (t.total_stack_count = stackSize));
        writeCode(".end method"); 
    }

    
    public void CodeGenMethodBodyNode(MethodBodyNode x) {
        if (x == null) {
            return;
        }

        CodeGenLocalVarDeclListNode(x.param); 

        CodeGenStatementNode(x.stat);
    }

    
    public void CodeGenLocalVarDeclListNode(ListNode x) {
        if (x == null) {
            return;
        }

        CodeGenLocalVarDeclNode((VarDeclNode) x.node);

        CodeGenLocalVarDeclListNode(x.next);
    }

    
    public void CodeGenLocalVarDeclNode(VarDeclNode x) {
        ListNode p;
        VarNode q;
        EntryTable c;

        if (x == null) {
            return;
        }

        
        c = current_table.classFindUp(x.position.image);

        for (p = x.vars; p != null; p = p.next) {
            q = (VarNode) p.node;

            
            current_table.add(new EntryVar(q.position.image, c, q.dim, n_locals++));
        }
    }

    
    public void CodeGenBlockNode(BlockNode x) {
        current_table.beginScope(); 
        CodeGenStatementListNode(x.stats);
        current_table.endScope(); 
    }

    
    public void CodeGenStatementListNode(ListNode x) {
        if (x == null) {
            return;
        }

        CodeGenStatementNode((StatementNode) x.node);

        CodeGenStatementListNode(x.next);
    }

    
    public void CodeGenPrintNode(PrintNode x) {
        type t;

        if (x == null) {
            return;
        }

        writeCode();
        writeCode(";begins print ");

        
        writeCode("getstatic java/lang/System/out Ljava/io/PrintStream;", 1);

        
        store = false;
        t = CodeGenExpreNode(x.expr);

        
        writeCode("invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V",
            -2);
    }

    
    public void CodeGenReadNode(ReadNode x) {
        type t = null;

        if (x == null) {
            return;
        }

        try {
            t = TypeCheckExpreNode(x.expr); 
        } catch (Exception e) {
        }

        if (t.ty == INT_TYPE) { 
            writeCode("invokestatic EZrt/Runtime/readInt()I", 1);
        } else { 
            writeCode("invokestatic EZrt/Runtime/readString()Ljava/lang/String;",
                1);
        }

        store = true; 
        CodeGenExpreNode(x.expr);
    }

    
    public void CodeGenReturnNode(ReturnNode x) {
        type t;

        if (x == null) {
            return;
        }

        writeCode();
        writeCode(";begins return ");

        
        store = false;
        t = CodeGenExpreNode(x.expr);

        
        if ((t.ty == INT_TYPE) && (t.dim == 0)) {
            writeCode("ireturn", -1);
        } else {
            writeCode("areturn", -1);
        }
    }

    
    public void CodeGenSuperNode(SuperNode x) {
        type t;
        String sup;

        if (x == null) {
            return;
        }

        
        EntryClass p = current_table.levelup.parent;

        sup = p.completeName();

        writeCode();
        writeCode(";begins super");
        writeCode("aload_0", 1);

        
        store = false;
        t = CodeGenExpreListNode(x.args);

        String arglist = (t.ty == null) ? "" : t.dscJava();
        int removeStack = (t.ty == null) ? 0 : ((EntryRec) t.ty).count;

        writeCode("invokespecial " + sup + "/<init>(" + arglist + ")V",
            -(removeStack + 1));
    }

    
    public void CodeGenAtribNode(AtribNode x) {
        type t1 = null;
        type t2;

        if (x == null) {
            return;
        }

        
        try {
            t1 = TypeCheckExpreNode(x.expr1);
        } catch (Exception e) {
        }

        
        store = false;
        t2 = CodeGenExpreNode(x.expr2);
        

        if ((t1.ty != t2.ty) && isSubClass(t1.ty, t2.ty)) { 
	        if ( t1.dim > 0 )
	        {
            	writeCode("checkcast " + t1.dscJava());
            }
            else
            {
            	EntryClass ec = (EntryClass) t1.ty;
            	writeCode("checkcast " + ec.completeName());
            }
        }

        
        store = true;
        CodeGenExpreNode(x.expr1);
    }

    
    public void CodeGenIfNode(IfNode x) {
        type t;
        String lab = newLabel();

        if (x == null) {
            return;
        }

        writeCode();
        writeCode(";begins if " + lab);

        
        store = false;
        t = CodeGenExpreNode(x.expr);

        
        writeCode("ifeq else" + lab, -1);

        
        CodeGenStatementNode(x.stat1);

        if (x.stat2 != null) { 

            
            writeCode("goto fi" + lab);

            
            putLabel("else" + lab + ":");

            
            CodeGenStatementNode(x.stat2);

            
            putLabel("fi" + lab + ":");
        } else { 
                 
            putLabel("else" + lab + ":");
        }
    }

    
    public void CodeGenForNode(ForNode x) {
        String lab = newLabel();
        String oldbreaklabel = breaklabel; 

        if (x == null) {
            return;
        }

        writeCode();
        writeCode(";begins for " + lab);

        
        CodeGenStatementNode(x.init);

        
        putLabel("forloop" + lab + ":");

        
        store = false;
        CodeGenExpreNode(x.expr);

        
        writeCode("ifeq rof" + lab, -1);
        breaklabel = "rof" + lab; 

        
        CodeGenStatementNode(x.stat);

        
        CodeGenStatementNode(x.incr);

        
        writeCode("goto forloop" + lab);

        
        putLabel(breaklabel + ":");

        breaklabel = oldbreaklabel; 
    }

    
    public void CodeGenBreakNode(BreakNode x) {
        if (x == null) {
            return;
        }

        writeCode();
        writeCode(";begins break ");

        
        writeCode("goto " + breaklabel);
    }

    
    public void CodeGenNopNode(NopNode x) {
        writeCode();
        writeCode(";begins empty statement ");
        writeCode("nop");
    }

    
    public type CodeGenNewObjectNode(NewObjectNode x) {
        type t;
        EntryMethod p;
        EntryClass c;

        if (x == null) {
            return null;
        }

        
        c = (EntryClass) current_table.classFindUp(x.name.image);

        
        writeCode("new " + ((EntryClass) c).completeName(), 1);

        
        writeCode("dup", 1);

        
        t = CodeGenExpreListNode(x.args);

        String arglist = (t.ty == null) ? "" : t.dscJava();
        int removeStack = (t.ty == null) ? 0 : ((EntryRec) t.ty).count;

        
        writeCode("invokespecial " + c.completeName() + "/<init>(" + arglist +
            ")V", -(removeStack + 1));

        
        t = new type(c, 0);

        return t;
    }

    
    public type CodeGenNewArrayNode(NewArrayNode x) {
        type t;
        EntryTable c;
        EntryRec r;

        if (x == null) {
            return null;
        }

        
        c = current_table.classFindUp(x.name.image);

        
        t = CodeGenExpreListNode(x.dims);
        r = (EntryRec) t.ty;

        if (r.count == 1) 
         {
            if (c == INT_TYPE) {
                writeCode("newarray I", 1);
            } else {
                writeCode("anewarray " + c.dscJava(), 1);
            }
        } else {
            writeCode("multianewarray " + EntryTable.strDim(r.count) +
                c.dscJava() + " " + r.count, 1);
        }

        return new type(c, r.count);
    }

    
    public type CodeGenExpreListNode(ListNode x) {
        type t;
        type t1;
        EntryRec r;
        int n;

        if (x == null) {
            return new type(null, 0);
        }

        
        t = CodeGenExpreNode((ExpreNode) x.node);

        
        t1 = CodeGenExpreListNode(x.next);

        
        n = (t1.ty == null) ? 0 : ((EntryRec) t1.ty).count;

        
        r = new EntryRec(t.ty, t.dim, n + 1, (EntryRec) t1.ty);

        
        t = new type(r, 0);

        return t;
    }

    
    public type CodeGenRelationalNode(RelationalNode x) {
        type t1;
        type t2;
        int op; 
        String lab = newLabel();

        if (x == null) {
            return null;
        }

        op = x.position.kind;

        
        t1 = CodeGenExpreNode(x.expr1);
        t2 = CodeGenExpreNode(x.expr2);

        if ((t1.dim == 0) && (t1.ty == INT_TYPE)) { 

            String s = selectBinInstruct(op); 
            writeCode(s + " relexpr" + lab, -2);
        } else { 

            if (op == ezConstants.EQ) { 
                writeCode("if_acmpeq relexpr" + lab, -2);
            } else {
                writeCode("if_acmpne relexpr" + lab, -2);
            }
        }

        writeCode("bipush 0", 1);
        writeCode("goto pxeler" + lab);
        putLabel("relexpr" + lab + ":");
        writeCode("bipush 1"); 
                             

        putLabel("pxeler" + lab + ":");

        return new type(INT_TYPE, 0);
    }

    
    public type CodeGenAddNode(AddNode x) {
        type t1;
        type t2;
        int op; 
        int i;
        int j;

        if (x == null) {
            return null;
        }

        
        op = x.position.kind;
        t1 = CodeGenExpreNode(x.expr1);
        t2 = CodeGenExpreNode(x.expr2);

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
            String s = selectBinInstruct(op); 
            writeCode(s, -1);

            return new type(INT_TYPE, 0);
        }

    
        if (j == 2) {
            
            writeCode("invokevirtual java/lang/String/concat" +
                "(Ljava/lang/String;)Ljava/lang/String;", -1);

            return new type(STRING_TYPE, 0);
        }

        
        if (t1.ty == INT_TYPE) { 
            writeCode("swap"); 
            writeCode("invokestatic java/lang/Integer/toString(I)" +
                "Ljava/lang/String;");
            writeCode("swap"); 
        } else { 
            writeCode("invokestatic java/lang/Integer/toString(I)" +
                "Ljava/lang/String;");
        }

        
        writeCode("invokevirtual java/lang/String/concat" +
            "(Ljava/lang/String;)Ljava/lang/String;", -1);

        return new type(STRING_TYPE, 0);
    }

    
    public type CodeGenMultNode(MultNode x) {
        type t1;
        type t2;
        int op; 

        if (x == null) {
            return null;
        }

        op = x.position.kind;
        t1 = CodeGenExpreNode(x.expr1);
        t2 = CodeGenExpreNode(x.expr2);

        String s = selectBinInstruct(op); 
        writeCode(s, -1);

        return new type(INT_TYPE, 0);
    }

    
    public type CodeGenUnaryNode(UnaryNode x) {
        type t;
        int op; 

        if (x == null) {
            return null;
        }

        op = x.position.kind;
        t = CodeGenExpreNode(x.expr);

        if (op == ezConstants.MINUS) {
            writeCode("ineg");
        }

        return new type(INT_TYPE, 0);
    }

    
    public type CodeGenIntConstNode(IntConstNode x) {
        if (x == null) {
            return null;
        }

        
        writeCode("ldc " + x.position.image, 1);

        return new type(INT_TYPE, 0);
    }

    
    public type CodeGenStringConstNode(StringConstNode x) {
        if (x == null) {
            return null;
        }

        
        writeCode("ldc " + x.position.image, 1);

        return new type(STRING_TYPE, 0);
    }

    
    public type CodeGenNullConstNode(NullConstNode x) {
        if (x == null) {
            return null;
        }

        
        writeCode("aconst_null", 1);

        return new type(NULL_TYPE, 0);
    }

    
    public type CodeGenVarNode(VarNode x) {
        EntryVar p;
        String s = store ? "store" : "load";
        String s2 = store ? "putfield" : "getfield";

        if (x == null) {
            return null;
        }

        
        p = current_table.varFind(x.position.image);

        if (p.local_count >= 0) { 

            if ((p.type == INT_TYPE) && (p.dim == 0)) { 
                writeCode("i" + s + " " + p.local_count, store ? (-1) : 1);
            } else {
                writeCode("a" + s + " " + p.local_count, store ? (-1) : 1);
            }
        } else { 
                 

            EntryClass v = p.my_table.levelup;

            
            writeCode("aload_0", 1);

            
            if (store) {
                writeCode("swap");
            }

            writeCode(s2 + " " + v.completeName() + "/" + p.name + " " +
                p.dscJava(), store ? (-2) : 0);
        }

        return new type(p.type, p.dim);
    }

    
    public type CodeGenCallNode(CallNode x) {
        EntryClass c;
        EntryMethod m;
        type t1;
        type t2;

        if (x == null) {
            return null;
        }

        
        t1 = CodeGenExpreNode(x.expr);

        
        t2 = CodeGenExpreListNode(x.args);

        
        c = (EntryClass) t1.ty;
        m = c.nested.methodFind(x.meth.image, (EntryRec) t2.ty);

        String arglist = (t2.ty == null) ? "" : t2.dscJava();
        int removeStack = (t2.ty == null) ? 0 : ((EntryRec) t2.ty).count;

        
        writeCode("invokevirtual " + c.completeName() + "/" + m.name + "(" +
            arglist + ")" + m.dscJava(), -removeStack);

        return new type(m.type, m.dim);
    }

    
    public type CodeGenIndexNode(IndexNode x) {
        EntryClass c;
        type t1;
        type t2;
        boolean b = store;

        if (x == null) {
            return null;
        }

        
        store = false;
        t1 = CodeGenExpreNode(x.expr1);

        
        if (b) {
            writeCode("swap");
        }

        
        t2 = CodeGenExpreNode(x.expr2);

        if (b) 
         {
            writeCode("swap"); 

            if ((t1.ty == INT_TYPE) && (t1.dim == 1)) { 
                writeCode("iastore", -3);
            } else {
                writeCode("aastore", -3);
            }
        } else {
            if ((t1.ty == INT_TYPE) && (t1.dim == 1)) { 
                writeCode("iaload", -1);
            } else {
                writeCode("aaload", -1);
            }
        }

        store = b;

        return new type(t1.ty, t1.dim - 1);
    }

    
    public type CodeGenDotNode(DotNode x) {
        EntryClass c;
        EntryVar v;
        type t;
        boolean b = store;

        if (x == null) {
            return null;
        }

        
        store = false;
        t = CodeGenExpreNode(x.expr);

        
        c = (EntryClass) t.ty;
        v = c.nested.varFind(x.field.image);

        if (b) {
            writeCode("swap"); 
            writeCode("putfield " + c.completeName() + "/" + v.name + " " +
                v.dscJava(), -2);
        } else {
            writeCode("getfield " + c.completeName() + "/" + v.name + " " +
                v.dscJava());
        }

        store = b;

        return new type(v.type, v.dim);
    }

    
    public type CodeGenExpreNode(ExpreNode x) {
        if (x instanceof NewObjectNode) {
            return CodeGenNewObjectNode((NewObjectNode) x);
        } else if (x instanceof NewArrayNode) {
            return CodeGenNewArrayNode((NewArrayNode) x);
        } else if (x instanceof RelationalNode) {
            return CodeGenRelationalNode((RelationalNode) x);
        } else if (x instanceof AddNode) {
            return CodeGenAddNode((AddNode) x);
        } else if (x instanceof MultNode) {
            return CodeGenMultNode((MultNode) x);
        } else if (x instanceof UnaryNode) {
            return CodeGenUnaryNode((UnaryNode) x);
        } else if (x instanceof CallNode) {
            return CodeGenCallNode((CallNode) x);
        } else if (x instanceof IntConstNode) {
            return CodeGenIntConstNode((IntConstNode) x);
        } else if (x instanceof StringConstNode) {
            return CodeGenStringConstNode((StringConstNode) x);
        } else if (x instanceof NullConstNode) {
            return CodeGenNullConstNode((NullConstNode) x);
        } else if (x instanceof IndexNode) {
            return CodeGenIndexNode((IndexNode) x);
        } else if (x instanceof DotNode) {
            return CodeGenDotNode((DotNode) x);
        } else if (x instanceof VarNode) {
            return CodeGenVarNode((VarNode) x);
        } else {
            return null;
        }
    }

    
    public void CodeGenStatementNode(StatementNode x) {
        if (x instanceof BlockNode) {
            CodeGenBlockNode((BlockNode) x);
        } else if (x instanceof VarDeclNode) {
            CodeGenLocalVarDeclNode((VarDeclNode) x);
        } else if (x instanceof AtribNode) {
            CodeGenAtribNode((AtribNode) x);
        } else if (x instanceof IfNode) {
            CodeGenIfNode((IfNode) x);
        } else if (x instanceof ForNode) {
            CodeGenForNode((ForNode) x);
        } else if (x instanceof PrintNode) {
            CodeGenPrintNode((PrintNode) x);
        } else if (x instanceof NopNode) {
            CodeGenNopNode((NopNode) x);
        } else if (x instanceof ReadNode) {
            CodeGenReadNode((ReadNode) x);
        } else if (x instanceof ReturnNode) {
            CodeGenReturnNode((ReturnNode) x);
        } else if (x instanceof SuperNode) {
            CodeGenSuperNode((SuperNode) x);
        } else if (x instanceof BreakNode) {
            CodeGenBreakNode((BreakNode) x);
        }
    }

    private String selectBinInstruct(int op) {
        switch (op) {
        case ezConstants.PLUS:
            return "iadd";

        case ezConstants.MINUS:
            return "isub";

        case ezConstants.STAR:
            return "imul";

        case ezConstants.SLASH:
            return "idiv";

        case ezConstants.REM:
            return "irem";

        case ezConstants.EQ:
            return "if_icmpeq";

        case ezConstants.NEQ:
            return "if_icmpne";

        case ezConstants.LT:
            return "if_icmplt";

        case ezConstants.GT:
            return "if_icmpgt";

        case ezConstants.LE:
            return "if_icmple";

        case ezConstants.GE:
            return "if_icmpge";
        }

        return null;
    }

    private String newLabel() {
        return Integer.toString(currlabel++);
    }

    private void createMain() {
        EntryClass v = (EntryClass) current_table.levelup;

        writeCode();
        writeCode(";Entry point for the JVM");
        writeCode(".method static public main([Ljava/lang/String;)V");
        writeCode(".limit  locals 1");
        writeCode(".limit  stack 1");
        writeCode("invokestatic EZrt/Runtime/initialize()I");
        writeCode("ifne         end");
        writeCode("invokestatic " + v.completeName() + "/start()I");
        writeCode("pop");
        putLabel("end:");
        writeCode("invokestatic EZrt/Runtime/finilizy()V");
        writeCode("return");
        writeCode(".end method");
    }

    private void writeCode(String s, int inc) {
        stackHigh += inc;

        if (stackHigh > stackSize) {
            stackSize = stackHigh;
        }

        fp.println("\t\t" + s);
    }

    private void writeCode(String s) {
        writeCode(s, 0);
    }

    private void writeCode() {
        fp.println();
    }

    private void putLabel(String s) {
        fp.println(s);
    }
}
