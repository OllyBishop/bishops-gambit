package io.github.ollybishop.bishopsgambit.model.piece;

import java.util.List;
import java.util.function.Supplier;

import io.github.ollybishop.bishopsgambit.game.Game;
import io.github.ollybishop.bishopsgambit.model.Square;
import io.github.ollybishop.bishopsgambit.testutil.SquareHelper;

public class PieceHelper
{
    private final Supplier<Game> gameSupplier;

    private final Piece piece;

    public PieceHelper( Supplier<Game> gameSupplier, Piece piece )
    {
        this.gameSupplier = gameSupplier;

        this.piece = piece;
    }

    List<Square> getControlledSquares()
    {
        return piece.getControlledSquares( gameSupplier.get().getActiveBoard() );
    }

    List<Square> getPseudoLegalMoves()
    {
        return piece.getPseudoLegalMoves( gameSupplier.get().getActiveBoard() );
    }

    List<Square> getLegalMoves()
    {
        return piece.getLegalMoves( gameSupplier.get().getActiveBoard() );
    }

    boolean controls( SquareHelper squareHelper )
    {
        return getControlledSquares().contains( squareHelper.getSquare() );
    }

    boolean canPseudoLegallyMoveTo( SquareHelper squareHelper )
    {
        return getPseudoLegalMoves().contains( squareHelper.getSquare() );
    }

    boolean canLegallyMoveTo( SquareHelper squareHelper )
    {
        return getLegalMoves().contains( squareHelper.getSquare() );
    }
}
