package mapi.lotto.model.statistic;

import mapi.lotto.util.LottoPrize;
import mapi.lotto.util.PlusPrize;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LottoPlusHitsTest {

    @Test
    void hitsIncrementFromZeroToSixExclusively_returnsCorrectResults() {
        var lotto = new LottoHits();
        var plus = new PlusHits();

        IntStream.range(0,6).forEach(hit -> {
            lotto.increment(hit);
            plus.increment(hit);
        });

        assertThat(lotto.getLAll()).isEqualTo(6);
        assertThat(lotto.getLACC()).isEqualTo(0.5);
        int lottoProfit = Stream.of(LottoPrize.FIVE, LottoPrize.FOUR, LottoPrize.THREE)
                .mapToInt(LottoPrize::getPrize)
                .sum();
        assertThat(lotto.getLBank()).isEqualTo(lottoProfit - (lotto.getLAll() * LottoHits.TICKET_COST));

        assertThat(plus.getPAll()).isEqualTo(6);
        assertThat(plus.getPACC()).isEqualTo(0.5);
        int plusProfit = Stream.of(PlusPrize.FIVE, PlusPrize.FOUR, PlusPrize.THREE)
                .mapToInt(PlusPrize::getPrize)
                .sum();
        assertThat(plus.getPBank()).isEqualTo(plusProfit - (plus.getPAll() * PlusHits.TICKET_COST));
    }

}