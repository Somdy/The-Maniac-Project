package TheManiac.powers;

import TheManiac.TheManiac;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RavelPower extends AbstractManiacPower implements CloneablePowerInterface {
    private static final Logger logger = LogManager.getLogger(RavelPower.class.getName());
    public static final String POWER_ID = TheManiac.makeID("RavelPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCard ravel;
    private static int offset = 0;
    
    public RavelPower(AbstractCreature owner, int amount, AbstractCard card) {
        this.name = NAME + card.name;
        this.ID = POWER_ID + offset;
        offset++;
        this.owner = owner;
        this.amount = amount;
        this.ravel = card;
        this.type = PowerType.BUFF;
        this.isUnravelling = true;
        this.loadImg("Ravel");
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        for (int i = 0; i < amount; i++) {
            playRavelledCard();
        }

        if (ravel.exhaust || ravel.isEthereal || ravel.purgeOnUse || ravel.type == AbstractCard.CardType.POWER)
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    private void playRavelledCard() {
        AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
        if (m != null && !m.isDeadOrEscaped() && ravel.canUse(AbstractDungeon.player, m)) {
            this.flash();

            AbstractCard copy = ravel.makeStatEquivalentCopy();
            AbstractDungeon.player.limbo.addToBottom(copy);
            copy.current_x = owner.hb.cX;
            copy.current_y = owner.hb.cY;
            
            if (copy.target == AbstractCard.CardTarget.SELF) {
                copy.target_x = AbstractDungeon.player.drawX * Settings.scale;
                copy.target_y = AbstractDungeon.player.drawY;
            } else {
                copy.target_x = m.hb.cX * Settings.scale;
                copy.target_y = m.hb.cY;
            }
            
            copy.calculateCardDamage(m);
            copy.purgeOnUse = true;

            AbstractDungeon.actionManager.addCardQueueItem(
                    new CardQueueItem(copy, m, 0, true, true), true);
            logger.info(this.name + " : 对 " + m.name + " 打出 " + copy.name);
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[amount > 1 ? 2 : 1] + ravel.name + DESCRIPTIONS[3];
        if (ravel.exhaust || ravel.isEthereal || ravel.purgeOnUse || ravel.type == AbstractCard.CardType.POWER)
            this.description += DESCRIPTIONS[4];
    }

    @Override
    public AbstractPower makeCopy() {
        return new RavelPower(owner, amount, ravel);
    }
}
