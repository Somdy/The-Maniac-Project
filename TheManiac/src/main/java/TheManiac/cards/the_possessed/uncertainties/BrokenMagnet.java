package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.BrokenMagnetAction;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BrokenMagnet extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("BrokenMagnet");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/broken_magnet.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 0;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    private float chance;
    
    public BrokenMagnet() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.chance = 0.05F;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.chance += curseInDeck();
        if (this.chance == 0F) {
            this.chance = 0.15F;
        }
        System.out.println(this.name + " has a chance of " + this.chance + " to get a Curse.");
        this.addToBot(new BrokenMagnetAction(1, 0.5F));
    }
    
    private float curseInDeck() {
        int num = 0;
        if (!AbstractDungeon.player.masterDeck.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (card.type == CardType.CURSE) {
                    num++;
                }
            }
        }
        return num * 0.05F;
    }

    @Override
    public AbstractCard makeCopy() {
        return new BrokenMagnet();
    }
}
