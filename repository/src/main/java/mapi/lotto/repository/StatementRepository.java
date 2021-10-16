package mapi.lotto.repository;

import java.sql.Date;
import java.util.List;
import mapi.lotto.model.LottoPlusStatement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepository extends CrudRepository<LottoPlusStatement, Long> {

    List<LottoPlusStatement> findByDateBetweenOrderByDate(Date from, Date to);

    LottoPlusStatement findTopByDate(Date date);

    LottoPlusStatement findTopByOrderByDateDesc();
}
