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

public interface IBundleManager {

	public String[] getBundleSymbolicIds();

	public IBundleInfo[] getBundles(IBundleId bundleId);

	public IBundleInfo[] getBundles();

	public IBundleInfo getBundle(Long bundleid);

	public IStatus start(IBundleId bundleId);

	public IStatus start(Long bundleId);

	public IStatus stop(IBundleId bundleId);

	public IStatus stop(Long bundleId);

	public IStatus diagnose(IBundleId bundleId);

}
