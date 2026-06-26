package io.github.ollybishop.bishopsgambit.game;

import io.github.ollybishop.bishopsgambit.model.Square;

class NoPieceOnSquareException extends IllegalStateException
{
    NoPieceOnSquareException( Square from )
    {
        super( String.format( "Cannot move a piece from empty square %s.", from ) );
    }
}
