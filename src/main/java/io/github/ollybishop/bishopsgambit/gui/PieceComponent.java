package io.github.ollybishop.bishopsgambit.gui;

import javax.swing.JLabel;

import io.github.ollybishop.bishopsgambit.io.Images;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;
import io.github.ollybishop.bishopsgambit.util.Sortable;

class PieceComponent extends JLabel implements Sortable
{
    private final Piece piece;

    Piece getPiece()
    {
        return this.piece;
    }

    PieceComponent( Piece piece )
    {
        this.piece = piece;
    }

    void setScale( int scale )
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
