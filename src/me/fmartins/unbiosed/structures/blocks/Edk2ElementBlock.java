package me.fmartins.unbiosed.structures.blocks;

import me.fmartins.unbiosed.structures.blocks.visitors.Edk2ElementBlockVisitor;

public interface Edk2ElementBlock {
	void addLine(String line);
	void accept(Edk2ElementBlockVisitor visitor);
}
