package io.github.ollybishop.bishopsgambit.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.ollybishop.bishopsgambit.game.Game;
import io.github.ollybishop.bishopsgambit.model.piece.Bishop;
import io.github.ollybishop.bishopsgambit.model.piece.King;
import io.github.ollybishop.bishopsgambit.model.piece.Knight;
import io.github.ollybishop.bishopsgambit.model.piece.Pawn;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;
import io.github.ollybishop.bishopsgambit.model.piece.Rook;

public class Board
{
    private final List<Square> squares = new ArrayList<>( 64 );

    // Castling rights
    private boolean whiteQueensideCastlingAllowed = true;
    private boolean whiteKingsideCastlingAllowed = true;
    private boolean blackQueensideCastlingAllowed = true;
    private boolean blackKingsideCastlingAllowed = true;

    private Pawn enPassantPawn = null;

    public boolean isWhiteQueensideCastlingAllowed()
    {
        return whiteQueensideCastlingAllowed;
    }

    public boolean isWhiteKingsideCastlingAllowed()
    {
        return whiteKingsideCastlingAllowed;
    }

    public boolean isBlackQueensideCastlingAllowed()
    {
        return blackQueensideCastlingAllowed;
    }

    public boolean isBlackKingsideCastlingAllowed()
    {
        return blackKingsideCastlingAllowed;
    }

    public Pawn getEnPassantPawn()
    {
        return enPassantPawn;
    }

    private Board()
    {
    }

    /**
     * Creates a chessboard containing 64 squares and assigns each of the given {@code pieces} to its starting square.
     * 
     * @param pieces the pieces to be assigned
     * @return a new board with all given pieces assigned to their starting squares
     * @throws NullPointerException     if {@code pieces} is null or contains null
     * @throws IllegalArgumentException if multiple pieces have the same starting square
     */
    public static Board createStartingBoard( Set<Piece> pieces )
    {
        Objects.requireNonNull( pieces, "Set of pieces must not be null" );

        Board board = new Board();

        // Create 64 unoccupied squares
        for ( char file = 'a'; file <= 'h'; file++ )
        {
            for ( char rank = '1'; rank <= '8'; rank++ )
            {
                board.squares.add( new Square( file, rank ) );
            }
        }

        // Assign all pieces to their starting squares
        for ( Piece piece : pieces )
        {
            Objects.requireNonNull( piece, "Set of pieces must not contain null values" );

            Square startingSquare = piece.getStartingSquare( board );

            if ( startingSquare.isOccupied() )
            {
                throw new IllegalArgumentException( String.format(
                    "Cannot assign %s to starting square '%s' because it is already occupied by %s",
                    piece, startingSquare, startingSquare.getPiece() ) );
            }

            startingSquare.setPiece( piece );
        }

        return board;
    }

    public Stream<Square> stream()
    {
        return squares.stream();
    }

    /**
     * Returns an unmodifiable snapshot of all pieces currently on the board.
     * <p>
     * The snapshot does not reflect subsequent changes to the board.
     *
     * @return an unmodifiable set of all pieces currently on the board
     */
    public Set<Piece> getPieces()
    {
        return stream().filter( Square::isOccupied )
                       .map( Square::getPiece )
                       .collect( Collectors.toUnmodifiableSet() );
    }

    /**
     * Returns a boolean indicating whether this board contains the given piece; i.e., whether the given piece is currently
     * occupying any square on this board.
     * 
     * @param piece the piece
     * @return {@code true} if this board contains the given piece; {@code false} otherwise
     */
    boolean containsPiece( Piece piece )
    {
        return stream().anyMatch( square -> square.getPiece() == piece );
    }

    /**
     * Returns the square at the given index, using the ordering defined by {@link Square#getIndex(char, char)}.
     * 
     * @param index the square index, from 0 to 63
     * @return the square at the given index
     * @throws IndexOutOfBoundsException if {@code index} is outside the range 0 to 63
     */
    public Square getSquare( int index )
    {
        return squares.get( index );
    }

    /**
     * Finds the square with the given file and rank.
     * 
     * @param file the file of the square to be found
     * @param rank the rank of the square to be found
     * @return the square with the given file and rank (if it exists); {@code null} otherwise
     */
    public Square getSquare( char file, char rank )
    {
        if ( isValidSquare( file, rank ) )
            return getSquare( Square.getIndex( file, rank ) );

        return null;
    }

    public Square getSquare( Square origin, int dx, int dy )
    {
        char file = (char) ( origin.getFile() + dx );
        char rank = (char) ( origin.getRank() + dy );

        return getSquare( file, rank );
    }

    public static boolean isValidSquare( char file, char rank )
    {
        return 'a' <= file && file <= 'h' && '1' <= rank && rank <= '8';
    }

