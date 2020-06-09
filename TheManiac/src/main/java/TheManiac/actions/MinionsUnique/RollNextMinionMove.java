package TheManiac.actions.MinionsUnique;

import TheManiac.minions.AbstractManiacMinion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RollNextMinionMove extends AbstractGameAction {
    public AbstractManiacMinion minion;
    private boolean forceMove;
    
    public RollNextMinionMove(AbstractManiacMinion minion) {
        this.minion = minion;
        this.forceMove = false;
    }

    public RollNextMinionMove(AbstractManiacMinion minion, boolean forceMove) {
        this.minion = minion;
        this.forceMove = forceMove;
    }
    
    @Override
    public void update() {
        if (shouldFinishCombat()) {
            this.isDone = true;
            return;
        }
        
        if (forceMove) {
            this.minion.forceNextMove(false);
        } else {
            this.minion.rollNextMinionMove();
        }
        this.isDone = true;
    }
    
    private boolean shouldFinishCombat() {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!(monster instanceof AbstractManiacMinion) && !monster.isDying && !monster.isEscaping) {
                return false;
            }
        }
        
        return true;
    }
}
