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

public interface IConfigurationElementInfo {

    public String getAttribute(String s);

    public String[] getAttributeNames();

    public IConfigurationElementInfo[] getChildren();

    public IConfigurationElementInfo getParent();

    public String getValue();

    public String getNamespaceIdentifier();

    public Long getContributorId();

    public boolean isValid();

    public String getName();

    public String getExtensionId();

}
