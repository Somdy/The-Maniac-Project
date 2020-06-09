package TheManiac.actions.PossessedMonsterAction;

import TheManiac.helper.ManiacImageMaster;
import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.lang.reflect.Method;

public class UncertaintyBlindAction extends AbstractGameAction {
    private AbstractPlayer player = AbstractDungeon.player;
    
    public UncertaintyBlindAction() {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (player.drawPile.isEmpty() && player.discardPile.isEmpty() && player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            AbstractCard card = null;
            
            if (!player.drawPile.isEmpty()) {
                card = player.drawPile.getRandomCard(true);
            }
            else if (!player.discardPile.isEmpty()) {
                card = player.discardPile.getRandomCard(true);
            }
            else if (!player.hand.isEmpty()) {
                card = player.hand.getRandomCard(true);
            }
            
            if (card == null || card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS) {
                card = player.discardPile.getRandomCard(true);
            }
            
            if (card != null) {
                if (card.name.equals("?")) {
                    card.rarity = returnRandomRarity();
                }
                if (card.rawDescription.equals("?")) {
                    card.portrait = ManiacImageMaster.BLIND_PORTRAIT;
                    if (card.type == AbstractCard.CardType.SKILL) 
                        card.type = AbstractCard.CardType.ATTACK;
                    if (card.target == AbstractCard.CardTarget.SELF)
                        card.target = AbstractCard.CardTarget.ENEMY;
                    card.name = "?";
                    try {
                        Method initTitle = AbstractCard.class.getDeclaredMethod("initializeTitle");
                        initTitle.setAccessible(true);
                        initTitle.invoke(card);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                card.rawDescription = "?";
                card.initializeDescription();

                player.limbo.group.add(card);
                card.setAngle(0.0F);
                card.targetDrawScale = 0.75F;
                card.target_x = (float)Settings.WIDTH / 2.0F;
                card.target_y = (float)Settings.HEIGHT / 2.0F;
                card.lighten(false);
                card.unfadeOut();
                card.unhover();
                card.untip();
                card.stopGlowing();
                this.addToBot(new UnlimboAction(card));
            } else
                BaseMod.logger.info("你牌都不见了？？？");
        }
        
        this.tickDuration();
    }
    
    private AbstractCard.CardRarity returnRandomRarity() {
        int rarity = MathUtils.random(0, 3);
        switch (rarity) {
            case 0:
                return AbstractCard.CardRarity.COMMON;
            case 1:
                return AbstractCard.CardRarity.UNCOMMON;
            case 2:
                return AbstractCard.CardRarity.RARE;
            case 3:
                return AbstractCard.CardRarity.CURSE;
            default:
                return AbstractCard.CardRarity.BASIC;
        }
    }
}
