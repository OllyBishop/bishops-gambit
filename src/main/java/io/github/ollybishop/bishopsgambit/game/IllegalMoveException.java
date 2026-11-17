package io.github.ollybishop.bishopsgambit.game;

import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;

class IllegalMoveException extends IllegalArgumentException
{
    private IllegalMoveException( String message )
    {
        super( message );
    }

    static IllegalMoveException wrongPlayer( Piece piece, Player activePlayer )
    {
        return new IllegalMoveException( String.format(
            "%s cannot be moved because it is %s's turn",
            piece, activePlayer ) );
    }

    static IllegalMoveException illegalPieceMove( Square from, Square to )
    {
        return new IllegalMoveException( String.format(
            "%s cannot legally move from '%s' to '%s'",
            from.getPiece(), from, to ) );
    }
}
