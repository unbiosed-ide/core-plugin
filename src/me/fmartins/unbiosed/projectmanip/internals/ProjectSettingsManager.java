package me.fmartins.unbiosed.projectmanip.internals;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.core.settings.model.extension.CFolderData;
import org.eclipse.cdt.core.settings.model.extension.CLanguageData;
import org.eclipse.cdt.internal.core.pdom.indexer.IndexerPreferences;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

@SuppressWarnings("restriction")
public class ProjectSettingsManager {
	private ProjectSettingsManager() {
	}
	
	public static void setIncludePaths(IProject project, List<String> includePaths) {
		configureIndexerToIncludeAllReferencedHeaders(project);
		
		ICProjectDescription projectDescription = CoreModel.getDefault().getProjectDescription(project);
		ICLanguageSettingEntry[] entries = IncludePathString2ICLanguageSettingEntryArray(includePaths);
		
		// Set include paths for all configurations in the CDT project
		for(ICConfigurationDescription confDesc : projectDescription.getConfigurations()) {
			IConfiguration configuration = ManagedBuildManager.getConfigurationForDescription(confDesc);
			CConfigurationData Cconfigdata = configuration.getConfigurationData();
	        CFolderData rootFolderData = Cconfigdata.getRootFolderData();
	        CLanguageData[] languageDatas = rootFolderData.getLanguageDatas();
	        
	        for(CLanguageData languageData : languageDatas) {
	        	languageData.setEntries(ICLanguageSettingEntry.INCLUDE_PATH, entries);
	        }
		}
		
		try {
			CoreModel.getDefault().setProjectDescription(project, projectDescription);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void configureIndexerToIncludeAllReferencedHeaders(IProject project) {
		Properties props = IndexerPreferences.getProperties(project);
		props.setProperty(IndexerPreferences.KEY_INDEX_ALL_HEADER_VERSIONS, "true");
		IndexerPreferences.setProperties(project, IndexerPreferences.getScope(project), props);
	}
	
	private static ICLanguageSettingEntry[] IncludePathString2ICLanguageSettingEntryArray(List<String> paths) {
		List<ICLanguageSettingEntry> entries = new ArrayList<>();
		ICLanguageSettingEntry[] entriesArray = new ICLanguageSettingEntry[paths.size()];
		
		for(String path : paths) {
			entries.add(new CIncludePathEntry(path, ICSettingEntry.LOCAL));
		}
		entries.toArray(entriesArray);
		
		return entriesArray;
	}
}
