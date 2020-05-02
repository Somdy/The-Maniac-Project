package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RandomEnemyLoseHPAction extends AbstractGameAction {
    private int loseHP;

    public RandomEnemyLoseHPAction(AbstractCreature source, int loseHP, AttackEffect attackEffect) {
        this.source = source;
        this.loseHP = loseHP;
        this.actionType = ActionType.SPECIAL;
        this.attackEffect = attackEffect;
    }
    @Override
    public void update() {
        this.target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null && this.target.currentHealth > 0) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this.target, this.source, this.loseHP, this.attackEffect));
        }
        this.isDone = true;
    }
}
