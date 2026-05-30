package br.com.devlucasnascimento.ui.custom.screen;

import br.com.devlucasnascimento.model.Space;
import br.com.devlucasnascimento.service.BoardService;
import br.com.devlucasnascimento.service.NotifierService;
import br.com.devlucasnascimento.ui.custom.button.CheckGameStatusButton;
import br.com.devlucasnascimento.ui.custom.button.FinishGameButton;
import br.com.devlucasnascimento.ui.custom.button.ResetButton;
import br.com.devlucasnascimento.ui.custom.frame.MainFrame;
import br.com.devlucasnascimento.ui.custom.input.NumberText;
import br.com.devlucasnascimento.ui.custom.panel.MainPanel;
import br.com.devlucasnascimento.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.devlucasnascimento.service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.*;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;


    public MainScreen(final Map<String, String> gameConfig) {

        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r = 0; r < 9; r+=3){
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3){
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardService.getSpaces(), c, endCol, r, endRow);
                mainPanel.add(generateSection(spaces));
            }
        }
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpacesFromSector(final List<List<Space>> spaces,
                                            final int initCol, final int endCol,
                                            final int initRow, final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++){
            for (int c = initCol; c <= endCol; c++){
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscriber(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }

    private void addFinishGameButton(JPanel mainPanel) {
         finishGameButton = new FinishGameButton(e -> {
            if (boardService.gamesIsFinished()){
                JOptionPane.showMessageDialog(null,"Parabéns você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null,"Seu jogo tem alguma Inconsistência");

            }
        });
         mainPanel.add(finishGameButton);

    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
         checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErros = boardService.hasErros();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErros ? " e contèm erros" : " e não contém erros";
            showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
         resetButton = new ResetButton(e -> {
            var dialogueResult = showConfirmDialog(
                    null,
                    "Deseja reiniciar o jogo?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if (dialogueResult == 0) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);
    }


}
