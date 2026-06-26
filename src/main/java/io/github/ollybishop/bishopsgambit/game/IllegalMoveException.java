package io.github.ollybishop.bishopsgambit.game;

import io.github.ollybishop.bishopsgambit.model.Square;

class IllegalMoveException extends IllegalArgumentException
{
    IllegalMoveException( Square from, Square to )
    {
        super( String.format( "The %s on %s cannot legally move to %s.", from.getPiece(), from, to ) );
    }
}
