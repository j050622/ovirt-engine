package org.ovirt.engine.ui.uicommonweb.models.vms;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ovirt.engine.core.common.businessentities.AuditLog;
import org.ovirt.engine.core.common.businessentities.VM;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.QueryType;
import org.ovirt.engine.ui.uicommonweb.Linq;

public class UserPortalVmEventListModel extends VmEventListModel {

    @Override
    protected void refreshModel() {
        if (getEntity() == null) {
            return;
        }

        VM vm = getEntity();
        super.syncSearch(QueryType.GetAllAuditLogsByVMId, new IdQueryParameters(vm.getId()));
    }

    @Override
    protected void preSearchCalled(VM vm) {
        // do nothing - only the webadmin sets the search string
    }

    @Override
    public void setItems(Collection value) {
        List<AuditLog> list = (List<AuditLog>) value;
        if (list != null) {
            Collections.sort(list, Linq.AuditLogComparer.reversed());
        }
        super.setItems(list);
    }

    @Override
    protected void onEntityContentChanged() {
        startProgress();
        super.onEntityContentChanged();
    }
}
