package org.cn2b2t.core.modules.gui;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedGUI extends GUI{
	
	List<GUIItem> container = new ArrayList();
	public int page = 1;
	
	public PagedGUI(GUIType type, String name) {
		super(type, name);
	}
	
	public int add(GUIItem i){
		container.add(i);
		return container.size()-1;
	}
	
	public void remove(GUIItem i){
		container.remove(i);
	}
	
	public void remove(int i){
		container.remove(i);
	}

	public List<GUIItem> getContainer() {
		return new ArrayList(container);
	}
	
	public void lastPage(){
		if(hasLastPage())
			page--;
		else
			throw new IndexOutOfBoundsException();
	}
	
	public void nextPage(){
		if(hasNextPage())
			page++;
		else
			throw new IndexOutOfBoundsException();
	}
	
	public abstract boolean hasLastPage();
	
	public abstract boolean hasNextPage();
	
}
