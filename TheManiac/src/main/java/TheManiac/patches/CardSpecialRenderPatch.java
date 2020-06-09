package TheManiac.patches;

import TheManiac.cards.the_possessed.possessed.AbstractPossessedCard;
import TheManiac.cards.the_possessed.possessed.BookClub;
import TheManiac.cards.the_possessed.uncertainties.ShadowVisions;
import TheManiac.helper.ManiacImageMaster;
import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import javassist.CtBehavior;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CardSpecialRenderPatch {
    
    /*@SpirePatch( clz = AbstractCard.class, method = "renderType" )
    public static class PossessionTypeRender {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:SinglePossessedCardViewPopup");
        public static final String[] TEXT = uiStrings.TEXT;
        
        @SpireInsertPatch( locator = TypeLocator.class, localvars = {"text"})
        public static void InsertType(AbstractCard _instance, SpriteBatch sb, @ByRef String[] text) {
            if (_instance instanceof AbstractPossessedCard) {
                text[0] = TEXT[0];
            }
        }
        
        public static class TypeLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(FontHelper.class, "cardTypeFont");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }*/
    
    @SpirePatch( clz = AbstractCard.class, method = "renderEnergy" )
    public static class PlagueMarkRender {
        public static Texture img = ImageMaster.loadImage("maniacMod/images/512defaults/outlook/plague_mark.png");
        
        @SpirePostfixPatch
        public static void PostfixPlagueMark(AbstractCard _instance, SpriteBatch sb) throws Exception {
            if (AbstractDungeon.player == null) {
                return;
            }
            
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof BookClub && ((BookClub) card).plagues.contains(_instance)) {
                    RenderPlagueMark(_instance, sb);
                    return;
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof BookClub && ((BookClub) card).plagues.contains(_instance)) {
                    RenderPlagueMark(_instance, sb);
                    return;
                }
            }

            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof BookClub && ((BookClub) card).plagues.contains(_instance)) {
                    RenderPlagueMark(_instance, sb);
                    return;
                }
            }
        }
        
        public static void RenderPlagueMark(AbstractCard _instance, SpriteBatch sb) throws Exception {
            Color color = Color.RED.cpy();
            
            Method renderHelper = SuperclassFinder.getSuperClassMethod(_instance.getClass(), "renderHelper", SpriteBatch.class, Color.class, Texture.class, float.class, float.class);
            renderHelper.setAccessible(true);
            renderHelper.invoke(_instance, sb, color, img, _instance.current_x - 512F, _instance.current_y - 490F);
        }
    }
    
    @SpirePatch( clz = AbstractCard.class, method = "renderBannerImage" )
    public static class PlagueMarkBannerRender {
        public static Texture banner = ImageMaster.loadImage("maniacMod/images/512defaults/outlook/banner_plagueMarked.png");
        
        public static SpireReturn Prefix(AbstractCard _instance, SpriteBatch sb, float drawX, float drawY) throws Exception {
            if (AbstractDungeon.player != null) {
                for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                    if (card instanceof BookClub && ((BookClub) card).plagues.contains(_instance)) {
                        RenderPlagueBanner(_instance, sb, drawX, drawY);
                        return SpireReturn.Return(null);
                    }
                }

                for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                    if (card instanceof BookClub && ((BookClub) card).plagues.contains(_instance)) {
                        RenderPlagueBanner(_instance, sb, drawX, drawY);
                        return SpireReturn.Return(null);
                    }
                }

                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (card instanceof BookClub && ((BookClub) card).plagues.contains(_instance)) {
                        RenderPlagueBanner(_instance, sb, drawX, drawY);
                        return SpireReturn.Return(null);
                    }
                }
            }
            
            return SpireReturn.Continue();
        }
        
        public static void RenderPlagueBanner(AbstractCard _instance, SpriteBatch sb, float drawX, float drawY) throws Exception {
            Color color = Color.WHITE.cpy();
            
            Method renderHelper = SuperclassFinder.getSuperClassMethod(_instance.getClass(), "renderHelper", SpriteBatch.class, Color.class, Texture.class, float.class, float.class);
            renderHelper.setAccessible(true);
            renderHelper.invoke(_instance, sb, color, banner, _instance.current_x - 510F, _instance.current_y - 512F);
        }
    }
    
    @SpirePatch( clz = AbstractCard.class, method = "renderCard" )
    public static class ShadowHideRender {
        
        @SpirePostfixPatch
        public static void ShadowCoverPostfix(AbstractCard _instance, SpriteBatch sb, boolean hovered, boolean selected) throws Exception {
            if (!Settings.hideCards) {
                if (CardMarkFieldPatch.ShadowVisionHideField.isShadowHidden.get(_instance)) {
                    //System.out.println("Now rendering card's back by Shadow Visions");
                    RenderShadowCover(_instance, sb, _instance.current_x, _instance.current_y);
                    _instance.hb.render(sb);
                }
            }
        }
        
        public static void RenderShadowCover(AbstractCard _instance, SpriteBatch sb, float drawX, float drawY) throws Exception {
            Color c = Color.WHITE.cpy();
            Method renderHelper = SuperclassFinder.getSuperClassMethod(_instance.getClass(), "renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
            renderHelper.setAccessible(true);
            renderHelper.invoke(_instance, sb, c, ManiacImageMaster.SHADOW_COVER_SMALL, drawX, drawY);
        }
    }
    
    @SpirePatch( clz = TipHelper.class, method = "renderTipForCard" )
    public static class ShadowHideWords {
        
        public static SpireReturn Prefix(AbstractCard c) {
            if (CardMarkFieldPatch.ShadowVisionHideField.isShadowHidden.get(c)) {
                return SpireReturn.Return(null);
            }
            
            return SpireReturn.Continue();
        }
    }
}
