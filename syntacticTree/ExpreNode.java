package syntacticTree;

import parser.*;


abstract public class ExpreNode extends GeneralNode {
    public ExpreNode(Token t) {
        super(t);
    }
}


/*
void TypeCheck(IfNode x)
{
    t = TypeCheck(x.expr);
    se t é "int"
        OK:
    senão
        erro: expressão deve ser inteira;
}

"tipo" TypeCheck(ExpreNode x){
    descobre a classe "real" de x e chama o método adequado;
    retorna o valor retornado na chamada;
}

"tipo" TypeCheck(RelationalNode x){
    t1 = TypeCheck(x.expr1);
    t2 = TypeCheck(x.expr2);
    se t1 e t2 são "int"
        retorna como resulltado "int";
    se x.position == "==" ou x.position == "!="
        se t1 é null e t2 é "string" ou vice-versa
            retorna como resultado "int"
        se t1 é null e t2 é objeto ou vice-versa
            retorna como resultado "int"
        se t1 e t2 são null
            retorna como resultado "int"
        se t1 e t2 são objetos de tipos compatíveis
            retorna como resultado "int"
    senão
        erro: operandos inválidos em operação relacional
}

"tipo" TypeCheck(VarNode x){
    procura x.position na tabela simbolos;
    se achou 
        retorna tipo da variável na tabela;
     senão
        erro: variável não definida;
}*/
