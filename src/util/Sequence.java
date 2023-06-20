package util;

public class Sequence {
    private static long id = 1;

    public static long getNextId() {
        return id++;
    }
}
