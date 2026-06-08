package io.github.ollybishop.bishopsgambit.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.github.ollybishop.bishopsgambit.board.Square;

@SuppressWarnings( "unused" )
@TestInstance( Lifecycle.PER_CLASS )
class ApplicationFrameTest
{
    private static final int UI_DELAY_MILLIS = 20;

    private final ApplicationFrame frame = new ApplicationFrame();

    private final SquareComp a1 = getSquareComp( 'a', '1' );
    private final SquareComp a2 = getSquareComp( 'a', '2' );
    private final SquareComp a3 = getSquareComp( 'a', '3' );
    private final SquareComp a4 = getSquareComp( 'a', '4' );
    private final SquareComp a5 = getSquareComp( 'a', '5' );
    private final SquareComp a6 = getSquareComp( 'a', '6' );
    private final SquareComp a7 = getSquareComp( 'a', '7' );
    private final SquareComp a8 = getSquareComp( 'a', '8' );

    private final SquareComp b1 = getSquareComp( 'b', '1' );
    private final SquareComp b2 = getSquareComp( 'b', '2' );
    private final SquareComp b3 = getSquareComp( 'b', '3' );
    private final SquareComp b4 = getSquareComp( 'b', '4' );
    private final SquareComp b5 = getSquareComp( 'b', '5' );
    private final SquareComp b6 = getSquareComp( 'b', '6' );
    private final SquareComp b7 = getSquareComp( 'b', '7' );
    private final SquareComp b8 = getSquareComp( 'b', '8' );

    private final SquareComp c1 = getSquareComp( 'c', '1' );
    private final SquareComp c2 = getSquareComp( 'c', '2' );
    private final SquareComp c3 = getSquareComp( 'c', '3' );
    private final SquareComp c4 = getSquareComp( 'c', '4' );
    private final SquareComp c5 = getSquareComp( 'c', '5' );
    private final SquareComp c6 = getSquareComp( 'c', '6' );
    private final SquareComp c7 = getSquareComp( 'c', '7' );
    private final SquareComp c8 = getSquareComp( 'c', '8' );

    private final SquareComp d1 = getSquareComp( 'd', '1' );
    private final SquareComp d2 = getSquareComp( 'd', '2' );
    private final SquareComp d3 = getSquareComp( 'd', '3' );
    private final SquareComp d4 = getSquareComp( 'd', '4' );
    private final SquareComp d5 = getSquareComp( 'd', '5' );
    private final SquareComp d6 = getSquareComp( 'd', '6' );
    private final SquareComp d7 = getSquareComp( 'd', '7' );
    private final SquareComp d8 = getSquareComp( 'd', '8' );

    private final SquareComp e1 = getSquareComp( 'e', '1' );
    private final SquareComp e2 = getSquareComp( 'e', '2' );
    private final SquareComp e3 = getSquareComp( 'e', '3' );
    private final SquareComp e4 = getSquareComp( 'e', '4' );
    private final SquareComp e5 = getSquareComp( 'e', '5' );
    private final SquareComp e6 = getSquareComp( 'e', '6' );
    private final SquareComp e7 = getSquareComp( 'e', '7' );
    private final SquareComp e8 = getSquareComp( 'e', '8' );

    private final SquareComp f1 = getSquareComp( 'f', '1' );
    private final SquareComp f2 = getSquareComp( 'f', '2' );
    private final SquareComp f3 = getSquareComp( 'f', '3' );
    private final SquareComp f4 = getSquareComp( 'f', '4' );
    private final SquareComp f5 = getSquareComp( 'f', '5' );
    private final SquareComp f6 = getSquareComp( 'f', '6' );
    private final SquareComp f7 = getSquareComp( 'f', '7' );
    private final SquareComp f8 = getSquareComp( 'f', '8' );

    private final SquareComp g1 = getSquareComp( 'g', '1' );
    private final SquareComp g2 = getSquareComp( 'g', '2' );
    private final SquareComp g3 = getSquareComp( 'g', '3' );
    private final SquareComp g4 = getSquareComp( 'g', '4' );
    private final SquareComp g5 = getSquareComp( 'g', '5' );
    private final SquareComp g6 = getSquareComp( 'g', '6' );
    private final SquareComp g7 = getSquareComp( 'g', '7' );
    private final SquareComp g8 = getSquareComp( 'g', '8' );

    private final SquareComp h1 = getSquareComp( 'h', '1' );
    private final SquareComp h2 = getSquareComp( 'h', '2' );
    private final SquareComp h3 = getSquareComp( 'h', '3' );
    private final SquareComp h4 = getSquareComp( 'h', '4' );
    private final SquareComp h5 = getSquareComp( 'h', '5' );
    private final SquareComp h6 = getSquareComp( 'h', '6' );
    private final SquareComp h7 = getSquareComp( 'h', '7' );
    private final SquareComp h8 = getSquareComp( 'h', '8' );

    private SquareComp getSquareComp( char file, char rank )
    {
        return frame.getSquareComps().get( Square.getIndex( file, rank ) );
    }

    private void mouseEvent( SquareComp squareComp, int type, int modifiers )
    {
        try
        {
            SwingUtilities.invokeAndWait( () ->
            {
                MouseEvent event = new MouseEvent( frame.getChessboardPane(),
                                                   type,
                                                   System.currentTimeMillis(),
                                                   modifiers,
                                                   squareComp.getX(),
                                                   squareComp.getY(),
                                                   1,
                                                   false,
                                                   MouseEvent.BUTTON1 );

                frame.getChessboardPane().dispatchEvent( event );
            } );

            Thread.sleep( UI_DELAY_MILLIS );
        }
        catch ( InvocationTargetException e )
        {
            throw new AssertionError( "Chess engine logic crashed during simulated move processing", e.getCause() );
        }
        catch ( InterruptedException e )
        {
            Thread.currentThread().interrupt();

            throw new RuntimeException( "Test execution thread was interrupted", e );
        }
    }

    private void press( SquareComp squareComp )
    {
        mouseEvent( squareComp, MouseEvent.MOUSE_PRESSED, MouseEvent.BUTTON1_DOWN_MASK );
    }

    private void release( SquareComp squareComp )
    {
        mouseEvent( squareComp, MouseEvent.MOUSE_RELEASED, MouseEvent.BUTTON1 );
    }

    private void click( SquareComp squareComp )
    {
        mouseEvent( squareComp, MouseEvent.MOUSE_CLICKED, MouseEvent.BUTTON1 );
    }

    @BeforeAll
    void beforeAll()
    {
        frame.setVisible( true );
    }

    @AfterAll
    void afterAll()
    {
        JOptionPane.showMessageDialog( frame,
                                       "All automated tests complete. The application will now close.",
                                       ApplicationFrameTest.class.getSimpleName(),
                                       JOptionPane.INFORMATION_MESSAGE );
    }

    @Test
    void pressAndRelease_e2e4()
    {
        press( e2 );
        release( e4 );

        assertEquals( e2, frame.getFromSquare() );
        assertEquals( e4, frame.getToSquare() );
    }
}
