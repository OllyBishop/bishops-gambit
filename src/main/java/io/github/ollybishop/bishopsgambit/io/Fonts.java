package io.github.ollybishop.bishopsgambit.io;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Fonts
{
    public static Font importFont( String fontName, FontFace fontFace )
    {
        Font font = null;

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        List<String> fontFamilyNames = Arrays.asList( graphicsEnvironment.getAvailableFontFamilyNames() );

        try
        {
            String fontPath = String.format( "/fonts/%s/%s-%s.ttf", fontName.toLowerCase(), fontName, fontFace );
            font = Font.createFont( Font.TRUETYPE_FONT, Fonts.class.getResourceAsStream( fontPath ) );

            if ( !fontFamilyNames.contains( font.getFontName() ) )
                graphicsEnvironment.registerFont( font );
        }
        catch ( FontFormatException | IOException e )
        {
            e.printStackTrace();
        }

        return font;
    }
}