    /**
     * Creates a shallow copy of this board that shares its square instances with the original.
     * <p>
     * New board states should be created by copying an existing board and replacing squares in the copy (see
     * {@link Board#copyAndMove(Square, Square)}). The shared square instances should <b>never</b> be modified directly.
     * 
     * @return a shallow copy of this board
     */
    private Board copy()
    {
        Board newBoard = new Board();

        newBoard.squares.addAll( squares );

        newBoard.whiteQueensideCastlingAllowed = whiteQueensideCastlingAllowed;
        newBoard.whiteKingsideCastlingAllowed = whiteKingsideCastlingAllowed;
        newBoard.blackQueensideCastlingAllowed = blackQueensideCastlingAllowed;
        newBoard.blackKingsideCastlingAllowed = blackKingsideCastlingAllowed;

        newBoard.enPassantPawn = enPassantPawn;

        return newBoard;
    }

    /**
     * Creates a shallow copy of this board and, in the copy, moves the piece occupying {@code from} to {@code to}. Also handles
     * the following special moves if applicable:
     * <ul>
     * <li>Castling: Moves the appropriate rook to the square adjacent to the king.</li>
     * <li>En passant: Removes the captured pawn.</li>
     * </ul>
     * <p>
     * Castling rights and en passant state in the copy are updated accordingly. This board remains unchanged.
     * <p>
     * This method does not check the legality of the requested move. Promotion is handled by
     * {@link Game#makeMove(Square, Square, Piece.Type)}.
     * 
     * @param from the square containing the piece to be moved
     * @param to   the destination square
     * @return a copy of this board with the move applied
     */
    public Board copyAndMove( Square from, Square to )
    {
        Board newBoard = copy();
        newBoard.move( from, to );
        return newBoard;
    }

    /**
     * Applies the move from {@code from} to {@code to} on this board, handling castling and en passant if applicable. Updates
     * castling rights and en passant state accordingly.
     * 
     * @param from the square containing the piece to be moved
     * @param to   the destination square
     */
    private void move( Square from, Square to )
    {
        movePiece( from, to );

        Piece piece = from.getPiece();

        int dx = Integer.signum( to.fileDiff( from ) );
        Square adjacent = getSquare( from, dx, 0 );

        // En passant
        if ( piece instanceof Pawn && piece.movedOneSquareDiagonallyForward( from, to ) && to.isEmpty() )
        {
            replace( adjacent, adjacent.copyWithoutPiece() );
        }
        // Castling
        else if ( piece instanceof King && piece.movedTwoSquaresHorizontally( from, to ) )
        {
            Rook rook = piece.getPlayer().getRook( dx );
            Square rookSquare = rook.getSquare( this );
            movePiece( rookSquare, adjacent );
        }

        if ( piece instanceof Rook || piece instanceof King )
            revokeCastlingRights( piece );

        if ( to.getPiece() instanceof Rook )
            revokeCastlingRights( to.getPiece() );

        updateEnPassantPawn( from, to );
    }

    /**
     * Moves the piece occupying {@code from} to {@code to} by replacing both squares on this board.
     * <p>
     * The replacement for {@code from} is unoccupied, and the replacement for {@code to} contains the piece from the original
     * {@code from} square. The original square instances remain unchanged, preserving any board states that share them.
     * 
     * @param from the square containing the piece to be moved
     * @param to   the destination square
     */
    private void movePiece( Square from, Square to )
    {
        Square newFrom = from.copyWithoutPiece();
        Square newTo = to.copyWithoutPiece();
        newTo.setPiece( from.getPiece() );
        replace( from, newFrom );
        replace( to, newTo );
    }

    /**
     * Replaces {@code oldSquare} on this board with {@code newSquare}.
     * 
     * @param oldSquare the square to be replaced
     * @param newSquare the replacement square
     * @throws IllegalArgumentException if {@code oldSquare} does not belong to this board
     */
    private void replace( Square oldSquare, Square newSquare )
    {
        int index = squares.indexOf( oldSquare );

        if ( index == -1 )
            throw new IllegalArgumentException( "The square to be replaced (" + oldSquare + ") does not belong to this board" );

        squares.set( index, newSquare );
    }

    public Piece promote( Pawn pawn, Piece.Type newType )
    {
        Square square = pawn.getSquare( this );
        Piece newPiece = Piece.newInstance(
            newType,
            pawn.getPlayer(),
            square.getFile(),
            square.getRank() );
        square.setPiece( newPiece );
        return newPiece;
    }

    private void revokeCastlingRights( Piece piece )
    {
        Player player = piece.getPlayer();

        switch ( player.getColour() )
        {
            case WHITE:
                if ( piece == player.getQueensideRook() ||
                     piece == player.getKing() )
                    whiteQueensideCastlingAllowed = false;

                if ( piece == player.getKingsideRook() ||
                     piece == player.getKing() )
                    whiteKingsideCastlingAllowed = false;

                break;

            case BLACK:
                if ( piece == player.getQueensideRook() ||
                     piece == player.getKing() )
                    blackQueensideCastlingAllowed = false;

                if ( piece == player.getKingsideRook() ||
                     piece == player.getKing() )
                    blackKingsideCastlingAllowed = false;

                break;
        }
    }

