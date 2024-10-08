package CtrlS;

import engine.Core;
import engine.GameState;

public class RoundState {
    private final GameState prevState;
    private final GameState currState;
    private final int roundScore;
    private final int roundBulletsShot;
    private final int roundShipsDestroyed;
    private final int roundCurrency;
    private final float roundHitRate;
    private final long roundTime;

    public RoundState(GameState prevState, GameState currState) {
        this.prevState = prevState;
        this.currState = currState;
        this.roundScore = currState.getScore() - prevState.getScore();
        this.roundBulletsShot = currState.getBulletsShot() - prevState.getBulletsShot();
        this.roundShipsDestroyed = currState.getShipsDestroyed() - prevState.getShipsDestroyed();
        this.roundHitRate = roundShipsDestroyed / (float) roundBulletsShot;
        this.roundTime = currState.getTime() - prevState.getTime();
        this.roundCurrency = calculateCurrency();
    }

    private int calculateCurrency() {

        int baseCurrency = roundScore / 10;
        int levelBonus = baseCurrency * currState.getLevel();
        int currency = baseCurrency + levelBonus;

        if (roundHitRate > 0.9) {
            currency += (int) (currency * 0.3); // 30% 보너스 지급
            Core.getLogger().info("hitRate bonus occurs (30%).");
        } else if (roundHitRate > 0.8) {
            currency += (int) (currency * 0.2); // 20% 보너스 지급
            Core.getLogger().info("hitRate bonus occurs (20%).");
        }

        // Round clear time in seconds
        // DEBUGGING NEEDED(playTime)
        long timeDifferenceInSeconds = (currState.getTime() - prevState.getTime()) / 1000;

        int timeBonus = 0;

        /*
          clear time   : 0 ~ 50    : +50
                       : 51 ~ 80   : +30
                       : 81 ~ 100  : +10
                       : 101 ~     : 0
         */
        if (timeDifferenceInSeconds <= 50) {
            timeBonus = 50;
        } else if (timeDifferenceInSeconds <= 80) {
            timeBonus = 30;
        } else if (timeDifferenceInSeconds <= 100) {
            timeBonus = 10;
        }
        currency += timeBonus;

        return currency;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public float getRoundHitRate() {
        return roundHitRate;
    }

    public long getRoundTime() {
        return roundTime;
    }

    public int getRoundCurrency() {
        return roundCurrency;
    }
}
