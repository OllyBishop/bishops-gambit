package io.github.ollybishop.bishopsgambit.game;

import io.github.ollybishop.bishopsgambit.board.Square;

public class UnoccupiedSquareException extends RuntimeException
{
    protected UnoccupiedSquareException( Square from )
    {
        super( String.format( "Cannot make a move from the unoccupied square %s.", from ) );
    }
}
