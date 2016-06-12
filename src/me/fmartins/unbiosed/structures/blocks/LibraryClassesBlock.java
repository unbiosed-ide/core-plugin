package me.fmartins.unbiosed.structures.blocks;

import java.util.LinkedList;
import java.util.List;

import me.fmartins.unbiosed.structures.blocks.visitors.Edk2ElementBlockVisitor;

public class LibraryClassesBlock implements Edk2ElementBlock {
	public List<String> libraryClasses = new LinkedList<String>();
	
	public LibraryClassesBlock() {
		
	}
	
	public void addLibraryClass(String libraryClassName) {
		this.libraryClasses.add(libraryClassName);
	}
	
	public List<String> getLibraryClasses() {
		return this.libraryClasses;
	}
	
	public void setLibraryClasses(List<String> libraryClasses) {
		this.libraryClasses = libraryClasses;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[LibraryClasses]" + System.lineSeparator());
		for(String libraryClass : this.libraryClasses) {
			buffer.append("  " + libraryClass + System.lineSeparator());
		}
		
		return buffer.toString();
	}

	@Override
	public void addLine(String line) {
		this.addLibraryClass(line);
	}

	@Override
	public void accept(Edk2ElementBlockVisitor visitor) {
		visitor.visit(this);
	}
}
