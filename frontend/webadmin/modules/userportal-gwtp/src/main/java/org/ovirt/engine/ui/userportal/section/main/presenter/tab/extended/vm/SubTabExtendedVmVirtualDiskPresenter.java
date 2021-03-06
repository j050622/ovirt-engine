package org.ovirt.engine.ui.userportal.section.main.presenter.tab.extended.vm;

import org.ovirt.engine.ui.common.presenter.AbstractSubTabPresenter;
import org.ovirt.engine.ui.common.widget.tab.ModelBoundTabData;
import org.ovirt.engine.ui.uicommonweb.models.userportal.UserPortalItemModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmDiskListModel;
import org.ovirt.engine.ui.uicommonweb.place.UserPortalApplicationPlaces;
import org.ovirt.engine.ui.userportal.ApplicationConstants;
import org.ovirt.engine.ui.userportal.gin.AssetProvider;
import org.ovirt.engine.ui.userportal.uicommon.model.vm.VmDiskListModelProvider;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class SubTabExtendedVmVirtualDiskPresenter
        extends AbstractSubTabExtendedVmPresenter<VmDiskListModel, SubTabExtendedVmVirtualDiskPresenter.ViewDef,
            SubTabExtendedVmVirtualDiskPresenter.ProxyDef> {

    private static final ApplicationConstants constants = AssetProvider.getConstants();

    @ProxyCodeSplit
    @NameToken(UserPortalApplicationPlaces.extendedVirtualMachineVirtualDiskSubTabPlace)
    public interface ProxyDef extends TabContentProxyPlace<SubTabExtendedVmVirtualDiskPresenter> {
    }

    public interface ViewDef extends AbstractSubTabPresenter.ViewDef<UserPortalItemModel> {
    }

    @TabInfo(container = ExtendedVmSubTabPanelPresenter.class)
    static TabData getTabData(
            VmDiskListModelProvider modelProvider) {
        return new ModelBoundTabData(constants.extendedVirtualMachineVirtualDiskSubTabLabel(), 5, modelProvider);
    }

    @Inject
    public SubTabExtendedVmVirtualDiskPresenter(EventBus eventBus, ViewDef view, ProxyDef proxy,
            PlaceManager placeManager, ExtendedVmMainTabSelectedItems selectedItems,
            VmDiskListModelProvider modelProvider) {
        super(eventBus, view, proxy, placeManager, selectedItems, modelProvider);
    }

}
