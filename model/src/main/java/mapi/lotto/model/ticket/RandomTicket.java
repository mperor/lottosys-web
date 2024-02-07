package mapi.lotto.model.ticket;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.util.TicketType;

@Entity
@DiscriminatorValue(TicketType.Constants.RANDOM)
public class RandomTicket extends LotteryTicket {

    public RandomTicket() {
    }

    public RandomTicket(TicketNumbers ticketNumbers) {
        super(ticketNumbers);
    }

    public RandomTicket(TicketNumbers ticketNumbers, LotteryResult lotteryResult) {
        super(ticketNumbers, lotteryResult);
    }
}
