/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.group;

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link GroupFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 1.0.0
 */
public interface GroupFactory extends KapuaEntityFactory<Group, GroupCreator, GroupQuery, GroupListResult> {

    /**
     * Instantiates a new {@link GroupCreator}.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link GroupCreator}
     * @param name    The name to set into the {@link GroupCreator}
     * @return The newly instantatied {@link GroupCreator}
     * @since 1.0.0
     */
    GroupCreator newCreator(KapuaId scopeId, String name);

}
