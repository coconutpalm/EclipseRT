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

import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.osgi.service.resolver.State;

public class ExtensionPointInfo implements IExtensionPointInfo, Serializable {

	private static final long serialVersionUID = -5528787248698393129L;

	private Long contributorId;
	private IExtensionInfo extensions[];
	private String label;
	private String namespaceIdentifier;
	private String simpleIdentifier;
	private String uniqueIdentifier;
	private boolean valid;

	public ExtensionPointInfo(IExtensionPoint ep, State state) {
		label = ep.getLabel();
		namespaceIdentifier = ep.getNamespaceIdentifier();
		simpleIdentifier = ep.getSimpleIdentifier();
		uniqueIdentifier = ep.getUniqueIdentifier();
		valid = ep.isValid();
		contributorId = state.getBundle(ep.getContributor().getName(), null).getBundleId();
		org.eclipse.core.runtime.IExtension es[] = ep.getExtensions();
		extensions = new IExtensionInfo[es.length];
		for (int i = 0; i < es.length; i++)
			extensions[i] = new ExtensionInfo(es[i], state);
	}

	public Long getContributorId() {
		return contributorId;
	}

	public IExtensionInfo[] getExtensions() {
		return extensions;
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
		StringBuffer buf = new StringBuffer("ExtensionPointInfo[");
		buf.append("uid=").append(getUniqueIdentifier());
		buf.append(";label=").append(getLabel());
		buf.append(";valid=").append(isValid());
		buf.append(";contributor=").append(getContributorId());
		buf.append(";extensions=").append(Arrays.asList(getExtensions()))
				.append("]");
		return buf.toString();
	}

}
