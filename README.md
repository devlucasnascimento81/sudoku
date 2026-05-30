# 🧩 Sudoku Java

Um jogo de Sudoku desenvolvido em Java com interface gráfica construída com **Java Swing**, rodando via terminal interativo com visualização do tabuleiro em tempo real.

---

## 🛠️ Tecnologias

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Java%20Swing-GUI-4A90D9?style=for-the-badge&logo=java&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)

---

## 📋 Funcionalidades

- **Iniciar novo jogo** — carrega o tabuleiro com os números fixos definidos via argumentos de inicialização
- **Inserir número** — permite ao jogador preencher uma posição informando coluna, linha e valor
- **Remover número** — apaga um número inserido pelo jogador (números fixos são protegidos)
- **Visualizar tabuleiro** — exibe o estado atual do jogo formatado no console
- **Verificar status** — mostra se o jogo está *não iniciado*, *incompleto* ou *completo*, e se há erros
- **Limpar jogo** — remove todos os números do jogador, mantendo os fixos
- **Finalizar jogo** — encerra o jogo quando todos os espaços estiverem preenchidos corretamente

---

## 🚀 Como rodar

### Pré-requisitos

- Java 21 ou superior instalado
- IntelliJ IDEA (ou outra IDE Java de sua preferência)

### Configurando os argumentos de inicialização

O jogo recebe as posições iniciais do tabuleiro via **Program Arguments**, no formato:

```
col,row;expected,fixed
```

- `col` e `row` — índices da posição (0 a 8)
- `expected` — valor correto daquela célula (1 a 9); use `0` para células vazias
- `fixed` — `true` se for um número fixo do puzzle, `false` se for célula vazia

**Exemplo de argumento para uma célula:**
```
0,0;5,true
```

### Configurando no IntelliJ IDEA

1. Vá em **Run → Edit Configurations...**
2. Clique em **+** e selecione **Application**
3. Defina:
   - **Name:** `Sudoku`
   - **Main class:** `br.com.devlucasnascimento.Main`
   - **Program arguments:** cole os 81 argumentos do tabuleiro (veja exemplo abaixo)
4. Clique em **OK** e rode com ▶

### Exemplo completo de argumentos (puzzle clássico)

```
0,0;5,true 0,1;3,true 0,2;0,false 0,3;0,false 0,4;7,true 0,5;0,false 0,6;0,false 0,7;0,false 0,8;0,false 1,0;6,true 1,1;0,false 1,2;0,false 1,3;1,true 1,4;9,true 1,5;5,true 1,6;0,false 1,7;0,false 1,8;0,false 2,0;0,false 2,1;9,true 2,2;8,true 2,3;0,false 2,4;0,false 2,5;0,false 2,6;0,false 2,7;6,true 2,8;0,false 3,0;8,true 3,1;0,false 3,2;0,false 3,3;0,false 3,4;6,true 3,5;0,false 3,6;0,false 3,7;0,false 3,8;3,true 4,0;4,true 4,1;0,false 4,2;0,false 4,3;8,true 4,4;0,false 4,5;3,true 4,6;0,false 4,7;0,false 4,8;1,true 5,0;7,true 5,1;0,false 5,2;0,false 5,3;0,false 5,4;2,true 5,5;0,false 5,6;0,false 5,7;0,false 5,8;6,true 6,0;0,false 6,1;6,true 6,2;0,false 6,3;0,false 6,4;0,false 6,5;0,false 6,6;2,true 6,7;8,true 6,8;0,false 7,0;0,false 7,1;0,false 7,2;0,false 7,3;4,true 7,4;1,true 7,5;9,true 7,6;0,false 7,7;0,false 7,8;5,true 8,0;0,false 8,1;0,false 8,2;0,false 8,3;0,false 8,4;8,true 8,5;0,false 8,6;0,false 8,7;7,true 8,8;9,true
```

---

## 📁 Estrutura do projeto

```
src/
└── br/com/devlucasnascimento/
    ├── Main.java                  # Ponto de entrada e menu interativo
    ├── model/
    │   ├── Board.java             # Lógica do tabuleiro
    │   ├── Space.java             # Representa cada célula do tabuleiro
    │   └── GameStatusEnum.java    # Status do jogo (não iniciado, incompleto, completo)
    └── util/
        └── BoardTemplate.java     # Template ASCII do tabuleiro
```

---

## 👤 Autor

Desenvolvido por **devlucasnascimento81**
