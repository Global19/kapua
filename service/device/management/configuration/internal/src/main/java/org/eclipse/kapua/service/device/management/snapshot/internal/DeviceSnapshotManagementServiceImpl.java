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
package org.eclipse.kapua.service.device.management.snapshot.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseException;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.management.snapshot.internal.exception.SnapshotGetManagementException;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestChannel;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestMessage;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestPayload;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotResponseMessage;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotResponsePayload;

import java.util.Date;

/**
 * Device snapshot service implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceSnapshotManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceSnapshotManagementService {

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    @Override
    public DeviceSnapshots get(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        SnapshotRequestChannel snapshotRequestChannel = new SnapshotRequestChannel();
        snapshotRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        snapshotRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        snapshotRequestChannel.setMethod(KapuaMethod.READ);

        SnapshotRequestPayload snapshotRequestPayload = new SnapshotRequestPayload();

        SnapshotRequestMessage snapshotRequestMessage = new SnapshotRequestMessage();
        snapshotRequestMessage.setScopeId(scopeId);
        snapshotRequestMessage.setDeviceId(deviceId);
        snapshotRequestMessage.setCapturedOn(new Date());
        snapshotRequestMessage.setPayload(snapshotRequestPayload);
        snapshotRequestMessage.setChannel(snapshotRequestChannel);

        //
        // Do get
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(snapshotRequestMessage, timeout);
        SnapshotResponseMessage responseMessage = (SnapshotResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, snapshotRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            SnapshotResponsePayload responsePayload = responseMessage.getPayload();

            String body;
            try {
                body = new String(responsePayload.getBody(), CHAR_ENCODING);
            } catch (Exception e) {
                throw new DeviceManagementResponseException(e, (Object) responsePayload.getBody());
            }

            DeviceSnapshots deviceSnapshots;
            try {
                deviceSnapshots = XmlUtil.unmarshal(body, DeviceSnapshotsImpl.class);
            } catch (Exception e) {
                throw new DeviceManagementResponseException(e, body);
            }

            return deviceSnapshots;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new SnapshotGetManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    @Override
    public void rollback(KapuaId scopeId, KapuaId deviceId, String snapshotId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notEmptyOrNull(snapshotId, "snapshotId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.execute, scopeId));

        //
        // Prepare the request
        SnapshotRequestChannel snapshotRequestChannel = new SnapshotRequestChannel();
        snapshotRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        snapshotRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        snapshotRequestChannel.setMethod(KapuaMethod.EXECUTE);
        snapshotRequestChannel.setSnapshotId(snapshotId);

        SnapshotRequestPayload snapshotRequestPayload = new SnapshotRequestPayload();

        SnapshotRequestMessage snapshotRequestMessage = new SnapshotRequestMessage();
        snapshotRequestMessage.setScopeId(scopeId);
        snapshotRequestMessage.setDeviceId(deviceId);
        snapshotRequestMessage.setCapturedOn(new Date());
        snapshotRequestMessage.setPayload(snapshotRequestPayload);
        snapshotRequestMessage.setChannel(snapshotRequestChannel);

        //
        // Do exec
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(snapshotRequestMessage, timeout);
        SnapshotResponseMessage responseMessage = (SnapshotResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, snapshotRequestMessage, responseMessage);

        if (!responseMessage.getResponseCode().isAccepted()) {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new SnapshotGetManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }
}
