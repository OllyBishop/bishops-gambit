package io.github.ollybishop.bishopsgambit.app;

import java.awt.EventQueue;

import io.github.ollybishop.bishopsgambit.gui.ApplicationFrame;

public class Main
{
    /**
     * Launch the application.
     */
    public static void main( String[] args )
    {
        EventQueue.invokeLater( () ->
        {
            try
            {
                ApplicationFrame frame = new ApplicationFrame();
                frame.setTitle( "Bishop's Gambit" );
                frame.setVisible( true );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        } );
    }
}
