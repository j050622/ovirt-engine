package org.ovirt.engine.core.bll.storage;

import java.util.List;

import org.ovirt.engine.core.bll.Backend;
import org.ovirt.engine.core.bll.QueriesCommandBase;
import org.ovirt.engine.core.common.businessentities.StorageType;
import org.ovirt.engine.core.common.businessentities.StorageDomain;
import org.ovirt.engine.core.common.queries.VdsIdParametersBase;
import org.ovirt.engine.core.common.vdscommands.VDSCommandType;
import org.ovirt.engine.core.common.vdscommands.VdsIdVDSCommandParametersBase;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dal.dbbroker.DbFacade;
import org.ovirt.engine.core.utils.linq.LinqUtils;
import org.ovirt.engine.core.utils.linq.Predicate;

public class GetVgListQuery<P extends VdsIdParametersBase> extends QueriesCommandBase<P> {
    public GetVgListQuery(P parameters) {
        super(parameters);
    }

    @Override
    protected void executeQueryCommand() {
        java.util.ArrayList<StorageDomain> vgsFromVds = (java.util.ArrayList<StorageDomain>) (Backend.getInstance()
                .getResourceManager().RunVdsCommand(VDSCommandType.GetVGList,
                                                    new VdsIdVDSCommandParametersBase(getParameters().getVdsId())))
                .getReturnValue();

        List<StorageDomain> vgsFromDb = LinqUtils.filter(DbFacade.getInstance().getStorageDomainDao().getAll(),
                new Predicate<StorageDomain>() {
                    @Override
                    public boolean eval(StorageDomain storageDomain) {
                        return storageDomain.getstorage_type() == StorageType.ISCSI
                                || storageDomain.getstorage_type() == StorageType.FCP;
                    }
                });

        java.util.HashSet<String> vgIdsFromDb = new java.util.HashSet<String>();

        for (StorageDomain domain : vgsFromDb) {
            vgIdsFromDb.add(domain.getstorage());
        }

        java.util.ArrayList<StorageDomain> returnValue = new java.util.ArrayList<StorageDomain>();

        for (StorageDomain domain : vgsFromVds) {
            if (domain.getId().equals(Guid.Empty) && !vgIdsFromDb.contains(domain.getstorage())) {
                returnValue.add(domain);
            }
        }
        getQueryReturnValue().setReturnValue(returnValue);
    }
}
