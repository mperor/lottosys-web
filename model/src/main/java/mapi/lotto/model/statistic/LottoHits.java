package mapi.lotto.model.statistic;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapi.lotto.util.LottoPrize;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LottoHits {

    private static final int TICKET_COST = 3;

    private int l0;
    private int l1;
    private int l2;
    private int l3;
    private int l4;
    private int l5;
    private int l6;
    private int lBank;
    @Column(name = "l_acc")
    private double lACC;
    @Transient
    private int lAll;

    public void increment(int index) {
        switch (index) {
            case 0 -> l0++;
            case 1 -> l1++;
            case 2 -> l2++;
            case 3 -> {
                l3++;
                lBank += LottoPrize.THREE.getPrize();
            }
            case 4 -> {
                l4++;
                lBank += LottoPrize.FOUR.getPrize();
            }
            case 5 -> {
                l5++;
                lBank += LottoPrize.FIVE.getPrize();
            }
            case 6 -> {
                l6++;
                lBank += LottoPrize.SIX.getPrize();
            }
            default -> throw new IllegalArgumentException("Hits can only be in range 0 to 6");
        }
        lAll++;
        lACC = (l3 + l4 + l5 + l6) / (double) lAll;
        lBank -= TICKET_COST;
    }

}
