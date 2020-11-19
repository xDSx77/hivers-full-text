package fr.epita.hivers.hivers;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class DefaultScope implements Scope {

    private final Map<@NotNull Class<?>, @NotNull Provider<?>> providersByMap = new HashMap<>();

    @Override
    public Map<@NotNull Class<?>, @NotNull Provider<?>> getProvidersByMap() {
        return providersByMap;
    }
}
