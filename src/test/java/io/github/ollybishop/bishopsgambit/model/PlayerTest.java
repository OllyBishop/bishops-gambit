package io.github.ollybishop.bishopsgambit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.ollybishop.bishopsgambit.model.piece.Piece;

class PlayerTest
{
    private Player white;
    private Player black;

    @BeforeEach
    void createPlayers()
    {
        white = new Player( Player.Colour.WHITE );
        black = new Player( Player.Colour.BLACK );
    }

    @Test
    void newPlayer_hasSixteenPiecesOwnedByPlayer()
    {
        assertEquals( 16, white.getPieces().size() );
        assertTrue( white.getPieces().stream().allMatch( piece -> piece.getPlayer() == white ) );

        assertEquals( 16, black.getPieces().size() );
        assertTrue( black.getPieces().stream().allMatch( piece -> piece.getPlayer() == black ) );
    }

    @Test
    void getPieces_includesCapturedPieces()
    {
        Board boardBeforeCapture = Board.createStartingBoard(
            Set.of( white.getQueensideRook(), black.getQueensideRook() ) );

        assertEquals( 2, boardBeforeCapture.getPieces().size() );
        assertNotNull( black.getQueensideRook().getSquare( boardBeforeCapture ) );

        Board boardAfterCapture = boardBeforeCapture.copyAndMove(
            boardBeforeCapture.getSquare( 'a', '1' ),
            boardBeforeCapture.getSquare( 'a', '8' ) );

        // Check Black's queenside rook is no longer on the board
        assertEquals( 1, boardAfterCapture.getPieces().size() );
        assertNull( black.getQueensideRook().getSquare( boardAfterCapture ) );

        // Check Black's queenside rook is still registered to Black despite being captured
        assertEquals( 16, black.getPieces().size() );
        assertTrue( black.getPieces().contains( black.getQueensideRook() ) );
    }

    @Test
    void getPieces_returnsUnmodifiableSet()
    {
        assertThrows(
            UnsupportedOperationException.class,
            white.getPieces()::clear );
    }

    @Test
    void getPieces_returnsLiveView()
    {
        Set<Piece> piecesView = white.getPieces();

        // Piece instantiation registers the new piece with its player
        Piece newPiece = Piece.newInstance( Piece.Type.PAWN, white, 'e', '4' );

        // Check the previously obtained view includes the newly registered piece
        assertEquals( 17, piecesView.size() );
        assertTrue( piecesView.contains( newPiece ) );
    }

    @Test
    void registerPiece_nullPiece_throwsNullPointerException()
    {
        assertThrows(
            NullPointerException.class,
            () -> white.registerPiece( null ) );

        // Check the rejected registration left White's piece count unchanged
        assertEquals( 16, white.getPieces().size() );
    }

    @Test
    void registerPiece_pieceOwnedByAnotherPlayer_throwsIllegalArgumentException()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> white.registerPiece( black.getKing() ) );

        // Check the rejected registration did not add Black's king to White's pieces
        assertEquals( 16, white.getPieces().size() );
        assertFalse( white.getPieces().contains( black.getKing() ) );
    }
}
