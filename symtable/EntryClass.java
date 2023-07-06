package symtable;

//classe corresponde a uma declaração de classe na tab. de símbolos
public class EntryClass extends EntryTable{
    public Symtable nested;     // tabela p/ declaração de elementos aninhados
    public EntryClass parent;   // entrada correspondente à superclasse
    
    
    public EntryClass(String n, Symtable t){
        name = n;       //nome da class declarada
        nested = new Symtable(this); //tabela onde inserir variáveis,
                                     //métodos ou classes
        parent = null; // sua superclasse
    }
}
