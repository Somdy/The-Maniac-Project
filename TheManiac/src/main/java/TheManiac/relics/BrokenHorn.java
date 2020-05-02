package TheManiac.relics;

import TheManiac.TheManiac;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.GremlinLeaderPower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static TheManiac.character.TheManiacCharacter.Enums.THE_MANIAC;

public class BrokenHorn extends CustomRelic {
    public static final String ID = "maniac:BrokenHorn";
    private static final String IMG_PATH = "maniacMod/images/relics/brokenHorn.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/brokenHorn_outline.png";
    private final AbstractPlayer player = AbstractDungeon.player;
    private boolean NotGremlinModified = true;
    
    public BrokenHorn() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.STARTER, LandingSound.HEAVY);
        this.counter = 0;
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
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new GremlinLeaderPower(AbstractDungeon.player)));
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (this.counter == 9) {
            try {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new BetterBrokenHorn());
            } catch (Exception e) {
                System.out.println("Not accessible to obtain Better Horn for some reason. Report this if you see it.");
                e.printStackTrace();
            }
        }
        else if (m.currentHealth == 0 && this.counter < 9) {
            this.flash();
            this.counter ++;
            this.addToBot(new RelicAboveCreatureAction(m, this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BrokenHorn();
    }
}
