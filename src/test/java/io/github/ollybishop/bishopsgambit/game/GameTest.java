package io.github.ollybishop.bishopsgambit.game;

import static io.github.ollybishop.bishopsgambit.testutil.TestAssertions.assertSameNotNull;
import static io.github.ollybishop.bishopsgambit.testutil.TestAssertions.assertThrowsWithMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;
import io.github.ollybishop.bishopsgambit.testutil.SquareHelper;

class GameTest
{
    private Game game;

    // ============================================================================================

    private final SquareHelper a1 = new SquareHelper( () -> game, 'a', '1' );
    private final SquareHelper a8 = new SquareHelper( () -> game, 'a', '8' );

    private final SquareHelper b7 = new SquareHelper( () -> game, 'b', '7' );

    private final SquareHelper c1 = new SquareHelper( () -> game, 'c', '1' );
    private final SquareHelper c8 = new SquareHelper( () -> game, 'c', '8' );

    private final SquareHelper d1 = new SquareHelper( () -> game, 'd', '1' );
    private final SquareHelper d8 = new SquareHelper( () -> game, 'd', '8' );

    private final SquareHelper e1 = new SquareHelper( () -> game, 'e', '1' );
    private final SquareHelper e7 = new SquareHelper( () -> game, 'e', '7' );
    private final SquareHelper e8 = new SquareHelper( () -> game, 'e', '8' );

    private final SquareHelper f1 = new SquareHelper( () -> game, 'f', '1' );
    private final SquareHelper f8 = new SquareHelper( () -> game, 'f', '8' );

    private final SquareHelper g1 = new SquareHelper( () -> game, 'g', '1' );
    private final SquareHelper g8 = new SquareHelper( () -> game, 'g', '8' );

    private final SquareHelper h1 = new SquareHelper( () -> game, 'h', '1' );
    private final SquareHelper h7 = new SquareHelper( () -> game, 'h', '7' );
    private final SquareHelper h8 = new SquareHelper( () -> game, 'h', '8' );

    // ============================================================================================

    private void makeMove( String uci )
    {
        game.makeMove( uci );
    }

    private void makeMove( String uci1, String uci2 )
    {
        makeMove( uci1 );
        makeMove( uci2 );
    }

    private void makeMove( SquareHelper from, SquareHelper to, Piece.Type newType )
    {
        game.makeMove( from.getSquare(), to.getSquare(), newType );
    }

    // ============================================================================================

    private int getNumberOfPliesPlayed()
    {
        return game.getNumberOfPliesPlayed();
    }

    private int getNumberOfLegalMoves()
    {
        return game.getActivePlayer().getNumberOfLegalMoves( game.getActiveBoard() );
    }

    private int getMaterialDifference()
    {
        return game.getActiveBoard().getMaterialDifference();
    }

    // ============================================================================================

    private boolean isInCheck()
    {
        return game.getActivePlayer().isInCheck( game.getActiveBoard() );
    }

    private boolean isCheckmated()
    {
        return game.getActivePlayer().isCheckmated( game.getActiveBoard() );
    }

    private boolean isStalemated()
    {
        return game.getActivePlayer().isStalemated( game.getActiveBoard() );
    }

    private boolean hasInsufficientMaterial()
    {
        return game.getActiveBoard().hasInsufficientMaterial();
    }

    private Game.Status getStatus()
    {
        return game.getStatus();
    }

    // ============================================================================================

    private boolean isWhiteQueensideCastlingAllowed()
    {
        return game.getActiveBoard().isWhiteQueensideCastlingAllowed();
    }

    private boolean isWhiteKingsideCastlingAllowed()
    {
        return game.getActiveBoard().isWhiteKingsideCastlingAllowed();
    }

    private boolean isBlackQueensideCastlingAllowed()
    {
        return game.getActiveBoard().isBlackQueensideCastlingAllowed();
    }

    private boolean isBlackKingsideCastlingAllowed()
    {
        return game.getActiveBoard().isBlackKingsideCastlingAllowed();
    }

