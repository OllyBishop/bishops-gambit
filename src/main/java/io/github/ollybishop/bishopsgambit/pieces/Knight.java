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
    public Type getType()
    {
        return Type.KNIGHT;
    }

    @Override
    public int getValue()
    {
        return 3;
    }

    @Override
    protected List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        List<Square> candidateSquares = new ArrayList<>();

        Square square = getSquare( board );

        for ( int dx : new int[] { -1, 1 } )
        {
            for ( int dy : new int[] { -1, 1 } )
            {
                for ( int n : new int[] { 1, 2 } )
                {
                    Square s = board.getSquare( square, n * dx, ( 3 - n ) * dy );

                    if ( s == null )
                        continue;

                    if ( includeFriendlySquares || s.isEmpty() || s.isOccupiedByOpponentOf( getPlayer() ) )
                        candidateSquares.add( s );
                }
            }
        }

        return candidateSquares;
    }
}
