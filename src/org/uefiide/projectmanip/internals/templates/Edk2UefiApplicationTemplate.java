package org.uefiide.projectmanip.internals.templates;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.Path;
import org.uefiide.projectmanip.ModuleProjectCreationContext;
import org.uefiide.projectmanip.internals.Edk2ModuleTemplate;
import org.uefiide.structures.Edk2Module;

public class Edk2UefiApplicationTemplate extends Edk2ModuleTemplate {
	private boolean includeStdlib;
	
	public Edk2UefiApplicationTemplate(ModuleProjectCreationContext context, boolean includeStdlib) {
		super(context);
		this.includeStdlib = includeStdlib;
	}
	
	@Override
	public void writeModuleTemplate() throws IOException {
		String moduleLocation = context.getModuleLocation();
		String moduleName = context.getModuleName();
		File infFile = new File(new Path(moduleLocation).append(moduleName + ".inf").toString());
		
		if(!infFile.exists()) {
			infFile.createNewFile();
		}
		
		Edk2Module newModule = new Edk2Module(infFile.getAbsolutePath(), context.getWorkspaceLocation());
	    Map<String, String> defines = newModule.getDefines();
	    List<String> sources = newModule.getSources();
	    List<String> libraries = newModule.getLibraryClasses();
	    List<String> packages = newModule.getPackages();
	    
		defines.put("MODULE_TYPE", "UEFI_APPLICATION");
		defines.put("INF_VERSION", "0x00010006");
		defines.put("BASE_NAME", moduleName);
		defines.put("VERSION_STRING", "0.1");
		defines.put("FILE_GUID", UUID.randomUUID().toString());
		defines.put("ENTRY_POINT", this.includeStdlib ? "ShellCEntryLib" : "UefiMain");
		
		sources.add(moduleName + ".c");
		
		packages.add("MdePkg/MdePkg.dec");
		packages.add("ShellPkg/ShellPkg.dec");
		
		libraries.add("UefiLib");
		libraries.add(this.includeStdlib ? "ShellCEntryLib" : "UefiApplicationEntryPoint");
		
		newModule.setDefinitions(defines);
		newModule.setPackages(packages);
		newModule.setSources(sources);
		newModule.setLibraryClasses(libraries);
		newModule.save();
	}

	@Override
	public void writeSourcesTemplate() throws IOException {
		String moduleLocation = context.getModuleLocation();
		String moduleName = context.getModuleName();
		File appSource = new File(new Path(moduleLocation).append(moduleName + ".c").toString());
		FileOutputStream writer = new FileOutputStream(appSource);
		
		if(!appSource.exists()) {
			appSource.createNewFile();
		}
		
		writer.write("#include <Uefi.h>\n".getBytes());
		writer.write("#include <Library/UefiLib.h>\n".getBytes());
		if(this.includeStdlib) {
			writer.write("#include <Library/ShellCEntryLib.h>\n".getBytes());
		} else {
			writer.write("#include <Library/UefiApplicationEntryPoint.h>\n".getBytes());
		}
		writer.write("\n".getBytes());
		if(this.includeStdlib) {
			writer.write("INTN\n".getBytes());
			writer.write("EFIAPI\n".getBytes());
			writer.write("ShellAppMain (\n".getBytes());
			writer.write("  IN UINTN Argc,\n".getBytes());
			writer.write("  IN CHAR16 **Argv\n".getBytes());
		} else {
			writer.write("EFI_STATUS\n".getBytes());
			writer.write("EFIAPI\n".getBytes());
			writer.write("UefiMain (\n".getBytes());
			writer.write("  IN EFI_HANDLE ImageHandle,\n".getBytes());
			writer.write("  IN EFI_SYSTEM_TABLE *SystemTable\n".getBytes());
		}
		writer.write("  )\n".getBytes());
		writer.write("{\n".getBytes());
		writer.write("  Print(L\"Hello there fellow Programmer.\\n\");\n".getBytes());
		writer.write("  Print(L\"Welcome to the world of EDK II.\\n\");\n".getBytes());
		writer.write("\n".getBytes());
		writer.write(this.includeStdlib ? 
				"  return(0);\n".getBytes() : 
				"  return EFI_SUCCESS;\n".getBytes());
		writer.write("}\n".getBytes());
		
		writer.close();
	}
}
