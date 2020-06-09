package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.powers.DecayPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class NatureCorrupt extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("NatureCorrupt");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/nature_corrupt.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = -2;
    
    public NatureCorrupt() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.magicNumber = this.baseMagicNumber = 6;
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DecayPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new DecayPower(m, this.magicNumber), this.magicNumber));
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new NatureCorrupt();
    }
}
