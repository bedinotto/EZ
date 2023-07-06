package symtable;

public class Symtable{
//apontador para o topo da tabela (mais recente)
public EntryTable top;

//número que controla o escopo (aninhamento) corrente
public int scptr;

//apontador para a entrada EntruClass de nível sup.
public EntryClass levelup;

public Symtable(){//cria uma tabela vazia
    top = null;
    scptr = 0;
    levelup = null;
}

//cria uma tabela vazia apontando para nível sup.
public Symtable(EntryClass up){
    top = null;
    scptr = 0;
    levelup = null;
}

public void add(EntryTable x){ //adiciona uma entrada à tabela
    x.next = top;       // inclui nova entrada no topo
    top = x;
    x.scope = scptr;    // atribui para a entrada o número do escopo;
    x.mytable = this;   // faz a entrada apontar para a própria tabela
}

public void beginScope(){
    scptr++;        // inicia novo aninhamento de variaáveis
}

public void endScope(){
    while(top != null && top.scope == scptr)
        top =  top.next; //retira todas as vars do aninhamento corrente
        scptr--;        // finaliza aninhamento corrente
}


public EntryTable classFindUp(String x) {
    EntryTable p = top;

    // Para cada elemento da tabela corrente
    while (p != null) {
        // Verifica se é uma entrada de classe ou tipo simples e então compara o nome
        if (((p instanceof EntryClass) || (p instanceof EntrySimple)) && p.name.equals(x))
            return p;
        p = p.next; // Próxima entrada
    }

    // Se não achou e é o nível mais externo, retorna null
    if (levelup == null)
        return null;

    // Procura no nível mais externo
    return levelup.mytable.classFindUp(x);
}

public EntryMethod methodFindInclass(String x, EntryRec r) {
        EntryTable p = top;
        EntryClass q;
        
        //para cada entrada da tabela
        while(p != null) {
            //verifica se tipo é EntryMethod e compara o nome
            if(p instanceof EntryMethod && p.name.equals(x)) {
                EntryMethod t = (EntryMethod) p;
                //compara os parâmetros
                if(t.param == null) {
                    if(r == null) return t;
                }
                else{
                    if(t.param.equals(r)) return t;
                }
            }
            p = p.next; // proxima entrada
        }
        return null; //não achou
    }
}
