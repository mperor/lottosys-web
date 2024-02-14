package mapi.lotto.model.ticket;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mapi.lotto.model.result.LotteryResult;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ticket_type")
public class LotteryTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ticket_type", insertable = false, updatable = false)
    private String ticketType;
    @Embedded
    private TicketNumbers ticketNumbers = new TicketNumbers();
    @JoinColumn(name = "lottery_result_id")
    @ManyToOne
    private LotteryResult lotteryResult;

    public LotteryTicket(TicketNumbers ticketNumbers) {
        this.ticketNumbers = ticketNumbers;
    }

    public LotteryTicket(TicketNumbers ticketNumbers, LotteryResult lotteryResult) {
        this.ticketNumbers = ticketNumbers;
        this.lotteryResult = lotteryResult;
    }
}
