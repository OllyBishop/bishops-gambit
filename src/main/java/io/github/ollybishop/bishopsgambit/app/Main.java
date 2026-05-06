package io.github.ollybishop.bishopsgambit.app;

import java.awt.EventQueue;

import io.github.ollybishop.bishopsgambit.gui.GUI;

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
                GUI frame = new GUI();
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
