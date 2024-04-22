package com.sap.ase.poker.service;

import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TableServiceTest {
    // TODO: implement me
    @MockBean
    private PokerCardsSupplier mockCardSupplier;
    @MockBean
    private CardShuffler mockCardShuffler;

    @MockBean
    private Deck mockDeck;

    @MockBean
    private ShuffledDeckSupplier deckSupplier;

    @MockBean
    TableService tableService;
    @BeforeEach
    void setUp()
    {
        mockCardSupplier = new PokerCardsSupplier();
        mockCardShuffler = new RandomCardShuffler();
        deckSupplier = new ShuffledDeckSupplier(mockCardSupplier, mockCardShuffler);
        tableService = new TableService(deckSupplier);
    }
    @Test
    void freshGameNoPlayers()
    {
        assertThat(tableService.getState()).isEqualTo(GameState.OPEN);
        assertThat(tableService.getPlayers()).isEmpty();
        tableService.start();
    }

    @Test
    void freshGameWithTwoPlayers()
    {
        tableService.addPlayer("playerOne", "playerOne");
        tableService.addPlayer("playerTwo", "playerTwo");
        assertThat(tableService.getState()).isEqualTo(GameState.OPEN);
        assertThat(tableService.getPlayers().size()).isEqualTo(2);
        tableService.start();
        assertThat(tableService.getState()).isEqualTo(GameState.PRE_FLOP);
        assertThat(tableService.getCurrentPlayer().get().getName()).isEqualTo("playerOne");
        assertThat(tableService.getCurrentPlayer().get().getHandCards().size()).isEqualTo(2);
        assertThat(tableService.getCommunityCards()).isEmpty();
        tableService.performAction("check",100);
        assertThat(tableService.getCurrentPlayer().get().getName()).isEqualTo("playerTwo");
        assertThat(tableService.getCurrentPlayer().get().getHandCards().size()).isEqualTo(2);
        tableService.performAction("check",100);
        assertThat(tableService.getState()).isEqualTo(GameState.FLOP);
        tableService.performAction("raise",10);
        tableService.performAction("call",10);
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(10);
        tableService.updateCurrentPlayer();
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(10);
        assertThat(tableService.getCommunityCards().size()).isEqualTo(3);
    }

    @Test
    void freshGameWithTwoPlayersRoundNotDone()
    {
        tableService.addPlayer("playerOne", "playerOne");
        tableService.addPlayer("playerTwo", "playerTwo");
        assertThat(tableService.getState()).isEqualTo(GameState.OPEN);
        assertThat(tableService.getPlayers().size()).isEqualTo(2);
        tableService.start();
        assertThat(tableService.getState()).isEqualTo(GameState.PRE_FLOP);
        assertThat(tableService.getCurrentPlayer().get().getName()).isEqualTo("playerOne");
        assertThat(tableService.getCurrentPlayer().get().getHandCards().size()).isEqualTo(2);
        assertThat(tableService.getCommunityCards()).isEmpty();
        tableService.performAction("check",100);
        assertThat(tableService.getCurrentPlayer().get().getName()).isEqualTo("playerTwo");
        assertThat(tableService.getCurrentPlayer().get().getHandCards().size()).isEqualTo(2);
        tableService.performAction("check",100);
        assertThat(tableService.getState()).isEqualTo(GameState.FLOP);
        tableService.performAction("raise",10);
        tableService.performAction("raise",20);
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(10);
        tableService.updateCurrentPlayer();
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(20);
        //assertThat(tableService.getCommunityCards().size()).isEqualTo(3);
    }

    @Test
    void freshGameWithTwoPlayersRaisesAndReRaisedDone()
    {
        tableService.addPlayer("playerOne", "playerOne");
        tableService.addPlayer("playerTwo", "playerTwo");
        assertThat(tableService.getState()).isEqualTo(GameState.OPEN);
        assertThat(tableService.getPlayers().size()).isEqualTo(2);
        tableService.start();
        assertThat(tableService.getState()).isEqualTo(GameState.PRE_FLOP);
        assertThat(tableService.getCurrentPlayer().get().getName()).isEqualTo("playerOne");
        assertThat(tableService.getCurrentPlayer().get().getHandCards().size()).isEqualTo(2);
        assertThat(tableService.getCommunityCards()).isEmpty();
        tableService.performAction("check",100);
        assertThat(tableService.getCurrentPlayer().get().getName()).isEqualTo("playerTwo");
        assertThat(tableService.getCurrentPlayer().get().getHandCards().size()).isEqualTo(2);
        tableService.performAction("check",100);
        assertThat(tableService.getState()).isEqualTo(GameState.FLOP);
        tableService.performAction("raise",10);
        tableService.performAction("raise",20);
        tableService.performAction("call",20);
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(20);
        tableService.updateCurrentPlayer();
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(20);
        assertThat(tableService.getCommunityCards().size()).isEqualTo(3);
    }

    @Test
    void freshGameWithTwoPlayersFullGameDone()
    {
        tableService.addPlayer("playerOne", "playerOne");
        tableService.addPlayer("playerTwo", "playerTwo");
        assertThat(tableService.getState()).isEqualTo(GameState.OPEN);
        assertThat(tableService.getPlayers().size()).isEqualTo(2);
        tableService.start();
        assertThat(tableService.getState()).isEqualTo(GameState.PRE_FLOP);
        assertThat(tableService.getCurrentPlayer().get().getName()).isEqualTo("playerOne");
        assertThat(tableService.getCurrentPlayer().get().getHandCards().size()).isEqualTo(2);
        assertThat(tableService.getCommunityCards()).isEmpty();
        tableService.performAction("check",100);
        assertThat(tableService.getCurrentPlayer().get().getName()).isEqualTo("playerTwo");
        assertThat(tableService.getCurrentPlayer().get().getHandCards().size()).isEqualTo(2);
        tableService.performAction("check",100);
        assertThat(tableService.getState()).isEqualTo(GameState.FLOP);
        tableService.performAction("raise",10);
        tableService.performAction("raise",20);
        tableService.performAction("call",20);
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(20);
        tableService.updateCurrentPlayer();
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(20);
        assertThat(tableService.getCommunityCards().size()).isEqualTo(3);
        assertThat(tableService.getState()).isEqualTo(GameState.TURN);
        tableService.performAction("raise",40);
        tableService.performAction("call",40);
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(40);
        tableService.updateCurrentPlayer();
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(40);
        assertThat(tableService.getCommunityCards().size()).isEqualTo(4);
        assertThat(tableService.getState()).isEqualTo(GameState.RIVER);
        tableService.performAction("raise",60);
        tableService.performAction("call",60);
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(60);
        tableService.updateCurrentPlayer();
        assertThat(tableService.getCurrentPlayer().get().getBet()).isEqualTo(60);
        assertThat(tableService.getCommunityCards().size()).isEqualTo(5);


    }
}