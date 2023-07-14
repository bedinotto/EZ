package symtable;

// used to declare the basic types
public class EntrySimple extends EntryTable{
    public EntrySimple(String name) {
        this.name = name;
    }

    public String dscJava(){ // return the descriptor of the type
        if (this.name.equals("int")) {
            return "I";
        } else {
            return "Ljava/lang/String;"; // string is a reference type in Java
        }
    }
}
