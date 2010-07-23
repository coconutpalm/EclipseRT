/*******************************************************************************
 * Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.mgmt.extension.host;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.ecf.internal.mgmt.extension.host.Activator;
import org.eclipse.ecf.mgmt.extension.ConfigurationElementInfo;
import org.eclipse.ecf.mgmt.extension.ExtensionInfo;
import org.eclipse.ecf.mgmt.extension.ExtensionPointInfo;
import org.eclipse.ecf.mgmt.extension.IConfigurationElementInfo;
import org.eclipse.ecf.mgmt.extension.IExtensionInfo;
import org.eclipse.ecf.mgmt.extension.IExtensionPointInfo;
import org.eclipse.ecf.mgmt.extension.IExtensionRegistryManager;
import org.eclipse.osgi.service.resolver.PlatformAdmin;
import org.eclipse.osgi.service.resolver.State;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ExtensionRegistryManager implements IExtensionRegistryManager,
		IAdaptable {

	private BundleContext context;
	private ServiceTracker platformAdminServiceTracker;

	public ExtensionRegistryManager(BundleContext ctxt) {
		context = ctxt;
	}

	private State getPlatformState() {
		PlatformAdmin platformAdmin = getPlatformAdmin();
		if (platformAdmin == null)
			return null;
		else
			return platformAdmin.getState(false);
	}

	private synchronized PlatformAdmin getPlatformAdmin() {
		if (platformAdminServiceTracker == null) {
			platformAdminServiceTracker = new ServiceTracker(context,
					org.eclipse.osgi.service.resolver.PlatformAdmin.class
							.getName(), null);
			platformAdminServiceTracker.open();
		}
		return (PlatformAdmin) platformAdminServiceTracker.getService();
	}

	public synchronized void close() {
		if (platformAdminServiceTracker != null) {
			platformAdminServiceTracker.close();
			platformAdminServiceTracker = null;
		}
		context = null;
	}

	private IConfigurationElementInfo[] createRemoteConfigurationElements(
			IConfigurationElement ces[], State state) {
		IConfigurationElementInfo results[] = new IConfigurationElementInfo[ces.length];
		for (int i = 0; i < ces.length; i++)
			results[i] = new ConfigurationElementInfo(ces[i], null, state);

		return results;
	}

	private IExtensionPointInfo[] createRemoteExtensionPoints(
			IExtensionPoint eps[], State state) {
		IExtensionPointInfo results[] = new IExtensionPointInfo[eps.length];
		for (int i = 0; i < eps.length; i++)
			results[i] = new ExtensionPointInfo(eps[i], state);

		return results;
	}

	private IExtensionInfo[] createRemoteExtensions(IExtension es[], State state) {
		IExtensionInfo results[] = new IExtensionInfo[es.length];
		for (int i = 0; i < es.length; i++)
			results[i] = new ExtensionInfo(es[i], state);

		return results;
	}

	public IConfigurationElementInfo[] getConfigurationElements(
			String extensionPointId) {
		State state = getPlatformState();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry == null || state == null)
			return null;
		else
			return createRemoteConfigurationElements(
					registry.getConfigurationElementsFor(extensionPointId),
					state);
	}

	public IExtensionInfo getExtension(String extensionPointId,
			String extensionId) {
		State state = getPlatformState();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry == null || state == null)
			return null;
		else
			return new ExtensionInfo(registry.getExtension(extensionPointId,
					extensionId), state);
	}

	public IExtensionPointInfo getExtensionPoint(String extensionPointId) {
		State state = getPlatformState();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry == null || state == null)
			return null;
		else
			return new ExtensionPointInfo(
					registry.getExtensionPoint(extensionPointId), state);
	}

	public IExtensionPointInfo[] getExtensionPointsForContributor(String contributorId) {
		State state = getPlatformState();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry == null || state == null)
			return null;
		IExtensionPoint eps[] = registry.getExtensionPoints();
		List<IExtensionPoint> l = new ArrayList<IExtensionPoint>();
		for (int i = 0; i < eps.length; i++)
			if (contributorId == null
					|| eps[i].getContributor().getName()
							.equals(contributorId))
				l.add(eps[i]);

		return createRemoteExtensionPoints(
				(IExtensionPoint[]) l.toArray(new IExtensionPoint[0]), state);
	}

	public IExtensionPointInfo[] getExtensionPoints() {
		return getExtensionPointsForContributor(null);
	}
	
	private List<IExtension> getAllExtensions(IExtensionRegistry registry) {
		String namespaces[] = registry.getNamespaces();
		List<IExtension> l = new ArrayList<IExtension>();
		for (int i = 0; i < namespaces.length; i++) {
			IExtension es[] = registry.getExtensions(namespaces[i]);
			for (int j = 0; j < es.length; j++)
				l.add(es[j]);

		}

		return l;
	}

	public IExtensionInfo[] getExtensionsForContributor(
			String contributorId) {
		State state = getPlatformState();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry == null || state == null)
			return null;
		List<IExtension> allExtensions = getAllExtensions(registry);
		List<IExtension> l = new ArrayList<IExtension>();
		for (Iterator<IExtension> i = allExtensions.iterator(); i.hasNext();) {
			IExtension e = (IExtension) i.next();
			if (contributorId == null || e.getContributor().getName().equals(contributorId))
				l.add(e);
		}

		return createRemoteExtensions(
				(IExtension[]) l.toArray(new IExtension[0]), state);
	}

	public IExtensionInfo[] getExtensions(String extensionPointId) {
		State state = getPlatformState();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry == null || state == null)
			return null;
		List<IExtension> allExtensions = getAllExtensions(registry);
		List<IExtension> l = new ArrayList<IExtension>();
		for (Iterator<IExtension> i = allExtensions.iterator(); i.hasNext();) {
			IExtension e = (IExtension) i.next();
			if (extensionPointId == null
					|| e.getExtensionPointUniqueIdentifier().equals(
							extensionPointId))
				l.add(e);
		}

		return createRemoteExtensions(
				(IExtension[]) l.toArray(new IExtension[0]), state);
	}

	public String[] getExtensionPointIds() {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry == null)
			return null;
		IExtensionPoint epoints[] = registry.getExtensionPoints();
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < epoints.length; i++)
			result.add(epoints[i].getUniqueIdentifier());

		return (String[]) result.toArray(new String[epoints.length]);
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == null)
			return null;
		IAdapterManager manager = Activator.getDefault().getAdapterManager();
		if (manager == null)
			return null;
		else
			return manager.loadAdapter(this, adapter.getName());
	}

}
