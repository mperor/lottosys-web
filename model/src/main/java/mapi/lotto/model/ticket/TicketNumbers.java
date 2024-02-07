package mapi.lotto.model.ticket;

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
public class TicketNumbers extends LotteryNumbers {

    private int n1;
    private int n2;
    private int n3;
    private int n4;
    private int n5;
    private int n6;

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


}
