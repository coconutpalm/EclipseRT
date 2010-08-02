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

import org.eclipse.core.runtime.IStatus;
import org.osgi.service.packageadmin.PackageAdmin;

public interface IPackageAdminManager {

	public static final int	BUNDLE_TYPE_FRAGMENT = PackageAdmin.BUNDLE_TYPE_FRAGMENT;

	public IExportedPackageInfo[] getExportedPackages(IBundleId bundle);
	public IExportedPackageInfo[] getExportedPackages(String name);
	public IExportedPackageInfo getExportedPackage(String name);

	public IStatus refreshPackages(IBundleId[] bundles);
	public IStatus resolveBundles(IBundleId[] bundles);
	
	public IRequiredBundleInfo[] getRequiredBundles(String symbolicName);
	
	public IBundleId[] getBundles(String symbolicName, String versionRange);
	public IBundleId[] getFragments(IBundleId bundleId);
	public IBundleId[] getHosts(IBundleId bundleId);
	
	public IBundleId getBundle(String clazzName);
	
	public Integer getBundleType(IBundleId bundleId);
	
}
