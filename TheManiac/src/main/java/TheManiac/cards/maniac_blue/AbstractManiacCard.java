package TheManiac.cards.maniac_blue;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractManiacCard extends CustomCard {
    private Logger logger = LogManager.getLogger(AbstractManiacCard.class.getName());
    public int maniacExtraMagicNumber;
    public int maniacOtherMagicNumber;
    public int maniacBaseExtraMagicNumber;
    public int maniacBaseOtherMagicNumber;
    public int baseEnchantNumber;
    public int enchantNumber;
    public int timesEnchanted;
    public int enchantment;
    public boolean upgradedManiacExtraMagicNumber;
    public boolean upgradedManiacOtherMagicNumber;
    public boolean isManiacExtraMagicNumberModified;
    public boolean isManiacOtherMagicNumberModified;
    public boolean isUnreal;
    public boolean applyAdditionalPowers;
    public boolean enchanted;
    public boolean isEnchantModified;
    public boolean isEnchanter;
    public static final Color ALT_MANIAC_BLUE = Color.valueOf("#002366");
    public static final Color ALT_MANIAC_GREEN = Color.valueOf("00ff00");

    public AbstractManiacCard(String id, String name, String img, int cost, String rawDescription,
                              CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isManiacExtraMagicNumberModified = false;
        isManiacOtherMagicNumberModified = false;
        isUnreal = false;
        applyAdditionalPowers = false;
        enchanted = false;
        isEnchantModified = false;
        isEnchanter = false;
        
        enchantment = 0;
        enchantNumber = baseEnchantNumber = 0;
        this.timesEnchanted = 0;
    }

    public static class ManiacCardTags {
        @SpireEnum public static AbstractCard.CardTags ManiacSpell;
        @SpireEnum public static AbstractCard.CardTags ManiacWeapon;
    }
    
    public boolean isInLimbo() {
        return AbstractDungeon.player.stance.ID.equals("maniac:Limbo");
    }

    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedManiacExtraMagicNumber) {
            maniacExtraMagicNumber = maniacBaseExtraMagicNumber;
            isManiacExtraMagicNumberModified = true;
        }
        else if (upgradedManiacOtherMagicNumber) {
            maniacOtherMagicNumber = maniacBaseOtherMagicNumber;
            isManiacOtherMagicNumberModified = true;
        }
        else if (enchanted) {
            enchantNumber = baseEnchantNumber;
            isEnchantModified = true;
        }
    }

    /*
    @Override
    public boolean freeToPlay() {
        if (this.freeToPlayOnce) {
            return true;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null &&
                (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasPower(BurnBloodPower.POWER_ID)) {
            return true;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null &&
                (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasPower("FreeAttackPower") && this.type == CardType.ATTACK) {
            return true;
        }

        return false;
    }
    */

    public void upgradeManiacExtraMagicNumber(int amount) {
        maniacBaseExtraMagicNumber += amount;
        maniacExtraMagicNumber = maniacBaseExtraMagicNumber;
        upgradedManiacExtraMagicNumber = true;
    }

    public void upgradeManiacOtherMagicNumber(int amount) {
        maniacBaseOtherMagicNumber += amount;
        maniacOtherMagicNumber = maniacBaseOtherMagicNumber;
        upgradedManiacOtherMagicNumber = true;
    }

    public void triggerOnTrack() {

    }
    
    public abstract void enchant();
    
    protected void modifyEnchants(int amount) {
        this.baseEnchantNumber += amount;
        this.enchantNumber = this.baseEnchantNumber;
    }
    
    protected void enchantName() {
        this.timesEnchanted++;
        if (!this.enchanted) {
            this.name = "×" + this.name;
        }
        this.enchanted = true;
        this.initializeTitle();
    }
    
    protected int enchantOpts(int firstOpt, int lastOpt) {
        int opt = AbstractDungeon.cardRandomRng.random(firstOpt, lastOpt);
        enchantment = opt;
        logger.info("Get a maniac card enchantment opt: " + enchantment);
        return opt;
    }

    public boolean canEnchant() {
        if (this.type == AbstractCard.CardType.CURSE) {
            return false;
        } else if (this.type == AbstractCard.CardType.STATUS) {
            return false;
        } else {
            return !this.isEnchanter;
        }
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public AbstractCard makeCopy() {
        return null;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        
        if (card instanceof AbstractManiacCard) {
            for (int i = 0; i < this.timesEnchanted; i++) {
                ((AbstractManiacCard) card).enchant();
            }

            ((AbstractManiacCard) card).enchanted = this.enchanted;
            ((AbstractManiacCard) card).timesEnchanted = this.timesEnchanted;
        }
        
        return card;
    }
}
