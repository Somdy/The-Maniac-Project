package TheManiac.actions;

import TheManiac.cards.status.TheUndigested;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;

public class MawellingUpgradeMudAction extends AbstractGameAction {
    private boolean gotMud = false;
    
    public MawellingUpgradeMudAction() {
        this.duration = 3.0F;
        this.actionType = ActionType.WAIT;
    }
    
    @Override
    public void update() {
        int muds = 0;
        int targetNum;
        if (this.duration == 3.0F) {
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (c instanceof TheUndigested) {
                    c.upgrade();
                    muds++;
                }
            }

            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c instanceof TheUndigested) {
                    c.upgrade();
                    muds++;
                }
            }
        }
        
        if (AbstractDungeon.ascensionLevel >= 19) {
            targetNum = 5;
        } else {
            targetNum = 3;
        }
        
        if (this.duration < 1.5F && !this.gotMud) {
            this.gotMud = true;
            if (muds < targetNum) {
                TheUndigested a = new TheUndigested();
                a.upgrade();
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(a));

                TheUndigested b = new TheUndigested();
                b.upgrade();
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(b));

                TheUndigested c = new TheUndigested();
                c.upgrade();
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));

                TheUndigested d = new TheUndigested();
                d.upgrade();
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(d));
            }
        }
        
        this.isDone = true;
    }
}
