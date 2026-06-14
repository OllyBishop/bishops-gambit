package io.github.ollybishop.bishopsgambit.player;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.board.Board;
import io.github.ollybishop.bishopsgambit.pieces.Bishop;
import io.github.ollybishop.bishopsgambit.pieces.King;
import io.github.ollybishop.bishopsgambit.pieces.Knight;
import io.github.ollybishop.bishopsgambit.pieces.Pawn;
import io.github.ollybishop.bishopsgambit.pieces.Piece;
import io.github.ollybishop.bishopsgambit.pieces.Queen;
import io.github.ollybishop.bishopsgambit.pieces.Rook;

public class Player
{
    private final Colour colour;

    private final List<Piece> pieces = new ArrayList<>();

    private final Rook queensideRook;
    private final Rook kingsideRook;

    private final King king;

    public Player( Colour colour )
    {
        this.colour = colour;

        char backRank = switch ( colour )
        {
            case WHITE -> '1';
            case BLACK -> '8';
        };

        char pawnRank = switch ( colour )
        {
            case WHITE -> '2';
            case BLACK -> '7';
        };

        new Pawn( this, 'a', pawnRank );
        new Pawn( this, 'b', pawnRank );
        new Pawn( this, 'c', pawnRank );
        new Pawn( this, 'd', pawnRank );
        new Pawn( this, 'e', pawnRank );
        new Pawn( this, 'f', pawnRank );
        new Pawn( this, 'g', pawnRank );
        new Pawn( this, 'h', pawnRank );

        new Knight( this, 'b', backRank );
        new Knight( this, 'g', backRank );

        new Bishop( this, 'c', backRank );
        new Bishop( this, 'f', backRank );

        this.queensideRook = new Rook( this, 'a', backRank );
        this.kingsideRook = new Rook( this, 'h', backRank );

        new Queen( this, 'd', backRank );

        this.king = new King( this, 'e', backRank );
    }

    public Colour getColour()
    {
        return this.colour;
    }

    public int getCoefficient()
    {
        return switch ( getColour() )
        {
            case WHITE -> 1;
            case BLACK -> -1;
        };
    }

    public List<Piece> getPieces()
    {
        return this.pieces;
    }

    public Rook getQueensideRook()
    {
        return this.queensideRook;
    }

    public Rook getKingsideRook()
    {
        return this.kingsideRook;
    }

    /**
     * Returns this player's original queenside or kingside rook, as indicated by the given castling
     * direction.
     * <p>
     * The castling direction is the horizontal direction from the king's starting square towards
     * the rook's starting square.
     * 
     * @param castlingDirection the castling direction
     * @return the queenside rook if {@code castlingDirection} is negative; the kingside rook if
     *         {@code castlingDirection} is positive
     * @throws IllegalArgumentException if {@code castlingDirection} is zero
     */
    public Rook getRook( int castlingDirection )
    {
        if ( castlingDirection < 0 )
            return getQueensideRook();

        if ( castlingDirection > 0 )
            return getKingsideRook();

        throw new IllegalArgumentException( "Castling direction must be non-zero." );
    }

    public King getKing()
    {
        return this.king;
    }

    @Override
    public String toString()
    {
        return getColour().toString();
    }

    /**
     * Returns a boolean indicating whether this player is in check.
     * 
     * @param board the chessboard
     * @return {@code true} if this player is in check; {@code false} otherwise
     */
    public boolean isInCheck( Board board )
    {
        return getKing().isUnderAttack( board );
    }

    /**
     * Returns a boolean indicating whether this player is checkmated.
     * 
     * @param board the chessboard
     * @return {@code true} if this player is checkmated; {@code false} otherwise
     */
    public boolean isCheckmated( Board board )
    {
        return hasNoLegalMoves( board ) && isInCheck( board );
    }

    /**
     * Returns a boolean indicating whether this player is stalemated.
     * 
     * @param board the chessboard
     * @return {@code true} if this player is stalemated; {@code false} otherwise
     */
    public boolean isStalemated( Board board )
    {
        return hasNoLegalMoves( board ) && !isInCheck( board );
    }

    /**
     * Calculates the number of legal moves this player can make.
     * 
     * @param board the chessboard
     * @return the number of legal moves this player can make
     */
    public int getNumberOfLegalMoves( Board board )
    {
        return getPieces().stream()
                          .filter( piece -> board.containsPiece( piece ) )
                          .mapToInt( piece -> piece.getLegalMoves( board ).size() )
                          .sum();
    }

    /**
     * Returns a boolean indicating whether this player has any legal moves.
     * 
     * @param board the chessboard
     * @return {@code true} if this player has no legal moves; {@code false} otherwise
     */
    private boolean hasNoLegalMoves( Board board )
    {
        return getNumberOfLegalMoves( board ) == 0;
    }

    public enum Colour
    {
        WHITE( "White" ),
        BLACK( "Black" );

        private final String str;

        private Colour( String str )
        {
            this.str = str;
        }

        @Override
        public String toString()
        {
            return this.str;
        }

        public Colour transpose()
        {
            return switch ( this )
            {
                case WHITE -> BLACK;
                case BLACK -> WHITE;
            };
        }
    }
}
