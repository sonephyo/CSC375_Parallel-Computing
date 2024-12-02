import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;

public class SIMDTest {

    static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        int[] arr2 = {21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};


//        System.out.println(SPECIES.length());
//        var v1 = IntVector.fromArray(SPECIES, arr1, 0);
//        var v2 = IntVector.fromArray(SPECIES, arr2, 0);
//
//        System.out.println(v1.toString());
//        System.out.println(v2.toString());
//
//        var result = v1.add(v2);
//        System.out.println(result.length());
//        System.out.println(Arrays.toString(result.toArray()));
//
        int[] finalResult = new int[arr1.length];
        int i = 0;
        for (; i < SPECIES.loopBound(arr1.length); i += SPECIES.length()) {
            var mask = SPECIES.indexInRange(i, arr1.length);
            var v1 = IntVector.fromArray(SPECIES, arr1, i, mask);
            var v2 = IntVector.fromArray(SPECIES, arr2, i, mask);
            var result = v1.add(v2, mask);
            result.intoArray(finalResult, i, mask);


            // Print values
            System.out.println("SPECIES.loopBound(arr1.length): " + SPECIES.loopBound(arr1.length) );
            System.out.println("mask: " + mask);
            System.out.println("v1: " + v1);
            System.out.println("v2: " + v2);
            System.out.println("result: " + result);
            System.out.println(Arrays.toString(finalResult));
            System.out.println("_______");
        }

        // tail cleanup loop
        for (; i < arr1.length; i++) {
            finalResult[i] = arr1[i] + arr2[i];
        }


        System.out.println(Arrays.toString(finalResult));



        double sum = 0;
        for (int j = 0; j< arr1.length; j += SPECIES.length()) {
            var mask = SPECIES.indexInRange(j, arr1.length);
            var V = IntVector.fromArray(SPECIES, arr1, j, mask);
            sum += V.reduceLanes(VectorOperators.ADD, mask);
            System.out.println("---");
            System.out.println(mask);
            System.out.println(V);
        }

        System.out.println("sum: " + sum);

    }
}
