package TheManiac.variables;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TheManiacOtherNumber extends DynamicVariable {

    @Override
    public String key() {
        return ("OtherNumber");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractManiacCard) card).isManiacOtherMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractManiacCard) card).maniacOtherMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractManiacCard) card).maniacBaseOtherMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractManiacCard) card).upgradedManiacOtherMagicNumber;
    }
}
