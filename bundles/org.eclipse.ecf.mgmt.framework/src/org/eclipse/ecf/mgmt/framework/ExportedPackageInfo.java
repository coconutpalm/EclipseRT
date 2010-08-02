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
import org.osgi.service.packageadmin.ExportedPackage;

public class ExportedPackageInfo implements IExportedPackageInfo, Serializable {

	private static final long serialVersionUID = -4178151140909582847L;
	private String name;
	private String version;
	private boolean removalPending;
	private BundleId exportingBundle;
	private BundleId[] importingBundles;
	
	public ExportedPackageInfo(ExportedPackage exportedPackage) {
		this.name = exportedPackage.getName();
		this.version = exportedPackage.getVersion().toString();
		this.removalPending = exportedPackage.isRemovalPending();
		this.exportingBundle = getBundleIdForBundle(exportedPackage.getExportingBundle());
		Bundle[] importingBundles = exportedPackage.getImportingBundles();
		if (importingBundles != null) {
			List bs = new ArrayList();
			for(int i=0; i < importingBundles.length; i++) {
				bs.add(getBundleIdForBundle(importingBundles[i]));
			}
			this.importingBundles = (BundleId[]) bs.toArray(new BundleId[] {});
		}
	}
	
	private BundleId getBundleIdForBundle(Bundle bundle) {
		return (bundle == null)?null:new BundleId(bundle.getSymbolicName(),bundle.getVersion().toString());
	}
	
	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public boolean isRemovalPending() {
		return removalPending;
	}

	public IBundleId getExportingBundle() {
		return exportingBundle;
	}

	public IBundleId[] getImportingBundles() {
		return importingBundles;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExportedPackageInfo[name="); //$NON-NLS-1$
		builder.append(name);
		builder.append(", version="); //$NON-NLS-1$
		builder.append(version);
		builder.append(", removalPending="); //$NON-NLS-1$
		builder.append(removalPending);
		builder.append(", exportingBundle="); //$NON-NLS-1$
		builder.append(exportingBundle);
		builder.append(", importingBundles="); //$NON-NLS-1$
		builder.append(Arrays.toString(importingBundles));
		builder.append("]"); //$NON-NLS-1$
		return builder.toString();
	}

}
