package com.sap.ase.poker.model;

public class Pot {

    private Integer pot;

    public Pot()
    {
        this.pot = 0;
    }

    public void addToPot( Integer amount)
    {
        this.pot += amount;
    }

    public void addToPot(Bets currentBets)
    {
        this.pot += currentBets.getAllCurrentBets().values().stream().mapToInt(d -> d).sum();;
    }

    public Integer getPot()
    {
        return this.pot;
    }

}
