package io.github.ollybishop.bishopsgambit.model;

import static io.github.ollybishop.bishopsgambit.testutil.TestAssertions.assertThrowsWithMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.ollybishop.bishopsgambit.model.piece.Piece;

class BoardTest
{
    // =========================================================================================
    // Helpers
    // =========================================================================================

    private Board createFullStartingBoard()
    {
        Player white = new Player( Player.Colour.WHITE );
        Player black = new Player( Player.Colour.BLACK );

        Set<Piece> pieces = new HashSet<>();
        pieces.addAll( white.getPieces() );
        pieces.addAll( black.getPieces() );

        return Board.createStartingBoard( pieces );
    }

    private Board copyAndMove( Board board, String... moves )
    {
        for ( String move : moves )
        {
            Square from = board.getSquare( move.charAt( 0 ), move.charAt( 1 ) );
            Square to = board.getSquare( move.charAt( 2 ), move.charAt( 3 ) );

            board = board.copyAndMove( from, to );
        }

        return board;
    }

    // =========================================================================================
    // Board Creation
    // =========================================================================================

    @Test
    void createStartingBoard_emptyPieceSet_createsSixtyFourUnoccupiedSquares()
    {
        Board board = Board.createStartingBoard( Set.of() );

        assertEquals( 64, board.stream().count() );
        assertTrue( board.stream().allMatch( Square::isEmpty ) );
    }

    @Test
    void createStartingBoard_assignsOnlySuppliedPieceToItsStartingSquare()
    {
        Player white = new Player( Player.Colour.WHITE );

        Board board = Board.createStartingBoard( Set.of( white.getQueensideRook() ) );

        assertSame( white.getQueensideRook(), board.getSquare( 'a', '1' ).getPiece() );
        assertEquals( 1, board.stream().filter( Square::isOccupied ).count() );
    }

    @Test
    void createStartingBoard_nullPieceSet_throwsNullPointerException()
    {
        assertThrowsWithMessage(
            NullPointerException.class,
            () -> Board.createStartingBoard( null ),
            "Set of pieces must not be null" );
    }

    @Test
    void createStartingBoard_nullElement_throwsNullPointerException()
    {
        Set<Piece> pieces = new HashSet<>();
        pieces.add( null );

        assertThrowsWithMessage(
            NullPointerException.class,
            () -> Board.createStartingBoard( pieces ),
            "Set of pieces must not contain null values" );
    }

    @Test
    void createStartingBoard_conflictingStartingSquares_throwsIllegalArgumentException()
    {
        Player white = new Player( Player.Colour.WHITE );

        Piece conflictingRook = Piece.newInstance( Piece.Type.ROOK, white, 'a', '1' );

        Set<Piece> pieces = Set.of( white.getQueensideRook(), conflictingRook );

        assertThrowsWithMessage(
            IllegalArgumentException.class,
            () -> Board.createStartingBoard( pieces ),
            "Tried to assign White Rook to its starting square 'a1', but it is already occupied by White Rook" );
    }

    // =========================================================================================
    // Pieces
    // =========================================================================================

    @Test
    void getPieces_returnsOnlyPiecesCurrentlyOnBoard()
    {
        Player white = new Player( Player.Colour.WHITE );

        assertEquals( 16, white.getPieces().size() );

        Set<Piece> whiteRooks = Set.of( white.getQueensideRook(), white.getKingsideRook() );

        Board board = Board.createStartingBoard( whiteRooks );

        assertEquals( whiteRooks, board.getPieces() );
    }

    @Test
    void getPieces_returnsUnmodifiableSet()
    {
        Set<Piece> pieces = createFullStartingBoard().getPieces();

        assertThrows( UnsupportedOperationException.class, pieces::clear );
    }

    // =========================================================================================
    // Board States
    // =========================================================================================

