/*******************************************************************************
 * Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.mgmt.extension;

public interface IExtensionRegistryManager {

    public String[] getExtensionPointIds();
    public IExtensionPointInfo getExtensionPoint(String extensionPointId);
    public IExtensionPointInfo[] getExtensionPointsForContributor(String contributorId);
    public IExtensionPointInfo[] getExtensionPoints();
    
    public IExtensionInfo getExtension(String extensionPointId, String extensionId);
    public IExtensionInfo[] getExtensionsForContributor(String contributorId);
    public IExtensionInfo[] getExtensions(String extensionPointId);

    public IConfigurationElementInfo[] getConfigurationElements(String extensionPointId);

}
