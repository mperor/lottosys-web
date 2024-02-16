package mapi.lotto.model.ticket;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mapi.lotto.model.LotteryNumbers;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collector;
import java.util.stream.Stream;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Embeddable
public class TicketNumbers extends LotteryNumbers {

    private int n1;
    private int n2;
    private int n3;
    private int n4;
    private int n5;
    private int n6;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TicketNumbers(int n1, int n2, int n3, int n4, int n5, int n6) {
        super(n1, n2, n3, n4, n5, n6);
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.n4 = n4;
        this.n5 = n5;
        this.n6 = n6;
    }

    @Override
    public int[] toArray() {
        return new int[]{n1, n2, n3, n4, n5, n6};
    }

    public static TicketNumbers generateDeltaNumbers() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        var it = Stream.of(
                        random.nextInt(1, 3),
                        random.nextInt(1, 6),
                        random.nextInt(1, 7),
                        random.nextInt(4, 8),
                        random.nextInt(6, 10),
                        random.nextInt(8, 15)
                ).map(integer -> Pair.of(integer, random.nextDouble()))
                .sorted(Comparator.comparingDouble(Pair::getSecond))
                .map(Pair::getFirst)
                .collect(sumPairsOfNumbersToList())
                .iterator();

        return new TicketNumbers(it.next(), it.next(), it.next(), it.next(), it.next(), it.next());
    }

    private static Collector<Integer, ArrayList<Integer>, ArrayList<Integer>> sumPairsOfNumbersToList() {
        return Collector.of(
                ArrayList::new,
                (list, element) -> {
                    if (!list.isEmpty())
                        list.add(list.get(list.size() - 1) + element);
                    else
                        list.add(element);
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }
        );
    }

    public static TicketNumbers generateRandomNumbers() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        var it = random.ints(1, 50)
                .distinct()
                .limit(6)
                .sorted()
                .boxed()
                .iterator();

        return new TicketNumbers(it.next(), it.next(), it.next(), it.next(), it.next(), it.next());
    }
}
