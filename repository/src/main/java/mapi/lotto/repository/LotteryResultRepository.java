package mapi.lotto.repository;

import mapi.lotto.model.result.LotteryResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LotteryResultRepository extends CrudRepository<LotteryResult, Long> {

    List<LotteryResult> findByLotteryDateBetweenOrderByLotteryDate(LocalDate from, LocalDate to);

    boolean existsByLotteryDateGreaterThanEqual(LocalDate localDate);
}
