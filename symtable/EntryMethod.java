package symtable;


// declaration of a method inside the symbol table
public class EntryMethod extends EntryTable{
    public EntryTable type; // type of the variable
    public EntryRec params; // parameters of the function
    public int dim; // 0 for scalar, 1 for 1D array, 2 for 2D array, etc.
    public int local_count_total; // local variable count for functions
    public int total_stack_count; // size of the stack frame
    public boolean fake; // true if this is a fake method
    public boolean has_super; // true if this method has a super call

    public EntryMethod(String name, EntryTable type, EntryRec params, int dim) {
        this.name = name;
        this.type = type;
        this.params = params;
        this.dim = dim;
        this.local_count_total = 0;
        this.total_stack_count = 0;
        this.fake = false;
        this.has_super = false;
    }

    public EntryMethod(String name, EntryTable type, boolean fake) {
        this.name = name;
        this.type = type;
        this.params = null;
        this.dim = 0;
        this.local_count_total = 0;
        this.total_stack_count = 0;
        this.fake = fake;
        this.has_super = false;
    }

    public String dscJava(){
        String s = strDim(dim);
        s += type.dscJava();
        return s;
    }
}
