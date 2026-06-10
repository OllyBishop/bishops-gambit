package io.github.ollybishop.bishopsgambit.pieces;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.board.Board;
import io.github.ollybishop.bishopsgambit.board.Square;
import io.github.ollybishop.bishopsgambit.player.Player;

public class Bishop extends Piece
{
    public Bishop( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Typ getType()
    {
        return Typ.BISHOP;
    }

    @Override
    public int getValue()
    {
        return 3;
    }

    @Override
    public List<Square> getPseudoLegalMoves( Board board )
    {
        return getPseudoLegalMoves( board, this );
    }

    public static List<Square> getPseudoLegalMoves( Board board, Piece piece )
    {
        List<Square> moves = new ArrayList<>();

        Square square = piece.getSquare( board );

        for ( int x : new int[] { -1, 1 } )
        {
            for ( int y : new int[] { -1, 1 } )
            {
                for ( int n = 1; n < 8; n++ )
                {
                    Square s = square.travel( board, n * x, n * y );

                    if ( s == null )
                        break;

                    if ( s.isOccupied() )
                    {
                        if ( s.isOccupiedByOpponentOf( piece.getPlayer() ) )
                            moves.add( s );

                        break;
                    }

                    moves.add( s );
                }
            }
        }

        return moves;
    }
}
