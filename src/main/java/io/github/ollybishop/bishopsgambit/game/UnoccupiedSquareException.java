package io.github.ollybishop.bishopsgambit.game;

import io.github.ollybishop.bishopsgambit.model.Square;

class UnoccupiedSquareException extends RuntimeException
{
    UnoccupiedSquareException( Square from )
    {
        super( String.format( "Cannot make a move from the unoccupied square %s.", from ) );
    }
}
