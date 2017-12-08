/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first;

import java.util.Map;

/**
 *
 * @author Alvar
 */
public class GeoData {

    /**
     * Tamaños espaciales de pixel
     */
	private double [] pixelscale;
        /**
         * Puntos fijos de georreferenciación
         */
	private double [] tiepoints;
        /**
         * GeoKeys contenidas en el archivo
         */
	private Map<Integer,String> codigos;

    /**
     * Constructor
     * @param scales Tamaños
     * @param points Puntos fijos
     * @param codes GeoKeys
     */
	public GeoData(double[]scales, double[]points, Map<Integer,String> codes)
	{
		pixelscale = scales;
		tiepoints = points;
		codigos = codes;
	}
	
    /**
     * Obtiene el tipo de proyección de la imagen sobre la tierra
     * @return String con la cadena leída de XML
     */
	public String getPCSType()
	{
		
		// Lo cargamos desde XML
		if (codigos.containsKey(GeoTiffIIOMetadataAdapter.ProjectedCSTypeGeoKey))
		{
			ParseXML pxml = new ParseXML("GeoTIFFcodes.xml");
                        String code = codigos.get(GeoTiffIIOMetadataAdapter.ProjectedCSTypeGeoKey);
                        
                        if (code.equalsIgnoreCase("32767"))
                        {
                            return new String("PCS no estándar");
                        }
                        String res = pxml.buscar(code, new String("value")).trim();
                        if (res == null)
                        {
                            res = new String("PCS no reconocido");
                        }
			return res;
		}

		else
		{
			return null;
		}
		
	}

    /**
     * Devuelve el tamaño de pixel en X
     * @return El tamaño de pixel en X
     */
	public double getXScale()
	{
		return pixelscale[0];
	}
    /**
     * Devuelve el tamaño de pixel en Y
     * @return El tamaño de pixel en Y
     */
	public double getYScale()
	{
		return pixelscale[1];	
	}
    /**
     * Devuelve el tamaño de pixel en Z
     * @return El tamaño de pixel en Z
     */
	public double getZScale()
	{
		return pixelscale[2];
	}

    /** 
     * Obtiene la coordenada I de (I,J,K) coordenadas de la imagen en raster-space
     * @param n Número de punto fijo
     * @return La coordenada I
     */
	public double getITiePoint(int n)
	{
		return tiepoints[(n-1)*6];
	}
    /**
     * Obtiene la coordenada J de (I,J,K), coordenadas de la imagen en raster-space
     * @param n Número de punto fijo
     * @return La coordenada J
     */
	public double getJTiePoint(int n)
	{
		return tiepoints[(n-1)*6+1];
	}
    /**
     * Obtiene la coordenada K de (I,J,K), coordenadas de la imagen en raster-space
     * (Normalmente 0)
     * @param n Número de punto fijo
     * @return La coordenada K
     */
	public double getKTiePoint(int n)
	{
		return tiepoints[(n-1)*6+2];
	}
    /**
     * Obtiene la coordenada X de (X,Y,Z), coordenadas de la imagen en model-space
     * @param n Número de punto fijo
     * @return La coordenada X
     */
	public double getXTiePoint(int n)
	{
		return tiepoints[(n-1)*6+3];		
	}
    /**
     * Obtiene la coordenada Y de (X,Y,Z), coordenadas de la imagen en model-space
     * @param n Número de punto fijo
     * @return La coordenada Y
     */
	public double getYTiePoint(int n)
	{
		return tiepoints[(n-1)*6+4];
	}

    /**
     *  Obtiene la coordenada Z de (X,Y,Z), coordenadas de la imagen en model-space
     * @param n Número de punto fijo
     * @return La coordenada Z
     */
	public double getZTiePoint(int n)
	{
		return tiepoints[(n-1)*6+5];
	}

    /**
     * Carga del XML la descripción correspondiente a las unidades lineales que
     * aparecen en la GeoKey
     * @return La descripción de las unidades
     */
	public String getUnidadLineal() {

		// Lo cargamos desde XML

//		 Lo cargamos desde XML
		if (codigos.containsKey(GeoTiffIIOMetadataAdapter.GeogLinearUnitsGeoKey))
		{
			ParseXML pxml = new ParseXML("GeoTIFFcodes.xml");
		
			return pxml.buscar(codigos.get(GeoTiffIIOMetadataAdapter.GeogLinearUnitsGeoKey), new String("value")).trim();
		}
		else
		{
			return null;
		}

	}

    /**
     * Obtiene los tamaños de pixel
     * @return en 0 el tamaño en X
     *         en 1 el tamaño en Y
     */
	public double[] getScales() {
		return pixelscale;
	}

    /**
     * Obtiene los puntos fijos
     * @return Los puntos fijos
     */
	public double[][] getTiePoints() {
		double [][] resultado = new double[tiepoints.length/6][6];
		for (int i=0; i < tiepoints.length/6; i++)
		{
			resultado[i][0]=tiepoints[i*6];
			resultado[i][1]=tiepoints[i*6+1];
			resultado[i][2]=tiepoints[i*6+2];
			resultado[i][3]=tiepoints[i*6+3];
			resultado[i][4]=tiepoints[i*6+4];
			resultado[i][5]=tiepoints[i*6+5];
			
		}
		return resultado;
	}
	
	


}
