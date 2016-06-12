package me.fmartins.unbiosed.structures.blocks.visitors;

import java.util.List;
import java.util.Map;

import me.fmartins.unbiosed.structures.blocks.DefinitionsBlock;
import me.fmartins.unbiosed.structures.blocks.LibraryClassesBlock;
import me.fmartins.unbiosed.structures.blocks.PackagesBlock;
import me.fmartins.unbiosed.structures.blocks.SourcesBlock;

public class BlockUpdateVisitor implements Edk2ElementBlockVisitor {
	private List<String> sources;
	private List<String> packages;
	private List<String> libraries;
	private Map<String, String> definitions;
	
	public BlockUpdateVisitor(List<String> sources, List<String> packages, List<String> libraryClasses, Map<String, String> definitions) {
		this.sources = sources;
		this.packages = packages;
		this.definitions = definitions;
		this.libraries = libraryClasses;
	}

	@Override
	public void visit(DefinitionsBlock d) {
		d.setDefinitions(this.definitions);
	}

	@Override
	public void visit(SourcesBlock s) {
		s.setSourceFiles(this.sources);
	}

	@Override
	public void visit(PackagesBlock p) {
		p.setPackages(this.packages);
	}

	@Override
	public void visit(LibraryClassesBlock libs) {
		libs.setLibraryClasses(this.libraries);
	}

}
