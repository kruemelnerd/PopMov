package de.philippveit.popmov.data;

/**
 * Used with the navigation Drawer
 * https://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
 * https://stackoverflow.com/questions/14750743/save-enum-to-sharedpreference/22951509
 */
public enum MovieFilterType {
    /**
     * Show all Top Rated Movies
     */
    TOP_RATED(5),

    /**
     * Show the popular Movies
     */
    POPULAR(10),

    /**
     * Show only the favorite Movies.
     */
    FAVORITE(15);

    private final int value;

    private MovieFilterType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static MovieFilterType fromInt(int value) {
        for (MovieFilterType type : MovieFilterType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }

}