package me.fmartins.unbiosed.startup;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IStartup;

import me.fmartins.unbiosed.events.Edk2ModuleObservablesManager;
import me.fmartins.unbiosed.projectmanip.ProjectStructureUpdater;


public class SetResourceChangeListenerStartup implements IStartup {

	@Override
	public void earlyStartup() {
		Edk2ModuleObservablesManager.init();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] currentProjects = root.getProjects();
		
		for(IProject project : currentProjects) {
			ProjectStructureUpdater.setResourceChangeListeners(project);
		}
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				for(IResourceDelta projectDelta: event.getDelta().getAffectedChildren()) {
					Edk2ModuleObservablesManager.notifyResourceChanged(projectDelta);
				}
			}
		}, IResourceChangeEvent.POST_CHANGE);
	}
}
