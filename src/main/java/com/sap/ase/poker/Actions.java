package com.sap.ase.poker;

import com.sap.ase.poker.model.Bets;
import com.sap.ase.poker.model.Player;

import java.util.ArrayList;

public class Actions {

    public static Boolean canCheck(Bets currentBets)
    {
        Boolean emptyBets = currentBets.getAllCurrentBets().isEmpty();
        Boolean noValuedBets = 0 == currentBets.getAllCurrentBets().values().stream().mapToInt(d -> d).sum();;

        return emptyBets || noValuedBets;
    }

    public static Boolean ableToRaise(Bets currentBets, Player currentPlayer, Integer amount)
    {
        return currentBets.currentHighestBet() < amount && currentPlayer.getCash() > amount;
    }

    public static Boolean exceedsAnyPlayersCash(ArrayList<Player> playerArrayList, Integer amount)
    {
        Boolean raiseExceedsAnyPlayer = false;
        for(Player player : playerArrayList)
        {
            raiseExceedsAnyPlayer = player.getCash() < amount;
            if (raiseExceedsAnyPlayer)
            {
                break;
            }
        }
        return raiseExceedsAnyPlayer;
    }

    public static Boolean canFold(ArrayList<Player> playerArrayList)
    {
        Integer remainingPlayers = 0;
        for(Player player : playerArrayList)
        {
            remainingPlayers += player.isActive() ? 1 : 0;
        }
        return remainingPlayers > 1;
    }
}
