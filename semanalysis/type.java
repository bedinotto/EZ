package semanalysis;

import symtable.*;


public class type {
    public EntryTable ty; // entry for the type table
    public int dim; // dimension

    public type(EntryTable t, int d) {
        ty = t;
        dim = d;
    }

    public String dscJava() {
        return EntryTable.strDim(dim) + ty.dscJava();
    }
}
