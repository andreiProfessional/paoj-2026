package com.pao.laboratory05.playlist;

import java.util.Comparator;

public class SongDurationComparator implements Comparator<Song> {

    @Override
    public int compare(Song a, Song b) {
        return Integer.compare(a.durationSeconds(), b.durationSeconds());
    }
}
