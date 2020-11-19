package fr.epita.hivers.hivers;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

public interface Scope {

    Map<@NotNull Class<?>, @NotNull Provider<?>> getProvidersByMap();

    default Scope addProvider(@NotNull final Provider<?> provider) {
        getProvidersByMap().put(provider.getType(), provider);
        return this;
    }

    @SuppressWarnings("unchecked")
    default <PROVIDED_T> Optional<? extends PROVIDED_T> instanceOf(@NotNull final Class<PROVIDED_T> bindClass) {
        return (Optional<? extends PROVIDED_T>) Optional.of(getProvidersByMap().getOrDefault(bindClass, Provider.getNullProvider(bindClass)))
                .map(Provider::getValue);
    }

    default <PROVIDED_T> PROVIDED_T instanceOfOrThrow(@NotNull final Class<PROVIDED_T> bindClass) {
        return instanceOf(bindClass).orElseThrow();
    }

    default boolean hasBindingFor(final Class<?> thisClass) {
        return getProvidersByMap().containsKey(thisClass);
    }
}
