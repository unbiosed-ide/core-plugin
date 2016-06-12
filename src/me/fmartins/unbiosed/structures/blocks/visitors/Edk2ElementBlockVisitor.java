package me.fmartins.unbiosed.structures.blocks.visitors;

import me.fmartins.unbiosed.structures.blocks.DefinitionsBlock;
import me.fmartins.unbiosed.structures.blocks.LibraryClassesBlock;
import me.fmartins.unbiosed.structures.blocks.PackagesBlock;
import me.fmartins.unbiosed.structures.blocks.SourcesBlock;

public interface Edk2ElementBlockVisitor {
	void visit(DefinitionsBlock defs);
	void visit(SourcesBlock sources);
	void visit(PackagesBlock packages);
	void visit(LibraryClassesBlock libs);
}
