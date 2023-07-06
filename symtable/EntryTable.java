package symtable;

//classe geral para as possíveis entradas na tabela de símbolo
abstract public class EntryTable{
    public String name;     //nome do símbolo (var,método ou classe)
    public EntryTable next; // apontador para próximo dentro da tabela
    public int scope;           // número do aninhamento corrente
    public Symtable mytable;    //aponta para a tabela da qual faz parte

}
