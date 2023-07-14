package symtable;

// EntryClass list to represent the types of a parameter list
public class EntryRec extends EntryTable{
    public EntryTable type; // type of the variable
    public int dim; // 0 for scalar, 1 for 1D array, 2 for 2D array, etc.
    public EntryRec next; // pointer to the next parameter
    public int count; // number of parameters from the current parameter to the end of the list

    public EntryRec(EntryTable type, int dim, int count) {
        this.type = type;
        this.dim = dim;
        this.next = null;
        this.count = count;
    }

    // create element and add to the end of the list
    public EntryRec(EntryTable type, int dim, int count, EntryRec next) {
        this.type = type;
        this.dim = dim;
        this.next = next;
        this.count = count;
    }
    
    // invert the order of the parameter list
    public EntryRec inverte(EntryRec ant) {
        EntryRec r = this;
        if (this.next != null) {
            r = this.next.inverte(this);
        }
        this.count = ant.count + 1;
        this.next = ant;
        return r;
    }

    public EntryRec inverte() {
        EntryRec r = this;

        this.count = 1;

        if (this.next != null) {
            r = this.next.inverte(this);
        }
        this.next = null;
        return r;
    }

    // return the representation of the parameter list as a string
    public String toStr(){
        String s;
        s = this.type.name;
        for(int i = 0; i < this.dim; i++){
            s += "[]";
        }
        if (this.next != null) {
            s += ", " + this.next.toStr();
        }
        return s;
    }

    // return the description of the parameter list in Java
    public String dscJava(){
        String s = strDim(dim);
        s += type.dscJava();

        if (this.next != null) {
            s += this.next.dscJava();
        }
        return s;
    }

    // verify if two parameter lists are equal
    public boolean equals(EntryRec x){
        EntryRec p;
        EntryRec q;

        if ((x == null) || (this.count != x.count)) {
            return false;
        }

        p = this;
        q = x;

        do {
            if ((p.type != q.type) || (p.dim != q.dim)) {
                return false;
            }
            p = p.next;
            q = q.next;
        } while ((p != null) && (q != null));
        
        return p == q;

    }
}
