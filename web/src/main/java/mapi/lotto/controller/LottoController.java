package mapi.lotto.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapi.lotto.model.LotteryNumbersException;
import mapi.lotto.model.result.LottoNumbers;
import mapi.lotto.model.result.PlusNumbers;
import mapi.lotto.model.statistic.LotteryStatistic;
import mapi.lotto.model.ticket.LotteryTicket;
import mapi.lotto.model.ticket.TicketNumbers;
import mapi.lotto.service.LotteryService;
import mapi.lotto.service.StatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class LottoController {

    private final LotteryService lotteryService;
    private final StatisticService statisticService;

    @GetMapping
    public String homePage() {
        return "index";
    }

    @GetMapping("/add")
    public String add() {
        return "add";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "logout";
    }

    @GetMapping(value = {"/random", "/math", "/static"})
    public String showSystem(Model model,
                             HttpServletRequest request,
                             @RequestParam(required = false) Integer year) {

        String name = request.getServletPath().replace("/", "");
        List<LotteryTicket> tickets = year == null
                ? lotteryService.getCurrentYearAndNewTickets(name)
                : lotteryService.getTicketsOfYear(name, Year.of(year));

        List<Integer> lotteryYears = statisticService.getActiveLotteryYears(name);

        model.addAttribute("lotteryYears", lotteryYears);
        model.addAttribute("tickets", tickets);
        model.addAttribute("name", name);
        model.addAttribute("colorClass", "%s-color".formatted(name));
        model.addAttribute("backgroundClass", "%s-bg".formatted(name));
        model.addAttribute("statsRef", "/%s-stats".formatted(name));
        model.addAttribute("selectedYear", year == null ? lotteryYears.get(0) : year);
        return "system";
    }

    @GetMapping("/{name}-stats")
    public String showStatistic(Model model, @PathVariable("name") String name) {

        List<LotteryStatistic> lotteryStatistics = statisticService.getStaticStatsByTicketName(name);
        LotteryStatistic currentYearStatistic = statisticService.getCurrentYearDynamicStatsByTicketName(name);
        if (currentYearStatistic != null) {
            lotteryStatistics.add(currentYearStatistic);
        }

        model.addAttribute("lotteryStatistics", lotteryStatistics);
        model.addAttribute("name", name);
        model.addAttribute("colorClass", "%s-color".formatted(name));
        model.addAttribute("backgroundClass", "%s-bg".formatted(name));
        return "stats";
    }

    @PostMapping("/lotto")
    public String postLotto(@RequestParam Integer l1,
                            @RequestParam Integer l2,
                            @RequestParam Integer l3,
                            @RequestParam Integer l4,
                            @RequestParam Integer l5,
                            @RequestParam Integer l6,
                            @RequestParam Integer p1,
                            @RequestParam Integer p2,
                            @RequestParam Integer p3,
                            @RequestParam Integer p4,
                            @RequestParam Integer p5,
                            @RequestParam Integer p6,
                            @RequestParam LocalDate date) {

        boolean isSaved = lotteryService.saveNewResultAndBindNewTickets(new LottoNumbers(l1, l2, l3, l4, l5, l6), new PlusNumbers(p1, p2, p3, p4, p5, p6), date);
        return isSaved ? "redirect:/" : "redirect:/add";
    }

    @PostMapping("{name}")
    public String postTicket(@PathVariable("name") String name,
                             @RequestParam Integer n1,
                             @RequestParam Integer n2,
                             @RequestParam Integer n3,
                             @RequestParam Integer n4,
                             @RequestParam Integer n5,
                             @RequestParam Integer n6) {

        boolean isSaved = lotteryService.saveTicketIfNewNotExist(name, new TicketNumbers(n1, n2, n3, n4, n5, n6));
        return isSaved ? "redirect:/%s".formatted(name) : "redirect:/add";
    }

    @ModelAttribute("year")
    public Year getCurrentYear() {
        return Year.now();
    }

    @ExceptionHandler(LotteryNumbersException.class)
    public String handleException() {
        return "redirect:/add";
    }

}
