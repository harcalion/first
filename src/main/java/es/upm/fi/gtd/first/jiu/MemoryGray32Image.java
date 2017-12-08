/*
 * MemoryGray16Image
 *
 * Copyright (c) 2003 Marco Schmidt.
 * All rights reserved.
 */

package es.upm.fi.gtd.first.jiu;



/**
 * An implementation of {@link Gray16Image} that keeps the complete image in memory.
 * This class inherits most of its functionality from its parent class
 * {@link MemoryShortChannelImage}, using one <code>short</code> channel.
 * @since 0.11.0
 * @author Marco Schmidt
 */
public class MemoryGray32Image extends MemoryIntChannelImage implements Gray32Image
{
	/**
	 * Creates a new MemoryGray16Image object with the specified resolution.
	 * Simply gives <code>1</code> (for one channel) and the two resolution arguments
	 * to the super constructor (of the parent class {@link MemoryShortChannelImage}).
	 * @param width the horizontal resolution, must be larger than zero
	 * @param height the vertical resolution, must be larger than zero
	 */
	public MemoryGray32Image(int width, int height)
	{
		super(1, width, height);
	}

	public PixelImage createCompatibleImage(int width, int height)
	{
		return new MemoryGray32Image(width, height);
	}

	public Class getImageType()
	{
		return Gray32Image.class;
	}

	public boolean isBlack(int x, int y)
	{
		return getShortSample(x, y) == 0;
	}

	public boolean isWhite(int x, int y)
	{
		return getShortSample(x, y) == (short)65535;
	}

	public void putBlack(int x, int y)
	{
		putSample(x, y, 0);
	}

	public void putWhite(int x, int y)
	{
		putSample(x, y, 65535);
	}
}
