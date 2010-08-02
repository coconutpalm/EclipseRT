/*******************************************************************************
* Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Composent, Inc. - initial API and implementation
******************************************************************************/
package org.eclipse.ecf.mgmt.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.service.packageadmin.RequiredBundle;

public class RequiredBundleInfo implements IRequiredBundleInfo, Serializable {

	private static final long serialVersionUID = 2717107820744798832L;
	
	private String name;
	private String version;
	private BundleId bundle;
	private BundleId[] requiringBundles;
	private boolean removalPending;
	
	public RequiredBundleInfo(RequiredBundle requiredBundle) {
		this.name = requiredBundle.getSymbolicName();
		this.version = requiredBundle.getVersion().toString();
		this.bundle = getBundleIdForBundle(requiredBundle.getBundle());
		this.removalPending = requiredBundle.isRemovalPending();
		Bundle[] requiringBundles = requiredBundle.getRequiringBundles();
		if (requiringBundles != null) {
			List bs = new ArrayList();
			for(int i=0; i < requiringBundles.length; i++) {
				bs.add(getBundleIdForBundle(requiringBundles[i]));
			}
			this.requiringBundles = (BundleId[]) bs.toArray(new BundleId[] {});
		}
	}
	
	private BundleId getBundleIdForBundle(Bundle bundle) {
		return (bundle == null)?null:new BundleId(bundle.getSymbolicName(),bundle.getVersion().toString());
	}
	
	public String getSymbolicName() {
		return name;
	}

	public IBundleId getBundle() {
		return bundle;
	}

	public IBundleId[] getRequiringBundles() {
		return requiringBundles;
	}

	public String getVersion() {
		return version;
	}

	public boolean isRemovalPending() {
		return removalPending;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RequiredBundleInfo[name="); //$NON-NLS-1$
		builder.append(name);
		builder.append(", version="); //$NON-NLS-1$
		builder.append(version);
		builder.append(", bundle="); //$NON-NLS-1$
		builder.append(bundle);
		builder.append(", requiringBundles="); //$NON-NLS-1$
		builder.append(Arrays.toString(requiringBundles));
		builder.append(", removalPending="); //$NON-NLS-1$
		builder.append(removalPending);
		builder.append("]"); //$NON-NLS-1$
		return builder.toString();
	}

}
