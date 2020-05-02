package TheManiac.cards.maniac_blue;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class AbstractManiacCard extends CustomCard {
    public int maniacExtraMagicNumber;
    public int maniacOtherMagicNumber;
    public int maniacBaseExtraMagicNumber;
    public int maniacBaseOtherMagicNumber;
    public boolean upgradedManiacExtraMagicNumber;
    public boolean upgradedManiacOtherMagicNumber;
    public boolean isManiacExtraMagicNumberModified;
    public boolean isManiacOtherMagicNumberModified;
    public boolean isUnreal;
    public boolean applyAdditionalPowers;
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
    }

    public static class ManiacCardTags {
        @SpireEnum public static AbstractCard.CardTags ManiacSpell;
        @SpireEnum public static AbstractCard.CardTags ManiacWeapon;
    }

    public boolean hasSpell() {
        boolean hasAnySpell = false;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.hasTag(ManiacCardTags.ManiacSpell) && card != this) {
                hasAnySpell = true;
            }
        }
        return hasAnySpell;
    }

    public boolean hasWeapon() {
        boolean hasAnyWeapon = false;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.hasTag(ManiacCardTags.ManiacWeapon) && card != this) {
                hasAnyWeapon = true;
            }
        }
        return hasAnyWeapon;
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
    }

    @Override
    public boolean freeToPlay() {
        if (this.freeToPlayOnce) {
            return true;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null &&
                (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasPower("maniac:BurnBloodPower")) {
            return true;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null &&
                (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasPower("FreeAttackPower") && this.type == CardType.ATTACK) {
            return true;
        }

        return false;
    }

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

    @Override
    public void upgrade() {

    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public AbstractCard makeCopy() {
        return null;
    }
}
