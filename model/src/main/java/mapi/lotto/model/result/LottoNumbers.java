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
public class LottoNumbers extends LotteryNumbers {

    private int l1;
    private int l2;
    private int l3;
    private int l4;
    private int l5;
    private int l6;

    public LottoNumbers(int l1, int l2, int l3, int l4, int l5, int l6) {
        super(l1, l2, l3, l4, l5, l6);
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
        this.l4 = l4;
        this.l5 = l5;
        this.l6 = l6;
    }

    @Override
    public int[] toArray() {
        return new int[]{l1, l2, l3, l4, l5, l6};
    }
}
