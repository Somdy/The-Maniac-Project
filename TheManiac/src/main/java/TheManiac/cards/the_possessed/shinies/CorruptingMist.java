package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import TheManiac.powers.DecayPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CorruptingMist extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("CorruptingMist");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/skill/corrupting_mist.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 0;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public CorruptingMist() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(mo, p, new DecayPower(mo, this.magicNumber), this.magicNumber));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CorruptingMist();
    }
}
