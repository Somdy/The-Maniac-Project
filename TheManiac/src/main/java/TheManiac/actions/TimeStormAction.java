package TheManiac.actions;

import TheManiac.character.TheManiacCharacter;
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
    private int times;
    
    public TimeStormAction(int amount, int times) {
        this.amount = amount;
        this.times = times;
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
                int totalDmg = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
                if (this.times > 1) {
                    totalDmg *= this.times;
                }
                if (AbstractDungeon.player instanceof TheManiacCharacter) {
                    ((TheManiacCharacter) AbstractDungeon.player).changeState("Call");
                }
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, TheManiacCharacter.charStrings.TEXT[7], 1.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WhirlwindEffect(new Color(0F, 0.14F, 0.4F, 0.5F), false)));
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(this.source, 
                        DamageInfo.createDamageMatrix(totalDmg, true, false), DamageInfo.DamageType.NORMAL, AttackEffect.SLASH_HORIZONTAL));
            } catch (Exception e) {
                System.out.println("Unable to play Time Storm Action for some reason. Report this if you see it. " + e);
            }
        }
        
        this.isDone = true;
    }
}
