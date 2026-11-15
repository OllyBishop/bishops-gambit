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
        String message = piece + " cannot be moved because it is " + activePlayer + "'s turn";
        return new IllegalMoveException( message );
    }

    static IllegalMoveException illegalPieceMove( Square from, Square to )
    {
        String message = from.getPiece() + " cannot legally move from " + from + " to " + to;
        return new IllegalMoveException( message );
    }
}
