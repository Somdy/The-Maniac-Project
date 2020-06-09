package TheManiac.cards.maniac_blue.attack;

import TheManiac.actions.SeekerKnifeAction;
import TheManiac.actions.TrackAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

import java.util.ArrayList;
import java.util.List;

public class SeekerKnife extends AbstractManiacCard {
    public static final String ID = "maniac:SeekerKnife";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/seeker_knife.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DMG = 2;
    private static final int UPGRADE_INCREASE = 4;
    private List<TooltipInfo> tips;

    public SeekerKnife() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SeekerKnifeAction(p, m, this.damage));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.enchantNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }
            else if (this.enchantment == 2) {
                AbstractCard card = this.makeCopy();
                if (this.upgraded) {
                    card.upgrade();
                }
                this.addToBot(new MakeTempCardInDiscardAction(card.makeStatEquivalentCopy(), this.enchantNumber));
            }
            else if (this.enchantment == 3) {
                this.addToBot(new TrackAction(this.enchantNumber, p.discardPile));
            } else {
                p.exhaustPile.addToTop(this.makeStatEquivalentCopy());
            }
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isInLimbo()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
        else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void WhenDetected() {
        int dmg = 2 * this.baseDamage;
        this.addToBot(new VFXAction(AbstractDungeon.player, new CleaveEffect(), 0.1F));
        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(dmg, true, false), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void enchant() {
        if (!enchanted) {
            switch (this.enchantOpts(1, 3)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                case 2:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[2];
                    break;
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(8);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(1);
        } else {
            this.modifyEnchants(3);
        }
        initializeDescription();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DMG);
            this.upgradeMagicNumber(UPGRADE_INCREASE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SeekerKnife();
    }
}
