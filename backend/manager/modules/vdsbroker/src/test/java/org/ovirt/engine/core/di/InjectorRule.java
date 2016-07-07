package org.ovirt.engine.core.di;

import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.enterprise.inject.spi.BeanManager;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class InjectorRule extends TestWatcher {

    // create a new injector instance
    private final Injector mockedInjector;

    public InjectorRule() {
        mockedInjector = mock(Injector.class);
        when(mockedInjector.getManager()).thenReturn(mock(BeanManager.class, RETURNS_MOCKS));
    }

    private void overrideInjector(Injector mockedInjector) {
        try {
            // set the internal injector
            Field holdingMember = Injector.class.getDeclaredField("injector");
            holdingMember.setAccessible(true);
            holdingMember.set(Injector.class, mockedInjector);
        } catch (Exception e) {
            // if something bad happened the test shouldn't run
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void starting(Description description) {
        super.starting(description);
        overrideInjector(mockedInjector);
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
        overrideInjector(null);
        reset(mockedInjector);
    }

    public <T> void bind(Class<T> pureClsType, T instance) {
        when(mockedInjector.instanceOf(pureClsType)).thenReturn(instance);
    }

}