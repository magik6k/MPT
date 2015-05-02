package net.magik6k.mpt.util;

public class MptFile implements Comparable<MptFile> {
	public String name;
	public String pack;
	public String content;

	@Override
	public int compareTo(MptFile f2) {
		return name.compareTo(f2.name);
	}
}
