package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Reforge extends AbstractManiacCard {
    public static final String ID = "maniac:Reforge";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/reforge.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int baseTimes = 1;
    
    public Reforge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true;
        this.isUnreal = true;
        this.isEnchanter = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int times = baseTimes;
        if (this.upgraded) {
            times++;
        }
        for (int i = 0; i < times; i++) {
            for (AbstractCard card : p.hand.group) {
                if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).canEnchant()) {
                    ((AbstractManiacCard) card).enchant();
                    card.superFlash(Color.PURPLE.cpy());
                }
            }
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Reforge();
    }
}
