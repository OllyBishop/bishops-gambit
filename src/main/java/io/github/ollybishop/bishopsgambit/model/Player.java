package io.github.ollybishop.bishopsgambit.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import io.github.ollybishop.bishopsgambit.model.piece.Bishop;
import io.github.ollybishop.bishopsgambit.model.piece.King;
import io.github.ollybishop.bishopsgambit.model.piece.Knight;
import io.github.ollybishop.bishopsgambit.model.piece.Pawn;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;
import io.github.ollybishop.bishopsgambit.model.piece.Queen;
import io.github.ollybishop.bishopsgambit.model.piece.Rook;

public class Player
{
    private final Colour colour;

    private final Set<Piece> pieces = new HashSet<>();

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

        queensideRook = new Rook( this, 'a', backRank );
        kingsideRook = new Rook( this, 'h', backRank );

        new Queen( this, 'd', backRank );

        king = new King( this, 'e', backRank );
    }

    public Colour getColour()
    {
        return colour;
    }

    /**
     * Returns this player's sign for player-relative calculations.
     * <p>
     * The sign is {@code 1} for White and {@code -1} for Black. It also matches the y-direction in which this player's pawns
     * move.
     * 
     * @return {@code 1} for White; {@code -1} for Black
     */
    public int getSign()
    {
        return switch ( colour )
        {
            case WHITE -> 1;
            case BLACK -> -1;
        };
    }

    /**
     * Returns an unmodifiable view of all pieces registered to this player, including captured pieces.
     * <p>
     * The view reflects subsequent piece registrations.
     *
     * @return an unmodifiable view of this player's registered pieces
     */
    public Set<Piece> getPieces()
    {
        return Collections.unmodifiableSet( pieces );
    }

    /**
     * Registers the given piece as belonging to this player. Registering an already registered piece has no effect.
     *
     * @param piece the piece to register
     * @throws NullPointerException     if {@code piece} is null
     * @throws IllegalArgumentException if {@code piece} belongs to another player
     */
    public void registerPiece( Piece piece )
    {
        Objects.requireNonNull( piece );

        if ( piece.getPlayer() != this )
            throw new IllegalArgumentException( "The piece being registered must belong to this player" );

        pieces.add( piece );
    }

    Rook getQueensideRook()
    {
        return queensideRook;
    }

    Rook getKingsideRook()
    {
        return kingsideRook;
    }

    /**
     * Returns this player's original queenside or kingside rook, as indicated by the given castling direction.
     * <p>
     * The castling direction is the horizontal direction from the king's starting square towards the rook's starting square.
     * 
     * @param castlingDirection the castling direction
     * @return the queenside rook if {@code castlingDirection} is negative; the kingside rook if {@code castlingDirection} is
     *         positive
     * @throws IllegalArgumentException if {@code castlingDirection} is zero
     */
    public Rook getRook( int castlingDirection )
    {
        if ( castlingDirection < 0 )
            return queensideRook;

        if ( castlingDirection > 0 )
            return kingsideRook;

        throw new IllegalArgumentException( "Castling direction must be non-zero" );
    }

    public King getKing()
    {
        return king;
    }

    @Override
    public String toString()
    {
        return colour.toString();
    }

    /**
     * Returns whether this player is in check.
     * 
     * @param board the chessboard
     * @return {@code true} if this player is in check; {@code false} otherwise
     */
    public boolean isInCheck( Board board )
    {
        return king.isUnderAttack( board );
    }

    /**
     * Returns whether this player is checkmated.
     * 
     * @param board the chessboard
     * @return {@code true} if this player is checkmated; {@code false} otherwise
     */
    public boolean isCheckmated( Board board )
    {
        return hasNoLegalMoves( board ) && isInCheck( board );
    }

    /**
     * Returns whether this player is stalemated.
     * 
     * @param board the chessboard
     * @return {@code true} if this player is stalemated; {@code false} otherwise
     */
    public boolean isStalemated( Board board )
    {
        return hasNoLegalMoves( board ) && !isInCheck( board );
    }

    /**
     * Returns the number of legal moves this player can make.
     * 
     * @param board the chessboard
     * @return the number of legal moves this player can make
     */
    public int getNumberOfLegalMoves( Board board )
    {
        return pieces.stream()
                     .filter( piece -> board.containsPiece( piece ) )
                     .mapToInt( piece -> piece.getLegalMoves( board ).size() )
                     .sum();
    }

    /**
     * Returns whether this player has no legal moves.
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
            return str;
        }

        public Colour opposite()
        {
            return switch ( this )
            {
                case WHITE -> BLACK;
                case BLACK -> WHITE;
            };
        }
    }
}
