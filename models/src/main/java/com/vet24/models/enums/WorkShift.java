package com.vet24.models.enums;

public enum WorkShift {
    FIRST_SHIFT(7, 15), //первая смена - с 07:00 до 15:00
    SECOND_SHIFT(15, 23) //вторая смена - с 15:00 до 23:00
    ;
    private final int startWorkShift;
    private final int endWorkShift;

    WorkShift(int startWorkShift, int endWorkShift) {
        this.startWorkShift = startWorkShift;
        this.endWorkShift = endWorkShift;
    }

    public int getStartWorkShift() {
        return startWorkShift;
    }

    public int getEndWorkShift() {
        return endWorkShift;
    }
}
