package io.github.ollybishop.bishopsgambit.gui;

import javax.swing.JLabel;

import io.github.ollybishop.bishopsgambit.io.Images;
import io.github.ollybishop.bishopsgambit.pieces.Piece;
import io.github.ollybishop.bishopsgambit.util.Sortable;

public class PieceComponent extends JLabel implements Sortable
{
    private final Piece piece;

    protected Piece getPiece()
    {
        return this.piece;
    }

    protected PieceComponent( Piece piece )
    {
        this.piece = piece;
    }

    protected void setScale( int scale )
    {
        setSize( scale, scale );
        setIcon( Images.createIcon( piece.getColour(), piece.getType(), scale ) );
    }

    @Override
    public int sortKey()
    {
        return piece.getType().ordinal();
    }
}
