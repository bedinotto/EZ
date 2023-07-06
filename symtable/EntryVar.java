package symtable;

// classe que abriga uma declaração de variável na tabela de simbolos
public class EntryVar extends EntryTable{
    public EntryTable type;     // apontador para o tipo da variável
    public int dim;             // número de dimensões da variável
    public int localcount;      // numeração de sequencial para as vars. locais

    //cria uma entrada para var. de classe
    public EntryVar(String n, EntryTable p, int d){
        name = n;       //nome da variável
        type = p;       //apontador para a classe
        dim = d;        //número de dimensões
        localcount = -1;
    }
    
    //cria uma entrada para var.local
    public EntryVar(String n, EntryTable p, int d, int k){
        name = n;       //nome da variável
        type = p;       //apontador para a classe
        dim = d;        //número de dimensões
        localcount = k; //inclui também o número sequencial
    }
}
        
