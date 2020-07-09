package TheManiac.actions;

import TheManiac.character.TheManiacCharacter;
import TheManiac.helper.ManiacImageMaster;
import TheManiac.stances.LimboStance;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

public class TimeStormAction extends AbstractGameAction {
    private int[] multiDmg;
    
    public TimeStormAction(int amount, int[] multiDmg) {
        this.amount = amount;
        this.multiDmg = multiDmg;
        this.source = AbstractDungeon.player;
        this.actionType = ActionType.DAMAGE;
        this.duration = 2f;
    }
    
    @Override
    public void update() {
        /*
        if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
            for (AbstractMonster monster : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                this.addToBot(new ApplyPowerAction(monster, this.source, new SlowPower(monster, this.amount), this.amount));
            }
        }
        */
        if (!AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()) {
            try {
                if (AbstractDungeon.player instanceof TheManiacCharacter) {
                    ((TheManiacCharacter) AbstractDungeon.player).changeState("Call");
                }
                this.addToBot(new ManiacTalkAction(TheManiacCharacter.charStrings.TEXT[7], 1.0f, 2.0f));
                this.addToBot(new VFXAction(new WhirlwindEffect(ManiacImageMaster.ColorDeviator(Color.ROYAL, 0.2F, 0.4F), false)));
                this.addToBot(new DamageAllEnemiesAction(source, multiDmg, DamageInfo.DamageType.NORMAL, AttackEffect.SLASH_HORIZONTAL));
            } catch (Exception e) {
                System.out.println("Unable to play Time Storm Action for some reason. Report this if you see it. " + e);
            }
        }
        
        this.isDone = true;
    }
}
