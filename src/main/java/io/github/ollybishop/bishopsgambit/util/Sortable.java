package io.github.ollybishop.bishopsgambit.util;

public interface Sortable extends Comparable<Sortable>
{
    int sortKey();

    @Override
    default int compareTo( Sortable o )
    {
        return Integer.compare( sortKey(), o.sortKey() );
    }
}
