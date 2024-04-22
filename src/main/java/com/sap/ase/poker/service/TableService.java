package com.sap.ase.poker.service;

import com.sap.ase.poker.Actions;
import com.sap.ase.poker.model.*;
import com.sap.ase.poker.model.deck.Card;
import com.sap.ase.poker.model.deck.Deck;
import com.sap.ase.poker.model.rules.HandRules;
import com.sap.ase.poker.model.rules.WinnerRules;
import com.sap.ase.poker.model.rules.Winners;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
public class TableService {

    private final Supplier<Deck> deckSupplier;
    private GameState gameState = GameState.OPEN;
    private ArrayList<Player> playerArrayList = new ArrayList<Player>();

    private ArrayList<Card> communityCards = new ArrayList<Card>();
    private Player currentPlayer;

    private Bets currentBets;
    private Pot currentPot;

    public TableService(Supplier<Deck> deckSupplier) {
        this.deckSupplier = deckSupplier;
    }

    public GameState getState() {
        // TODO: implement me
        return this.gameState;
    }

    public List<Player> getPlayers() {
        // TODO: implement me
        return playerArrayList;
    }

    public List<Card> getPlayerCards(String playerId) {
        // TODO: implement me
        List<Card> playerHand = new ArrayList<Card>();
        for (Player player: playerArrayList)
        {
            if (player.getId().equals(playerId))
                playerHand = player.getHandCards();
        }
        return playerHand;
    }

    public List<Card> getCommunityCards() {
        // TODO: implement me
        return communityCards;
    }

    public Optional<Player> getCurrentPlayer() {
        // TODO: implement me
        return Optional.of(currentPlayer);
    }

    public Map<String, Integer> getBets() {
        // TODO: implement me
        return currentBets.getAllCurrentBets();
    }

    public int getPot() {
        // TODO: implement me
        return currentPot.getPot();
    }

    public Optional<Player> getWinner() {
        // TODO: implement me
        WinnerRules winnerRules = new WinnerRules(new HandRules() );
        List<Player> activePlayers = new ArrayList<>();
        for(Player player : playerArrayList)
        {
            if(player.isActive())
            {
                activePlayers.add(player);
            }
        }
        Winners winners = winnerRules.findWinners(communityCards,activePlayers);
        return Optional.of(winners.getWinners().get(0));
    }

    public List<Card> getWinnerHand() {
        // TODO: implement me
        return getWinner().get().getHandCards();
    }

    public void start() {
        // TODO: implement me
        if (getPlayers().size() > 1)
        {
            gameState = GameState.PRE_FLOP;
            Deck deck = deckSupplier.get();
            deck.shuffle();
            for (Player player: getPlayers() )
            {
                player.setActive();
                List<Card> hand = new ArrayList<Card>();
                hand.add(deck.draw());
                hand.add(deck.draw());
                player.setHandCards(hand);
            }
            currentBets= new Bets();
            currentPot = new Pot();
            currentPlayer = getPlayers().get(0);
        }
        else
        {

        }

    }

    public void addPlayer(String playerId, String playerName) {
        // TODO: implement me
        Player player = new Player(playerId,playerName,100);
        player.setInactive();
        playerArrayList.add(player);
        System.out.printf("Player joined the table: %s%n", playerId);
    }

    public void performAction(String action, int amount) throws IllegalAmountException,IllegalActionException {
        // TODO: implement me
        switch (action)
        {
            case "check":
                if (Actions.canCheck(currentBets))
                {
                    currentPlayer.bet(0);
                    currentBets.addBet(currentPlayer.getName(), currentPlayer.getBet());
                }
                else
                {
                    throw new IllegalActionException("Bets have been placed this round");
                }
                break;
            case "fold":
                if(Actions.canFold(playerArrayList)) {
                    currentPlayer.setInactive();
                    currentPot.addToPot(currentBets.getBetForId(currentPlayer.getId()));
                    currentBets.removeBetForId(currentPlayer.getId());
                }
                else
                {
                    gameState = GameState.ENDED;
                }
                break;
            case "raise":
                if (!Actions.exceedsAnyPlayersCash(playerArrayList, amount))
                {
                    if (Actions.ableToRaise(currentBets, currentPlayer, amount))
                    {
                        Integer raiseAmount = amount - currentBets.currentHighestBet();
                        currentPlayer.bet(raiseAmount);
                        currentBets.addBet(currentPlayer.getId(), currentPlayer.getBet());
                    } else
                    {
                        throw new IllegalAmountException("invalid raise attempt");
                    }
                }
                else
                {
                    throw new IllegalAmountException("Raise exceeds at least one player's cash");
                }
                break;
            case "call":
                Integer callAmount = currentBets.currentHighestBet() - currentPlayer.getBet();
                currentPlayer.bet(callAmount);
                currentBets.addBet(currentPlayer.getId(), callAmount);
                break;
        }
        updateCurrentPlayer();

        if (bettingRoundCanEnd())
        {
            currentPot.addToPot(currentBets);
            currentBets.clearAllBets();
            setGameState();
        }

        System.out.printf("Action performed: %s, amount: %d%n", action, amount);
    }

    private Boolean bettingRoundCanEnd()
    {
        Boolean roundFinished = false;
        Integer numberActivePlayers = 0;
        for(Player player : playerArrayList)
        {
            if (player.isActive())
            {
                numberActivePlayers++;
            }
        }
        if (currentBets.getAllCurrentBets().size() == numberActivePlayers )
        {
            roundFinished = currentBets.allBetsEqual();
        }
        return roundFinished;
    }

    public void updateCurrentPlayer()
    {
        Integer i = playerArrayList.indexOf(currentPlayer);
        if (i < (playerArrayList.size()-1))
        {
            i += 1;
            currentPlayer = playerArrayList.get(i);
        }
        else
        {
            currentPlayer = playerArrayList.get(0);
        }
    }

    private void setGameState()
    {
        switch (getState())
        {
            case OPEN -> {gameState = GameState.PRE_FLOP;}
            case PRE_FLOP -> {gameState = GameState.FLOP;}
            case FLOP -> {communityCards.add(deckSupplier.get().draw());
                          communityCards.add(deckSupplier.get().draw());
                          communityCards.add(deckSupplier.get().draw());
                          gameState = GameState.TURN;}
            case TURN -> {communityCards.add(deckSupplier.get().draw());
                          gameState = GameState.RIVER;}
            case RIVER ->  {communityCards.add(deckSupplier.get().draw());
                            gameState = GameState.ENDED;}
            case ENDED -> {getWinner(); getWinnerHand();}
        }

    }

}
