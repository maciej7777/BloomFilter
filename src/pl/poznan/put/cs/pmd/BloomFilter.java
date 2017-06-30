package pl.poznan.put.cs.pmd;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Maciej Uniejewski on 2017-04-28.
 */

public class BloomFilter {

    private int size;
    private int p;
    private ArrayList<Integer> aFactors;
    private ArrayList<Integer> bFactors;
    private BitSet bitMap;

    public BloomFilter(int size, int k, int range) {

        this.size = size;
        this.p = generateFirstPrimeNumberGreaterThan(range);
        this.aFactors = generateNumbersFromInterval(k, 1, p-1);
        this.bFactors = generateNumbersFromInterval(k, 0, p-1);

        System.out.println(p);
        bitMap = new BitSet(size);
    }

    public void add(int key) {
        for (int i = 0; i < aFactors.size(); i++) {
            int positionToSet = countHxFunction(key, i);
            bitMap.set(positionToSet);
        }
    }

    private Long countGxFunction(int x, int position) {
        Long xValue = Long.valueOf(x);
        Long aValue = Long.valueOf(aFactors.get(position));
        Long bValue = Long.valueOf(bFactors.get(position));

        return (xValue*aValue + bValue) % p;
    }

    private Long countGxFunctionLongNumbers(int x, int position) {
        Long xValue = Long.valueOf(x);
        Long aValue = Long.valueOf(aFactors.get(position));
        Long bValue = Long.valueOf(bFactors.get(position));

        return (((xValue%p)*(aValue%p)%p)+ (bValue%p)) % p;
    }

    private int countHxFunction(int x, int position) {
        return (int)(countGxFunction(x, position) % size);
    }

    public Boolean contains(int key) {
        for (int i = 0; i < aFactors.size(); i++) {
            int positionToSet = countHxFunction(key, i);
            if (!bitMap.get(positionToSet)) {
                return false;
            }
        }
        return true;
    }

    private Integer generateFirstPrimeNumberGreaterThan(Integer number) {
        if (number % 2 == 0) {
            number++;
        } else {
            number += 2;
        }

        while (true) {
            if (isNumberPrime(number)) {
                return number;
            }
            number += 2;
        }
    }

    private boolean isNumberPrime(Integer number) {
        BigInteger checkedNumber = BigInteger.valueOf(number);

        return checkedNumber.isProbablePrime(1000000);
    }

    private ArrayList<Integer> generateNumbersFromInterval(int numberOfValues, int min, int max) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < numberOfValues; i++) {
            Integer randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
            numbers.add(randomNumber);
        }
        return numbers;
    }
}

