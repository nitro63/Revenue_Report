package com.Enums;

public enum Months {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);
    private final int value;

    Months(final int newValue){
        value = newValue;
    }
    public int getValue(){return value;}
    public static Months get(int value ) {
        return Months.values()[-- value];
}
}
