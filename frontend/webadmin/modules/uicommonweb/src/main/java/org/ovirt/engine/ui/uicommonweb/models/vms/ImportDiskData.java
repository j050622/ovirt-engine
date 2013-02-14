package org.ovirt.engine.ui.uicommonweb.models.vms;

import java.util.ArrayList;
import java.util.Map;

import org.ovirt.engine.core.common.businessentities.Quota;
import org.ovirt.engine.core.common.businessentities.VolumeType;
import org.ovirt.engine.core.common.businessentities.StorageDomain;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.ui.uicommonweb.models.EntityModel;
import org.ovirt.engine.ui.uicompat.Event;
import org.ovirt.engine.ui.uicompat.EventArgs;
import org.ovirt.engine.ui.uicompat.IEventListener;

public class ImportDiskData {
    ArrayList<StorageDomain> storageDomains;
    StorageDomain selectedStorageDomain;
    EntityModel collapseSnapshots;
    private ArrayList<StorageDomain> allStorageDomains;
    VolumeType volumeType = VolumeType.Sparse;
    VolumeType selectedVolumeType;
    Map<Guid, ArrayList<Quota>> storageQuotaList;
    Quota selectedQuota;

    public Map<Guid, ArrayList<Quota>> getStorageQuotaList() {
        return storageQuotaList;
    }

    public void setStorageQuotaList(Map<Guid, ArrayList<Quota>> storageQuotaList) {
        this.storageQuotaList = storageQuotaList;
    }

    public Quota getSelectedQuota() {
        if (getQuotaList().contains(selectedQuota)) {
            return selectedQuota;
        }
        if (getQuotaList().size() > 0) {
            return getQuotaList().get(0);
        }

        return null;
    }

    public void setSelectedQuota(Quota selectedQuota) {
        this.selectedQuota = selectedQuota;
    }

    public VolumeType getSelectedVolumeType() {
        if ((Boolean) collapseSnapshots.getEntity() && selectedVolumeType != null) {
            return selectedVolumeType;
        }
        return getVolumeType();
    }

    public void setSelectedVolumeType(VolumeType selectedVolumeType) {
        this.selectedVolumeType = selectedVolumeType;
    }

    public ArrayList<StorageDomain> getStorageDomains() {
        if ((Boolean) collapseSnapshots.getEntity() && storageDomains != null && !storageDomains.isEmpty()) {
            return allStorageDomains;
        }
        return storageDomains;
    }

    public void setStorageDomains(ArrayList<StorageDomain> storageDomains) {
        this.storageDomains = storageDomains;
    }

    public StorageDomain getSelectedStorageDomain() {
        if (selectedStorageDomain == null && !storageDomains.isEmpty()) {
            selectedStorageDomain = storageDomains.get(0);
        }

        return selectedStorageDomain;
    }

    public void setSelectedStorageDomain(StorageDomain selectedStorageDomain) {
        this.selectedStorageDomain = selectedStorageDomain;
    }

    public void setSelectedStorageDomainString(String value) {
        for (StorageDomain storageDomain : getStorageDomains()) {
            if (storageDomain.getstorage_name().equals(value)) {
                setSelectedStorageDomain(storageDomain);
                break;
            }
        }
    }

    public void setAllStorageDomains(ArrayList<StorageDomain> filteredStorageDomain) {
        allStorageDomains = filteredStorageDomain;
    }

    public VolumeType getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(VolumeType type) {
        volumeType = type;
    }

    public void setCollapseSnapshot(EntityModel collapseSnapshotsModel) {
        this.collapseSnapshots = collapseSnapshotsModel;
        collapseSnapshots.getEntityChangedEvent().addListener(new IEventListener() {
            @Override
            public void eventRaised(Event ev, Object sender, EventArgs args) {
                setVolumeType(VolumeType.Sparse);
            }
        });
    }

    public ArrayList<Quota> getQuotaList() {
        if (storageQuotaList == null || allStorageDomains.isEmpty() || storageDomains.isEmpty()) {
            return new ArrayList<Quota>();
        }
        if (selectedStorageDomain == null) {
            selectedStorageDomain = storageDomains.get(0);
        }
        return storageQuotaList.get(selectedStorageDomain.getId());
    }

    public void setSelectedQuotaString(String value) {
        if (getQuotaList() != null) {
            for (Quota quota : getQuotaList()) {
                if (quota.getQuotaName().equals(value)) {
                    setSelectedQuota(quota);
                    break;
                }
            }
        }
    }
}
