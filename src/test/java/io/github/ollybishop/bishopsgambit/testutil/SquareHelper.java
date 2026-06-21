package io.github.ollybishop.bishopsgambit.testutil;

import java.util.function.Supplier;

import io.github.ollybishop.bishopsgambit.game.Game;
import io.github.ollybishop.bishopsgambit.model.Square;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;
import io.github.ollybishop.bishopsgambit.model.piece.PieceHelper;

public class SquareHelper
{
    private final Supplier<Game> gameSupplier;

    private final char file;
    private final char rank;

    public SquareHelper( Supplier<Game> gameSupplier, char file, char rank )
    {
        this.gameSupplier = gameSupplier;

        this.file = file;
        this.rank = rank;
    }

    public char getFile()
    {
        return file;
    }

    public char getRank()
    {
        return rank;
    }

    public Square getSquare()
    {
        return gameSupplier.get().getActiveBoard().getSquare( file, rank );
    }

    public Piece getPiece()
    {
        return getSquare().getPiece();
    }

    public PieceHelper getPieceHelper()
    {
        return new PieceHelper( gameSupplier, getPiece() );
    }
}
