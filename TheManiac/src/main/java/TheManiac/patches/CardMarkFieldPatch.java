package TheManiac.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CardMarkFieldPatch {
    
    @SpirePatch( clz = AbstractCard.class, method = SpirePatch.CLASS )
    public static class TranslocationCopyField {
        public static SpireField<Boolean> isGatewayCopy = new SpireField<>(() -> Boolean.FALSE);
        public static SpireField<Boolean> isGatewayNega = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch( clz = AbstractCard.class, method = SpirePatch.CLASS )
    public static class ShadowVisionHideField {
        public static SpireField<Boolean> isShadowHidden = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch( clz = AbstractCard.class, method = SpirePatch.CLASS )
    public static class FlashbackField {
        public static SpireField<Boolean> flashbacked = new SpireField<>(() -> Boolean.FALSE);
    }
}
