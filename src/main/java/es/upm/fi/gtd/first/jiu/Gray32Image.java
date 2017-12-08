/*
 * Gray16Image
 *
 * Copyright (c) 2003 Marco Schmidt.
 * All rights reserved.
 */

package es.upm.fi.gtd.first.jiu;



/**
 * Interface for grayscale images using integer samples that are sixteen bits large.
 * Valid sample values must lie in the interval 0 to 65535 (including both of those values).
 * Like all grayscale images, implementations of this class are supposed to have one channel only.
 * Simply merges the two interfaces GrayIntegerImage and ShortChannelImage without adding methods of its own.
 * @author Marco Schmidt
 * @since 0.11.0
 * @see IntChannelImage
 * @see GrayIntegerImage
 */
public interface Gray32Image extends GrayIntegerImage, IntChannelImage
{
}
