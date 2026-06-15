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

    public List<Square> getControlledSquares()
    {
        return piece.getControlledSquares( gameSupplier.get().getActiveBoard() );
    }

    public boolean controls( SquareHelper squareHelper )
    {
        return piece.controls( squareHelper.getSquare(), gameSupplier.get().getActiveBoard() );
    }
}
