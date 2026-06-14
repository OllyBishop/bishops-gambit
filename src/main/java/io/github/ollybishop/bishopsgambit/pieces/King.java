package io.github.ollybishop.bishopsgambit.pieces;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.board.Board;
import io.github.ollybishop.bishopsgambit.board.Square;
import io.github.ollybishop.bishopsgambit.player.Player;

public class King extends Piece
{
    public King( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Type getType()
    {
        return Type.KING;
    }

    @Override
    public int getValue()
    {
        return 0;
    }

    @Override
    public List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        List<Square> candidateSquares = new ArrayList<>();

        Square square = getSquare( board );

        for ( int dx : new int[] { -1, 0, 1 } )
        {
            for ( int dy : new int[] { -1, 0, 1 } )
            {
                if ( dx == 0 && dy == 0 )
                    continue;

                Square s = board.getSquare( square, dx, dy );

                if ( s == null )
                    continue;

                if ( includeFriendlySquares || s.isEmpty() || s.isOccupiedByOpponentOf( getPlayer() ) )
                    candidateSquares.add( s );
            }
        }

        return candidateSquares;
    }

    @Override
    public List<Square> getLegalMoves( Board board )
    {
        if ( isUnderAttack( board ) )
            return super.getLegalMoves( board );

        List<Square> legalMoves = new ArrayList<>( super.getLegalMoves( board ) );

        for ( int dx : new int[] { -1, 1 } )
        {
            Rook rook = getPlayer().getRook( dx );

            if ( board.isCastlingAllowed( rook ) )
            {
                Square kingSquare = getSquare( board );
                Square rookSquare = rook.getSquare( board );

                // One square adjacent to king (rook moves here)
                Square kingAdjacent = board.getSquare( kingSquare, dx, 0 );

                // Two squares adjacent to king (king moves here)
                Square kingDestination = board.getSquare( kingSquare, 2 * dx, 0 );

                // One square adjacent to rook (same as 'k2' when castling kingside)
                Square rookAdjacent = board.getSquare( rookSquare, -dx, 0 );

                if ( kingAdjacent.isEmpty() &&
                     kingDestination.isEmpty() &&
                     rookAdjacent.isEmpty() &&
                     !kingAdjacent.isControlledByOpponentOf( getPlayer(), board ) &&
                     !kingDestination.isControlledByOpponentOf( getPlayer(), board ) )
                    legalMoves.add( kingDestination );
            }
        }

        return legalMoves;
    }
}
