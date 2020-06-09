package TheManiac.relics;

import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.GremlinLeaderPower;
import TheManiac.powers.GremlinLeaderUpgradePower;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static TheManiac.character.TheManiacCharacter.Enums.THE_MANIAC;

public class BrokenHorn extends CustomRelic implements CustomSavable<Boolean> {
    public static final String ID = "maniac:BrokenHorn";
    private static final String IMG_PATH = "maniacMod/images/relics/brokenHorn.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/brokenHorn_outline.png";
    private static final String NEW_IMG_PATH = "maniacMod/images/relics/betterbrokenHorn.png";
    private final AbstractPlayer player = AbstractDungeon.player;
    private boolean upgraded;
    private boolean initPower;
    
    public BrokenHorn() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.STARTER, LandingSound.HEAVY);
        this.counter = 0;
        this.initPower = false;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            if (this.player instanceof TheManiacCharacter) {
                if (this.player.chosenClass == THE_MANIAC) {
                    ((TheManiacCharacter) this.player).changeState("Hit");
                }
            }
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction useCardAction) {
        if (card.type == AbstractCard.CardType.ATTACK && card.damage > 10 && !card.purgeOnUse) {
            if (this.player instanceof TheManiacCharacter) {
                if (this.player.chosenClass == THE_MANIAC) {
                    ((TheManiacCharacter) this.player).changeState("Attack");
                }
            }
        }
    }

    @Override
    public void atBattleStart() {
        this.flash();
        if (upgraded) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new GremlinLeaderUpgradePower()));
            initPower = true;
        }
        else this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new GremlinLeaderPower(AbstractDungeon.player)));
        
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void atTurnStartPostDraw() {
        try {
            if (!initPower && !player.hasRelic(BrokenHorn.ID))
                this.addToBot(new ApplyPowerAction(player, player, new GremlinLeaderUpgradePower()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (!upgraded) {
            if (this.counter == 9) {
                upgraded = true;
                this.setTexture(ImageMaster.loadImage(NEW_IMG_PATH));
                this.counter = 0;
                getUpdatedDescription();
            } else if (m.currentHealth == 0 && this.counter < 9) {
                this.flash();
                this.counter++;
                this.addToBot(new RelicAboveCreatureAction(m, this));
            }
        } else {
            if (m.currentHealth == 0) {
                this.flash();
                this.counter++;
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        if (upgraded)
            this.description = DESCRIPTIONS[1];
        else
            this.description = DESCRIPTIONS[0];
        
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        return this.description;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BrokenHorn();
    }

    @Override
    public Boolean onSave() {
        return this.upgraded;
    }

    @Override
    public void onLoad(Boolean upgraded) {
        if (upgraded) {
            this.upgraded = true;
            this.setTexture(ImageMaster.loadImage(NEW_IMG_PATH));
            this.flavorText = DESCRIPTIONS[2];
            getUpdatedDescription();
        }
    }
}
