package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.RiskUniversalThrillActions;
import TheManiac.powers.PlaguePower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ToyLeperchaun extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("ToyLeperchaun");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/skill/toy_leperchaun.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 2;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public ToyLeperchaun() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.magicNumber = this.baseMagicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(monster, p, new PlaguePower(monster, p, this.magicNumber), this.magicNumber));
                this.addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, this.magicNumber, false), this.magicNumber));
            }
        }
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (!onUse)
            this.addToBot(new RiskUniversalThrillActions(p, m, AbstractDungeon.cardRandomRng.random(99), this.magicNumber, this.target));
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, 
                new PlaguePower(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber / 2), this.magicNumber / 2));
    }

    @Override
    public void smith(int level) {
        super.smith(level);
        upgradeMagicNumber(level);
    }

    @Override
    public AbstractCard makeCopy() {
        return new ToyLeperchaun();
    }
}
