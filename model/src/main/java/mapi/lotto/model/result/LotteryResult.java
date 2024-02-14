package mapi.lotto.model.result;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mapi.lotto.model.ticket.LotteryTicket;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
public class LotteryResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate lotteryDate;
    @Embedded
    private LottoNumbers lottoNumbers = new LottoNumbers();
    @Embedded
    private PlusNumbers plusNumbers = new PlusNumbers();
    @OneToMany(mappedBy = "lotteryResult")
    private Set<LotteryTicket> tickets;

    protected LotteryResult() {
    }

    public LotteryResult(LocalDate lotteryDate, LottoNumbers lottoNumbers, PlusNumbers plusNumbers) {
        this.lotteryDate = lotteryDate;
        this.lottoNumbers = lottoNumbers;
        this.plusNumbers = plusNumbers;
    }
}
