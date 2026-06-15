package io.github.ollybishop.bishopsgambit.model.piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import io.github.ollybishop.bishopsgambit.game.Game;
import io.github.ollybishop.bishopsgambit.testutil.SquareHelper;

class PieceTest
{
    private Game game;

    // ============================================================================================

    private final SquareHelper a1 = new SquareHelper( () -> game, 'a', '1' );
    private final SquareHelper a2 = new SquareHelper( () -> game, 'a', '2' );
    private final SquareHelper a3 = new SquareHelper( () -> game, 'a', '3' );
    private final SquareHelper a4 = new SquareHelper( () -> game, 'a', '4' );
    private final SquareHelper a5 = new SquareHelper( () -> game, 'a', '5' );
    private final SquareHelper a6 = new SquareHelper( () -> game, 'a', '6' );
    private final SquareHelper a7 = new SquareHelper( () -> game, 'a', '7' );
    private final SquareHelper a8 = new SquareHelper( () -> game, 'a', '8' );

    private final SquareHelper b1 = new SquareHelper( () -> game, 'b', '1' );
    private final SquareHelper b2 = new SquareHelper( () -> game, 'b', '2' );
    private final SquareHelper b3 = new SquareHelper( () -> game, 'b', '3' );
    private final SquareHelper b6 = new SquareHelper( () -> game, 'b', '6' );
    private final SquareHelper b7 = new SquareHelper( () -> game, 'b', '7' );
    private final SquareHelper b8 = new SquareHelper( () -> game, 'b', '8' );

    private final SquareHelper c1 = new SquareHelper( () -> game, 'c', '1' );
    private final SquareHelper c2 = new SquareHelper( () -> game, 'c', '2' );
    private final SquareHelper c3 = new SquareHelper( () -> game, 'c', '3' );
    private final SquareHelper c6 = new SquareHelper( () -> game, 'c', '6' );
    private final SquareHelper c7 = new SquareHelper( () -> game, 'c', '7' );
    private final SquareHelper c8 = new SquareHelper( () -> game, 'c', '8' );

    private final SquareHelper d1 = new SquareHelper( () -> game, 'd', '1' );
    private final SquareHelper d2 = new SquareHelper( () -> game, 'd', '2' );
    private final SquareHelper d3 = new SquareHelper( () -> game, 'd', '3' );
    private final SquareHelper d6 = new SquareHelper( () -> game, 'd', '6' );
    private final SquareHelper d7 = new SquareHelper( () -> game, 'd', '7' );
    private final SquareHelper d8 = new SquareHelper( () -> game, 'd', '8' );

    private final SquareHelper e1 = new SquareHelper( () -> game, 'e', '1' );
    private final SquareHelper e2 = new SquareHelper( () -> game, 'e', '2' );
    private final SquareHelper e3 = new SquareHelper( () -> game, 'e', '3' );
    private final SquareHelper e4 = new SquareHelper( () -> game, 'e', '4' );
    private final SquareHelper e5 = new SquareHelper( () -> game, 'e', '5' );
    private final SquareHelper e6 = new SquareHelper( () -> game, 'e', '6' );
    private final SquareHelper e7 = new SquareHelper( () -> game, 'e', '7' );
    private final SquareHelper e8 = new SquareHelper( () -> game, 'e', '8' );

    private final SquareHelper f1 = new SquareHelper( () -> game, 'f', '1' );
    private final SquareHelper f2 = new SquareHelper( () -> game, 'f', '2' );
    private final SquareHelper f3 = new SquareHelper( () -> game, 'f', '3' );
    private final SquareHelper f6 = new SquareHelper( () -> game, 'f', '6' );
    private final SquareHelper f7 = new SquareHelper( () -> game, 'f', '7' );
    private final SquareHelper f8 = new SquareHelper( () -> game, 'f', '8' );

    private final SquareHelper g1 = new SquareHelper( () -> game, 'g', '1' );
    private final SquareHelper g2 = new SquareHelper( () -> game, 'g', '2' );
    private final SquareHelper g3 = new SquareHelper( () -> game, 'g', '3' );
    private final SquareHelper g6 = new SquareHelper( () -> game, 'g', '6' );
    private final SquareHelper g7 = new SquareHelper( () -> game, 'g', '7' );
    private final SquareHelper g8 = new SquareHelper( () -> game, 'g', '8' );

    private final SquareHelper h1 = new SquareHelper( () -> game, 'h', '1' );
    private final SquareHelper h2 = new SquareHelper( () -> game, 'h', '2' );
    private final SquareHelper h3 = new SquareHelper( () -> game, 'h', '3' );
    private final SquareHelper h4 = new SquareHelper( () -> game, 'h', '4' );
    private final SquareHelper h5 = new SquareHelper( () -> game, 'h', '5' );
    private final SquareHelper h6 = new SquareHelper( () -> game, 'h', '6' );
    private final SquareHelper h7 = new SquareHelper( () -> game, 'h', '7' );
    private final SquareHelper h8 = new SquareHelper( () -> game, 'h', '8' );

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
    // Controlled Squares
    // ============================================================================================

