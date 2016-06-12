package me.fmartins.unbiosed.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import me.fmartins.unbiosed.projectmanip.ModuleProjectCreationContext;
import me.fmartins.unbiosed.projectmanip.ModuleProjectCreationContextBuilder;
import me.fmartins.unbiosed.projectmanip.NewEdk2ModuleProjectCreator;
import me.fmartins.unbiosed.structures.Edk2Element;
import me.fmartins.unbiosed.wizards.pages.NewEdk2ModuleProjectPage;
import me.fmartins.unbiosed.wizards.pages.NewLibraryClassProjectPage;
import me.fmartins.unbiosed.wizards.pages.NewUefiDriverProjectPage;

public class NewEdk2ModuleProjectWizard extends Wizard implements INewWizard, IRunnableWithProgress {

	private NewEdk2ModuleProjectPage newModuleWizardPage;
	private NewLibraryClassProjectPage libraryClassPage;
	private NewUefiDriverProjectPage uefiDriverPage;
	
	public NewEdk2ModuleProjectWizard() {
		super();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
	
	@Override
	public void addPages() {
		super.addPages();
		this.newModuleWizardPage = new NewEdk2ModuleProjectPage("New EDK2 Module Project");
		newModuleWizardPage.setPageComplete(false);
		addPage(newModuleWizardPage);
		
		this.libraryClassPage = new NewLibraryClassProjectPage();
		libraryClassPage.setPageComplete(false);
		addPage(libraryClassPage);
		
		this.uefiDriverPage = new NewUefiDriverProjectPage();
		uefiDriverPage.setPageComplete(true);
		addPage(uefiDriverPage);
	}
	
	@Override
	public void run(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
		arg0.beginTask("Creating New EDK2 module project", 10);
		
		String workspacePath = null;
		if(newModuleWizardPage.shouldInferWorkspacePath()) {
			workspacePath = Edk2Element.inferWorkspaceFromElementPath(newModuleWizardPage.getNewModuleRootFolder());
		} else {
			workspacePath = newModuleWizardPage.getWorkspace();
		}
		
		String libraryClassName = null;
		String libraryClassHeaderLocation = null;
		boolean followsUefiDriverModel = false;
		switch(newModuleWizardPage.getSelectedModuleType()) {
		case LIBRARY_CLASS_IMPLEMENTATION:
			libraryClassName = libraryClassPage.GetLibraryClassName();
			libraryClassHeaderLocation = libraryClassPage.GetLibraryClassHeaderPath();
			break;
			
		case UEFI_DRIVER:
			followsUefiDriverModel = uefiDriverPage.followsUefiDriverModel();
			break;
			
		case UEFI_APPLICATION:
		case UEFI_STDLIB_APPLICATION:
		default:
			break;
		}
		
		ModuleProjectCreationContextBuilder contextBuilder = new ModuleProjectCreationContextBuilder();
		ModuleProjectCreationContext context = contextBuilder
				.withModuleName(newModuleWizardPage.getNewModuleName())
				.withModuleLocation(newModuleWizardPage.getNewModuleRootFolder())
				.withWorkspaceLocation(workspacePath)
				.withModuleType(newModuleWizardPage.getSelectedModuleType())
				.withLibraryClassName(libraryClassName)
				.withLibraryClassHeaderLocation(libraryClassHeaderLocation)
				.withFollowsUefiDriverModel(followsUefiDriverModel)
				.build();
				
		NewEdk2ModuleProjectCreator.CreateNewEdk2ModuleProject(context, arg0);
		arg0.done();
	}

	@Override
	public boolean performFinish() {
		try {
			getContainer().run(false, true, this);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean canFinish() {
		boolean isFinished = newModuleWizardPage.isPageComplete();
		
		if(isFinished){
			switch(newModuleWizardPage.getSelectedModuleType()) {
			case UEFI_APPLICATION:
			case UEFI_STDLIB_APPLICATION:
				return true;
				
			case LIBRARY_CLASS_IMPLEMENTATION:
				return libraryClassPage.isPageComplete();
				
			case UEFI_DRIVER:
				return uefiDriverPage.isPageComplete();
			}
		}
		
		return false;
		//return super.canFinish(); /*!(newModuleWizardPage.getSelectedModuleType() == Edk2ModuleType.LIBRARY_CLASS_IMPLEMENTATION) ||*/
				
	}

}
