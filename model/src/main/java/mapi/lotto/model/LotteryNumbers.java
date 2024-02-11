package mapi.lotto.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class LotteryNumbers implements Iterable<Integer> {

    public static final Validations.Range LOTTERY_NUMBERS_RANGE = new Validations.Range(1, 49);

    protected LotteryNumbers() {
    }

    protected LotteryNumbers(int n1, int n2, int n3, int n4, int n5, int n6) {
        if (Validations.containDuplicates(n1, n2, n3, n4, n5, n6))
            throw new LotteryNumbersException("Lottery numbers with duplicates is not allowed!: %s".formatted(List.of(n1, n2, n3, n4, n5, n6)));
        if (Validations.isOutOfLotteryRange(LOTTERY_NUMBERS_RANGE, n1, n2, n3, n4, n5, n6))
            throw new LotteryNumbersException("Lottery numbers are not in lottery range!: range(%s, %s) %s".formatted(LOTTERY_NUMBERS_RANGE.min, LOTTERY_NUMBERS_RANGE.max, List.of(n1, n2, n3, n4, n5, n6)));
        if (!Validations.isAscending(n1, n2, n3, n4, n5, n6))
            throw new LotteryNumbersException("Lottery numbers are not in ascending order!: %s".formatted(List.of(n1, n2, n3, n4, n5, n6)));
    }

    public abstract int[] toArray();

    @Override
    public Iterator<Integer> iterator() {
        return Arrays.stream(toArray()).iterator();
    }

    public int countCommonNumbers(LotteryNumbers lotteryNumbers) {
        int[] input = lotteryNumbers.toArray();
        return (int) Arrays.stream(this.toArray())
                .filter(number -> Arrays.binarySearch(input, number) >= 0)
                .count();
    }

    public boolean containNumber(int number) {
        return Arrays.binarySearch(toArray(), number) >= 0;
    }

    private static class Validations {

        private static boolean containDuplicates(int... numbers) {
            for (int i = 0; i < numbers.length; i++) {
                for (int j = i + 1; j < numbers.length; j++) {
                    if (numbers[i] == numbers[j]) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static boolean isOutOfLotteryRange(Range range, int... numbers) {
            return !Arrays.stream(numbers)
                    .allMatch(number -> number >= range.min && number <= range.max);
        }

        private record Range(int min, int max) {
        }

        private static boolean isAscending(int... numbers) {
            for (int i = 0; i < numbers.length - 1; i++) {
                if (numbers[i] >= numbers[i + 1])
                    return false;
            }
            return true;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
}
