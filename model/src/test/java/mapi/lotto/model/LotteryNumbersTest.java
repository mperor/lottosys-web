package mapi.lotto.model;

import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import mapi.lotto.model.ticket.TicketNumbers;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LotteryNumbersTest {

    @Test
    void createLotteryNumbers_illegalArguments_throwsLotteryNumbersException() {
        // numbers with duplicates
        String duplicatesMessage = "with duplicates";
        assertThatThrownBy(() -> new LottoNumbers(1, 1, 2, 3, 4, 5)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(duplicatesMessage);
        assertThatThrownBy(() -> new PlusNumbers(2, 2, 3, 4, 5, 6)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(duplicatesMessage);
        assertThatThrownBy(() -> new TicketNumbers(3, 3, 4, 5, 6, 7)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(duplicatesMessage);
        // numbers outside the range <1,49>
        String illegalRangeMessage = "not in lottery range";
        assertThatThrownBy(() -> new LottoNumbers(0, 1, 2, 3, 4, 5)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(illegalRangeMessage);
        assertThatThrownBy(() -> new PlusNumbers(1, 2, 3, 4, 5, 55)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(illegalRangeMessage);
        assertThatThrownBy(() -> new TicketNumbers(-1, 2, 3, 4, 5, 6)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(illegalRangeMessage);
        // not in ascending order
        String notAscendingMessage = "not in ascending order";
        assertThatThrownBy(() -> new LottoNumbers(6, 5, 4, 3, 2, 1)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(notAscendingMessage);
        assertThatThrownBy(() -> new PlusNumbers(1, 2, 3, 4, 6, 5)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(notAscendingMessage);
        assertThatThrownBy(() -> new TicketNumbers(4, 5, 6, 1, 2, 3)).isInstanceOf(LotteryNumbersException.class).hasMessageContaining(notAscendingMessage);
    }

    @Test
    void correctlyCreatedLottoNumbers_containsCorrectNumbers() {
        assertThat(new LottoNumbers(1, 2, 3, 4, 5, 6)).containsExactly(1, 2, 3, 4, 5, 6);
        assertThat(new PlusNumbers(11, 12, 13, 14, 15, 16)).containsExactly(11, 12, 13, 14, 15, 16);
        assertThat(new TicketNumbers(21, 22, 23, 24, 25, 26)).containsExactly(21, 22, 23, 24, 25, 26);
    }

}