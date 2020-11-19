package fr.epita.hivers.hivers;

import fr.epita.hivers.annotation.Nullable;

import javax.validation.constraints.NotNull;
import java.util.function.Supplier;

public class Prototype<PROVIDED_D> implements Provider<PROVIDED_D> {

    @NotNull private final Supplier<@Nullable PROVIDED_D> supplier;
    @NotNull private final Class<PROVIDED_D> type;

    public Prototype(final Class<PROVIDED_D> type, final Supplier<PROVIDED_D> supplier) {
        this.supplier = supplier;
        this.type = type;
    }

    @Override
    public PROVIDED_D getValue() {
        return supplier.get();
    }

    @Override
    public Class<PROVIDED_D> getType() {
        return type;
    }
}
