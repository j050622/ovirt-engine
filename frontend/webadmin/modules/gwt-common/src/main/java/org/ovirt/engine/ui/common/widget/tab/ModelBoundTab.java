package org.ovirt.engine.ui.common.widget.tab;

import org.ovirt.engine.ui.common.uicommon.model.ModelProvider;
import org.ovirt.engine.ui.uicommonweb.models.HasEntity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ModelBoundTab extends SimpleTab implements HasHandlers {

    private final EventBus eventBus;

    public ModelBoundTab(final ModelBoundTabData tabData, AbstractTabPanel tabPanel, EventBus eventBus) {
        super(tabData, tabPanel);
        setGroupTitle(tabData.getGroupTitle());
        setGroupPriority(tabData.getGroupPriority());
        setIcon(tabData.getIcon());

        this.eventBus = eventBus;
        // Update tab accessibility
        setAccessible(tabData.getModelProvider().getModel().getIsAvailable());
        registerModelEventListeners(tabData.getModelProvider());
    }

    void registerModelEventListeners(final ModelProvider<? extends HasEntity> modelProvider) {
        modelProvider.getModel().getPropertyChangedEvent().addListener((ev, sender, args) -> {
            // Update tab accessibility when 'IsAvailable' property changes
            if ("IsAvailable".equals(args.propertyName)) { //$NON-NLS-1$
                boolean isAvailable = modelProvider.getModel().getIsAvailable();
                setAccessible(isAvailable);
            }
        });
    }

    @Override
    public void setAccessible(boolean accessible) {
        boolean wasAccessible = isAccessible();
        super.setAccessible(accessible);

        if (accessible != wasAccessible) {
            TabAccessibleChangeEvent.fire(this, this);
        }
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }

}
