package mapi.lotto.model.result;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class LotteryResult {

    @Id
    @Getter(AccessLevel.PACKAGE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate lotteryDate;
    @Embedded
    private LottoNumbers lottoNumbers = new LottoNumbers();
    @Embedded
    private PlusNumbers plusNumbers = new PlusNumbers();

    protected LotteryResult() {
    }

    public LotteryResult(LocalDate lotteryDate, LottoNumbers lottoNumbers, PlusNumbers plusNumbers) {
        this.lotteryDate = lotteryDate;
        this.lottoNumbers = lottoNumbers;
        this.plusNumbers = plusNumbers;
    }
}
