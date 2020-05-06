package TheManiac.actions;

import TheManiac.monsters.enemies.Destroyer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CorruardianSummonAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(CorruardianSummonAction.class.getName());
    private float x;
    private float y;
    
    public CorruardianSummonAction(float x, float y) {
        this.x = x;
        this.y = y;
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            Destroyer destroyer1 = new Destroyer(this.x + 150F, this.y - 60F);
            Destroyer destroyer2 = new Destroyer(this.x - 300F, this.y - 60F);
            AbstractMonster m1 = destroyer1;
            AbstractMonster m2 = destroyer2;
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(m1);
                r.onSpawnMonster(m2);
            }
            m1.init();
            m1.applyPowers();
            m2.init();
            m2.applyPowers();

            (AbstractDungeon.getCurrRoom()).monsters.addMonster((int)this.x, m1);
            (AbstractDungeon.getCurrRoom()).monsters.addMonster((int)this.x, m2);
            logger.info("Spawned two destroyers");
            
            this.addToBot(new ApplyPowerAction(m1, m1, new MinionPower(m1)));
            this.addToBot(new ApplyPowerAction(m2, m2, new MinionPower(m2)));

            if (ModHelper.isModEnabled("Lethality")) {
                this.addToBot(new ApplyPowerAction(m1, m1, new StrengthPower(m1, 3), 3));
                this.addToBot(new ApplyPowerAction(m2, m2, new StrengthPower(m2, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation")) {
                this.addToBot(new ApplyPowerAction(m1, m1, new SlowPower(m1, 0)));
                this.addToBot(new ApplyPowerAction(m2, m2, new SlowPower(m2, 0)));
            }

            m1.showHealthBar();
            m1.usePreBattleAction();
            m2.showHealthBar();
            m2.usePreBattleAction();
        }
        
        this.isDone = true;
    }
}
