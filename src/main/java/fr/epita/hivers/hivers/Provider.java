package fr.epita.hivers.hivers;

import fr.epita.hivers.annotation.Nullable;

import javax.validation.constraints.NotNull;

public interface Provider<PROVIDED_T> {

    static <PROVIDED_T> Provider<PROVIDED_T> getNullProvider(final Class<PROVIDED_T> clazz) {
        return new Provider<>() {
            @Override
            public @Nullable PROVIDED_T getValue() {
                return null;
            }

            @Override
            public @NotNull Class<PROVIDED_T> getType() {
                return clazz;
            }
        };
    }

    @Nullable PROVIDED_T getValue();

    @NotNull Class<PROVIDED_T> getType();
}
