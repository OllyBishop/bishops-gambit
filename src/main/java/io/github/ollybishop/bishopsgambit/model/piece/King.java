package io.github.ollybishop.bishopsgambit.model.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;

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
    List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        List<Square> candidateSquares = new ArrayList<>();

        Square square = getSquare( board );

        // Standard moves
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
    List<Square> getPseudoLegalMoves( Board board )
    {
        List<Square> pseudoLegalMoves = super.getPseudoLegalMoves( board );

        Square kingSquare = getSquare( board );

        // Castling moves
        for ( int dx : new int[] { -1, 1 } )
        {
            Rook rook = getPlayer().getRook( dx );

            if ( board.isCastlingAllowed( rook ) )
            {
                Square rookSquare = rook.getSquare( board );

                // One square adjacent to king (rook moves here)
                Square kingAdjacent = board.getSquare( kingSquare, dx, 0 );

                // Two squares adjacent to king (king moves here)
                Square kingDestination = board.getSquare( kingSquare, 2 * dx, 0 );

                // One square adjacent to rook (same as kingDestination when castling kingside)
                Square rookAdjacent = board.getSquare( rookSquare, -dx, 0 );

                // If there is a clear path between the king and rook
                if ( kingAdjacent.isEmpty() &&
                     kingDestination.isEmpty() &&
                     rookAdjacent.isEmpty() )
                    pseudoLegalMoves.add( kingDestination );
            }
        }

        return pseudoLegalMoves;
    }

    @Override
    public List<Square> getLegalMoves( Board board )
    {
        Square kingSquare = getSquare( board );
        boolean kingIsInCheck = kingSquare.isControlledByOpponentOf( getPlayer(), board );

        // Castling moves are legal only if the king does not start in, pass through or land in check
        Predicate<Square> castlingMoveIsLegal = kingDestination ->
        {
            if ( movedTwoSquaresHorizontally( kingSquare, kingDestination ) )
            {
                if ( kingIsInCheck )
                    return false;

                int dx = Integer.signum( kingDestination.fileDiff( kingSquare ) );

                // One square adjacent to king (rook moves here)
                Square kingAdjacent = board.getSquare( kingSquare, dx, 0 );

                // If king passes through or lands in check
                if ( kingAdjacent.isControlledByOpponentOf( getPlayer(), board ) ||
                     kingDestination.isControlledByOpponentOf( getPlayer(), board ) )
                    return false;
            }

            return true;
        };

        return super.getLegalMoves( board ).stream()
                                           .filter( castlingMoveIsLegal )
                                           .toList();
    }
}
