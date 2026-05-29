package br.com.devlucasnascimento.model;

import java.util.Collection;
import java.util.List;

import static br.com.devlucasnascimento.model.GameStatusEnum.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    // BUG 6 CORRIGIDO: lógica reordenada para evitar falso NON_STARTED
    // quando todos os espaços são fixos e o board está completo.
    public GameStatusEnum getStatus() {
        var allSpaces = spaces.stream().flatMap(Collection::stream).toList();

        // Se algum espaço ainda não tem valor → INCOMPLETE ou NON_STARTED
        boolean hasAnyNull = allSpaces.stream().anyMatch(s -> isNull(s.getActual()));
        boolean anyNonFixedFilled = allSpaces.stream().anyMatch(s -> !s.isFixed() && nonNull(s.getActual()));

        if (!hasAnyNull) {
            return COMPLETE;
        }
        if (!anyNonFixedFilled) {
            return NON_STARTED;
        }
        return INCOMPLETE;
    }

    public boolean hasErrors() {
        if (getStatus() == NON_STARTED) {
            return false;
        }
        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final int value) {
        var space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }
        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row) {
        var space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }
        space.clearSpace();
        return true;
    }

    // BUG 5 CORRIGIDO: reset() agora filtra explicitamente apenas os não-fixos,
    // tornando a intenção clara (não depende de guarda implícita em Space).
    public void reset() {
        spaces.forEach(col -> col.stream()
                .filter(space -> !space.isFixed())
                .forEach(Space::clearSpace));
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus().equals(COMPLETE);
    }
}
