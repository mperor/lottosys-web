package mapi.lotto.model.statistic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class LotteryStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int lotteryYear;
    @Column(name = "lottery_all")
    private int lotteryAll;
    @Embedded
    private LottoHits lottoHits = new LottoHits();
    @Embedded
    private PlusHits plusHits = new PlusHits();
    private String ticketType;

    public LotteryStatistic(int lotteryYear, int lotteryAll, LottoHits lottoHits, PlusHits plusHits, String ticketType) {
        this.lotteryYear = lotteryYear;
        this.lotteryAll = lotteryAll;
        this.lottoHits = lottoHits;
        this.plusHits = plusHits;
        this.ticketType = ticketType;
    }
}
