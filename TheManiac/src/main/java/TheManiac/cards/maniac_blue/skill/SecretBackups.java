package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.FoilsPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecretBackups extends AbstractManiacCard {
    public static final String ID = "maniac:SecretBackups";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/secret_backups.png";
    private static final String SHROUD_IMG = "maniacMod/images/1024portraits/maniac_blue/skill/emergency_protection.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int drawAmt = 1;

    public SecretBackups() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = drawAmt;
        this.isShroud = true;
        this.shrouded = false;
        
        this.cardsToPreview = new SecretBackups(null, this.upgraded);
    }

    public SecretBackups(boolean upgraded) {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = drawAmt;
        this.isShroud = true;
        
        if (upgraded) {
            this.upgrade();
        }
    }

    public SecretBackups(UUID corUUID, boolean upgraded) {
        super(ID, EXTENDED_DESCRIPTION[2], SHROUD_IMG, -2, EXTENDED_DESCRIPTION[1], TYPE, COLOR, CardRarity.COMMON, TARGET);
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 4;
        this.isShroud = true;
        this.shrouded = true;
        this.storedUUID = corUUID;
        
        if (corUUID != null) {
            this.cardsToPreview = new SecretBackups(upgraded);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        
    }

    @Override
    public void triggerWhenDrawn() {
        if (shrouded) return;
        this.addToBot(new DrawCardAction(AbstractDungeon.player, this.magicNumber, false));
        if (upgraded) this.addToBot(new GainEnergyAction(1));
    }

    @Override
    public void onOtherCardDrawn(AbstractCard card, boolean inHand, boolean inExhaustPile) {
        if (inHand && shrouded && InShroud(this.storedUUID)) {
            if (card.type == CardType.ATTACK && (card.costForTurn >= 1 && !card.freeToPlayOnce || card.cost == -1 && card.energyOnUse >= 1)) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, 
                        new FoilsPower(AbstractDungeon.player, this.maniacExtraMagicNumber)));
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void enchant() {
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeManiacExtraMagicNumber(1);
            if (!shrouded) {
                AbstractCard c = new SecretBackups(null, this.upgraded);
                c.upgrade();
                this.cardsToPreview = c;
                this.rawDescription = UPGRADE_DESCRIPTION;
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        if (shrouded) return new SecretBackups(null, false);
        return new SecretBackups();
    }

    @Override
    public AbstractCard makeShroudCopy() {
        AbstractCard card = new SecretBackups(this.uuid, this.upgraded);
        
        if (upgraded) card.upgrade();
        
        return card;
    }
}
