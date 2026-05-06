package io.github.ollybishop.bishopsgambit.gui;

import javax.swing.JLabel;

import io.github.ollybishop.bishopsgambit.io.Images;
import io.github.ollybishop.bishopsgambit.pieces.Piece;
import io.github.ollybishop.bishopsgambit.util.Orderable;

public class PieceComp extends JLabel implements Orderable
{
    private final Piece piece;

    protected Piece getPiece()
    {
        return this.piece;
    }

    protected PieceComp( Piece piece )
    {
        this.piece = piece;
    }

    protected void setScale( int scale )
    {
        setSize( scale, scale );
        setIcon( Images.createIcon( piece.getColour(), piece.getType(), scale ) );
    }

    @Override
    public int compareTo( Orderable o )
    {
        if ( !(o instanceof PieceComp) )
            throw new IllegalArgumentException( "The object being compared must be an instance of PieceComp." );

        return Integer.compare( o.ordinal(), ordinal() );
    }

    @Override
    public int ordinal()
    {
        return piece.getType().ordinal();
    }
}
