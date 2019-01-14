package mapi.lotto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stats", schema = "lotto")
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column
    private Short year;

    @Column
    private int p0;

    @Column
    private int p1;

    @Column
    private int p2;

    @Column
    private int p3;

    @Column
    private int p4;

    @Column
    private int p5;

    @Column
    private int p6;

    @Column(name = "p_acc")
    private double pACC;

    @Column
    private int pBank;

    @Column
    private int l0;

    @Column
    private int l1;

    @Column
    private int l2;

    @Column
    private int l3;

    @Column
    private int l4;

    @Column
    private int l5;

    @Column
    private int l6;

    @Column
    private int lAll;

    @Column(name = "l_acc")
    private double lACC;

    @Column
    private int lBank;

    @Column
    private int ticketOrdinal;

    protected Stat() {
    }

    protected Stat(Short year, double pACC, int pBank, int lAll, double lACC, int lBank) {
	this.year = year;
	this.pACC = pACC;
	this.pBank = pBank;
	this.lAll = lAll;
	this.lACC = lACC;
	this.lBank = lBank;
    }

    protected Stat(Short year, double pACC, int pBank, int lAll, double lACC, int lBank, int ticketNumber) {
	this.year = year;
	this.pACC = pACC;
	this.pBank = pBank;
	this.lAll = lAll;
	this.lACC = lACC;
	this.lBank = lBank;
	this.ticketOrdinal = ticketNumber;
    }

    public Stat(short year, double plusAcc, int plusBank, int all, double lottoAcc, int lottoBank, int[] lottoStatArray, int[] plusStatArray) {
	this(year, plusAcc, plusBank, all, lottoAcc, lottoBank);
	if (lottoStatArray.length != 7 || plusStatArray.length != 7) {
	    throw new IllegalArgumentException("lotto and plus stat array size must be equal 7");
	} else {
	    this.l0 = lottoStatArray[0];
	    this.l1 = lottoStatArray[1];
	    this.l2 = lottoStatArray[2];
	    this.l3 = lottoStatArray[3];
	    this.l4 = lottoStatArray[4];
	    this.l5 = lottoStatArray[5];
	    this.l6 = lottoStatArray[6];
	    this.p0 = plusStatArray[0];
	    this.p1 = plusStatArray[1];
	    this.p2 = plusStatArray[2];
	    this.p3 = plusStatArray[3];
	    this.p4 = plusStatArray[4];
	    this.p5 = plusStatArray[5];
	    this.p6 = plusStatArray[6];
	}
    }

    public Stat(short year, double plusAcc, int plusBank, int all, double lottoAcc, int lottoBank, int[] lottoStatArray, int[] plusStatArray, int ticketNumber) {
	this(year, plusAcc, plusBank, all, lottoAcc, lottoBank, ticketNumber);
	if (lottoStatArray.length != 7 || plusStatArray.length != 7) {
	    throw new IllegalArgumentException("lotto and plus stat array size must be equal 7");
	} else {
	    this.l0 = lottoStatArray[0];
	    this.l1 = lottoStatArray[1];
	    this.l2 = lottoStatArray[2];
	    this.l3 = lottoStatArray[3];
	    this.l4 = lottoStatArray[4];
	    this.l5 = lottoStatArray[5];
	    this.l6 = lottoStatArray[6];
	    this.p0 = plusStatArray[0];
	    this.p1 = plusStatArray[1];
	    this.p2 = plusStatArray[2];
	    this.p3 = plusStatArray[3];
	    this.p4 = plusStatArray[4];
	    this.p5 = plusStatArray[5];
	    this.p6 = plusStatArray[6];
	}
    }

    public long getId() {
	return id;
    }

    public Short getYear() {
	return year;
    }

    public void setYear(Short year) {
	this.year = year;
    }

    public int getP0() {
	return p0;
    }

    public void setP0(int p0) {
	this.p0 = p0;
    }

    public int getP1() {
	return p1;
    }

    public void setP1(int p1) {
	this.p1 = p1;
    }

    public int getP2() {
	return p2;
    }

    public void setP2(int p2) {
	this.p2 = p2;
    }

    public int getP3() {
	return p3;
    }

    public void setP3(int p3) {
	this.p3 = p3;
    }

    public int getP4() {
	return p4;
    }

    public void setP4(int p4) {
	this.p4 = p4;
    }

    public int getP5() {
	return p5;
    }

    public void setP5(int p5) {
	this.p5 = p5;
    }

    public int getP6() {
	return p6;
    }

    public void setP6(int p6) {
	this.p6 = p6;
    }

    public double getpACC() {
	return pACC;
    }

    public void setpACC(double pACC) {
	this.pACC = pACC;
    }

    public int getpBank() {
	return pBank;
    }

    public void setpBank(int pBank) {
	this.pBank = pBank;
    }

    public int getL0() {
	return l0;
    }

    public void setL0(int l0) {
	this.l0 = l0;
    }

    public int getL1() {
	return l1;
    }

    public void setL1(int l1) {
	this.l1 = l1;
    }

    public int getL2() {
	return l2;
    }

    public void setL2(int l2) {
	this.l2 = l2;
    }

    public int getL3() {
	return l3;
    }

    public void setL3(int l3) {
	this.l3 = l3;
    }

    public int getL4() {
	return l4;
    }

    public void setL4(int l4) {
	this.l4 = l4;
    }

    public int getL5() {
	return l5;
    }

    public void setL5(int l5) {
	this.l5 = l5;
    }

    public int getL6() {
	return l6;
    }

    public void setL6(int l6) {
	this.l6 = l6;
    }

    public int getlAll() {
	return lAll;
    }

    public void setlAll(int lAll) {
	this.lAll = lAll;
    }

    public double getlACC() {
	return lACC;
    }

    public void setlACC(double lACC) {
	this.lACC = lACC;
    }

    public int getlBank() {
	return lBank;
    }

    public void setlBank(int lBank) {
	this.lBank = lBank;
    }

    public int getTicketOrdinal() {
	return ticketOrdinal;
    }

    public void setTicketOrdinal(int ticketOrdinal) {
	this.ticketOrdinal = ticketOrdinal;
    }

}
