package simdLearning;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;

public class VectorAPIOperations {

    static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;


    static void multiply(int[] array, int b) {
//        int i = 0;
//        int bound = SPECIES.loopBound(array.length); // Give the SIMD operations it can do in the multiple of SPECIES (will not include the outliers remainders)
//        IntVector byVector = IntVector.broadcast(SPECIES, b);
//        System.out.println(i);
//        System.out.println(bound);
//        System.out.println(byVector);
//        System.out.println("---");
//        for (; i < bound; i += SPECIES.length()) {
//            IntVector vec = IntVector.fromArray(SPECIES, array, i);
//            IntVector multi = vec.mul(byVector);
//            System.out.println(multi);
//            multi.intoArray(array, i);
//        }
//        for (; i< array.length; i++) {
//            array[i] *= b;
//        }


        System.out.println("------");

        int bound = SPECIES.loopBound(array.length);
        VectorSpecies<Integer> species = IntVector.SPECIES_128;
        IntVector intVector = IntVector.broadcast(species, 4);
        VectorMask<Integer> mask = VectorMask.fromValues(species, true, false, false, true);
        IntVector result = intVector.mul(0, mask);
        System.out.println(species);
        System.out.println(intVector);
        System.out.println(mask);
        System.out.println(result);
        System.out.println("---");
        int i = 0;
        for (; i < bound; i += SPECIES.length()) {
            IntVector vec = IntVector.fromArray(SPECIES, array, i);
            IntVector multi = vec.mul(0, mask);
            System.out.println(multi);
            multi.intoArray(array, i);
        }
        for (; i< array.length; i++) {
            array[i] *= b;
        }
        System.out.println(Arrays.toString(array));
    }
    public static void main(String[] args) {


        multiply(new int[]{1, 2, 3, 4, 5,3,5,3,4,3,51,3,5,3,53,4}, 2);
    }
}
