package symtable;

// class that represents a variable in the symbol table
public class EntryVar extends EntryTable{
    public EntryTable type; // type of the variable
    public int dim; // 0 for scalar, 1 for 1D array, 2 for 2D array, etc.
    public int local_count; // local variable count for functions

    // create a new class variable entry
    public EntryVar(String name, EntryTable type, int dim) {
        this.name = name; // name of the variable
        this.type = type; // type of the variable
        this.dim = dim; // dimension of the variable
        this.local_count = -1; // always -1 for non-local variables
    }

    // create a new local variable entry
    public EntryVar(String name, EntryTable type, int dim, int localCount) {
        this.name = name;
        this.type = type;
        this.dim = dim;
        this.local_count = localCount; // local variable count for functions
    }

    public String dscJava() {
        String s = strDim(this.dim);
        s += this.type.dscJava();
        return s;
    }
}
