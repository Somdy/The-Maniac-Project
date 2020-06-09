package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class EvilEntity extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("EvilEntity");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/skill/evil_entity.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public EvilEntity() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        int degree = Math.round(AbstractDungeon.player.masterDeck.group.size() * 0.1F);
        int amount = Math.max(1, numOfRisks());
        System.out.println(this.name + " needs at least " + degree + " risks and you have " + amount + " or less.");
        if (amount >= degree) {
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, amount), amount));
        }
        else {
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -amount), -amount));
        }
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (onUse) {
            int amounts = AbstractDungeon.cardRandomRng.randomBoolean() ? -1 : 2;
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, amounts), amounts));
        }
    }

    private int numOfRisks() {
        int num = 0;
        if (!AbstractDungeon.player.masterDeck.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (card instanceof AbstractRisksCard) {
                    num++;
                }
            }
        }
        return num;
    }

    @Override
    public AbstractCard makeCopy() {
        return new EvilEntity();
    }
}
