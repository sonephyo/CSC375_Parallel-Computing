package Project1_FLP.Enum;

import java.util.Random;

public enum Station_Type {
    TypeA(1),
    TypeB(2),
    TypeC(3),
    TypeD(4);


    private final int value;

    private Station_Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
