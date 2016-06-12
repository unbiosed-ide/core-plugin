package me.fmartins.unbiosed.structures.blocks;

import java.util.LinkedList;
import java.util.List;

import me.fmartins.unbiosed.structures.blocks.visitors.Edk2ElementBlockVisitor;

public class SourcesBlock implements Edk2ElementBlock {
	public List<String> sourceFiles = new LinkedList<String>();
	
	public SourcesBlock() {
		
	}
	
	public void addSourceFile(String sourceFileName) {
		this.sourceFiles.add(sourceFileName);
	}
	
	public List<String> getSourceFiles() {
		return this.sourceFiles;
	}
	
	public void setSourceFiles(List<String> sources) {
		this.sourceFiles = sources;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[Sources]" + System.lineSeparator());
		for(String source : this.sourceFiles) {
			buffer.append("  " + source + System.lineSeparator());
		}
		
		return buffer.toString();
	}

	@Override
	public void addLine(String line) {
		this.addSourceFile(line.trim());
	}

	@Override
	public void accept(Edk2ElementBlockVisitor visitor) {
		visitor.visit(this);
	}
}
