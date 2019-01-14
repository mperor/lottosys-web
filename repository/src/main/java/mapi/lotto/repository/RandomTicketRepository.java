package mapi.lotto.repository;

import mapi.lotto.model.LottoPlusStatement;
import mapi.lotto.model.RandomTicket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RandomTicketRepository extends CrudRepository<RandomTicket, Long> {

    RandomTicket findTopByOrderByRandomTicketIdDesc();
    
    RandomTicket findTopByLottoPlusStatement(LottoPlusStatement lottoPlusStatement);
	    
}
