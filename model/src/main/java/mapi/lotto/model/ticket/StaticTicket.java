package mapi.lotto.model.ticket;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.util.TicketType;

@Entity
@DiscriminatorValue(TicketType.Constants.STATIC)
public class StaticTicket extends LotteryTicket {

    public StaticTicket() {
    }

    public StaticTicket(TicketNumbers ticketNumbers) {
        super(ticketNumbers);
    }

    public StaticTicket(TicketNumbers ticketNumbers, LotteryResult lotteryResult) {
        super(ticketNumbers, lotteryResult);
    }
}
