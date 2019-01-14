package mapi.lotto.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mapi.lotto.model.LottoPlusStatement;
import mapi.lotto.model.Stat;
import mapi.lotto.model.Ticket;
import mapi.lotto.service.LottoService;
import mapi.lotto.util.LottoUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LottoController {

    @Autowired
    private LottoService lottoService;

    @RequestMapping("/")
    public ModelAndView getIndexSite() {
	ModelAndView mav = new ModelAndView("index");
	mav.addObject("year", LottoUtil.getCurrentYear());
	return mav;
    }

    @RequestMapping(value = {"/random", "/math", "/static"}, method = RequestMethod.GET)
    public ModelAndView getSystemSite(HttpServletRequest request) {
	String name = request.getServletPath().replace("/", "");
	String colorClass = name + "-color";
	String backgroundClass = name + "-bg";
	String statsRef = request.getServletPath() + "-stats";

	List<LottoPlusStatement> statements = (List<LottoPlusStatement>) lottoService.getThisYearStatements();
	Iterable<Ticket> tickets = (Iterable<Ticket>) lottoService.getThisYearTickets(name);

	ModelAndView mav = new ModelAndView("system");
	mav.addObject("statements", statements);
	mav.addObject("tickets", tickets);
	mav.addObject("name", name);
	mav.addObject("colorClass", colorClass);
	mav.addObject("backgroundClass", backgroundClass);
	mav.addObject("statsRef", statsRef);
	mav.addObject("year", LottoUtil.getCurrentYear());
	return mav;
    }

    @RequestMapping(value = {"/random-stats", "/static-stats", "/math-stats"}, method = RequestMethod.GET)
    public ModelAndView getStatsSite(HttpServletRequest request) {
	String name = request.getServletPath().replace("/", "");
	name = name.split("-")[0];
	String colorClass = name + "-color";
	String backgroundClass = name + "-bg";

	List<Stat> stats = lottoService.getStaticStatsByTicketName(name);
	if (lottoService.getDynamicStatByTicketName(name) != null) {
	    stats.add(lottoService.getDynamicStatByTicketName(name));
	}
	ModelAndView mav = new ModelAndView("stats");
	mav.addObject("stats", stats);
	mav.addObject("name", name);
	mav.addObject("colorClass", colorClass);
	mav.addObject("backgroundClass", backgroundClass);
	mav.addObject("year", LottoUtil.getCurrentYear());
	return mav;
    }

    @RequestMapping(value = {"/lotto"}, method = RequestMethod.POST)
    public String postLotto(@RequestParam(value = "l1") String l1,
	    @RequestParam(value = "l2") String l2,
	    @RequestParam(value = "l3") String l3,
	    @RequestParam(value = "l4") String l4,
	    @RequestParam(value = "l5") String l5,
	    @RequestParam(value = "l6") String l6,
	    @RequestParam(value = "p1") String p1,
	    @RequestParam(value = "p2") String p2,
	    @RequestParam(value = "p3") String p3,
	    @RequestParam(value = "p4") String p4,
	    @RequestParam(value = "p5") String p5,
	    @RequestParam(value = "p6") String p6,
	    @RequestParam(value = "date") String date) {

	String[] params = new String[]{l1, l2, l3, l4, l5, l6, p1, p2, p3, p4, p5, p6};
	for (String param : params) {
	    if (param == null || !LottoUtil.isNaturalNumber(param)) {
		return "redirect:/add";
	    }
	}

	boolean isSaved;
	isSaved = lottoService.saveStatement(new int[]{Integer.valueOf(l1), Integer.valueOf(l2), Integer.valueOf(l3), Integer.valueOf(l4), Integer.valueOf(l5), Integer.valueOf(l6)},
		new int[]{Integer.valueOf(p1), Integer.valueOf(p2), Integer.valueOf(p3), Integer.valueOf(p4), Integer.valueOf(p5), Integer.valueOf(p6)}, date);
	return isSaved ? "redirect:/" : "redirect:/add";
    }

    @RequestMapping(value = {"/random", "/math", "/static"}, method = RequestMethod.POST)
    public String postTicket(@RequestParam(value = "n1") String n1,
	    @RequestParam(value = "n2") String n2,
	    @RequestParam(value = "n3") String n3,
	    @RequestParam(value = "n4") String n4,
	    @RequestParam(value = "n5") String n5,
	    @RequestParam(value = "n6") String n6,
	    HttpServletRequest request) {

	String name = request.getServletPath().replace("/", "");
	boolean isSaved;
	String[] params = new String[]{n1, n2, n3, n4, n5, n6};
	for (String param : params) {
	    if (param == null || !LottoUtil.isNaturalNumber(param)) {
		return "redirect:/add";
	    }
	}

	isSaved = lottoService.saveTicket(name, new int[]{Integer.valueOf(n1), Integer.valueOf(n2), Integer.valueOf(n3), Integer.valueOf(n4), Integer.valueOf(n5), Integer.valueOf(n6)});
	return isSaved ? "redirect:/" + name : "redirect:/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {
	ModelAndView mav = new ModelAndView("add");
	mav.addObject("year", LottoUtil.getCurrentYear());
	return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
	ModelAndView mav = new ModelAndView("login");
	mav.addObject("year", LottoUtil.getCurrentYear());
	return mav;
    }

}
