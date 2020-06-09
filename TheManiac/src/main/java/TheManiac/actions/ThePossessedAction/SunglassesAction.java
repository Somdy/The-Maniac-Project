package TheManiac.actions.ThePossessedAction;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SunglassesAction extends AbstractGameAction {
    private DamageInfo info;

    public SunglassesAction(DamageInfo info) {
        this.info = info;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            
            for (AbstractCard card : DrawCardAction.drawnCards) {
                if (card.type == AbstractCard.CardType.SKILL) {
                    AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(true);
                    this.addToBot(new DamageAction(monster, this.info, AttackEffect.BLUNT_LIGHT));
                    this.addToBot(new DrawCardAction(1, new SunglassesAction(this.info)));
                }
            }
            
        }
        
        this.tickDuration();
    }
}
