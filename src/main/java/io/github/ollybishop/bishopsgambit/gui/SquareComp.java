package io.github.ollybishop.bishopsgambit.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import io.github.ollybishop.bishopsgambit.board.Square;
import io.github.ollybishop.bishopsgambit.io.Fonts;
import io.github.ollybishop.bishopsgambit.io.Fonts.Weight;
import io.github.ollybishop.bishopsgambit.util.ColorUtils;
import io.github.ollybishop.bishopsgambit.util.ComponentUtils;

public class SquareComp extends JLayeredPane
{
    private static final Color DARK = new Color( 209, 139, 71 );
    private static final Color LIGHT = new Color( 254, 206, 157 );
    private static final Color HIGHLIGHT = ColorUtils.blend( Color.YELLOW, Color.WHITE );

    private static final Font ROBOTO_MEDIUM = Fonts.importFont( "Roboto", Weight.MEDIUM );

    /*
     * New squares are created throughout the game, but the file and rank of a given square never
     * change. For this reason, we use the file and rank as identifiers for (rather than directly
     * associate a square with) each component.
     */
    private final char file;
    private final char rank;

    private final Color defaultBg;
    private final Color defaultFg;

    private final JLabel fileLbl;
    private final JLabel rankLbl;
    private final MoveMarker moveMarker;

    protected char getFile()
    {
        return this.file;
    }

    protected char getRank()
    {
        return this.rank;
    }

    protected SquareComp( char file, char rank )
    {
        this.file = file;
        this.rank = rank;

        this.defaultBg = Square.getParity( file, rank ) == 0 ? DARK : LIGHT;
        this.defaultFg = Square.getParity( file, rank ) == 0 ? LIGHT : DARK;

        this.fileLbl = new JLabel( String.valueOf( file ) );
        fileLbl.setVerticalAlignment( SwingConstants.BOTTOM );
        fileLbl.setHorizontalAlignment( SwingConstants.RIGHT );
        fileLbl.setForeground( defaultFg );
        fileLbl.setFont( ROBOTO_MEDIUM );

        this.rankLbl = new JLabel( String.valueOf( rank ) );
        rankLbl.setVerticalAlignment( SwingConstants.TOP );
        rankLbl.setHorizontalAlignment( SwingConstants.LEFT );
        rankLbl.setForeground( defaultFg );
        rankLbl.setFont( ROBOTO_MEDIUM );

        this.moveMarker = new MoveMarker();

        add( fileLbl, DEFAULT_LAYER );
        add( rankLbl, DEFAULT_LAYER );
        add( moveMarker, MODAL_LAYER );

        resetBackground();
        showMoveMarker( false );

        setOpaque( true );
    }

    protected void addPieceComp( PieceComp pieceComp )
    {
        add( pieceComp, PALETTE_LAYER, 0 );

        // Ensures the piece is positioned within the bounds of the square
        pieceComp.setLocation( 0, 0 );
    }

    private void resetBackground()
    {
        setBackground( defaultBg );
    }

    protected void showFile( char rank )
    {
        fileLbl.setVisible( this.rank == rank );
    }

    protected void showRank( char file )
    {
        rankLbl.setVisible( this.file == file );
    }

    protected void showMoveMarker( boolean b )
    {
        moveMarker.setVisible( b );
    }

    protected boolean hasMoveMarker()
    {
        return moveMarker.isVisible();
    }

    /**
     * Changes the background color of this component to its highlighted color.
     */
    protected void select()
    {
        setBackground( ColorUtils.blend( defaultBg, HIGHLIGHT, 1, 3 ) );
    }

    /**
     * Changes the background color of this button to its default color.
     */
    protected void deselect()
    {
        resetBackground();
    }

    /**
     * Sets the border of this button to an empty border.
     */
    protected void resetBorder()
    {
        setBorder( BorderFactory.createEmptyBorder() );
    }

    protected void setScale( int scale )
    {
        Dimension dimension = new Dimension( scale, scale );

        setMinimumSize( dimension );
        setPreferredSize( dimension );

        fileLbl.setSize( dimension );
        rankLbl.setSize( dimension );
        moveMarker.setSize( dimension );

        int x = scale / 20;
        int y = scale / 40;

        fileLbl.setLocation( -x, -y );
        rankLbl.setLocation( x, y );

        ComponentUtils.resizeFont( fileLbl, scale / 5 );
        ComponentUtils.resizeFont( rankLbl, scale / 5 );
    }

    protected int getIndex()
    {
        return Square.getIndex( file, rank );
    }

    protected void debugLayeringIssues()
    {
        int totalCount = getComponentCount();

        int defaultCount = getComponentCountInLayer( DEFAULT_LAYER );
        int paletteCount = getComponentCountInLayer( PALETTE_LAYER );
        int modalCount = getComponentCountInLayer( MODAL_LAYER );

        if ( defaultCount + paletteCount + modalCount < totalCount )
            System.err.println( String.format( "Total number of components (%d) does not equal the sum of the number of components in each layer (%d default, %d palette, %d modal) for square '%s%s'",
                                               totalCount,
                                               defaultCount,
                                               paletteCount,
                                               modalCount,
                                               getFile(),
                                               getRank() ) );
    }

    private class MoveMarker extends JComponent
    {
        private static final Color BLACK_TRANSLUCENT = ColorUtils.changeAlpha( Color.BLACK, 64 );

        private MoveMarker()
        {
            setBackground( null );
        }

        @Override
        protected void paintComponent( Graphics g )
        {
            super.paintComponent( g );

            int xMid = getWidth() / 2;
            int yMid = getHeight() / 2;

            int diameter = Math.min( getWidth(), getHeight() ) * 2 / 5;
            int radius = diameter / 2;

            g.setColor( BLACK_TRANSLUCENT );
            g.fillOval( xMid - radius, yMid - radius, diameter, diameter );
        }
    }
}
