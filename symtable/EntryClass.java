package symtable;

// stores a class declaration in the symbol table
public class EntryClass extends EntryTable {
    public Symtable nested; // symtable for nested declarations
    public EntryClass parent; // pointer to parent class
    
    public EntryClass(String name, Symtable table) {
        this.name = name;
        this.nested = new Symtable(this);
        this.parent = null;
    }
    public String completeName(){
        String p;
        Symtable t;
        EntryClass up;

        t = this.my_table;
        up = (EntryClass) t.levelup;
        if (up == null) {
            p = ""; // no parent
        } else {
            p = up.completeName() + "$"; // parent name
        }
        return p + this.name;
    }

    public String dscJava(){
        return "L" + this.completeName() + ";";
    }
}