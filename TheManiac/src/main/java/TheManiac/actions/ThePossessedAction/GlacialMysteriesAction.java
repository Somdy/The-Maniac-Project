package TheManiac.actions.ThePossessedAction;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GlacialMysteriesAction extends AbstractGameAction {
    private AbstractPlayer player;
    
    public GlacialMysteriesAction() {
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            for (AbstractCard card : player.hand.group) {
                card.modifyCostForCombat(1);
            }
            
            while (!player.hand.isEmpty()) {
                AbstractCard card = player.hand.getRandomCard(true);
                player.hand.group.remove(card);
                AbstractDungeon.getCurrRoom().souls.remove(card);
                player.limbo.group.add(card);
                card.current_y = MathUtils.random(-210F, -190F) * Settings.scale;
                card.target_x = Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
                card.target_y = Settings.HEIGHT / 2.0F;
                card.targetAngle = 0.0F;
                card.lighten(false);
                card.drawScale = 0.12F;
                card.targetDrawScale = 0.75F;
                card.applyPowers();
                this.addToBot(new NewQueueCardAction(card, AbstractDungeon.getMonsters().getRandomMonster(true), false, true));
                this.addToBot(new UnlimboAction(card));
            }

            if (!Settings.FAST_MODE) {
                addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            } else {
                addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }
            
            this.isDone = true;
        }
    }
}
