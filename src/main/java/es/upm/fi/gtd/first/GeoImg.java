package es.upm.fi.gtd.first;

import java.io.File;


import javax.imageio.metadata.IIOMetadata;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

/**
 * Contenedor de datos para una imagen y sus datos más necesarios
 */
public class GeoImg {
    /**
     * Datos geográficos de la imagen
     */
	private GeoData datos;
        /**
         * Imagen contenida
         */
	private PlanarImage img;
        /**
         * Archivo desde el que se cargó la imagen, o que ha sido construido
         */
	private File f = null;
        /**
         * Metadatos del archivo
         */
	private IIOMetadata streamData;
        /**
         * Metadatos de la imagen
         */
	private IIOMetadata imageData;
        
    /**
     * Constructor cuando conocemos: 
     * @param gd Datos geográficos
     * @param imagen Imagen
     * @param stream Metadatos del archivo
     * @param image Metadatos de la imagen     
     */
	/*public GeoImg(GeoData gd, PlanarImage imagen, IIOMetadata stream, IIOMetadata image)
	{ // Cuando ya se ha cargado la imagen
		if (datos==null)
		{
			datos = gd;
		}
		img = imagen;
		streamData = stream;
		imageData = image;

	}*/
    /**
     * Constructor cuando conocemos:
     * @param gd Datos geográficos
     * @param file Archivo origen de la imagen
     * @param stream Metadatos del archivo
     * @param image Metadatos de la imagen
     */
	/*public GeoImg(GeoData gd, File file, IIOMetadata stream, IIOMetadata image)
	{ // Cuando todavía es un fichero que se ha seleccionado 
		datos = gd;
		f = file;
		streamData = stream;
		imageData = image;
	}*/
    /**
     * Constructor cuando sabemos todos los datos
     * @param gd Datos geográficos
     * @param imagen Imagen contenida
     * @param file Archivo origen
     * @param stream Metadatos del archivo
     * @param image Metadatos de la imagen
     */
	public GeoImg(GeoData gd, PlanarImage imagen, File file, IIOMetadata stream, IIOMetadata image)
	{// Ya sabemos todos los datos
		datos = gd;
		img = imagen;
		f = file;
		streamData = stream;
		imageData = image;
	}
    /**
     * Obtiene el fichero
     * @return El fichero
     */
	public File getFile()
	{
		return f;
	}
    /**
     * Obtiene los datos geográficos
     * @return Los datos geográficos
     */
	public GeoData getData()
	{
		return datos;
	}
    /**
     * Obtiene la imagen contenida
     * @return La imagen contenida
     */
	public PlanarImage getImage()
	{
		return img;
	}
    /**
     * Obtiene los metadatos de fichero
     * @return Los metadatos de fichero
     */
	public IIOMetadata getStreamMetadata()
	{
		return streamData;		
	}
    /**
     * Obtiene los metadatos de imagen
     * @return Los metadatos de imagen
     */
	public IIOMetadata getImageMetadata()
	{
		return imageData;
	}
    /**
     * Modifica la imagen contenida
     * @param op Nodo de operación JAI con la imagen
     */
	public void setImage(RenderedOp op) {
		
		img = op;
	}
    /**
     * Modifica la imagen contenida
     * @param sourceImage Imagen que se almacenará
     */
	public void setImage(PlanarImage sourceImage) {
		img = sourceImage;
		
	}
    /**
     * Pasa el objeto a un String
     * @return String representativo del objeto
     */
    @Override
	public String toString()
	{
		return f.getName();
	}

    void setFile(File file) {
        f = file;
    }
}
/**
 * Contenedor auxiliar con los datos geográficos más usados
 */

