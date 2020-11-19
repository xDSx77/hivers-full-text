package fr.epita.hivers;

import fr.epita.hivers.annotation.Nullable;
import fr.epita.hivers.hivers.DefaultScope;
import fr.epita.hivers.hivers.Provider;
import fr.epita.hivers.hivers.Scope;

import javax.validation.constraints.NotNull;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Hivers {

    private final Map<@NotNull Class<?>, @NotNull Provider<?>> providersByMap = new HashMap<>();
    private final Deque<Scope> scopes = new ArrayDeque<>();

    public Hivers() {
        scopes.push(new DefaultScope());
    }


    public Scope push(final Scope newScope) {
        if (!scopes.contains(newScope)) {
            scopes.push(newScope);
        }
        return newScope;
    }

    public @Nullable Scope pop() {
        return scopes.size() > 1 ? scopes.pop() : null;
    }

    public <PROVIDER_T> @NotNull Scope addProvider(@NotNull final Provider<PROVIDER_T> provider) {
        final var topScope = Objects.requireNonNull(scopes.peek());

        topScope.addProvider(provider);
        return topScope;
    }

    @SuppressWarnings("unchecked")
    public <REQ_T, RESULT_T extends REQ_T> Optional<? extends RESULT_T> instanceOf(final Class<REQ_T> thisClass) {
        return scopes.stream()
                .filter(scope -> scope.hasBindingFor(thisClass))
                .findFirst()
                .map(scope -> (RESULT_T) scope.instanceOfOrThrow(thisClass));
    }

    public <RESULT_T> RESULT_T instanceOfOrThrow(final Class<RESULT_T> thisClass) {
        return instanceOf(thisClass).orElseThrow();
    }
}
