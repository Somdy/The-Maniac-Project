package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.ShatterAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.BurnBloodPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BurnBlood extends AbstractManiacCard {
    public static final String ID = "maniac:BurnBlood";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/burn_blood.png";
    private static final String SHROUD_IMG = "maniacMod/images/1024portraits/maniac_blue/attack/shatter.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int LIFE_COST = 3;

    public BurnBlood() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = LIFE_COST;
        this.exhaust = true;
    }

    public BurnBlood(boolean upgraded) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = LIFE_COST;
        this.exhaust = true;
        
        if (upgraded) {
            this.upgrade();
        }
    }

    public BurnBlood(UUID corUUID, boolean upgraded) {
        super(ID, EXTENDED_DESCRIPTION[1], SHROUD_IMG, COST, EXTENDED_DESCRIPTION[0], CardType.ATTACK, COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = upgraded ? 12 : 8;
        this.isShroud = true;
        this.shrouded = true;
        this.storedUUID = corUUID;
        this.exhaust = true;
        
        if (corUUID != null) {
            this.cardsToPreview = new BurnBlood(upgraded);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!shrouded) {
            this.addToBot(new VFXAction(new OfferingEffect(), 0.05f));
            this.addToBot(new LoseHPAction(p, p, this.magicNumber));
            this.addToBot(new ApplyPowerAction(p, p, new BurnBloodPower(p, p, 1), 1));
        }
        
        if (shrouded && findShroudCard(this.storedUUID)) {
            this.addToBot(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4F,
                    m.hb.cY - m.hb.height / 4.0F)));
            this.addToBot(new ShatterAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!shrouded) return true;

        return findShroudCard(this.storedUUID);
    }

    @Override
    public boolean canEnchant() {
        return false;
    }

    @Override
    public void enchant() {
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.isShroud = true;
            if (!shrouded) {
                this.cardsToPreview = new BurnBlood(null, this.upgraded);
                this.rawDescription = UPGRADE_DESCRIPTION;
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        if (shrouded) return new BurnBlood(null, false);
        return new BurnBlood();
    }

    @Override
    public AbstractCard makeShroudCopy() {
        AbstractCard card = new BurnBlood(this.uuid, this.upgraded);
        
        if (upgraded) card.upgrade();
        
        return card;
    }
}
