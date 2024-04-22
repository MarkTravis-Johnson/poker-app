package com.sap.ase.poker.model.rules;

import com.sap.ase.poker.model.Bets;
import com.sap.ase.poker.model.Pot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PotsTest {

    private Pot currentPot;
    private Bets currentBets;

    @BeforeEach
    void setUp()
    {
        currentPot = new Pot();
        currentBets = new Bets();
    }

    @Test
    public void getEmptyPot()
    {
        assertThat(currentPot.getPot()).isEqualTo(0);
    }

    @Test
    public void singleBetAddedToPot()
    {
        currentPot.addToPot(10);
        assertThat(currentPot.getPot()).isEqualTo(10);
    }

    @Test
    public void allBetsAddedToPot()
    {
        currentBets.addBet("playerOne", 10);
        currentBets.addBet("playerTwo", 10);
        currentBets.addBet("playerOne", 10);
        currentBets.addBet("playerTwo", 10);
        currentPot.addToPot(currentBets);
        assertThat(currentPot.getPot()).isEqualTo(40);
    }
}
