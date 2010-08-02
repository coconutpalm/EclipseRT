/*******************************************************************************
 * Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.mgmt.framework.host;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.status.SerializableStatus;
import org.eclipse.ecf.mgmt.framework.BundleId;
import org.eclipse.ecf.mgmt.framework.ExportedPackageInfo;
import org.eclipse.ecf.mgmt.framework.IBundleId;
import org.eclipse.ecf.mgmt.framework.IExportedPackageInfo;
import org.eclipse.ecf.mgmt.framework.IPackageAdminManager;
import org.eclipse.ecf.mgmt.framework.IRequiredBundleInfo;
import org.eclipse.ecf.mgmt.framework.RequiredBundleInfo;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.packageadmin.RequiredBundle;
import org.osgi.util.tracker.ServiceTracker;

public class PackageAdminManager extends AbstractFrameworkManager implements
		IPackageAdminManager, IAdaptable {

	private ServiceTracker packageAdminTracker;
	private Object packageAdminTrackerLock = new Object();

	public PackageAdminManager(BundleContext context, LogService log) {
		super(context, log);
	}

	public PackageAdminManager(BundleContext context) {
		this(context, null);
	}

	private PackageAdmin getPackageAdmin() {
		synchronized (packageAdminTrackerLock) {
			if (packageAdminTracker == null) {
				packageAdminTracker = new ServiceTracker(context,
						PackageAdmin.class.getName(), null);
				packageAdminTracker.open();
			}
		}
		return (PackageAdmin) packageAdminTracker.getService();
	}

	private Bundle findBundleForId(IBundleId bundle) {
		Bundle[] allBundles = context.getBundles();
		Bundle result = null;
		for (int i = 0; i < allBundles.length; i++) {
			// Find bundle with symbolic name and version matching exactly
			if (allBundles[i].getSymbolicName()
					.equals(bundle.getSymbolicName())
					&& allBundles[i].getVersion().toString()
							.equals(bundle.getVersion())) {
				result = allBundles[i];
			}
		}
		return result;
	}

	private Bundle[] findBundlesForIds(IBundleId[] bundles) {
        if (bundles == null) return null;
        List results = new ArrayList();
        for(int i=0; i < bundles.length; i++) {
        	Bundle b = findBundleForId(bundles[i]);
        	if (b != null) results.add(b);
        }
		return (Bundle[]) results.toArray(new Bundle[] {});
	}

	public IExportedPackageInfo[] getExportedPackages(IBundleId bundleId) {
		PackageAdmin pa = getPackageAdmin();
		ExportedPackage[] exportedPackages = null;
		if (bundleId == null) {
			exportedPackages = pa.getExportedPackages((Bundle) null);
		} else {
			Bundle localBundle = findBundleForId(bundleId);
			if (localBundle == null)
				return null;
			exportedPackages = pa.getExportedPackages(localBundle);
		}
		if (exportedPackages == null)
			return null;
		List results = new ArrayList();
		for (int i = 0; i < exportedPackages.length; i++) {
			results.add(new ExportedPackageInfo(exportedPackages[i]));
		}
		return (IExportedPackageInfo[]) results
				.toArray(new IExportedPackageInfo[] {});
	}

	public IExportedPackageInfo[] getExportedPackages(String name) {
		if (name == null) return null;
		ExportedPackage[] eps = getPackageAdmin().getExportedPackages(name);
		if (eps == null) return null;
		List results = new ArrayList();
		for(int i=0; i < eps.length; i++) results.add(new ExportedPackageInfo(eps[i]));
		return (IExportedPackageInfo[]) results.toArray(new IExportedPackageInfo[] {});
	}

	public IExportedPackageInfo getExportedPackage(String name) {
		if (name == null) return null;
		ExportedPackage ep = getPackageAdmin().getExportedPackage(name);
		if (ep == null) return null;
		return new ExportedPackageInfo(ep);
	}

	public IStatus refreshPackages(IBundleId[] bundles) {
		Bundle[] bs = findBundlesForIds(bundles);
		try {
			getPackageAdmin().refreshPackages(bs);
		} catch (Exception e) {
			return createErrorStatus("refreshPackages failed", e); //$NON-NLS-1$
		}
		return new SerializableStatus(Status.OK_STATUS);
	}

	public IStatus resolveBundles(IBundleId[] bundles) {
		Bundle[] bs = findBundlesForIds(bundles);
		try {
			getPackageAdmin().resolveBundles(bs);
		} catch (Exception e) {
			return createErrorStatus("resolveBundles failed", e); //$NON-NLS-1$
		}
		return new SerializableStatus(Status.OK_STATUS);
	}

	public IRequiredBundleInfo[] getRequiredBundles(String symbolicName) {
		RequiredBundle[] rbs = getPackageAdmin().getRequiredBundles(symbolicName);
		if (rbs == null) return null;
		List results = new ArrayList();
		for(int i=0; i < rbs.length; i++) results.add(new RequiredBundleInfo(rbs[i]));
		return (IRequiredBundleInfo[]) results.toArray(new IRequiredBundleInfo[] {});
	}

	public IBundleId[] getBundles(String symbolicName, String versionRange) {
		if (symbolicName == null) return null;
		Bundle[] bundles = getPackageAdmin().getBundles(symbolicName, versionRange);
		return getBundleIdsForBundles(bundles);
	}

	private IBundleId[] getBundleIdsForBundles(Bundle[] bundles) {
		if (bundles == null) return null;
		List results = new ArrayList();
		for(int i=0; i < bundles.length; i++) results.add(new BundleId(bundles[i].getSymbolicName(),bundles[i].getVersion().toString()));
		return (IBundleId[]) results.toArray(new IBundleId[] {});
	}
	
	public IBundleId[] getFragments(IBundleId bundleId) {
		Bundle bundle = findBundleForId(bundleId);
		if (bundle == null) return null;
		Bundle[] bundles = getPackageAdmin().getFragments(bundle);
		return getBundleIdsForBundles(bundles);
	}

	public IBundleId[] getHosts(IBundleId bundleId) {
		Bundle bundle = findBundleForId(bundleId);
		if (bundle == null) return null;
		Bundle[] bundles = getPackageAdmin().getHosts(bundle);
		return getBundleIdsForBundles(bundles);
	}

	public IBundleId getBundle(String clazzName) {
		if (clazzName == null) return null;
		Class clazz = getClassForName(clazzName);
		if (clazz == null) return null;
		Bundle b = getPackageAdmin().getBundle(clazz);
		return (b == null)?null:new BundleId(b.getSymbolicName(),b.getVersion().toString());
	}
	
	protected Class getClassForName(String clazzName) {
		try {
			return Class.forName(clazzName);
		} catch (ClassNotFoundException e) {
			logError("getClassForName className="+clazzName+" could not be loaded", e); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (NoClassDefFoundError e) {
			logError("getClassForName className="+clazzName+" could not be loaded", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return null;
	}

	public Integer getBundleType(IBundleId bundleId) {
		Bundle b = findBundleForId(bundleId);
		if (b == null) return null;
		int bt = getPackageAdmin().getBundleType(b);
		return new Integer(bt);
	}

	public void close() {
		synchronized (packageAdminTrackerLock) {
			if (packageAdminTracker != null) {
				packageAdminTracker.close();
				packageAdminTracker = null;
			}
		}
	}
}
