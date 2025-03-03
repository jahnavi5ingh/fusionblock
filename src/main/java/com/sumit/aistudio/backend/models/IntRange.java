package com.sumit.aistudio.backend.models;

public class IntRange {
    //has min max
    private int min;
    private int max;

    //conmstruct form min max
    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }
    //construct from string which has min max sepeared byt a punctuate which counld be ,:-; etc.
    public IntRange(String range) {
        String[] split = range.split("[,:;_]");
        this.min = Integer.parseInt(split[0]);
        this.max = Integer.parseInt(split[1]);
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "IntRange{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }

    public static void main(String[] args) {
        //test 1
        IntRange range = new IntRange("1:10");
        System.out.println(range.getMin());
        System.out.println(range.getMax());
        //test 2
        IntRange range2 = new IntRange(1, 10);
        System.out.println(range2.getMin());
        System.out.println(range2.getMax());
        //test 3
        IntRange range3 = new IntRange("1,10");
        System.out.println(range3.getMin());
        System.out.println(range3.getMax());
        //test 4
        IntRange range4 = new IntRange("1-10");
        System.out.println(range4.getMin());
        System.out.println(range4.getMax());
        //test 5
        IntRange range5 = new IntRange("1;10");
        System.out.println(range5.getMin());
        System.out.println(range5.getMax());
        //test 6
        IntRange range6 = new IntRange("1_10");
        System.out.println(range6.getMin());
        System.out.println(range6.getMax());

    }
}
