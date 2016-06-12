package me.fmartins.unbiosed.structures.blocks;

import java.util.LinkedList;
import java.util.List;

import me.fmartins.unbiosed.structures.blocks.visitors.Edk2ElementBlockVisitor;

public class UnknownBlock implements Edk2ElementBlock {
	public List<String> lines = new LinkedList<String>();
	
	public UnknownBlock() {
		
	}
	
	public List<String> getLines() {
		return this.lines;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		for(String line : this.lines) {
			buffer.append(line + System.lineSeparator());
		}
		
		return buffer.toString();
	}
	
	@Override
	public void addLine(String sourceFileName) {
		this.lines.add(sourceFileName);
	}

	@Override
	public void accept(Edk2ElementBlockVisitor visitor) {
	}
}
