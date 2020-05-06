package TheManiac.variables;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class EnchantNumber extends DynamicVariable {
    @Override
    public String key() {
        return "Enchants";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractManiacCard)card).isEnchantModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractManiacCard)card).enchantNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractManiacCard)card).baseEnchantNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractManiacCard)card).enchanted;
    }
}