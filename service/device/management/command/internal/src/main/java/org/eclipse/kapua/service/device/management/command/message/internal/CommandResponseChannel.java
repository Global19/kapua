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
package org.eclipse.kapua.service.device.management.command.message.internal;

import org.eclipse.kapua.service.device.management.command.internal.CommandAppProperties;
import org.eclipse.kapua.service.device.management.commons.message.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseChannel;

/**
 * Command {@link KapuaResponseChannel}.
 *
 * @since 1.0.0
 */
public class CommandResponseChannel extends KapuaAppChannelImpl implements KapuaResponseChannel {

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public CommandResponseChannel() {
        setAppName(CommandAppProperties.APP_NAME);
        setVersion(CommandAppProperties.APP_VERSION);
    }
}
