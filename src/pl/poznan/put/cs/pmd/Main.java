package pl.poznan.put.cs.pmd;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by Maciej Uniejewski on 2017-06-30.
 */
public class Main {
    private Main(){

    }

    public static void main(String[] args) {

        long start = System.nanoTime();
        int n = 10000;
        int range = 100000000;
        double factor = 10;
        int size = (int) Math.round(factor * n);

        int k = 1;

        Random random = new Random(0);

        BloomFilter bf = new BloomFilter(size, k, range);

        HashSet<Integer> set = new HashSet<>(n);

        while(set.size() < n) {
            set.add(random.nextInt(range));
        }

        set.forEach(bf::add);

        int TP = 0;
        int FP = 0;
        int TN = 0;
        int FN = 0;

        for(int i = 0; i < range; i++) {
            Boolean containsBF = bf.contains(i);
            Boolean containsHS = set.contains(i);

            if(containsBF && containsHS) {
                TP++;
            } else if(!containsBF && !containsHS) {
                TN++;
            } else if(!containsBF && containsHS) {
                FN++;
            }  else if(containsBF && !containsHS) {
                FP++;
            }
        }
        long elapsedTime = System.nanoTime() - start;

        System.out.println("TP = " + String.format("%6d", TP) + "\tTPR = " + String.format("%1.4f", (double) TP/ (double) n));
        System.out.println("TN = " + String.format("%6d", TN) + "\tTNR = " + String.format("%1.4f", (double) TN/ (double) (range-n)));
        System.out.println("FN = " + String.format("%6d", FN) + "\tFNR = " + String.format("%1.4f", (double) FN/ (double) (n)));
        System.out.println("FP = " + String.format("%6d", FP) + "\tFPR = " + String.format("%1.4f", (double) FP/ (double) (range-n)));
        System.out.println("It took me " + elapsedTime/1000000000F + " seconds to calculate it.");
    }
}
