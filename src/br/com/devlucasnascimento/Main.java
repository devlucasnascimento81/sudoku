package br.com.devlucasnascimento;

import br.com.devlucasnascimento.model.Board;
import br.com.devlucasnascimento.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static br.com.devlucasnascimento.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        // BUG 1 CORRIGIDO: adicionado tratamento de erro no parsing dos args,
        // evitando ArrayIndexOutOfBoundsException com mensagem clara ao usuário.
        if (args == null || args.length == 0) {
            System.out.println("Erro: nenhuma posição inicial informada.");
            System.out.println("Use o formato: col,row;expected,fixed (ex: 0,0;5,true)");
            System.exit(1);
        }

        Map<String, String> positions;
        try {
            positions = Stream.of(args)
                    .collect(toMap(
                            k -> k.split(";")[0],
                            v -> v.split(";")[1]
                    ));
        } catch (Exception e) {
            System.out.println("Erro ao processar os argumentos. Use o formato: col,row;expected,fixed");
            System.exit(1);
            return;
        }

        var option = -1;
        while (true) {
            System.out.println("\nSelecione uma das opções:");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }
        }
    }

    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = positions.get("%s,%s".formatted(i, j));

                if (positionConfig == null) {
                    System.out.printf("Erro: posição [%d,%d] não encontrada nos argumentos.%n", i, j);
                    System.out.println("Certifique-se de que todos os 81 espaços foram informados.");
                    board = null;
                    return;
                }

                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar!");
    }

    private static void inputNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado.");
            return;
        }

        System.out.println("Informe a coluna em que o número será inserido (0-8):");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha em que o número será inserido (0-8):");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição [%s,%s] (1-9):%n", col, row);
        var value = runUntilGetValidNumber(1, 9);

        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s,%s] tem um valor fixo e não pode ser alterada.%n", col, row);
        } else {
            System.out.printf("Número %d inserido na posição [%d,%d] com sucesso.%n", value, col, row);
        }
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado.");
            return;
        }

        System.out.println("Informe a coluna do número que deseja remover (0-8):");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha do número que deseja remover (0-8):");
        var row = runUntilGetValidNumber(0, 8);

        if (!board.clearValue(col, row)) {
            System.out.printf("O número na posição [%s,%s] é fixo e não pode ser removido.%n", col, row);
        } else {
            System.out.printf("Número removido da posição [%d,%d] com sucesso.%n", col, row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado.");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col : board.getSpaces()) {
                args[argPos++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("Veja o estado atual do seu jogo:");
        System.out.printf((BOARD_TEMPLATE) + "%n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado.");
            return;
        }

        System.out.printf("O jogo atualmente se encontra no status: %s%n", board.getStatus().getLabel());
        if (board.hasErrors()) {
            System.out.println("O jogo contém erros.");
        } else {
            System.out.println("O jogo não contém erros.");
        }
    }

    private static void clearGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado.");
            return;
        }

        System.out.println("Tem certeza que deseja limpar seu jogo? (sim/não)");
        var confirmacao = scanner.next();

        while (!confirmacao.equalsIgnoreCase("sim") && !confirmacao.equalsIgnoreCase("não")) {
            System.out.println("Informe 'sim' ou 'não':");
            confirmacao = scanner.next();
        }

        if (confirmacao.equalsIgnoreCase("sim")) {
            board.reset();
            System.out.println("Jogo limpo! Os números fixos foram mantidos.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado.");
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("Parabéns, você concluiu o jogo!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros, verifique o board e corrija-os.");
        } else {
            System.out.println("Você ainda precisa preencher alguns espaços.");
        }
    }

    private static int runUntilGetValidNumber(final int min, final int max) {
        var current = scanner.nextInt();
        while (current < min || current > max) {
            System.out.printf("Informe um número entre %s e %s:%n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }
}
