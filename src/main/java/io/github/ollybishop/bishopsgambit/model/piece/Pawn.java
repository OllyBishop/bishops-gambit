package io.github.ollybishop.bishopsgambit.model.piece;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;

public class Pawn extends Piece
{
    public Pawn( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Type getType()
    {
        return Type.PAWN;
    }

    @Override
    public int getValue()
    {
        return 1;
    }

    @Override
    List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        List<Square> candidateSquares = new ArrayList<>();

        Square square = getSquare( board );
        int dy = getPlayerSign();

        Square oneRankForward = board.getSquare( square, 0, dy );

        if ( oneRankForward.isEmpty() )
        {
            candidateSquares.add( oneRankForward );

            Square twoRanksForward = board.getSquare( square, 0, 2 * dy );

            if ( square == getStartSquare( board ) && twoRanksForward.isEmpty() )
                candidateSquares.add( twoRanksForward );
        }

        List<Square> captureSquares = getControlledSquares( board ).stream()
                                                                   .filter( s -> canCaptureOn( s, board ) )
                                                                   .toList();

        candidateSquares.addAll( captureSquares );

        return candidateSquares;
    }

    /**
     * Returns whether this pawn can capture on the given square.
     * <p>
     * A pawn can capture on the given square if it is occupied by an opposing piece, or if moving
     * there would capture the board's en passant pawn. This method does not check whether making
     * the move would leave the moving player in check.
     * 
     * @param square the destination square
     * @param board  the chessboard
     * @return {@code true} if this pawn can capture on the given square; {@code false} otherwise
     */
    private boolean canCaptureOn( Square square, Board board )
    {
        if ( square.isOccupiedByOpponentOf( getPlayer() ) )
            return true;

        Pawn enPassantPawn = board.getEnPassantPawn();

        if ( enPassantPawn == null )
            return false;

        char file = square.getFile();
        char rank = getSquare( board ).getRank();

        return board.getSquare( file, rank ).getPiece() == enPassantPawn;
    }

    /**
     * For pawns, controlled squares are not simply candidate squares with friendly-occupied squares
     * included. A pawn controls its forward diagonals: two for non-edge pawns, or one for a-pawns
     * and h-pawns.
     */
    @Override
    public List<Square> getControlledSquares( Board board )
    {
        List<Square> controlledSquares = new ArrayList<>();

        Square square = getSquare( board );
        int dy = getPlayerSign();

        for ( int dx : new int[] { -1, 1 } )
        {
            Square diagonal = board.getSquare( square, dx, dy );

            if ( diagonal != null )
                controlledSquares.add( diagonal );
        }

        return controlledSquares;
    }
}
