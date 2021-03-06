package com.johnzaro_nikosece.medialab_battleship.dataStructures;

import java.util.ArrayList;

public class BoundedList<T> extends ArrayList<T>
{
	private int maxSize;
	
	public BoundedList(int maxSize)
	{
		super();
		
		this.maxSize = maxSize;
	}
	
	public void addElement(T element)
	{
		if(size() >= maxSize)
			remove(size() - 1);
		
		add(0, element);
	}
}
