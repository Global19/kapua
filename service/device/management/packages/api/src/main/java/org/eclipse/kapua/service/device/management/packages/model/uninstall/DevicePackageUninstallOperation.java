/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model.uninstall;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device package uninstall operation entity definition.
 *
 * @since 1.0
 */
public interface DevicePackageUninstallOperation {

    /**
     * Get the package identifier
     *
     * @return
     */
    KapuaId getId();

    /**
     * Set the package identifier
     *
     * @param id
     */
    void setId(KapuaId id);

    /**
     * Get the package name
     *
     * @return
     */
    String getName();

    /**
     * Set the package name
     *
     * @param packageName
     */
    void setName(String packageName);

    /**
     * Get the package version
     *
     * @return
     */
    String getVersion();

    /**
     * Set the package version
     *
     * @param version
     */
    void setVersion(String version);

    /**
     * Get the package uninstall status
     *
     * @return
     */
    DevicePackageUninstallStatus getStatus();

    /**
     * Set the package uninstall status
     *
     * @param status
     */
    void setStatus(DevicePackageUninstallStatus status);
}
