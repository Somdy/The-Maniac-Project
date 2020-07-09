package TheManiac.cards.temp;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.cards.the_possessed.uncertainties.EnigmaticFortress;
import TheManiac.relics.EnigmaticDecoder;
import TheManiac.relics.EnigmaticEncoder;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DarkMatter extends AbstractManiacCard {
    public static final String ID = TheManiac.makeID("DarkMatter");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/colorless/skill/dark_matter.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 1;
    
    public DarkMatter() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (canObtainRelics(p)) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new EnigmaticEncoder());
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new EnigmaticDecoder());
            if (p.masterDeck.findCardById(EnigmaticFortress.ID) != null) {
                AbstractCard card = AbstractDungeon.player.masterDeck.findCardById(EnigmaticFortress.ID);
                p.masterDeck.removeCard(card);
            }
        }
    }

    private boolean canObtainRelics(AbstractPlayer player) {
        if (player == null) return false;

        if (!player.drawPile.isEmpty()) {
            for (AbstractCard c : player.drawPile.group) {
                if (c instanceof DarkMatter)
                    return false;
            }
        }

        if (!player.hand.isEmpty()) {
            for (AbstractCard c : player.hand.group) {
                if (c instanceof DarkMatter && c != this)
                    return false;
            }
        }

        if (!player.discardPile.isEmpty()) {
            for (AbstractCard c : player.discardPile.group) {
                if (c instanceof DarkMatter)
                    return false;
            }
        }

        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = Color.BLACK.cpy();
    }

    @Override
    public boolean canEnchant() {
        return false;
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new DarkMatter();
    }
}
