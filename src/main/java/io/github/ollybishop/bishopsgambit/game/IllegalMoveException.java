package io.github.ollybishop.bishopsgambit.game;

import io.github.ollybishop.bishopsgambit.model.Square;

public class IllegalMoveException extends RuntimeException
{
    protected IllegalMoveException( Square from, Square to )
    {
        super( String.format( "The %s occupying %s cannot legally move to %s.", from.getPiece(), from, to ) );
    }
}
