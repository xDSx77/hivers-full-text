package fr.epita.hivers;

import fr.epita.hivers.hivers.DefaultScope;
import fr.epita.hivers.hivers.Prototype;
import fr.epita.hivers.hivers.Singleton;
import org.junit.jupiter.api.Test;

class HiversTest {

    @Test
    public void testBasicHiversUserCase() {

        final var hivers = new Hivers();

        hivers.addProvider(new Prototype<>(TestService.class, PingService::new));
        hivers.addProvider(new Prototype<>(Nested.class, () -> new Nested(hivers.instanceOfOrThrow(TestService.class))));
        hivers.addProvider(new Singleton<>(Nested.class, new Nested(hivers.instanceOfOrThrow(TestService.class))));

        hivers.instanceOfOrThrow(TestService.class).ping();
        System.out.println(hivers.instanceOfOrThrow(TestService.class));
        System.out.println(hivers.instanceOfOrThrow(TestService.class));
        System.out.println(hivers.instanceOfOrThrow(TestService.class));

        hivers.push(new DefaultScope());
        hivers.addProvider(new Singleton<>(TestService.class, new PongService()));
        System.out.println(hivers.instanceOfOrThrow(TestService.class));
        System.out.println(hivers.instanceOfOrThrow(TestService.class));
        System.out.println(hivers.instanceOfOrThrow(TestService.class));

        hivers.instanceOf(TestService.class).orElseThrow().ping();

        hivers.pop();
        hivers.instanceOf(TestService.class).orElseThrow().ping();
    }

    public interface TestService {
        void ping();
    }

    public static class PingService implements TestService {
        @Override
        public void ping() { System.out.println("ping"); }
    }

    public static class PongService implements TestService {
        @Override
        public void ping() { System.out.println("pong"); }
    }

    public static class Nested {
        private final TestService testService;

        public Nested(final TestService testService) {
            this.testService = testService;
        }
    }

}