    // Pawns

    @Test
    void initialSetup_whiteAPawn_controlsRightDiagonalOnly()
    {
        PieceHelper whiteAPawn = a2.getPieceHelper();

        assertEquals( 1, whiteAPawn.getControlledSquares().size() );

        assertFalse( whiteAPawn.controls( a3 ) );
        assertFalse( whiteAPawn.controls( a4 ) );
        assertTrue( whiteAPawn.controls( b3 ) );
    }

    @Test
    void initialSetup_whiteEPawn_controlsDiagonalsOnly()
    {
        PieceHelper whiteEPawn = e2.getPieceHelper();

        assertEquals( 2, whiteEPawn.getControlledSquares().size() );

        assertTrue( whiteEPawn.controls( d3 ) );
        assertFalse( whiteEPawn.controls( e3 ) );
        assertFalse( whiteEPawn.controls( e4 ) );
        assertTrue( whiteEPawn.controls( f3 ) );
    }

    @Test
    void initialSetup_whiteHPawn_controlsDiagonalsOnly()
    {
        PieceHelper whiteHPawn = h2.getPieceHelper();

        assertEquals( 1, whiteHPawn.getControlledSquares().size() );

        assertTrue( whiteHPawn.controls( g3 ) );
        assertFalse( whiteHPawn.controls( h3 ) );
        assertFalse( whiteHPawn.controls( h4 ) );
    }

    @Test
    void initialSetup_blackAPawn_controlsRightDiagonalOnly()
    {
        PieceHelper blackAPawn = a7.getPieceHelper();

        assertEquals( 1, blackAPawn.getControlledSquares().size() );

        assertFalse( blackAPawn.controls( a6 ) );
        assertFalse( blackAPawn.controls( a5 ) );
        assertTrue( blackAPawn.controls( b6 ) );
    }

    @Test
    void initialSetup_blackEPawn_controlsDiagonalsOnly()
    {
        PieceHelper blackEPawn = e7.getPieceHelper();

        assertEquals( 2, blackEPawn.getControlledSquares().size() );

        assertTrue( blackEPawn.controls( d6 ) );
        assertFalse( blackEPawn.controls( e6 ) );
        assertFalse( blackEPawn.controls( e5 ) );
        assertTrue( blackEPawn.controls( f6 ) );
    }

    @Test
    void initialSetup_blackHPawn_controlsDiagonalsOnly()
    {
        PieceHelper blackHPawn = h7.getPieceHelper();

        assertEquals( 1, blackHPawn.getControlledSquares().size() );

        assertTrue( blackHPawn.controls( g6 ) );
        assertFalse( blackHPawn.controls( h6 ) );
        assertFalse( blackHPawn.controls( h5 ) );
    }

    // Knights

    @Test
    void initialSetup_whiteQueensideKnight_controlledSquares()
    {
        PieceHelper whiteQueensideKnight = b1.getPieceHelper();

        assertEquals( 3, whiteQueensideKnight.getControlledSquares().size() );

        assertTrue( whiteQueensideKnight.controls( a3 ) );
        assertTrue( whiteQueensideKnight.controls( c3 ) );
        assertTrue( whiteQueensideKnight.controls( d2 ) );
    }

    @Test
    void initialSetup_whiteKingsideKnight_controlledSquares()
    {
        PieceHelper whiteKingsideKnight = g1.getPieceHelper();

        assertEquals( 3, whiteKingsideKnight.getControlledSquares().size() );

        assertTrue( whiteKingsideKnight.controls( e2 ) );
        assertTrue( whiteKingsideKnight.controls( f3 ) );
        assertTrue( whiteKingsideKnight.controls( h3 ) );
    }

    @Test
    void initialSetup_blackQueensideKnight_controlledSquares()
    {
        PieceHelper blackQueensideKnight = b8.getPieceHelper();

        assertEquals( 3, blackQueensideKnight.getControlledSquares().size() );

        assertTrue( blackQueensideKnight.controls( a6 ) );
        assertTrue( blackQueensideKnight.controls( c6 ) );
        assertTrue( blackQueensideKnight.controls( d7 ) );
    }

    @Test
    void initialSetup_blackKingsideKnight_controlledSquares()
    {
        PieceHelper blackKingsideKnight = g8.getPieceHelper();

        assertEquals( 3, blackKingsideKnight.getControlledSquares().size() );

        assertTrue( blackKingsideKnight.controls( e7 ) );
        assertTrue( blackKingsideKnight.controls( f6 ) );
        assertTrue( blackKingsideKnight.controls( h6 ) );
    }

    // Bishops

