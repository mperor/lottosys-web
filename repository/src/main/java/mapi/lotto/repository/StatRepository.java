package mapi.lotto.repository;

import java.util.List;
import mapi.lotto.model.Stat;
import org.springframework.data.repository.CrudRepository;

public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findTopByYearAndTicketOrdinal(short year, int ticketOrdinal);

    List<Stat> findByTicketOrdinal(int ticketOrdinal);
}
