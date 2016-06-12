package me.fmartins.unbiosed.projectmanip;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import me.fmartins.unbiosed.projectmanip.internals.Edk2ModuleTemplate;
import me.fmartins.unbiosed.structures.Edk2Module;
import me.fmartins.unbiosed.structures.Edk2Module.Edk2ModuleType;

public class NewEdk2ModuleProjectCreator {
	public static void CreateNewEdk2ModuleProject(ModuleProjectCreationContext context, IProgressMonitor monitor) {
		String moduleFolder = context.getModuleLocation();
		String moduleName = context.getModuleName();
		Edk2ModuleType type = context.getModuleType();
		String workspace = context.getWorkspaceLocation();
		
		try {
			IPath moduleFolderPath = new Path(moduleFolder);
			IPath moduleLocationPath = moduleFolderPath.append(moduleName + ".inf");
			
			Edk2ModuleTemplate template = Edk2ModuleTemplate.getTemplate(type, context);
			template.createModuleTemplate();
			
			Edk2Module projectModule = new Edk2Module(moduleLocationPath.toString(), workspace);
			ExistingEdk2ModuleProjectCreator.createEDK2ProjectFromExistingModule(projectModule, monitor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
