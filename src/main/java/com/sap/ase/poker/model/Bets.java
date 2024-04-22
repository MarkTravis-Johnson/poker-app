package com.sap.ase.poker.model;

import java.util.HashMap;
import java.util.Map;

public class Bets {
    private HashMap<String, Integer> currentBets;
    private Integer highestBet;

    public Bets()
    {
        this.currentBets = new HashMap<String, Integer>();
        this.highestBet = 0;
    }

    public void addBet(String id, Integer amount)
    {
        Integer newAmount = amount;
        //if the id already exists then this is a call or raise bet
        if(this.currentBets.containsKey(id))
        {
            newAmount += this.currentBets.get(id);
            this.currentBets.put(id,  newAmount);
        }
        else
        {
            this.currentBets.put(id, newAmount);
        }
        this.highestBet = newAmount > this.highestBet ? newAmount : this.highestBet;
    }

    public HashMap<String, Integer> getAllCurrentBets() {
        return currentBets;
    }

    public Integer currentHighestBet()
    {
        return this.highestBet;
    }

    public void clearAllBets()
    {
        this.highestBet = 0;
        this.currentBets.clear();
    }

    public Integer getBetForId(String id)
    {
        return this.currentBets.get(id);
    }

    public void removeBetForId(String id)
    {
        this.currentBets.remove(id);
    }

    public Boolean allBetsEqual()
    {
        String firstId = (String) currentBets.keySet().toArray()[0];
        Integer firstAmount = currentBets.get(firstId);
        return  currentBets.values().stream().allMatch(n-> {
            if (n > 0) {
                return firstAmount / n == 1;
            } else {
                return n.equals(firstAmount);
            }
        });
    }
}
