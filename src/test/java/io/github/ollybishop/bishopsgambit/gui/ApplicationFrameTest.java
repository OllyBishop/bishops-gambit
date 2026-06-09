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

    private final SquareComponent a1 = getSquareComponent( 'a', '1' );
    private final SquareComponent a2 = getSquareComponent( 'a', '2' );
    private final SquareComponent a3 = getSquareComponent( 'a', '3' );
    private final SquareComponent a4 = getSquareComponent( 'a', '4' );
    private final SquareComponent a5 = getSquareComponent( 'a', '5' );
    private final SquareComponent a6 = getSquareComponent( 'a', '6' );
    private final SquareComponent a7 = getSquareComponent( 'a', '7' );
    private final SquareComponent a8 = getSquareComponent( 'a', '8' );

    private final SquareComponent b1 = getSquareComponent( 'b', '1' );
    private final SquareComponent b2 = getSquareComponent( 'b', '2' );
    private final SquareComponent b3 = getSquareComponent( 'b', '3' );
    private final SquareComponent b4 = getSquareComponent( 'b', '4' );
    private final SquareComponent b5 = getSquareComponent( 'b', '5' );
    private final SquareComponent b6 = getSquareComponent( 'b', '6' );
    private final SquareComponent b7 = getSquareComponent( 'b', '7' );
    private final SquareComponent b8 = getSquareComponent( 'b', '8' );

    private final SquareComponent c1 = getSquareComponent( 'c', '1' );
    private final SquareComponent c2 = getSquareComponent( 'c', '2' );
    private final SquareComponent c3 = getSquareComponent( 'c', '3' );
    private final SquareComponent c4 = getSquareComponent( 'c', '4' );
    private final SquareComponent c5 = getSquareComponent( 'c', '5' );
    private final SquareComponent c6 = getSquareComponent( 'c', '6' );
    private final SquareComponent c7 = getSquareComponent( 'c', '7' );
    private final SquareComponent c8 = getSquareComponent( 'c', '8' );

    private final SquareComponent d1 = getSquareComponent( 'd', '1' );
    private final SquareComponent d2 = getSquareComponent( 'd', '2' );
    private final SquareComponent d3 = getSquareComponent( 'd', '3' );
    private final SquareComponent d4 = getSquareComponent( 'd', '4' );
    private final SquareComponent d5 = getSquareComponent( 'd', '5' );
    private final SquareComponent d6 = getSquareComponent( 'd', '6' );
    private final SquareComponent d7 = getSquareComponent( 'd', '7' );
    private final SquareComponent d8 = getSquareComponent( 'd', '8' );

    private final SquareComponent e1 = getSquareComponent( 'e', '1' );
    private final SquareComponent e2 = getSquareComponent( 'e', '2' );
    private final SquareComponent e3 = getSquareComponent( 'e', '3' );
    private final SquareComponent e4 = getSquareComponent( 'e', '4' );
    private final SquareComponent e5 = getSquareComponent( 'e', '5' );
    private final SquareComponent e6 = getSquareComponent( 'e', '6' );
    private final SquareComponent e7 = getSquareComponent( 'e', '7' );
    private final SquareComponent e8 = getSquareComponent( 'e', '8' );

    private final SquareComponent f1 = getSquareComponent( 'f', '1' );
    private final SquareComponent f2 = getSquareComponent( 'f', '2' );
    private final SquareComponent f3 = getSquareComponent( 'f', '3' );
    private final SquareComponent f4 = getSquareComponent( 'f', '4' );
    private final SquareComponent f5 = getSquareComponent( 'f', '5' );
    private final SquareComponent f6 = getSquareComponent( 'f', '6' );
    private final SquareComponent f7 = getSquareComponent( 'f', '7' );
    private final SquareComponent f8 = getSquareComponent( 'f', '8' );

    private final SquareComponent g1 = getSquareComponent( 'g', '1' );
    private final SquareComponent g2 = getSquareComponent( 'g', '2' );
    private final SquareComponent g3 = getSquareComponent( 'g', '3' );
    private final SquareComponent g4 = getSquareComponent( 'g', '4' );
    private final SquareComponent g5 = getSquareComponent( 'g', '5' );
    private final SquareComponent g6 = getSquareComponent( 'g', '6' );
    private final SquareComponent g7 = getSquareComponent( 'g', '7' );
    private final SquareComponent g8 = getSquareComponent( 'g', '8' );

    private final SquareComponent h1 = getSquareComponent( 'h', '1' );
    private final SquareComponent h2 = getSquareComponent( 'h', '2' );
    private final SquareComponent h3 = getSquareComponent( 'h', '3' );
    private final SquareComponent h4 = getSquareComponent( 'h', '4' );
    private final SquareComponent h5 = getSquareComponent( 'h', '5' );
    private final SquareComponent h6 = getSquareComponent( 'h', '6' );
    private final SquareComponent h7 = getSquareComponent( 'h', '7' );
    private final SquareComponent h8 = getSquareComponent( 'h', '8' );

    private SquareComponent getSquareComponent( char file, char rank )
    {
        return frame.getSquareComponents().get( Square.getIndex( file, rank ) );
    }

    private void mouseEvent( SquareComponent squareComponent, int type, int modifiers )
    {
        try
        {
            SwingUtilities.invokeAndWait( () ->
            {
                MouseEvent event = new MouseEvent( frame.getChessboardPane(),
                                                   type,
                                                   System.currentTimeMillis(),
                                                   modifiers,
                                                   squareComponent.getX(),
                                                   squareComponent.getY(),
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

    private void press( SquareComponent squareComponent )
    {
        mouseEvent( squareComponent, MouseEvent.MOUSE_PRESSED, MouseEvent.BUTTON1_DOWN_MASK );
    }

    private void release( SquareComponent squareComponent )
    {
        mouseEvent( squareComponent, MouseEvent.MOUSE_RELEASED, MouseEvent.BUTTON1 );
    }

    private void click( SquareComponent squareComponent )
    {
        mouseEvent( squareComponent, MouseEvent.MOUSE_CLICKED, MouseEvent.BUTTON1 );
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
