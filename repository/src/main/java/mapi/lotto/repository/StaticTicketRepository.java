package mapi.lotto.repository;

import mapi.lotto.model.LottoPlusStatement;
import mapi.lotto.model.StaticTicket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticTicketRepository extends CrudRepository<StaticTicket, Long> {

    StaticTicket findTopByOrderByStaticTicketIdDesc();

    StaticTicket findTopByLottoPlusStatement(LottoPlusStatement lottoPlusStatement);

}
