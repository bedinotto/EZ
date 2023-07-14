package symtable;

public class Symtable {
    // pointer to the top of the symbol table
    public EntryTable top;
    // pointer to the current scope
    public int scptr;
    // pointer to the levelup entry
    public EntryClass levelup;

    // create a new symbol table
    public Symtable() {
        this.top = null;
        this.scptr = 0;
        this.levelup = null;
    }

    // create a new symbol table with a parent symbol table
    public Symtable(EntryClass sup) {
        this.top = null;
        this.scptr = 0;
        this.levelup = sup;
    }

    // add a new entry to the symbol table
    public void add(EntryTable entry) {
        entry.next = this.top;
        this.top = entry;
        entry.scope = this.scptr;
        entry.my_table = this;
    }

    // begin new scope
    public void beginScope() {
        this.scptr++;
    }

    // end current scope
    public void endScope() {
        while (this.top != null && this.top.scope == this.scptr) {
            this.top = this.top.next; // remove all entries in the current scope
        }
        this.scptr--; // finish the current scope
    }

    // find a symbol in the symbol table and in the parent symbol tables
    // search for an entry of a type EntryClass or EntrySimple
    public EntryTable classFindUp(String name) {
        EntryTable p = this.top;

        // for each entry in the current symbol table
        while (p != null) {
            // verify if the entry is a class declaration or a simple variable declaration and if the name matches
            if ((p instanceof EntryClass || p instanceof EntrySimple) && p.name.equals(name)) {
                return p;
            }
            p = p.next; // go to the next entry
        }
        if (this.levelup == null) { // if did not find the symbol in the current symbol table and there is no parent symbol table
            return null;
        }
        // go to the parent symbol table
        return this.levelup.my_table.classFindUp(name);
    }

    // search the n-th entry of a type EntryVar
    public EntryVar varFind(String name, int scope) {
        EntryTable p = this.top;
        EntryClass q;
        while (p != null) {
            if (p instanceof EntryVar && p.name.equals(name)) {
                if (--scope == 0) {
                    return (EntryVar) p;
                }
            }
            p = p.next;
        }
        q = this.levelup;
        if (q.parent == null) {
            return null;
        }
        return q.parent.nested.varFind(name, scope);
    }

    // find a symbol in the symbol table and in the parent symbol tables
    // search for an entry of a type EntryVar
    public EntryVar varFind(String name){
        return varFind(name, 1);
    }

    // find a symbol in the symbol table and in the parent symbol tables
    // search for an entry of a type EntryMethod
    public EntryMethod methodFind(String name, EntryRec argu){
        EntryTable p = this.top;
        EntryClass q;

        while (p != null) {
            if (p instanceof EntryMethod && p.name.equals(name)) {
                EntryMethod m = (EntryMethod) p;
                
                if (m.params == null){
                    if (argu == null){
                        return m;
                    }
                } else {
                    if (m.params.equals(argu)){
                        return m;
                    }
                }
            }
            p = p.next;
        }

        q = this.levelup;

        if (q.parent == null) {
            return null;
        }

        return q.parent.nested.methodFind(name, argu);
    }

    // search for "name" symbol with a list of arguments "argu" in the current symbol table, not in the parent symbol tables
    // search for an entry of a type EntryMethod
    public EntryMethod methodFindInClass(String name, EntryRec argu){
        EntryTable p = this.top;

        while (p != null){
            if (p instanceof EntryMethod && p.name.equals(name)){
                EntryMethod m = (EntryMethod) p;
                if (m.params == null){
                    if (argu == null){
                        return m;
                    }
                } else {
                    if (m.params.equals(argu)){
                        return m;
                    }
                }
            }
            p = p.next;
        }
        return null; // not found
    }

}
