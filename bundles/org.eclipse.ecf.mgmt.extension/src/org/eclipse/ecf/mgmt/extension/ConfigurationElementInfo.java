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
import java.util.Arrays;
import java.util.Properties;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.osgi.service.resolver.State;

public class ConfigurationElementInfo implements IConfigurationElementInfo,
		Serializable {

	private static final long serialVersionUID = 6764665579990478373L;
	private Properties attributes;
	private ConfigurationElementInfo children[];
	private Long contributorId;
	private boolean valid;
	private String value;
	private IConfigurationElementInfo parent;
	private String namespaceIdentifier;
	private String name;
	private String extensionId;

	public ConfigurationElementInfo(IConfigurationElement e,
			IConfigurationElementInfo parent, State state) {
		name = e.getName();
		extensionId = e.getDeclaringExtension().getUniqueIdentifier();
		namespaceIdentifier = e.getNamespaceIdentifier();
		this.parent = parent;
		value = e.getValue();
		valid = e.isValid();
		contributorId = state.getBundle(e.getContributor().getName(), null).getBundleId();
		IConfigurationElement children[] = e.getChildren();
		this.children = new ConfigurationElementInfo[children.length];
		for (int i = 0; i < children.length; i++)
			this.children[i] = new ConfigurationElementInfo(children[i], this,
					state);
		String attributeNames[] = e.getAttributeNames();
		attributes = new Properties();
		for (int i = 0; i < attributeNames.length; i++) {
			String v = e.getAttribute(attributeNames[i]);
			if (v != null)
				attributes.put(attributeNames[i], v);
		}
	}
	
	public ConfigurationElementInfo(IConfigurationElement configElement, State state) {
		this(configElement, null, state);
	}

	public String getName() {
		return name;
	}

	public String getAttribute(String name) {
		return attributes.getProperty(name);
	}

	public String[] getAttributeNames() {
		return (String[]) attributes.keySet().toArray(new String[0]);
	}

	public IConfigurationElementInfo[] getChildren() {
		return children;
	}

	public Long getContributorId() {
		return contributorId;
	}

	public String getNamespaceIdentifier() {
		return namespaceIdentifier;
	}

	public IConfigurationElementInfo getParent() {
		return parent;
	}

	public String getValue() {
		return value;
	}

	public boolean isValid() {
		return valid;
	}

	public String getExtensionId() {
		return extensionId;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("ConfigurationElementInfo[");
		buf.append(getExtensionId());
		buf.append(";nsid=").append(getNamespaceIdentifier()).append(".")
				.append(getName());
		buf.append(";value=").append(getValue());
		buf.append(";valid=").append(isValid());
		IConfigurationElementInfo children[] = getChildren();
		buf.append(";children=")
				.append(((String) (children != null ? Arrays
						.asList(children) : "{}")));
		buf.append(";contributor=").append(getContributorId());
		buf.append(";attributes=").append(attributes).append("]");
		return buf.toString();
	}

}
