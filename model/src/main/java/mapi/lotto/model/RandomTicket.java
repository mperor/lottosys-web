package mapi.lotto.model;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import jakarta.persistence.*;

@Entity
@Table(name = "random_ticket")
public class RandomTicket implements Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "random_ticket_id", nullable = false, unique = true)
    private long randomTicketId;

    @Column
    private int n1;

    @Column
    private int n2;

    @Column
    private int n3;

    @Column
    private int n4;

    @Column
    private int n5;

    @Column
    private int n6;

    @OneToOne
    @JoinColumn(name = "lpsId", nullable = true)
    private LottoPlusStatement lottoPlusStatement;

    protected RandomTicket() {
    }

    public RandomTicket(int r1, int r2, int r3, int r4, int r5, int r6) {
	this.n1 = r1;
	this.n2 = r2;
	this.n3 = r3;
	this.n4 = r4;
	this.n5 = r5;
	this.n6 = r6;
    }

    public RandomTicket(int[] array) {
	if (array.length != 6) {
	    throw new IllegalArgumentException("Incorrect array elements !");
	}
	this.n1 = array[0];
	this.n2 = array[1];
	this.n3 = array[2];
	this.n4 = array[3];
	this.n5 = array[4];
	this.n6 = array[5];
    }

    public long getRandomTicketId() {
	return randomTicketId;
    }

    public LottoPlusStatement getLottoPlusStatement() {
	return lottoPlusStatement;
    }

    public void setLottoPlusStatement(LottoPlusStatement lottoPlusStatement) {
	this.lottoPlusStatement = lottoPlusStatement;
    }

    public int getN1() {
	return n1;
    }

    public void setN1(int n1) {
	this.n1 = n1;
    }

    public int getN2() {
	return n2;
    }

    public void setN2(int n2) {
	this.n2 = n2;
    }

    public int getN3() {
	return n3;
    }

    public void setN3(int n3) {
	this.n3 = n3;
    }

    public int getN4() {
	return n4;
    }

    public void setN4(int n4) {
	this.n4 = n4;
    }

    public int getN5() {
	return n5;
    }

    public void setN5(int n5) {
	this.n5 = n5;
    }

    public int getN6() {
	return n6;
    }

    public void setN6(int n6) {
	this.n6 = n6;
    }

    public int[] getRandomTicketArray() {
	return new int[]{n1, n2, n3, n4, n5, n6};
    }

    public Set<Integer> getTicketSet() {
	return new TreeSet<>(Arrays.asList(new Integer[]{n1, n2, n3, n4, n5, n6}));
    }

}
