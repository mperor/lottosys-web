package mapi.lotto.model.ticket;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.util.TicketType;

@Entity
@DiscriminatorValue(TicketType.Constants.MATH)
public class MathTicket extends LotteryTicket {

    public MathTicket() {
    }

    public MathTicket(TicketNumbers ticketNumbers) {
        super(ticketNumbers);
    }

    public MathTicket(TicketNumbers ticketNumbers, LotteryResult lotteryResult) {
        super(ticketNumbers, lotteryResult);
    }
}