    @Test
    void copyAndMove_sharesUnchangedSquaresWithOriginalBoard()
    {
        Board boardBeforeMove = createFullStartingBoard();

        Board boardAfterMove = copyAndMove( boardBeforeMove, "e2e4" );

        for ( char file = 'a'; file <= 'h'; file++ )
        {
            for ( char rank = '1'; rank <= '8'; rank++ )
            {
                Square oldSquare = boardBeforeMove.getSquare( file, rank );
                Square newSquare = boardAfterMove.getSquare( file, rank );

                if ( file == 'e' && ( rank == '2' || rank == '4' ) )
                    assertNotSame( oldSquare, newSquare, "Expected a replacement square on '" + oldSquare + "'" );
                else
                    assertSame( oldSquare, newSquare, "Expected a shared square on '" + oldSquare + "'" );
            }
        }
    }

    @Test
    void movingPiece_doesNotMutatePreviousBoardState()
    {
        Board boardBeforeMove = createFullStartingBoard();

        Piece whitePawn = boardBeforeMove.getSquare( 'e', '2' ).getPiece();

        Board boardAfterMove = copyAndMove( boardBeforeMove, "e2e4" );

        assertSame( whitePawn, boardBeforeMove.getSquare( 'e', '2' ).getPiece() );
        assertTrue( boardBeforeMove.getSquare( 'e', '4' ).isEmpty() );

        assertTrue( boardAfterMove.getSquare( 'e', '2' ).isEmpty() );
        assertSame( whitePawn, boardAfterMove.getSquare( 'e', '4' ).getPiece() );
    }

    @Test
    void capturingPiece_doesNotMutatePreviousBoardState()
    {
        Board boardBeforeCapture = copyAndMove( createFullStartingBoard(), "e2e4", "d7d5" );

        Piece whitePawn = boardBeforeCapture.getSquare( 'e', '4' ).getPiece();
        Piece blackPawn = boardBeforeCapture.getSquare( 'd', '5' ).getPiece();

        Board boardAfterCapture = copyAndMove( boardBeforeCapture, "e4d5" );

        assertSame( whitePawn, boardBeforeCapture.getSquare( 'e', '4' ).getPiece() );
        assertSame( blackPawn, boardBeforeCapture.getSquare( 'd', '5' ).getPiece() );

        assertTrue( boardAfterCapture.getSquare( 'e', '4' ).isEmpty() );
        assertSame( whitePawn, boardAfterCapture.getSquare( 'd', '5' ).getPiece() );
        assertNull( blackPawn.getSquare( boardAfterCapture ) );
    }

    @Test
    void castling_doesNotMutatePreviousBoardState()
    {
        Board boardBeforeCastling = copyAndMove(
            createFullStartingBoard(),
            "e2e4", "e7e5",
            "g1f3", "b8c6",
            "f1c4", "g8f6" );

        Piece whiteKing = boardBeforeCastling.getSquare( 'e', '1' ).getPiece();
        Piece whiteRook = boardBeforeCastling.getSquare( 'h', '1' ).getPiece();

        assertTrue( boardBeforeCastling.isWhiteKingsideCastlingAllowed() );
        assertTrue( boardBeforeCastling.isWhiteQueensideCastlingAllowed() );

        Board boardAfterCastling = copyAndMove( boardBeforeCastling, "e1g1" );

        assertSame( whiteKing, boardBeforeCastling.getSquare( 'e', '1' ).getPiece() );
        assertSame( whiteRook, boardBeforeCastling.getSquare( 'h', '1' ).getPiece() );
        assertTrue( boardBeforeCastling.getSquare( 'f', '1' ).isEmpty() );
        assertTrue( boardBeforeCastling.getSquare( 'g', '1' ).isEmpty() );
        assertTrue( boardBeforeCastling.isWhiteKingsideCastlingAllowed() );
        assertTrue( boardBeforeCastling.isWhiteQueensideCastlingAllowed() );

        assertTrue( boardAfterCastling.getSquare( 'e', '1' ).isEmpty() );
        assertTrue( boardAfterCastling.getSquare( 'h', '1' ).isEmpty() );
        assertSame( whiteKing, boardAfterCastling.getSquare( 'g', '1' ).getPiece() );
        assertSame( whiteRook, boardAfterCastling.getSquare( 'f', '1' ).getPiece() );
        assertFalse( boardAfterCastling.isWhiteKingsideCastlingAllowed() );
        assertFalse( boardAfterCastling.isWhiteQueensideCastlingAllowed() );
    }

