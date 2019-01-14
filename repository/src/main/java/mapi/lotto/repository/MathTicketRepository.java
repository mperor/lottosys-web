package mapi.lotto.repository;

import mapi.lotto.model.LottoPlusStatement;
import mapi.lotto.model.MathTicket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MathTicketRepository extends CrudRepository<MathTicket, Long> {

    MathTicket findTopByOrderByMathTicketIdDesc();
    
    MathTicket findTopByLottoPlusStatement(LottoPlusStatement lottoPlusStatement);
}
