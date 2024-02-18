package mapi.lotto.model.statistic;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapi.lotto.util.PlusPrize;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlusHits {

    static final int TICKET_COST = 1;

    private int p0;
    private int p1;
    private int p2;
    private int p3;
    private int p4;
    private int p5;
    private int p6;
    private int pBank;
    @Column(name = "p_acc")
    private double pACC;
    @Transient
    private int pAll;

    public void increment(int index) {
        switch (index) {
            case 0 -> p0++;
            case 1 -> p1++;
            case 2 -> p2++;
            case 3 -> {
                p3++;
                pBank += PlusPrize.THREE.getPrize();
            }
            case 4 -> {
                p4++;
                pBank += PlusPrize.FOUR.getPrize();
            }
            case 5 -> {
                p5++;
                pBank += PlusPrize.FIVE.getPrize();
            }
            case 6 -> {
                p6++;
                pBank += PlusPrize.SIX.getPrize();
            }
            default -> throw new IllegalArgumentException("Hits only in range 0 to 6");
        }
        pAll++;
        pACC = (p3 + p4 + p5 + p6) / (double) pAll;
        pBank -= TICKET_COST;
    }

}
