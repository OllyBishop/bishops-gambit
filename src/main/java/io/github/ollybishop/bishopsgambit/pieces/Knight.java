package io.github.ollybishop.bishopsgambit.pieces;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.board.Board;
import io.github.ollybishop.bishopsgambit.board.Square;
import io.github.ollybishop.bishopsgambit.player.Player;

public class Knight extends Piece
{
    public Knight( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Typ getType()
    {
        return Typ.KNIGHT;
    }

    @Override
    public int getValue()
    {
        return 3;
    }

    @Override
    public List<Square> getPseudoLegalMoves( Board board )
    {
        List<Square> moves = new ArrayList<>();

        Square square = getSquare( board );

        for ( int x : new int[] { -1, 1 } )
        {
            for ( int y : new int[] { -1, 1 } )
            {
                for ( int n : new int[] { 1, 2 } )
                {
                    Square s = square.travel( board, n * x, (3 - n) * y );

                    if ( s != null && !s.isOccupiedBy( getPlayer() ) )
                        moves.add( s );
                }
            }
        }

        return moves;
    }
}
