package io.github.ollybishop.bishopsgambit.io;

public enum FontFace
{
    THIN( "Thin" ),
    LIGHT( "Light" ),
    REGULAR( "Regular" ),
    MEDIUM( "Medium" ),
    BOLD( "Bold" ),
    BLACK( "Black" ),

    THIN_ITALIC( "ThinItalic" ),
    LIGHT_ITALIC( "LightItalic" ),
    REGULAR_ITALIC( "RegularItalic" ),
    MEDIUM_ITALIC( "MediumItalic" ),
    BOLD_ITALIC( "BoldItalic" ),
    BLACK_ITALIC( "BlackItalic" );

    private final String fileSuffix;

    private FontFace( String fileSuffix )
    {
        this.fileSuffix = fileSuffix;
    }

    /**
     * Returns the suffix used to form the matching {@code .ttf} font resource filename.
     */
    @Override
    public String toString()
    {
        return fileSuffix;
    }
}
