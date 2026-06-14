package io.github.ollybishop.bishopsgambit.pieces;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.board.Board;
import io.github.ollybishop.bishopsgambit.board.Square;
import io.github.ollybishop.bishopsgambit.player.Player;

public class Rook extends Piece
{
    public Rook( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Type getType()
    {
        return Type.ROOK;
    }

    @Override
    public int getValue()
    {
        return 5;
    }

    @Override
    protected List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        return getCandidateSquares( this, board, includeFriendlySquares );
    }

    protected static List<Square> getCandidateSquares( Piece piece, Board board, boolean includeFriendlySquares )
    {
        List<Square> candidateSquares = new ArrayList<>();

        Square square = piece.getSquare( board );

        for ( int dx : new int[] { 0, 1 } )
        {
            for ( int dy : new int[] { -1, 1 } )
            {
                for ( int n = 1; n < 8; n++ )
                {
                    Square s = board.getSquare( square, n * dx * dy, n * ( 1 - dx ) * dy );

                    if ( s == null )
                        break;

                    if ( s.isOccupied() )
                    {
                        if ( includeFriendlySquares || s.isOccupiedByOpponentOf( piece.getPlayer() ) )
                            candidateSquares.add( s );

                        break;
                    }

                    candidateSquares.add( s );
                }
            }
        }

        return candidateSquares;
    }
}
