package fr.epita.hivers.hivers;

import fr.epita.hivers.annotation.Nullable;

import javax.validation.constraints.NotNull;
import java.util.function.Supplier;

public class Singleton<PROVIDED_D> implements Provider<PROVIDED_D> {

    @NotNull private final Supplier<@Nullable PROVIDED_D> supplier;
    @NotNull private final Class<PROVIDED_D> type;

    private PROVIDED_D value;
    private boolean isInitialized = false;

    public Singleton(@NotNull final Class<PROVIDED_D> type, @NotNull final Supplier<PROVIDED_D> supplier) {
        this.supplier = supplier;
        this.type = type;
    }

    public Singleton(@NotNull final Class<PROVIDED_D> type, @Nullable final PROVIDED_D value) {
        this(type, () -> value);
    }

    @Override
    public @Nullable PROVIDED_D getValue() {
        if (!isInitialized) {
            isInitialized = true;
            this.value = supplier.get();
        }
        return value;
    }

    @Override
    public @NotNull Class<PROVIDED_D> getType() {
        return type;
    }
}
