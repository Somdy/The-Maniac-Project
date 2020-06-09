package TheManiac.actions.ThePossessedAction.Uniques;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GiftsDrawCardAction extends AbstractGameAction {
    private DamageInfo info;
    
    public GiftsDrawCardAction(DamageInfo info) {
        this.info = info;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        int cardsToDraw = 10 - AbstractDungeon.player.hand.size();
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (cardsToDraw > 0) {
                this.addToBot(new DrawCardAction(AbstractDungeon.player, cardsToDraw));
            }
        }
        
        this.tickDuration();
        if (this.isDone) {
            for (int i = 0; i < cardsToDraw; i++) {
                this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(this.info.base, false, false), this.info.type, AttackEffect.FIRE, true));
            }
        }
    }
}
