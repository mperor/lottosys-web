package mapi.lotto.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mapi.lotto.model.ticket.*;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum TicketType {

    RANDOM(Constants.RANDOM, RandomTicket::new),
    STATIC(Constants.STATIC, StaticTicket::new),
    MATH(Constants.MATH, MathTicket::new);

    private final String name;
    private final Function<TicketNumbers, LotteryTicket> creator;

    public static TicketType fromString(String name) {
        if (name != null) {
            for (TicketType t : TicketType.values()) {
                if (t.getName().equals(name)) {
                    return t;
                }
            }
        }
        throw new IllegalArgumentException("Ticket type name '%s' has not been found!".formatted(name));
    }

    public static class Constants {
        public static final String RANDOM = "random";
        public static final String STATIC = "static";
        public static final String MATH = "math";
    }
}
