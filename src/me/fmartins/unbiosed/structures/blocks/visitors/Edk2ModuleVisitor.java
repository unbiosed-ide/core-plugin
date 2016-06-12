package me.fmartins.unbiosed.structures.blocks.visitors;

import java.util.List;
import java.util.Map;

import me.fmartins.unbiosed.structures.blocks.DefinitionsBlock;
import me.fmartins.unbiosed.structures.blocks.LibraryClassesBlock;
import me.fmartins.unbiosed.structures.blocks.PackagesBlock;
import me.fmartins.unbiosed.structures.blocks.SourcesBlock;

public class Edk2ModuleVisitor implements Edk2ElementBlockVisitor {
	private List<String> sources;
	private List<String> packages;
	private List<String> libraries;
	private Map<String, String> definitions;
	
	public Edk2ModuleVisitor(List<String> sources, List<String> packages, List<String> libraries, Map<String, String> definitions) {
		this.sources = sources;
		this.packages = packages;
		this.libraries = libraries;
		this.definitions = definitions;
	}
	@Override
	public void visit(DefinitionsBlock d) {
		definitions.putAll(d.entries());
	}

	@Override
	public void visit(SourcesBlock s) {
		sources.addAll(s.getSourceFiles());

	}

	@Override
	public void visit(PackagesBlock p) {
		packages.addAll(p.getPackages());
	}
	@Override
	public void visit(LibraryClassesBlock libs) {
		libraries.addAll(libs.getLibraryClasses());
		
	}
}
