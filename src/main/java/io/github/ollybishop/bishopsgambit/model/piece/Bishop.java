package io.github.ollybishop.bishopsgambit.model.piece;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;

public class Bishop extends Piece
{
    private final Square.Shade squareShade;

    public Bishop( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );

        squareShade = Square.getShade( startFile, startRank );
    }

    public Square.Shade getSquareShade()
    {
        return this.squareShade;
    }

    @Override
    public Type getType()
    {
        return Type.BISHOP;
    }

    @Override
    public int getValue()
    {
        return 3;
    }

    @Override
    List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        return getCandidateSquares( this, board, includeFriendlySquares );
    }

    static List<Square> getCandidateSquares( Piece piece, Board board, boolean includeFriendlySquares )
    {
        List<Square> candidateSquares = new ArrayList<>();

        Square square = piece.getSquare( board );

        for ( int dx : new int[] { -1, 1 } )
        {
            for ( int dy : new int[] { -1, 1 } )
            {
                for ( int n = 1; n < 8; n++ )
                {
                    Square s = board.getSquare( square, n * dx, n * dy );

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
