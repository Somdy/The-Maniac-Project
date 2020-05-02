package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CollectingAction extends AbstractGameAction {
    private AbstractRelic.RelicTier[] relicRarity = {AbstractRelic.RelicTier.COMMON, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.RelicTier.RARE};
    private DamageInfo damageInfo;
    private AbstractRelic relic;
    private int randomRarity;
    private static final Logger logger = LogManager.getLogger(CollectingAction.class.getName());

    public CollectingAction(AbstractCreature target, DamageInfo damageInfo, int relicRarity) {
        this.damageInfo = damageInfo;
        this.target = target;
        this.source = AbstractDungeon.player;
        this.randomRarity = relicRarity;
        this.actionType = ActionType.DAMAGE;
        this.duration = 2f;
    }
    @Override
    public void update() {
        if (this.duration == 2f && this.target != null) {
            this.target.damage(damageInfo);
            if (((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0 && !this.target.halfDead && !this.target.hasPower("Minion")) {
                if (this.randomRarity > 2) {
                    this.randomRarity = 0;
                }
                int random = AbstractDungeon.miscRng.random(randomRarity, 2);
                relic = AbstractDungeon.returnRandomRelic(relicRarity[random]);
                while (relic.relicId.equals(BottledFlame.ID) || relic.relicId.equals(BottledLightning.ID) || relic.relicId.equals(BottledTornado.ID)) {
                    logger.info("Get relic " + relic.name + " from Collecting. Aborted, roll again.");
                    relic = AbstractDungeon.returnRandomRelic(relicRarity[random]);
                }
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic);
                logger.info("Obtain " + relic.name + " from Collecting. The base rarity is " + relicRarity[randomRarity]);
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        this.isDone = true;
    }
}
