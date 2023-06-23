# EZ

This is a project of compiler for a simple language called EZ. The compiler is written in Java, using JavaCC as the parser generator.

This project is part of the course "Compilers" at the Universidade Federal do Pampa, Brazil.

This project follows the book "Como Construir um Compilador Utilizando Ferramentas Java" by Marcio Delamaro.

## How to run

### Prerequisites

- JavaCC

### Compile

```$ cd parser```

```/parser$ javacc ez.jj```

```$ cd ..```

```$ javac parser/ez.java```

### Run

```$ java parser.ez <input_file>```

### Run with debug

```$ java parser.ez -debug_AS <input_file>```