    @Test
    void initialSetup_whiteQueensideBishop_controlledSquares()
    {
        PieceHelper whiteQueensideBishop = c1.getPieceHelper();

        assertEquals( 2, whiteQueensideBishop.getControlledSquares().size() );

        assertTrue( whiteQueensideBishop.controls( b2 ) );
        assertTrue( whiteQueensideBishop.controls( d2 ) );
    }

    @Test
    void initialSetup_whiteKingsideBishop_controlledSquares()
    {
        PieceHelper whiteKingsideBishop = f1.getPieceHelper();

        assertEquals( 2, whiteKingsideBishop.getControlledSquares().size() );

        assertTrue( whiteKingsideBishop.controls( e2 ) );
        assertTrue( whiteKingsideBishop.controls( g2 ) );
    }

    @Test
    void initialSetup_blackQueensideBishop_controlledSquares()
    {
        PieceHelper blackQueensideBishop = c8.getPieceHelper();

        assertEquals( 2, blackQueensideBishop.getControlledSquares().size() );

        assertTrue( blackQueensideBishop.controls( b7 ) );
        assertTrue( blackQueensideBishop.controls( d7 ) );
    }

    @Test
    void initialSetup_blackKingsideBishop_controlledSquares()
    {
        PieceHelper blackKingsideBishop = f8.getPieceHelper();

        assertEquals( 2, blackKingsideBishop.getControlledSquares().size() );

        assertTrue( blackKingsideBishop.controls( e7 ) );
        assertTrue( blackKingsideBishop.controls( g7 ) );
    }

    // Rooks

    @Test
    void initialSetup_whiteQueensideRook_controlledSquares()
    {
        PieceHelper whiteQueensideRook = a1.getPieceHelper();

        assertEquals( 2, whiteQueensideRook.getControlledSquares().size() );

        assertTrue( whiteQueensideRook.controls( a2 ) );
        assertTrue( whiteQueensideRook.controls( b1 ) );
    }

    @Test
    void initialSetup_whiteKingsideRook_controlledSquares()
    {
        PieceHelper whiteKingsideRook = h1.getPieceHelper();

        assertEquals( 2, whiteKingsideRook.getControlledSquares().size() );

        assertTrue( whiteKingsideRook.controls( g1 ) );
        assertTrue( whiteKingsideRook.controls( h2 ) );
    }

    @Test
    void initialSetup_blackQueensideRook_controlledSquares()
    {
        PieceHelper blackQueensideRook = a8.getPieceHelper();

        assertEquals( 2, blackQueensideRook.getControlledSquares().size() );

        assertTrue( blackQueensideRook.controls( a7 ) );
        assertTrue( blackQueensideRook.controls( b8 ) );
    }

    @Test
    void initialSetup_blackKingsideRook_controlledSquares()
    {
        PieceHelper blackKingsideRook = h8.getPieceHelper();

        assertEquals( 2, blackKingsideRook.getControlledSquares().size() );

        assertTrue( blackKingsideRook.controls( g8 ) );
        assertTrue( blackKingsideRook.controls( h7 ) );
    }

    // Queens

    @Test
    void initialSetup_whiteQueen_controlledSquares()
    {
        PieceHelper whiteQueen = d1.getPieceHelper();

        assertEquals( 5, whiteQueen.getControlledSquares().size() );

        assertTrue( whiteQueen.controls( c1 ) );
        assertTrue( whiteQueen.controls( c2 ) );
        assertTrue( whiteQueen.controls( d2 ) );
        assertTrue( whiteQueen.controls( e1 ) );
        assertTrue( whiteQueen.controls( e2 ) );
    }

    @Test
    void initialSetup_blackQueen_controlledSquares()
    {
        PieceHelper blackQueen = d8.getPieceHelper();

        assertEquals( 5, blackQueen.getControlledSquares().size() );

        assertTrue( blackQueen.controls( c8 ) );
        assertTrue( blackQueen.controls( c7 ) );
        assertTrue( blackQueen.controls( d7 ) );
        assertTrue( blackQueen.controls( e8 ) );
        assertTrue( blackQueen.controls( e7 ) );
    }

    // Kings

    @Test
    void initialSetup_whiteKing_controlledSquares()
    {
        PieceHelper whiteKing = e1.getPieceHelper();

        assertEquals( 5, whiteKing.getControlledSquares().size() );

        assertTrue( whiteKing.controls( d1 ) );
        assertTrue( whiteKing.controls( d2 ) );
        assertTrue( whiteKing.controls( e2 ) );
        assertTrue( whiteKing.controls( f1 ) );
        assertTrue( whiteKing.controls( f2 ) );
    }

    @Test
    void initialSetup_blackKing_controlledSquares()
    {
        PieceHelper blackKing = e8.getPieceHelper();

        assertEquals( 5, blackKing.getControlledSquares().size() );

        assertTrue( blackKing.controls( d8 ) );
        assertTrue( blackKing.controls( d7 ) );
        assertTrue( blackKing.controls( e7 ) );
        assertTrue( blackKing.controls( f8 ) );
        assertTrue( blackKing.controls( f7 ) );
    }
}
