package mapi.lotto.model.result;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mapi.lotto.model.LotteryNumbers;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Embeddable
public class PlusNumbers extends LotteryNumbers {

    public static final PlusNumbers EMPTY = new PlusNumbers();

    private int p1;
    private int p2;
    private int p3;
    private int p4;
    private int p5;
    private int p6;

    public PlusNumbers(int p1, int p2, int p3, int p4, int p5, int p6) {
        super(p1, p2, p3, p4, p5, p6);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.p6 = p6;
    }

    @Override
    public int[] toArray() {
        return new int[]{p1, p2, p3, p4, p5, p6};
    }

}
