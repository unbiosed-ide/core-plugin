package me.fmartins.unbiosed.wizards;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import me.fmartins.unbiosed.projectmanip.ExistingEdk2ModuleProjectCreator;
import me.fmartins.unbiosed.structures.Edk2Module;
import me.fmartins.unbiosed.wizards.pages.ExistingModuleWizardPage;

public class ExistingModuleProjectWizard extends Wizard implements INewWizard, IRunnableWithProgress {

	private ExistingModuleWizardPage existingModuleWizardPage;

	public ExistingModuleProjectWizard() {
		super();
	}

	@Override
	public void addPages() {
		super.addPages();
		this.existingModuleWizardPage = new ExistingModuleWizardPage("Existing EDK2 Module Project");
		this.existingModuleWizardPage.setPageComplete(false);
		addPage(existingModuleWizardPage);
	}

	@Override
	public void run(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
		try {
			arg0.beginTask("Creating Existing EDK2 module project", 10);
			
			Edk2Module projectModule  = null;
			if(existingModuleWizardPage.shouldInferWorkspacePath()) {
				projectModule = new Edk2Module(existingModuleWizardPage.getLocation());
			} else {
				projectModule = new Edk2Module(existingModuleWizardPage.getLocation(), existingModuleWizardPage.getWorkspacePath());
			}
			
			ExistingEdk2ModuleProjectCreator.createEDK2ProjectFromExistingModule(projectModule, arg0);
			arg0.done();
		} catch (CoreException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
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
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
	}
}
