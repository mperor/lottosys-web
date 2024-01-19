package mapi.lotto.model;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import jakarta.persistence.*;

@Entity
@Table(name = "math_ticket")
public class MathTicket implements Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "math_ticket_id", nullable = false, unique = true)
    private long mathTicketId;

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

    protected MathTicket() {
    }

    public MathTicket(int n1, int n2, int n3, int n4, int n5, int n6) {
	this.n1 = n1;
	this.n2 = n2;
	this.n3 = n3;
	this.n4 = n4;
	this.n5 = n5;
	this.n6 = n6;
    }

    public MathTicket(int[] array) {
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

    public long getMathTicketId() {
	return mathTicketId;
    }

    @Override
    public LottoPlusStatement getLottoPlusStatement() {
	return lottoPlusStatement;
    }

    @Override
    public void setLottoPlusStatement(LottoPlusStatement lottoPlusStatement) {
	this.lottoPlusStatement = lottoPlusStatement;
    }

    @Override
    public int getN1() {
	return n1;
    }

    @Override
    public void setN1(int n1) {
	this.n1 = n1;
    }

    @Override
    public int getN2() {
	return n2;
    }

    @Override
    public void setN2(int n2) {
	this.n2 = n2;
    }

    @Override
    public int getN3() {
	return n3;
    }

    @Override
    public void setN3(int n3) {
	this.n3 = n3;
    }

    @Override
    public int getN4() {
	return n4;
    }

    @Override
    public void setN4(int n4) {
	this.n4 = n4;
    }

    @Override
    public int getN5() {
	return n5;
    }

    @Override
    public void setN5(int n5) {
	this.n5 = n5;
    }

    @Override
    public int getN6() {
	return n6;
    }

    @Override
    public void setN6(int n6) {
	this.n6 = n6;
    }

    public int[] getMathTicketArray() {
	return new int[]{n1, n2, n3, n4, n5, n6};
    }

    @Override
    public Set<Integer> getTicketSet() {
	return new TreeSet<>(Arrays.asList(new Integer[]{n1, n2, n3, n4, n5, n6}));
    }

}
