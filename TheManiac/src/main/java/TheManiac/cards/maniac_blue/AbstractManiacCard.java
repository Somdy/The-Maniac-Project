package TheManiac.cards.maniac_blue;

import TheManiac.stances.LimboStance;
import basemod.abstracts.CustomCard;
import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public abstract class AbstractManiacCard extends CustomCard {
    private static final Logger logger = LogManager.getLogger(AbstractManiacCard.class.getName());
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
    public boolean isShroud;
    public boolean shrouded;
    public UUID storedUUID;
    
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
        isShroud = false;
        shrouded = false;
        
        maniacExtraMagicNumber = maniacBaseExtraMagicNumber = -1;
        maniacOtherMagicNumber = maniacBaseOtherMagicNumber = -1;
        enchantment = 0;
        enchantNumber = baseEnchantNumber = 0;
        this.timesEnchanted = 0;
        
        storedUUID = null;
    }

    public boolean isInLimbo() {
        return AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID);
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
    
    public void WhenDetected() {
        
    }

    public void onOtherCardDrawn(AbstractCard card, boolean inHand, boolean inExhaustPile) {

    }

    public int onAttackedToModifyDamage(DamageInfo info, int damageAmount, boolean inHand) {
        return damageAmount;
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
        }
        else if (this.rarity == CardRarity.RARE) {
            return false;
        } else {
            return !this.isEnchanter && !this.isShroud;
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
    
    public AbstractCard makeShroudCopy() {
        return this.makeStatEquivalentCopy();
    }
    
    public void returnShroudCard() {
        if (shrouded) return;
        
        AbstractPlayer p = AbstractDungeon.player;
        float PADDING = 25.0F * Settings.scale;
        
        if (p.hand.size() >= 10) {
            p.createHandIsFullDialog();
            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(this.makeShroudCopy(), 
                    Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
        } else {
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(this.makeShroudCopy(),
                    Settings.WIDTH / 2.0F - PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
        }
    }
    
    public boolean InShroud(UUID targetID) {
        if (AbstractDungeon.player != null) {
            for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
                if (card.uuid == targetID)
                    return true;
            }
        }
        
        return false;
    }
    
    public boolean findShroudCard(UUID targetID) {
        if (targetID == null || AbstractDungeon.player == null) {
            logger.info("ERROR: " + this.name + " 没有具体的预览卡牌目标");
            logger.info("需要确定的目标id : " +  targetID);
            return false;
        }
        
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card.uuid == targetID) return true;
        }
        
        return false;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        
        if (card instanceof AbstractManiacCard) {
            ((AbstractManiacCard) card).maniacBaseExtraMagicNumber = this.maniacBaseExtraMagicNumber;
            ((AbstractManiacCard) card).maniacBaseOtherMagicNumber = this.maniacBaseOtherMagicNumber;
            ((AbstractManiacCard) card).applyAdditionalPowers = this.applyAdditionalPowers;
            ((AbstractManiacCard) card).isEnchanter = this.isEnchanter;
            ((AbstractManiacCard) card).isEnchantModified = this.isEnchantModified;
            ((AbstractManiacCard) card).isShroud = this.isShroud;
            ((AbstractManiacCard) card).shrouded = this.shrouded;
        }
        
        card.retain = this.retain;
        card.selfRetain = this.selfRetain;
        card.purgeOnUse = this.purgeOnUse;
        card.isEthereal = this.isEthereal;
        card.exhaust = this.exhaust;
        
        return card;
    }
    
    private BitmapFont getCardTitleFont() throws Exception {
        BitmapFont font;
        
        Field useSmallTitleFont = AbstractCard.class.getDeclaredField("useSmallTitleFont");
        useSmallTitleFont.setAccessible(true);

        if (!useSmallTitleFont.getBoolean(this)) {
            font = FontHelper.cardTitleFont;
        } else {
            font = FontHelper.cardTitleFont_small;
        }

        return font;
    }
    
    @SpireOverride
    protected void renderTitle(SpriteBatch sb) {
        BitmapFont font = null;
        try {
            font = getCardTitleFont();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (font == null) {
            font = FontHelper.cardTitleFont_small;
        }
        font.getData().setScale(this.drawScale);
        if (this.enchanted && this.upgraded) {
            Color color = Settings.PURPLE_RELIC_COLOR.cpy();
            color.a = 1F;
            FontHelper.renderRotatedText(sb, font, this.name, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, color);
            return;
        }
        if (this.enchanted) {
            Color color = Settings.BLUE_TEXT_COLOR.cpy();
            color.a = 1F;
            FontHelper.renderRotatedText(sb, font, this.name, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, color);
            return;
        }
        SpireSuper.call(sb);
    }

    @Override
    public void renderCardPreview(SpriteBatch sb) {
        if (this.shrouded && !findShroudCard(this.storedUUID))
            return;
            
        super.renderCardPreview(sb);
    }
}