    @Test
    void enPassant_doesNotMutatePreviousBoardState()
    {
        Board boardBeforeCapture = copyAndMove(
            createFullStartingBoard(),
            "e2e4", "a7a6",
            "e4e5", "d7d5" );

        Piece whitePawn = boardBeforeCapture.getSquare( 'e', '5' ).getPiece();
        Piece blackPawn = boardBeforeCapture.getSquare( 'd', '5' ).getPiece();

        assertSame( blackPawn, boardBeforeCapture.getEnPassantPawn() );

        Board boardAfterCapture = copyAndMove( boardBeforeCapture, "e5d6" );

        assertSame( whitePawn, boardBeforeCapture.getSquare( 'e', '5' ).getPiece() );
        assertSame( blackPawn, boardBeforeCapture.getSquare( 'd', '5' ).getPiece() );
        assertTrue( boardBeforeCapture.getSquare( 'd', '6' ).isEmpty() );
        assertSame( blackPawn, boardBeforeCapture.getEnPassantPawn() );

        assertTrue( boardAfterCapture.getSquare( 'e', '5' ).isEmpty() );
        assertTrue( boardAfterCapture.getSquare( 'd', '5' ).isEmpty() );
        assertSame( whitePawn, boardAfterCapture.getSquare( 'd', '6' ).getPiece() );
        assertNull( boardAfterCapture.getEnPassantPawn() );
        assertNull( blackPawn.getSquare( boardAfterCapture ) );
    }

    @Test
    void movingRook_revokesCastlingRightsOnNewBoardOnly()
    {
        Board boardBeforeMove = copyAndMove( createFullStartingBoard(), "h2h4", "a7a6" );

        assertTrue( boardBeforeMove.isWhiteKingsideCastlingAllowed() );
        assertTrue( boardBeforeMove.isWhiteQueensideCastlingAllowed() );

        Board boardAfterMove = copyAndMove( boardBeforeMove, "h1h3" );

        assertTrue( boardBeforeMove.isWhiteKingsideCastlingAllowed() );
        assertTrue( boardBeforeMove.isWhiteQueensideCastlingAllowed() );

        assertFalse( boardAfterMove.isWhiteKingsideCastlingAllowed() );
        assertTrue( boardAfterMove.isWhiteQueensideCastlingAllowed() );
        assertTrue( boardAfterMove.isBlackKingsideCastlingAllowed() );
        assertTrue( boardAfterMove.isBlackQueensideCastlingAllowed() );
    }

    @Test
    void makingMove_preservesPreviouslyRevokedCastlingRights()
    {
        Board boardBeforeMove = copyAndMove(
            createFullStartingBoard(),
            "h2h4", "a7a6",
            "h1h3", "a6a5" );

        assertFalse( boardBeforeMove.isWhiteKingsideCastlingAllowed() );
        assertTrue( boardBeforeMove.isWhiteQueensideCastlingAllowed() );

        Board boardAfterMove = copyAndMove( boardBeforeMove, "b2b3" );

        assertFalse( boardAfterMove.isWhiteKingsideCastlingAllowed() );
        assertTrue( boardAfterMove.isWhiteQueensideCastlingAllowed() );
        assertTrue( boardAfterMove.isBlackKingsideCastlingAllowed() );
        assertTrue( boardAfterMove.isBlackQueensideCastlingAllowed() );
    }
}
