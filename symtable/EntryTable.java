package symtable;

// abstract class to represent the entries in the symbol table
abstract public class EntryTable {
    public String name;
    public EntryTable next;
    public int scope;
    public Symtable my_table;

    abstract public String dscJava();

    static public String strDim(int n){
        String s = "";
        for(int i=0; i<n; i++) s += "[";
        return s;
    }
}