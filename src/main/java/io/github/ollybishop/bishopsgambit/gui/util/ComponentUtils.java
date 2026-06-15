package io.github.ollybishop.bishopsgambit.gui.util;

import java.awt.Component;

public class ComponentUtils
{
    public static void resizeFont( Component component, float size )
    {
        component.setFont( component.getFont().deriveFont( size ) );
    }
}
