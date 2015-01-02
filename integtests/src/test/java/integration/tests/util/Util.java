package integration.tests.util;

import java.util.List;
import com.google.common.base.Throwables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Util {
    private Util(){}

    public static Matcher<? extends Throwable> causalChainContains(final Class<?> cls) {
        return new TypeSafeMatcher<Throwable>() {
            @Override
            protected boolean matchesSafely(Throwable item) {
                final List<Throwable> causalChain = Throwables.getCausalChain(item);
                for (Throwable throwable : causalChain) {
                    if(cls.isAssignableFrom(throwable.getClass())){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("exception with causal chain containing " + cls.getSimpleName());
            }
        };
    }
}
