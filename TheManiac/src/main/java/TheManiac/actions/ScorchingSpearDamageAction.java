package TheManiac.actions;

import TheManiac.powers.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScorchingSpearDamageAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(ScorchingSpearDamageAction.class.getName());
    private String[] randomDebuff = {"Vulnerable", "Weak", "Weakness", "Bleeding", "Frightened", "Agonal", "Poison", "Slow"};
    private boolean applyPowers;
    private int powers;
    
    public ScorchingSpearDamageAction(int damage, int powers, boolean applyPowers) {
        this.source = AbstractDungeon.player;
        this.amount = damage;
        this.powers = powers;
        this.applyPowers = applyPowers;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDead && !monster.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS), AttackEffect.FIRE));
                }
                if (this.applyPowers) {
                    int debuffOrder = AbstractDungeon.miscRng.random(0, 7);
                    logger.info("Applying debuff: " + randomDebuff[debuffOrder] + " to enemy: " + monster.name);
                    switch (randomDebuff[debuffOrder]) {
                        case "Vulnerable":
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new VulnerablePower(monster, this.powers, false), this.powers));
                            break;
                        case "Weak":
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new WeakPower(monster, this.powers, false), this.powers));
                            break;
                        case "Weakness":
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new WeaknessPower(monster, this.powers), this.powers));
                            break;
                        case "Bleeding":
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new BleedingPower(monster, this.powers), this.powers));
                            break;
                        case "Frightened":
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new FrightenedPower(monster, this.powers), this.powers));
                            break;
                        case "Agonal":
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new AgonalPower(monster, this.powers), this.powers));
                            break;
                        case "Poison":
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new PoisonPower(monster, this.source, this.powers), this.powers));
                            break;
                        case "Slow":
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new SlowPower(monster, this.powers), this.powers));
                            break;
                        default:
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new InfernoFlamePower(monster, this.powers), this.powers));
                    }
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        
        this.isDone = true;
    }
}