    // ============================================================================================

    @BeforeEach
    void printName( TestInfo testInfo )
    {
        System.out.println( "Running test " + testInfo.getDisplayName() + "..." );
    }

    @BeforeEach
    void newGame()
    {
        game = new Game();
    }

    @AfterEach
    void printBoard()
    {
        game.getActiveBoard().print();
        System.out.println();
    }

    // ============================================================================================
    // Invalid Moves
    // ============================================================================================

    @Test
    void fromSquareUnoccupied_unoccupiedSquareException()
    {
        assertThrowsWithMessage( UnoccupiedSquareException.class,
                                 () -> makeMove( "e3e4" ),
                                 "Cannot make a move from the unoccupied square e3." );
    }

    @Test
    void toSquareOccupied_byFriendlyPiece_illegalMoveException()
    {
        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1e2" ),
                                 "The White King occupying e1 cannot legally move to e2." );
    }

    // ============================================================================================
    // Checkmate
    // ============================================================================================

    @Test
    void checkmate_foolsMate()
    {
        makeMove( "f2f3", "e7e5" );
        makeMove( "g2g4", "d8h4" );

        assertEquals( 4, getNumberOfPliesPlayed() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertTrue( isInCheck() );
        assertTrue( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.CHECKMATE );
    }

    @Test
    void checkmate_scholarsMate()
    {
        makeMove( "e2e4", "e7e5" );
        makeMove( "d1h5", "b8c6" );
        makeMove( "f1c4", "g8f6" );
        makeMove( "h5f7" );

        assertEquals( 7, getNumberOfPliesPlayed() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 1, getMaterialDifference() );

        assertTrue( isInCheck() );
        assertTrue( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.CHECKMATE );
    }

    // ============================================================================================
    // Castling
    // ============================================================================================

    @Test
    void kingsideCastling()
    {
        Piece whiteKing = e1.getPiece();
        Piece blackKing = e8.getPiece();
        Piece whiteKingsideRook = h1.getPiece();
        Piece blackKingsideRook = h8.getPiece();

        makeMove( "e2e4", "e7e5" );
        makeMove( "g1f3", "g8f6" );
        makeMove( "f1c4", "f8c5" );
        makeMove( "e1g1", "e8g8" );

        assertEquals( 8, getNumberOfPliesPlayed() );
        assertEquals( 30, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.DEFAULT );

        assertSameNotNull( whiteKing, g1.getPiece() );
        assertSameNotNull( blackKing, g8.getPiece() );
        assertSameNotNull( whiteKingsideRook, f1.getPiece() );
        assertSameNotNull( blackKingsideRook, f8.getPiece() );
    }

    @Test
    void queensideCastling()
    {
        Piece whiteKing = e1.getPiece();
        Piece blackKing = e8.getPiece();
        Piece whiteQueensideRook = a1.getPiece();
        Piece blackQueensideRook = a8.getPiece();

        makeMove( "d2d4", "d7d5" );
        makeMove( "b1c3", "b8c6" );
        makeMove( "c1f4", "c8f5" );
        makeMove( "d1d2", "d8d7" );
        makeMove( "e1c1", "e8c8" );

        assertEquals( 10, getNumberOfPliesPlayed() );
        assertEquals( 30, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.DEFAULT );

        assertSameNotNull( whiteKing, c1.getPiece() );
        assertSameNotNull( blackKing, c8.getPiece() );
        assertSameNotNull( whiteQueensideRook, d1.getPiece() );
        assertSameNotNull( blackQueensideRook, d8.getPiece() );
    }

    @Test
    void kingsideCastling_fSquareOccupied_byEnemyKnight_illegalMoveException()
    {
        makeMove( "e2e4", "g8f6" );
        makeMove( "g1f3", "f6g4" );
        makeMove( "f1c4", "g4e3" );
        makeMove( "d2d3", "e3f1" );

        Piece blackKnight = f1.getPiece();
        assertEquals( blackKnight.getColour(), Player.Colour.BLACK );
        assertEquals( blackKnight.getType(), Piece.Type.KNIGHT );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1g1" ),
                                 "The White King occupying e1 cannot legally move to g1." );

        assertEquals( 8, getNumberOfPliesPlayed() );
        assertEquals( 37, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.DEFAULT );
    }

    @Test
    void kingsideCastling_fSquareOccupied_byEnemyBishop_illegalMoveException()
    {
        makeMove( "e2e4", "b7b6" );
        makeMove( "g1f3", "c8a6" );
        makeMove( "d2d4", "a6f1" );

        Piece blackBishop = f1.getPiece();
        assertEquals( blackBishop.getColour(), Player.Colour.BLACK );
        assertEquals( blackBishop.getType(), Piece.Type.BISHOP );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1g1" ),
                                 "The White King occupying e1 cannot legally move to g1." );

        assertEquals( 6, getNumberOfPliesPlayed() );
        assertEquals( 32, getNumberOfLegalMoves() );
        assertEquals( -3, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.DEFAULT );
    }

    @Test
    void kingsideCastling_fSquareControlled_byEnemyPawn_illegalMoveException()
    {
        makeMove( "d2d4", "g8f6" );
        makeMove( "d4d5", "e7e5" );
        makeMove( "d5e6", "f8c5" );
        makeMove( "e6e7" );

        Piece whiteDPawn = e7.getPiece();
        assertEquals( whiteDPawn.getColour(), Player.Colour.WHITE );
        assertEquals( whiteDPawn.getType(), Piece.Type.PAWN );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e8g8" ),
                                 "The Black King occupying e8 cannot legally move to g8." );
    }

    @Test
    void kingsideCastling_gSquareControlled_byEnemyPawn_illegalMoveException()
    {
        makeMove( "g2g4", "g8f6" );
        makeMove( "g4g5", "e7e5" );
        makeMove( "g5g6", "f8c5" );
        makeMove( "g6h7" );

        Piece whiteGPawn = h7.getPiece();
        assertEquals( whiteGPawn.getColour(), Player.Colour.WHITE );
        assertEquals( whiteGPawn.getType(), Piece.Type.PAWN );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e8g8" ),
                                 "The Black King occupying e8 cannot legally move to g8." );
    }

    // ============================================================================================
    // En Passant
    // ============================================================================================

    @Test
    void enPassant_illegalMoveException()
    {
        makeMove( "d2d4", "e7e6" );
        makeMove( "d4d5", "e6e5" );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "d5e6" ),
                                 "The White Pawn occupying d5 cannot legally move to e6." );

        assertEquals( 4, getNumberOfPliesPlayed() );
        assertEquals( 29, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.DEFAULT );
    }

    @Test
    void enPassant_onlyLegalMove_inCheck()
    {
        makeMove( "e2e4", "e7e6" );
        makeMove( "e4e5", "d8h4" );
        makeMove( "e1e2", "b8c6" );
        makeMove( "e2e3", "h4g3" );
        makeMove( "e3e4", "d7d5" );

        assertEquals( 10, getNumberOfPliesPlayed() );
        assertEquals( 1, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertTrue( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.CHECK );

        assertDoesNotThrow( () -> makeMove( "e5d6" ) );
    }

    // ============================================================================================
    // Stalemate
    // ============================================================================================

    @Test
    void stalemate_nineteenPlies()
    {
        makeMove( "e2e3", "a7a5" );
        makeMove( "d1h5", "a8a6" );
        makeMove( "h5a5", "h7h5" );
        makeMove( "h2h4", "a6h6" );
        makeMove( "a5c7", "f7f6" );
        makeMove( "c7d7", "e8f7" );
        makeMove( "d7b7", "d8d3" );
        makeMove( "b7b8", "d3h7" );
        makeMove( "b8c8", "f7g6" );
        makeMove( "c8e6" );

        assertEquals( 19, getNumberOfPliesPlayed() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 10, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertTrue( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.STALEMATE );
    }

    @Test
    void stalemate_twentyFourPlies_noCaptures()
    {
        makeMove( "d2d4", "e7e5" ); // 1. d4, d5
        makeMove( "d1d2", "e5e4" ); // 2. Qd2, e4
        makeMove( "a2a4", "a7a5" ); // 3. a4, a5
        makeMove( "d2f4", "f7f5" ); // 4. Qf4, f5
        makeMove( "h2h3", "d8h4" ); // 5. h3, Qh4
        makeMove( "f4h2", "f8b4" ); // 6. Qh2, Bb4+
        makeMove( "b1d2", "d7d6" ); // 7. Nd2, d6
        makeMove( "a1a3", "c8e6" ); // 8. Ra3, Be6
        makeMove( "a3g3", "e6b3" ); // 9. Rg3, Bb3
        makeMove( "c2c4", "c7c5" ); // 10. c4, c5
        makeMove( "f2f3", "f5f4" ); // 11. f3, f4
        makeMove( "d4d5", "e4e3" ); // 12. d5, e3

        assertEquals( 24, getNumberOfPliesPlayed() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertTrue( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.STALEMATE );
    }

    // ============================================================================================
    // Insufficient Material
    // ============================================================================================

    @Test
    void insufficientMaterial_kingVersusKing()
    {
        makeMove( "e2e4", "d7d5" );
        makeMove( "e4d5", "d8d5" );
        makeMove( "f1d3", "d5a2" );
        makeMove( "d3h7", "a2b1" );
        makeMove( "h7g8", "b1c2" );
        makeMove( "g8f7", "e8f7" );
        makeMove( "a1a7", "c2c1" );
        makeMove( "a7b7", "h8h2" );
        makeMove( "b7b8", "h2g2" );
        makeMove( "d1c1", "g2g1" );
        makeMove( "h1g1", "a8b8" );
        makeMove( "c1c7", "b8b2" );
        makeMove( "c7c8", "b2d2" );
        makeMove( "c8f8", "f7f8" );
        makeMove( "g1g7", "d2f2" );
        makeMove( "g7e7", "f8e7" );
        makeMove( "e1f2" );

        assertEquals( 33, getNumberOfPliesPlayed() );
        assertEquals( 8, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertTrue( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.INSUFFICIENT_MATERIAL );
    }

    @Test
    void insufficientMaterial_kingAndKnightVersusKing()
    {
        makeMove( "e2e4", "d7d5" );
        makeMove( "e4d5", "d8d5" );
        makeMove( "f1d3", "d5a2" );
        makeMove( "d3h7", "a2b1" );
        makeMove( "h7g8", "b1c2" );
        makeMove( "g8f7", "e8f7" );
        makeMove( "a1a7", "c2c1" );
        makeMove( "a7b7", "h8h2" );
        makeMove( "b7b8", "h2g2" );
        makeMove( "d1c1", "a8b8" );
        makeMove( "c1c7", "b8b2" );
        makeMove( "c7c8", "b2d2" );
        makeMove( "c8f8", "f7e6" );
        makeMove( "h1h7", "d2f2" );
        makeMove( "f8f2", "g2f2" );
        makeMove( "h7g7", "f2f3" );
        makeMove( "g7e7", "e6e7" );
        makeMove( "g1f3" );

        assertEquals( 35, getNumberOfPliesPlayed() );
        assertEquals( 8, getNumberOfLegalMoves() );
        assertEquals( 3, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertTrue( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.INSUFFICIENT_MATERIAL );
    }

    @Test
    void insufficientMaterial_kingAndBishopVersusKing()
    {
        makeMove( "e2e4", "d7d5" );
        makeMove( "e4d5", "d8d5" );
        makeMove( "f1d3", "d5a2" );
        makeMove( "d3h7", "a2b1" );
        makeMove( "h7g8", "b1c2" );
        makeMove( "g8f7", "e8f7" );
        makeMove( "a1a7", "c2c1" );
        makeMove( "a7b7", "h8h2" );
        makeMove( "b7b8", "h2g2" );
        makeMove( "d1c1", "g2g1" );
        makeMove( "h1g1", "a8b8" );
        makeMove( "c1c7", "b8b2" );
        makeMove( "c7c8", "b2d2" );
        makeMove( "g1g7", "f7g7" );
        makeMove( "c8d7", "d2f2" );
        makeMove( "d7e7", "f8e7" );
        makeMove( "e1f2" );

        assertEquals( 33, getNumberOfPliesPlayed() );
        assertEquals( 17, getNumberOfLegalMoves() );
        assertEquals( -3, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertTrue( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.INSUFFICIENT_MATERIAL );
    }

    @Test
    void insufficientMaterial_kingAndBishopVersusKingAndBishop_bishopsOnSameColour()
    {
        makeMove( "e2e4", "d7d5" );
        makeMove( "e4d5", "d8d5" );
        makeMove( "f1d3", "d5a2" );
        makeMove( "d3h7", "a2b1" );
        makeMove( "h7g8", "b1c2" );
        makeMove( "g8f7", "e8f7" );
        makeMove( "a1a7", "h8h2" );
        makeMove( "a7b7", "h2g2" );
        makeMove( "b7b8", "g2g1" );
        makeMove( "h1g1", "a8b8" );
        makeMove( "d1c2", "b8b2" );
        makeMove( "c2c7", "b2d2" );
        makeMove( "c7c8", "d2f2" );
        makeMove( "g1g7", "f7g7" );
        makeMove( "e1f2", "g7f6" );
        makeMove( "c8d7", "f6e5" );
        makeMove( "d7e7", "f8e7" );

        assertEquals( 34, getNumberOfPliesPlayed() );
        assertEquals( 15, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertTrue( hasInsufficientMaterial() );
        assertEquals( getStatus(), Game.Status.INSUFFICIENT_MATERIAL );
    }

    // ============================================================================================
    // Revoke Castling Rights
    // ============================================================================================

    @Test
    void revokeCastlingRights_byMovingQueensideRook()
    {
        makeMove( "b1c3", "b8c6" );
        makeMove( "a1b1", "a8b8" );

        assertFalse( isWhiteQueensideCastlingAllowed() );
        assertTrue( isWhiteKingsideCastlingAllowed() );
        assertFalse( isBlackQueensideCastlingAllowed() );
        assertTrue( isBlackKingsideCastlingAllowed() );
    }

    @Test
    void revokeCastlingRights_byMovingKingsideRook()
    {
        makeMove( "g1f3", "g8f6" );
        makeMove( "h1g1", "h8g8" );

        assertTrue( isWhiteQueensideCastlingAllowed() );
        assertFalse( isWhiteKingsideCastlingAllowed() );
        assertTrue( isBlackQueensideCastlingAllowed() );
        assertFalse( isBlackKingsideCastlingAllowed() );
    }

    @Test
    void revokeCastlingRights_byMovingKing()
    {
        makeMove( "e2e4", "e7e5" );
        makeMove( "e1e2", "e8e7" );

        assertFalse( isWhiteQueensideCastlingAllowed() );
        assertFalse( isWhiteKingsideCastlingAllowed() );
        assertFalse( isBlackQueensideCastlingAllowed() );
        assertFalse( isBlackKingsideCastlingAllowed() );
    }

    @Test
    void revokeCastlingRights_byCapturingQueensideRook()
    {
        makeMove( "g2g3", "g7g6" );
        makeMove( "f1g2", "f8g7" );
        makeMove( "b2b3", "b7b6" );
        makeMove( "g2a8", "g7a1" );

        assertFalse( isWhiteQueensideCastlingAllowed() );
        assertTrue( isWhiteKingsideCastlingAllowed() );
        assertFalse( isBlackQueensideCastlingAllowed() );
        assertTrue( isBlackKingsideCastlingAllowed() );
    }

    @Test
    void revokeCastlingRights_byCapturingKingsideRook()
    {
        makeMove( "b2b3", "b7b6" );
        makeMove( "c1b2", "c8b7" );
        makeMove( "g2g3", "g7g6" );
        makeMove( "b2h8", "b7h1" );

        assertTrue( isWhiteQueensideCastlingAllowed() );
        assertFalse( isWhiteKingsideCastlingAllowed() );
        assertTrue( isBlackQueensideCastlingAllowed() );
        assertFalse( isBlackKingsideCastlingAllowed() );
    }

    // ============================================================================================
    // Pawn Promotion
    // ============================================================================================

    @Test
    void pawnPromotion_newPieceTypeQueen()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8q", "g2h1q" );

        Piece whiteQueen = a8.getPiece();
        assertEquals( whiteQueen.getColour(), Player.Colour.WHITE );
        assertEquals( whiteQueen.getType(), Piece.Type.QUEEN );

        Piece blackQueen = h1.getPiece();
        assertEquals( blackQueen.getColour(), Player.Colour.BLACK );
        assertEquals( blackQueen.getType(), Piece.Type.QUEEN );
    }

    @Test
    void pawnPromotion_newPieceTypeRook()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8r", "g2h1r" );

        Piece whiteRook = a8.getPiece();
        assertEquals( whiteRook.getColour(), Player.Colour.WHITE );
        assertEquals( whiteRook.getType(), Piece.Type.ROOK );

        Piece blackRook = h1.getPiece();
        assertEquals( blackRook.getColour(), Player.Colour.BLACK );
        assertEquals( blackRook.getType(), Piece.Type.ROOK );
    }

    @Test
    void pawnPromotion_newPieceTypeBishop()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8b", "g2h1b" );

        Piece whiteBishop = a8.getPiece();
        assertEquals( whiteBishop.getColour(), Player.Colour.WHITE );
        assertEquals( whiteBishop.getType(), Piece.Type.BISHOP );

        Piece blackBishop = h1.getPiece();
        assertEquals( blackBishop.getColour(), Player.Colour.BLACK );
        assertEquals( blackBishop.getType(), Piece.Type.BISHOP );
    }

    @Test
    void pawnPromotion_newPieceTypeKnight()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8n", "g2h1n" );

        Piece whiteKnight = a8.getPiece();
        assertEquals( whiteKnight.getColour(), Player.Colour.WHITE );
        assertEquals( whiteKnight.getType(), Piece.Type.KNIGHT );

        Piece blackKnight = h1.getPiece();
        assertEquals( blackKnight.getColour(), Player.Colour.BLACK );
        assertEquals( blackKnight.getType(), Piece.Type.KNIGHT );
    }

    @Test
    void pawnPromotion_newPieceTypePawn_invalidPromotionException()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );

        // Attempt to promote a pawn to a pawn
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( b7, a8, Piece.Type.PAWN ),
                                 "The new piece type (Pawn) must be one of Knight, Bishop, Rook or Queen." );
    }

    @Test
    void pawnPromotion_newPieceTypeKing_invalidPromotionException()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );

        // Attempt to promote a pawn to a king
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( b7, a8, Piece.Type.KING ),
                                 "The new piece type (King) must be one of Knight, Bishop, Rook or Queen." );
    }

    @Test
    void pawnPromotion_newPieceTypeNull_invalidPromotionException()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );

        // Attempt to move a pawn to the last rank without promoting
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( "b7a8" ),
                                 "Promotion is mandatory, but no new piece type was specified." );
    }

    @Test
    void pawnPromotion_promotingPieceNotAPawn_invalidPromotionException()
    {
        makeMove( "g2g3", "g7g6" );
        makeMove( "f1g2", "b7b6" );

        // Attempt to promote a bishop by moving it to the last rank
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( "g2a8q" ),
                                 "The promoting piece (White Bishop) must be a Pawn." );
    }

    @Test
    void pawnPromotion_promotionSquareNotOnLastRank_invalidPromotionException()
    {
        // Attempt to promote a pawn without moving it to the last rank
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( "c2c4q" ),
                                 "The promotion square (c4) must be on White's last rank." );
    }
}
