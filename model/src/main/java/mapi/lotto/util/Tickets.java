package mapi.lotto.util;

public enum Tickets {
    RANDOM("random"),
    STATIC("static"),
    MATH("math");

    private String name;

    private Tickets(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public static Tickets fromString(String name) {
	if (name != null) {
	    for (Tickets t : Tickets.values()) {
		if (t.getName().equals(name)) {
		    return t;
		}
	    }
	}
	throw new IllegalArgumentException("Name has not been found!");
    }

}
