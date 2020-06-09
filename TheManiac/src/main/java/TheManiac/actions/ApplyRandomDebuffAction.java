package TheManiac.actions;

import TheManiac.powers.BleedingPower;
import TheManiac.powers.FrightenedPower;
import TheManiac.powers.WeaknessPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ApplyRandomDebuffAction extends AbstractGameAction {
    private Logger logger = LogManager.getLogger(ApplyRandomDebuffAction.class.getName());
    private AbstractCreature target;
    private AbstractCreature source;
    private int amount;
    private int times;
    boolean isSourceMonster;
    
    public ApplyRandomDebuffAction(AbstractCreature source, AbstractCreature target, int amount, boolean isSourceMonster, int times) {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.times = times;
        this.isSourceMonster = isSourceMonster;
        this.actionType = ActionType.DEBUFF;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.target == null) {
                this.isDone = true;
                return;
            }
            
            if (this.target instanceof AbstractMonster) {
                if (this.target.isDeadOrEscaped()) {
                    this.isDone = true;
                    return;
                }
            }
            
            if (this.times <= 0) {
                this.addToBot(new ApplyPowerAction(this.target, this.source, this.chooseRandomDebuff(), this.amount));
            } else {
                for (int i = 0; i < this.times; i++) {
                    this.addToBot(new ApplyPowerAction(this.target, this.source, this.chooseRandomDebuff(), this.amount));
                }
            }
            this.isDone = true;
        }
    }
    
    private AbstractPower chooseRandomDebuff() {
        ArrayList<AbstractPower> debuffs = new ArrayList<>();
        
        debuffs.add(new VulnerablePower(this.target, this.amount, this.isSourceMonster));
        debuffs.add(new WeakPower(this.target, this.amount, this.isSourceMonster));
        debuffs.add(new PoisonPower(this.target, this.source, this.amount));
        debuffs.add(new BleedingPower(this.target, this.amount));
        debuffs.add(new WeaknessPower(this.target, this.amount));
        debuffs.add(new FrightenedPower(this.target, this.amount));
        
        return debuffs.get(AbstractDungeon.cardRandomRng.random(0, debuffs.size() - 1));
    }
}
