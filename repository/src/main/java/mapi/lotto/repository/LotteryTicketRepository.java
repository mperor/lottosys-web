package mapi.lotto.repository;

import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.ticket.LotteryTicket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryTicketRepository extends CrudRepository<LotteryTicket, Long> {

    List<LotteryTicket> findAllByTicketTypeOrderByIdDesc(String ticketType);

    List<LotteryTicket> findAllByLotteryResult(LotteryResult result);

    List<LotteryTicket> findAllByTicketTypeAndLotteryResult(String ticketType, LotteryResult result);

    boolean existsByTicketTypeAndLotteryResultIsNull(String name);

    @Query("SELECT t FROM LotteryTicket t JOIN FETCH t.lotteryResult " +
            "WHERE t.ticketType = :ticketType AND t.lotteryResult.lotteryDate BETWEEN :from AND :to " +
            "ORDER BY t.lotteryResult.lotteryDate ASC")
    List<LotteryTicket> findAllByTicketTypeAndLotteryResult_LotteryDateBetween(String ticketType, LocalDate from, LocalDate to);

    Optional<LotteryTicket> findTop1ByTicketTypeOrderByLotteryResultLotteryDateAsc(String ticketType);

}
