package mapi.lotto.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mapi.lotto.model.LotteryNumbers;
import mapi.lotto.model.LotteryNumbersException;
import mapi.lotto.model.result.LotteryResult;
import mapi.lotto.model.ticket.LotteryTicket;
import mapi.lotto.model.ticket.TicketNumbers;
import mapi.lotto.service.LotteryService;
import mapi.lotto.service.mapi.lotto.result.LotteryResultClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LotteryApiController {

    private final LotteryService lotteryService;
    private final LotteryResultClient lotteryResultClient;

    @GetMapping("/results/current-year")
    List<LotteryResult> getCurrentYearResults() {
        return lotteryService.getCurrentYearResults();
    }

    @PostMapping("/results")
    ResponseEntity<?> createResult(@RequestBody LotteryResult result) {
        return ResponseEntity.ok(lotteryService.saveNewResultAndBindNewTickets(
                result.getLottoNumbers(),
                result.getPlusNumbers(),
                result.getLotteryDate())
        );
    }

    @GetMapping("/tickets/{name}/current-year")
    List<LotteryTicket> getCurrentYearTickets(@PathVariable String name) {
        return lotteryService.getCurrentYearAndNewTickets(name);
    }

    @PostMapping("/tickets/{name}")
    ResponseEntity<?> createResult(@PathVariable String name, @RequestBody TicketNumbers numbers) {
        return ResponseEntity.ok(lotteryService.saveTicketIfNewNotExist(name, numbers));
    }

    @GetMapping("/process/fetch-last-lottery-results")
    LotteryResult downloadLastLotteryResult() {
        return lotteryResultClient.downloadLastLotteryResult();
    }

    @GetMapping("/process/generate-random-numbers")
    LotteryNumbers generateRandom() {
        return TicketNumbers.generateRandomNumbers();
    }

    @GetMapping("/process/generate-delta-numbers")
    LotteryNumbers generateDelta() {
        return TicketNumbers.generateDeltaNumbers();
    }

    @ExceptionHandler({LotteryNumbersException.class, IllegalArgumentException.class})
    CustomError handleException(Exception e, HttpServletRequest request) {
        return new CustomError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getServletPath());
    }

    @RequiredArgsConstructor
    @Getter
    private static final class CustomError {
        private final int status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private final LocalDateTime timestamp = LocalDateTime.now();
        private final String message;
        private final String path;
    }
}
