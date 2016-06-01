/*
Copyright (c) 2016 Red Hat, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.ovirt.engine.api.v3.adapters;

import static org.ovirt.engine.api.v3.adapters.V3OutAdapters.adaptOut;

import org.ovirt.engine.api.model.StorageConnectionExtensions;
import org.ovirt.engine.api.v3.V3Adapter;
import org.ovirt.engine.api.v3.types.V3StorageConnectionExtensions;

public class V3StorageConnectionExtensionsOutAdapter implements V3Adapter<StorageConnectionExtensions, V3StorageConnectionExtensions> {
    @Override
    public V3StorageConnectionExtensions adapt(StorageConnectionExtensions from) {
        V3StorageConnectionExtensions to = new V3StorageConnectionExtensions();
        if (from.isSetActions()) {
            to.setActions(adaptOut(from.getActions()));
        }
        if (from.isSetActive()) {
            to.setActive(from.getActive());
        }
        if (from.isSetSize()) {
            to.setSize(from.getSize());
        }
        if (from.isSetTotal()) {
            to.setTotal(from.getTotal());
        }
        to.getStorageConnectionExtension().addAll(adaptOut(from.getStorageConnectionExtensions()));
        return to;
    }
}