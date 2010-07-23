package org.eclipse.ecf.mgmt.extension;

import org.eclipse.ecf.remoteservice.IAsyncCallback;
import org.eclipse.equinox.concurrent.future.IFuture;

@SuppressWarnings("restriction")
public interface IExtensionRegistryManagerAsync {

    public void getExtensionPointIdsAsync(IAsyncCallback<String[]> callback);
    public IFuture getExtensionPointIdsAsync();

    public void getExtensionPointAsync(String extensionPointId, IAsyncCallback<IExtensionPointInfo> callback);
    public IFuture getExtensionPointAsync(String extensionPointId);

    public void getExtensionPointsForContributorAsync(String contributorId, IAsyncCallback<IExtensionPointInfo[]> callback);
    public IFuture getExtensionPointsForContributorAsync(String contributorId);
    
    public void getExtensionPointsAsync(IAsyncCallback<IExtensionPointInfo[]> callback);
    public IFuture getExtensionPointsAsync();

    public void getExtensionAsync(String extensionPointId, String extensionId, IAsyncCallback<IExtensionInfo> callback);
    public IFuture getExtensionAsync(String extensionPointId, String extensionId);

    public void getExtensionsForContributorAsync(String contributorId, IAsyncCallback<IExtensionInfo[]> callback);
    public IFuture getExtensionsForContributorAsync(String contributorId);

    public void getExtensionsAsync(String extensionPointId, IAsyncCallback<IExtensionInfo[]> callback);
    public IFuture getExtensionsAsync(String extensionPointId);

    public void getConfigurationElementsAsync(String extensionPointId, IAsyncCallback<IConfigurationElementInfo[]> callback);
    public IFuture getConfigurationElementsAsync(String extensionPointId);

}