    public boolean isCastlingAllowed( Rook rook )
    {
        return switch ( rook.getColour() )
        {
            case WHITE -> switch ( rook.getBoardSide() )
            {
                case QUEENSIDE -> whiteQueensideCastlingAllowed;
                case KINGSIDE -> whiteKingsideCastlingAllowed;
            };

            case BLACK -> switch ( rook.getBoardSide() )
            {
                case QUEENSIDE -> blackQueensideCastlingAllowed;
                case KINGSIDE -> blackKingsideCastlingAllowed;
            };
        };
    }

    private void updateEnPassantPawn( Square from, Square to )
    {
        Piece piece = from.getPiece();

        if ( piece instanceof Pawn && piece.movedTwoSquaresForward( from, to ) )
            // Allow this pawn to be captured en passant on the next turn
            enPassantPawn = (Pawn) piece;
        else
            enPassantPawn = null;
    }

    public boolean isLegalMove( Square from, Square to )
    {
        return from.getPiece().getLegalMoves( this ).contains( to );
    }

    public boolean moveWouldLeavePlayerInCheck( Player player, Square from, Square to )
    {
        return player.isInCheck( copyAndMove( from, to ) );
    }

    /**
     * Returns the material difference on this board from White's perspective.
     * <p>
     * A positive value means White is ahead, while a negative value means Black is ahead.
     * 
     * @return the total white material value minus the total black material value
     */
    public int getMaterialDifference()
    {
        return getPieces().stream()
                          .mapToInt( piece -> piece.getPlayerSign() * piece.getValue() )
                          .sum();
    }

    public boolean hasInsufficientMaterial()
    {
        // Get all pieces on the board except kings
        List<Piece> pieces = getPieces().stream()
                                        .filter( piece -> !( piece instanceof King ) )
                                        .toList();

        return switch ( pieces.size() )
        {
            // King versus king
            case 0 -> true;

            // King and knight/bishop versus king
            case 1 -> pieces.get( 0 ) instanceof Knight ||
                      pieces.get( 0 ) instanceof Bishop;

            // King and bishop versus king and bishop with the bishops on the same colour
            case 2 -> pieces.get( 0 ) instanceof Bishop first &&
                      pieces.get( 1 ) instanceof Bishop second &&
                      first.getColour() != second.getColour() &&
                      first.getSquareShade() == second.getSquareShade();

            default -> false;
        };
    }

    public void print()
    {
        Printer.print( this );
    }

    public static enum Side
    {
        QUEENSIDE, KINGSIDE
    }

    private static class Printer
    {
        /*
         * For each box-drawing character below, n is the number of line segments obtained by splitting the character at its
         * endpoints and junctions.
         * 
         * To convert a light box-drawing character to its heavy equivalent, add (1 << n) - 1, i.e. one less than two to the
         * power of n.
         */

        // n = 1
        private static final char HORIZONTAL = '\u2500';
        private static final char VERTICAL = '\u2502';

        // n = 2
        private static final char DOWN_RIGHT = '\u250c';
        private static final char DOWN_LEFT = '\u2510';
        private static final char UP_RIGHT = '\u2514';
        private static final char UP_LEFT = '\u2518';

        // n = 3
        private static final char VERTICAL_RIGHT = '\u251c';
        private static final char VERTICAL_LEFT = '\u2524';
        private static final char DOWN_HORIZONTAL = '\u252c';
        private static final char UP_HORIZONTAL = '\u2534';

        // n = 4
        private static final char VERTICAL_HORIZONTAL = '\u253c';

        private static final String UPPER_ROW = getRow( DOWN_RIGHT, DOWN_HORIZONTAL, DOWN_LEFT );
        private static final String INNER_ROW = getRow( VERTICAL_RIGHT, VERTICAL_HORIZONTAL, VERTICAL_LEFT );
        private static final String LOWER_ROW = getRow( UP_RIGHT, UP_HORIZONTAL, UP_LEFT );

        private static String getRow( char right, char horizontal, char left )
        {
            StringBuilder row = new StringBuilder();

            row.append( right );

            for ( char file = 'a'; file <= 'h'; file++ )
            {
                row.append( HORIZONTAL )
                   .append( HORIZONTAL )
                   .append( HORIZONTAL );

                if ( file < 'h' )
                    row.append( horizontal );
            }

            row.append( left );

            return row.toString();
        }

        private static void print( Board board )
        {
            System.out.println( UPPER_ROW );

            for ( char rank = '8'; rank >= '1'; rank-- )
            {
                StringBuilder pieceRow = new StringBuilder();

                pieceRow.append( VERTICAL );

                for ( char file = 'a'; file <= 'h'; file++ )
                {
                    Square square = board.getSquare( file, rank );

                    pieceRow.append( ' ' )
                            .append( square.isOccupied() ? square.getPiece().toChar() : ' ' )
                            .append( ' ' )
                            .append( VERTICAL );
                }

                System.out.println( pieceRow );

                if ( rank > '1' )
                    System.out.println( INNER_ROW );
            }

            System.out.println( LOWER_ROW );
        }
    }
}
