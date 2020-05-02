package TheManiac.powers;

import TheManiac.cards.maniac_blue.attack.DoomScytheAttack;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DoomScythePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:DoomScythePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/DoomScythePower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/DoomScythePower.png";
    private int powers;
    private boolean applyDoom;
    
    public DoomScythePower(int amount, int powers, boolean applyDoom) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.powers = powers;
        this.applyDoom = applyDoom;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        AbstractMonster m = (AbstractMonster)action.target;
        if (card.type == AbstractCard.CardType.ATTACK && !card.cardID.equals(DoomScytheAttack.ID) && m != null && !m.isDeadOrEscaped()) {
            this.flash();
            
            DoomScytheAttack doomScythe = new DoomScytheAttack();
            doomScythe.setScytheAttackValues(this.amount, this.powers, this.applyDoom);

            AbstractCard c = doomScythe.makeStatEquivalentCopy();
            AbstractDungeon.player.limbo.addToBottom(c);
            c.current_x = this.owner.hb_x;
            c.current_y = this.owner.hb_y;
            c.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            c.target_y = Settings.HEIGHT / 2.0F;

            c.calculateCardDamage(m);

            c.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(c, m, 0, true, true), true);
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (this.amount + 2) + DESCRIPTIONS[1] + (this.powers + 1);
        if (applyDoom) {
            this.description += DESCRIPTIONS[3];
        }
        else {
            this.description += DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DoomScythePower(amount, powers, applyDoom);
    }
}
