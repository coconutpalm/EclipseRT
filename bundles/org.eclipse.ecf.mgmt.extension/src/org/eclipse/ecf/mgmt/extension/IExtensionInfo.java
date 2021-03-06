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

public interface IExtensionInfo {

    public String getSimpleIdentifier();

    public String getUniqueIdentifier();

    public boolean isValid();

    public String getLabel();

    public String getNamespaceIdentifier();

    public String getExtensionPointUniqueIdentifier();

    public Long getContributorId();

    public IConfigurationElementInfo[] getConfigurationElements();

}
