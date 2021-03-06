package org.ovirt.engine.ui.webadmin.section.main.presenter.tab;

import java.util.List;

import org.ovirt.engine.core.common.businessentities.network.VnicProfileView;
import org.ovirt.engine.ui.common.place.PlaceRequestFactory;
import org.ovirt.engine.ui.common.uicommon.model.MainModelProvider;
import org.ovirt.engine.ui.common.widget.OvirtBreadCrumbs;
import org.ovirt.engine.ui.common.widget.tab.ModelBoundTabData;
import org.ovirt.engine.ui.uicommonweb.models.profiles.VnicProfileListModel;
import org.ovirt.engine.ui.uicommonweb.place.WebAdminApplicationPlaces;
import org.ovirt.engine.ui.webadmin.section.main.presenter.AbstractMainTabWithDetailsPresenter;
import org.ovirt.engine.ui.webadmin.section.main.presenter.MainTabPanelPresenter;
import org.ovirt.engine.ui.webadmin.widget.tab.MenuLayoutMenuDetails;
import org.ovirt.engine.ui.webadmin.widget.tab.WebadminMenuLayout;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.annotation.GenEvent;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class MainTabVnicProfilePresenter extends AbstractMainTabWithDetailsPresenter<VnicProfileView, VnicProfileListModel, MainTabVnicProfilePresenter.ViewDef, MainTabVnicProfilePresenter.ProxyDef> {

    @GenEvent
    public class VnicProfileSelectionChange {

        List<VnicProfileView> selectedItems;

    }

    @ProxyCodeSplit
    @NameToken(WebAdminApplicationPlaces.vnicProfileMainTabPlace)
    public interface ProxyDef extends TabContentProxyPlace<MainTabVnicProfilePresenter> {
    }

    public interface ViewDef extends AbstractMainTabWithDetailsPresenter.ViewDef<VnicProfileView> {
    }

    @TabInfo(container = MainTabPanelPresenter.class)
    static TabData getTabData(
            MainModelProvider<VnicProfileView, VnicProfileListModel> modelProvider, WebadminMenuLayout menuLayout) {
        MenuLayoutMenuDetails menuTabDetails =
                menuLayout.getDetails(WebAdminApplicationPlaces.vnicProfileMainTabPlace);
        return new ModelBoundTabData(menuTabDetails.getSecondaryTitle(), menuTabDetails.getSecondaryPriority(),
                menuTabDetails.getPrimaryTitle(), menuTabDetails.getPrimaryPriority(), modelProvider,
                menuTabDetails.getIcon());
    }

    @Inject
    public MainTabVnicProfilePresenter(EventBus eventBus, ViewDef view, ProxyDef proxy,
            PlaceManager placeManager, MainModelProvider<VnicProfileView, VnicProfileListModel> modelProvider,
            OvirtBreadCrumbs<VnicProfileView, VnicProfileListModel> breadCrumbs) {
        super(eventBus, view, proxy, placeManager, modelProvider, null, breadCrumbs);
    }

    @Override
    protected void fireTableSelectionChangeEvent() {
        VnicProfileSelectionChangeEvent.fire(this, getSelectedItems());
    }

    @Override
    protected PlaceRequest getMainTabRequest() {
        return PlaceRequestFactory.get(WebAdminApplicationPlaces.vnicProfileMainTabPlace);
    }

}
