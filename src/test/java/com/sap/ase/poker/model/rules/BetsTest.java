package com.sap.ase.poker.model.rules;

import com.sap.ase.poker.model.Bets;
import com.sap.ase.poker.model.Pot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class BetsTest {

    private Bets currentBets;
    private Pot currentPot;

    @BeforeEach
    void setUp()
    {
        currentPot = new Pot();
        currentBets = new Bets();
    }

    @Test
    public void npBets()
    {
        assertThat(currentBets.getAllCurrentBets().size()).isEqualTo(0);
    }

    @Test
    public void singleBet()
    {
        currentBets.addBet("playerOne", 23);
        assertThat(currentBets.getAllCurrentBets().size()).isEqualTo(1);
        assertThat(currentBets.currentHighestBet()).isEqualTo(23);
        assertThat(currentBets.getBetForId("playerOne")).isEqualTo(23);
    }

    @Test
    public void multipleBets()
    {
        currentBets.addBet("playerOne", 10);
        currentBets.addBet("playerTwo", 20);
        currentBets.addBet("playerThree", 30);
        currentBets.addBet("playerFour", 400);
        assertThat(currentBets.getAllCurrentBets().size()).isEqualTo(4);
        assertThat(currentBets.currentHighestBet()).isEqualTo(400);
        assertThat(currentBets.getBetForId("playerOne")).isEqualTo(10);
        assertThat(currentBets.getBetForId("playerTwo")).isEqualTo(20);
        assertThat(currentBets.getBetForId("playerThree")).isEqualTo(30);
        assertThat(currentBets.getBetForId("playerFour")).isEqualTo(400);
        assertThat(currentBets.getBetForId("playerFive")).isEqualTo(null);
    }

    @Test
    public void clearTheBets()
    {
        currentBets.addBet("playerOne", 10);
        currentBets.addBet("playerTwo", 20);
        currentBets.addBet("playerThree", 30);
        currentBets.addBet("playerFour", 400);
        assertThat(currentBets.getAllCurrentBets().size()).isEqualTo(4);
        assertThat(currentBets.currentHighestBet()).isEqualTo(400);
        assertThat(currentBets.getBetForId("playerOne")).isEqualTo(10);
        assertThat(currentBets.getBetForId("playerTwo")).isEqualTo(20);
        assertThat(currentBets.getBetForId("playerThree")).isEqualTo(30);
        assertThat(currentBets.getBetForId("playerFour")).isEqualTo(400);
        currentBets.clearAllBets();
        assertThat(currentBets.getAllCurrentBets().size()).isEqualTo(0);
        assertThat(currentBets.currentHighestBet()).isEqualTo(0);

    }
}
