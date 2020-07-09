package TheManiac.cards.temp;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.RemoveCardFromDeckAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.cards.the_possessed.shinies.JarOfFireflies;
import TheManiac.cards.the_possessed.uncertainties.EnigmaticFortress;
import TheManiac.relics.EnigmaticDecoder;
import TheManiac.relics.EnigmaticEncoder;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class FortressExpedition2 extends AbstractManiacCard {
    public static final String ID = TheManiac.makeID("FortressExpedition2");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/enigmatic_fortress.png";
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 2;
    private int opt;
    
    public FortressExpedition2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.purgeOnUse = true;
        opt = 0;
        initData();
    }

    public FortressExpedition2(int opt) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.purgeOnUse = true;
        this.opt = opt;
        initData();
    }
    
    public void initData() {
        switch (opt) {
            case 1:
                this.rawDescription = EXTENDED_DESCRIPTION[0];
                break;
            case 2:
                this.rawDescription = EXTENDED_DESCRIPTION[1];
                break;
            default:
                this.rawDescription = EXTENDED_DESCRIPTION[2];
                break;
        }
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (opt) {
            case 1:
                this.addToBot(new MakeTempCardInDrawPileAction(new DarkMatter(), 3, true, true));
                break;
            case 2:
                this.addToBot(new RemoveCardFromDeckAction(2, true, false));
                if (p.masterDeck.findCardById(EnigmaticFortress.ID) != null) {
                    AbstractCard card = p.masterDeck.findCardById(EnigmaticFortress.ID);
                    p.masterDeck.removeCard(card);
                }
                break;
            default:
                break;
        }
        
        //clearExpeditions();
    }
    
    private void clearExpeditions() {
        AbstractPlayer p = AbstractDungeon.player;
        if (!p.hand.isEmpty()) {
            for (AbstractCard c : p.hand.group) {
                if (c instanceof FortressExpedition2) p.hand.moveToExhaustPile(c);
            }
        }

        if (!p.drawPile.isEmpty()) {
            for (AbstractCard c : p.drawPile.group) {
                if (c instanceof FortressExpedition2) p.drawPile.moveToExhaustPile(c);
            }
        }

        if (!p.discardPile.isEmpty()) {
            for (AbstractCard c : p.discardPile.group) {
                if (c instanceof FortressExpedition2) p.discardPile.moveToExhaustPile(c);
            }
        }
    }

    @Override
    public boolean canEnchant() {
        return false;
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public AbstractCard makeCopy() {
        return new FortressExpedition2(opt);
    }
}
