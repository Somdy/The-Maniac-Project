package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.DetectAction;
import TheManiac.actions.FlashbackAction;
import TheManiac.actions.TrackAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.WeaknessPower;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.List;

public class Traceback extends AbstractManiacCard {
    public static final String ID = "maniac:Traceback";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/traceback.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int TRACK = 5;
    private static final int TRACK_ADD = 3;
    private static final int UPGRADE_TRACK = 1;
    private List<TooltipInfo> tips;
    
    public Traceback() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = TRACK;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = TRACK_ADD;
        this.isUnreal = true;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isInLimbo()) {
            this.addToBot(new DetectAction(this.maniacExtraMagicNumber, p.exhaustPile));
        }
        this.addToBot(new FlashbackAction(this, this.magicNumber, false));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                boolean applyWeakness = false;
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (monster.intent == AbstractMonster.Intent.ATTACK) {
                        applyWeakness = true;
                        break;
                    }
                }
                
                if (applyWeakness) {
                    for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                        this.addToBot(new ApplyPowerAction(mo, p, new WeaknessPower(mo, this.enchantNumber), this.enchantNumber));
                    }
                }
            }
            else if (this.enchantment == 2) {
                int count = 0;
                if (p.exhaustPile != null) {
                    for (AbstractCard card : p.exhaustPile.group) {
                        if (card.type == CardType.STATUS) {
                            count++;
                        }
                    }
                }
                if (count > 0) {
                    this.addToBot(new GainBlockAction(p, (count * this.enchantNumber)));
                }
            } else {
                if (p.exhaustPile != null) {
                    for (AbstractCard card : p.exhaustPile.group) {
                        if (card.type == CardType.CURSE) {
                            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.enchantNumber), this.enchantNumber));
                        }
                    }
                }
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
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(4);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(3);
        } else {
            this.modifyEnchants(1);
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
            this.upgradeMagicNumber(UPGRADE_TRACK);
            this.upgradeManiacExtraMagicNumber(UPGRADE_TRACK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Traceback();
    }
}
