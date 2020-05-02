package TheManiac.actions;

import TheManiac.monsters.enemies.Sneckouette;
import TheManiac.powers.SilhouettePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SneckouetteSummonAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(SneckouetteSummonAction.class.getName());
    private int summon_power;
    private int summon_hp;
    private float x;
    private float y;
    
    public SneckouetteSummonAction(float x, float y, int silAmt, int summonHp) {
        this.x = x;
        this.y = y;
        this.summon_power = silAmt;
        this.summon_hp = summonHp;
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
            Sneckouette snecko1 = new Sneckouette(this.x - 377F, this.y, true, true, this.summon_hp);
            Sneckouette snecko2 = new Sneckouette(this.x + 284F, this.y, true, true, this.summon_hp);
            AbstractMonster m1 = snecko1;
            AbstractMonster m2 = snecko2;
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
            
            this.addToBot(new ApplyPowerAction(m1, m1, new SilhouettePower(m1, this.summon_power), this.summon_power));
            this.addToBot(new ApplyPowerAction(m2, m2, new SilhouettePower(m2, this.summon_power), this.summon_power));
            logger.info("Get silhouette amount: " + this.summon_power);

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
