package kr.co.topquadrant.citationTracker.co;

import com.tqk.ontobase.core.util.TreeIterator;


public interface WrapTree {

	public void closeResource();

	public TreeIterator<String> getIterator();
}
