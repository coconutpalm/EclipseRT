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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.osgi.service.resolver.State;

public class ExtensionInfo implements IExtensionInfo, Serializable {
	
	private static final long serialVersionUID = -7781788436602741571L;
	private String label;
	private String identifier;
	private Long contributorId;
	private String namespaceIdentifier;
	private String simpleIdentifier;
	private String uniqueIdentifier;
	private boolean valid;
	private IConfigurationElementInfo configurationElementInfos[];

	public ExtensionInfo(IExtension declaringExtension, State state) {
		namespaceIdentifier = declaringExtension.getNamespaceIdentifier();
		label = declaringExtension.getLabel();
		contributorId = state.getBundle(declaringExtension.getContributor().getName(), null).getBundleId();
		identifier = declaringExtension.getExtensionPointUniqueIdentifier();
		simpleIdentifier = declaringExtension.getSimpleIdentifier();
		uniqueIdentifier = declaringExtension.getUniqueIdentifier();
		valid = declaringExtension.isValid();
		IConfigurationElement configElements[] = declaringExtension
				.getConfigurationElements();
		configurationElementInfos = createConfigurationElementInfos(
				configElements, state);
	}

	private IConfigurationElementInfo[] createConfigurationElementInfos(
			IConfigurationElement configurationElements[], State state) {
		if (configurationElements == null)
			return null;
		List<ConfigurationElementInfo> results = new ArrayList<ConfigurationElementInfo>();
		for (int i = 0; i < configurationElements.length; i++)
			results.add(new ConfigurationElementInfo(configurationElements[i],
					state));

		return (IConfigurationElementInfo[]) results
				.toArray(new IConfigurationElementInfo[0]);
	}

	public Long getContributorId() {
		return contributorId;
	}

	public String getExtensionPointUniqueIdentifier() {
		return identifier;
	}

	public String getLabel() {
		return label;
	}

	public String getNamespaceIdentifier() {
		return namespaceIdentifier;
	}

	public String getSimpleIdentifier() {
		return simpleIdentifier;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public boolean isValid() {
		return valid;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("ExtensionInfo[");
		buf.append(getExtensionPointUniqueIdentifier());
		buf.append(";namespace=").append(getNamespaceIdentifier());
		buf.append(";sid=").append(getSimpleIdentifier());
		buf.append(";uid=").append(getUniqueIdentifier());
		buf.append(";valid=").append(isValid());
		buf.append(";label=").append(getLabel());
		buf.append(";contributor=").append(getContributorId()).append("]");
		IConfigurationElementInfo configElementInfo[] = getConfigurationElements();
		buf.append(";configElements=").append(
				((String) (configElementInfo != null ? Arrays
						.asList(configElementInfo) : "{}")));
		buf.append("]");
		return buf.toString();
	}

	public IConfigurationElementInfo[] getConfigurationElements() {
		return configurationElementInfos;
	}

}
