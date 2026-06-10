package io.github.ollybishop.bishopsgambit.board;

import io.github.ollybishop.bishopsgambit.pieces.Piece;
import io.github.ollybishop.bishopsgambit.player.Player;

public class Square
{
    private final char file;
    private final char rank;

    private Piece piece;

    protected Square( char file, char rank )
    {
        this.file = file;
        this.rank = rank;
    }

    public char getFile()
    {
        return this.file;
    }

    public char getRank()
    {
        return this.rank;
    }

    public Piece getPiece()
    {
        return this.piece;
    }

    protected void setPiece( Piece piece )
    {
        this.piece = piece;
    }

    @Override
    public String toString()
    {
        return String.format( "%s%s", getFile(), getRank() );
    }

    /**
     * Creates a new square that has the same file and rank as {@code this}. No piece is assigned to
     * the new square; that is, the new square is unoccupied.
     * 
     * @return a new square that has the same file and rank as {@code this}
     */
    @Override
    protected Square clone()
    {
        return new Square( getFile(), getRank() );
    }

    /**
     * Returns whether this square is occupied by a piece.
     * 
     * @return {@code true} if this square is occupied by a piece; {@code false} otherwise
     */
    public boolean isOccupied()
    {
        return getPiece() != null;
    }

    /**
     * Returns whether this square is occupied by one of the given player's pieces.
     * 
     * @param player the player to check
     * @return {@code true} if this square is occupied by one of the given player's pieces;
     *         {@code false} otherwise
     */
    public boolean isOccupiedBy( Player player )
    {
        return player.getPieces().contains( getPiece() );
    }

    /**
     * Returns whether this square is occupied by an opposing piece.
     * 
     * @param player the player whose opponent is checked
     * @return {@code true} if this square is occupied by an opposing piece; {@code false} otherwise
     */
    public boolean isOccupiedByOpponentOf( Player player )
    {
        return isOccupied() && !isOccupiedBy( player );
    }

    /**
     * Returns whether this square is a pseudo-legal move destination for any opposing piece.
     * 
     * @param player the player whose opponent's pieces are considered
     * @param board  the chessboard
     * @return {@code true} if this square is pseudo-legally reachable by any opposing piece;
     *         {@code false} otherwise
     */
    public boolean isPseudoLegallyReachableByOpponentOf( Player player, Board board )
    {
        return board.getPieces()
                    .stream()
                    .filter( piece -> piece.getPlayer() != player )
                    .anyMatch( piece -> piece.canPseudoLegallyMoveTo( this, board ) );
    }

    public boolean isOnLastRank( Player player )
    {
        return switch ( player.getColour() )
        {
            case WHITE -> getRank() == '8';
            case BLACK -> getRank() == '1';
        };
    }

    public Square travel( Board board, int x, int y )
    {
        return board.getSquare( (char) (getFile() + x), (char) (getRank() + y) );
    }

    public int fileDiff( Square square )
    {
        return getFile() - square.getFile();
    }

    public int rankDiff( Square square )
    {
        return getRank() - square.getRank();
    }

    public int getParity()
    {
        return getParity( getFile(), getRank() );
    }

    public static int getParity( char file, char rank )
    {
        return (file + rank) % 2;
    }

    public int getIndex()
    {
        return getIndex( getFile(), getRank() );
    }

    public static int getIndex( char file, char rank )
    {
        return 8 * (file - 'a') + rank - '1';
    }
}
