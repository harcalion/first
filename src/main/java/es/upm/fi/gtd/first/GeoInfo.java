package es.upm.fi.gtd.first;

import javax.measure.quantity.Length;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

import org.jscience.geography.coordinates.LatLong;
import org.jscience.geography.coordinates.UTM;
import org.jscience.geography.coordinates.crs.CoordinatesConverter;
import org.jscience.geography.coordinates.crs.ReferenceEllipsoid;

/**
 * Clase que permite realizar transformaciones de coordenadas geográficas
 * @author Alvar
 */
public class GeoInfo {
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    /* Radios ecuatoriales de diferentes Datum */
    @Deprecated
	double radioEcuatorialWGS84_NAD83 = 6378137;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialGRS80 = 6378137;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialWGS72 = 6378135;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialAus1965 = 6378160;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialKra1940 = 6378245;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialInt = 6378388;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialClake = 6378249.1;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialClarke = 6378206.4;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialAiry = 6377563.4;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialBessel = 6377397.2;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioEcuatorialEverest = 6377276.3;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
	/* Radios polares de diferentes Datum */
    @Deprecated
	double radioPolarWGS84_NAD83 = 6356752.3142;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarGRS80 = 6356752.3141;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarWGS72 = 6356750.5;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarAus1965 = 6356774.7;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarKra1940 = 6356863.0;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarInt = 6356911.9;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarClake = 6356514.9;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarClarke = 6356583.8;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarAiry = 6356256.9;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarBessel = 6356079.0;
        /**
         * @deprecated Valor para calcular latitud/longitud "a mano"
         */
    @Deprecated
	double radioPolarEverest = 6356075.4;
	
    /**
     * @deprecated Calcula latitud y longitud "a mano"
     * @param datum Datum o elipsoide utilizado
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return en 0 Latitud
     *         en 1 Longitud
     */
    @Deprecated
	public double[] calcularLatLong (String datum, double x, double y)
	{
		if (true)
		{
			double a = radioEcuatorialWGS84_NAD83;
			double b = radioPolarWGS84_NAD83;
			// Desconocidos
			double lat = 0;
			double longi = 0;
			double long0 = 0;
			double k0 = 0.9996;
			// Excentricidad de la Tierra
			double e = Math.sqrt((1-Math.pow(b,2))/Math.pow(a,2));
			double e2 = Math.pow(e,2);
			// Medida derivada e'
			double eprima = e2/(1-e2);
			double n = (a-b)/(a+b);
			// This is the radius of curvature of the earth in the meridian plane
			double rho = a*(1-e2)/Math.pow((1-e2*Math.pow(Math.sin(lat),2)),1.5);
			// This is the radius of curvature of the earth perpendicular to the meridian plane
			double ni = a/Math.pow((1-e2*Math.pow(Math.sin(lat),2)),0.5);
			double p = (longi - long0);
			// Seno de un segundo de arco
			double sin1 = Math.PI/(180*60*60);
			
			// Coordenadas corregidas
			double xCorregida = x - 500000;
			double yCorregida = y - 500000;
			// Calcular el arco meridional
			double M = yCorregida/k0;
			// Calcular footprint latitude
			
			
		}
		return null;
		
	}
	
    /**
     * Construye un String de latitud/longitud con el formato adecuado
     * @param geozona Zona geográfica correspondiente
     * @param georef N/S
     * @param geoX Coordenada X
     * @param geoY Coordenada Y
     * @param unidad Unidades lineales
     * @param geoproj Elipsoide utilizado
     * @param ic
     * @return en 0 Latitud
     *         en 1 Longitud
     */
	public static String[] convertirUTM (int geozona, char georef, double geoX, double geoY, Unit<Length> unidad, ReferenceEllipsoid geoproj, ParseXML ic)
	{
            
		 
		  //Convierto las unidades geográficas en latitud/longitud
		  // Tengo que introducirle los valores correctos
		  boolean W = false;
		  boolean S = false;
		  CoordinatesConverter<UTM,LatLong> UTMToLatLong = UTM.CRS.getConverterTo(LatLong.CRS);
		  UTM u = UTM.valueOf(geozona, georef, geoX, geoY, unidad);
		  //UTM u = new UTM(geozona,georef,geoX,geoY,unidad);
		  LatLong ll = UTMToLatLong.convert(u);
		  //LatLong ll = UTM.utmToLatLong(u, geoproj);
		  double latitud = ll.latitudeValue(NonSI.DEGREE_ANGLE);
		  double longitud = ll.longitudeValue(NonSI.DEGREE_ANGLE);
		  
		  double latGrados = Math.floor(latitud);
		  double latMinutos = latitud - latGrados;
		  latMinutos *= 60;
		  double latSegundos = latMinutos - Math.floor(latMinutos);
		  latMinutos = Math.floor(latMinutos);
		  latSegundos *= 60;
		  latSegundos = Math.floor(latSegundos);
		  int latG = (int) latGrados;
		  int latM = (int) latMinutos;
		  int latS = (int) latSegundos;
		  
		  if (longitud < 0)
		  { // Longitud W, hay que marcarla así y no hacer floor 
			  // hasta después de hacer abs
			  longitud = Math.abs(longitud);
			  W = true;
			  			  
		  }
		  double longGrados = Math.floor(longitud);
		  double longMinutos = longitud - longGrados;
		  longMinutos *= 60;
		  double longSegundos = longMinutos - Math.floor(longMinutos);
		  longMinutos = Math.floor(longMinutos);
		  longSegundos *= 60;
		  longSegundos = Math.floor(longSegundos);
		  int longG = (int) longGrados;
		  int longM = (int) longMinutos;
		  int longS = (int) longSegundos;
		  
		  String [] resultado = new String[2];
		  if (S)
		  {
			  resultado[0] = new String (latG+"º"+latM+"'"+latS+'"'+ic.buscar(157)); // Tag 157
		  }
		  else
		  {
			  resultado[0] = new String (latG+"º"+latM+"'"+latS+'"'+ic.buscar(158)); // Tag 158
		  }
		  if (W)
		  {
			  resultado[1] = new String (longG+"º"+longM+"'"+longS+'"'+ic.buscar(159)); // Tag 159  
		  }
		  else
		  {
			  resultado[1] = new String (longG+"º"+longM+"'"+longS+'"'+ic.buscar(160)); // Tag 160
		  }
		  
		  return resultado;
	}

    /**
     * Obtiene el número de zona de proyección
     * @param geozona Descripción de la proyección cargada de XML
     * @return Un entero con la zona de proyección
     */
	public static int obtenerZona(String geozona) {
		

		geozona = geozona.substring(geozona.length()-3, geozona.length()-1);
		if (!Character.isDigit(geozona.charAt(0)))
		{// Se quita, sólo hay una cifra			
			geozona = geozona.substring(1);
		}
		int indice = Integer.parseInt(geozona);
		
		System.out.println("La zona es "+indice);
		
		return indice;
	}
    /**
     * Obtiene el marcador N/S para la zona utilizada
     * @param geozona Descripción de la proyección cargada de XML
     * @return N ó S
     */
	public static char obtenerGeoref (String geozona)
	{
		char ref = geozona.charAt(geozona.length()-1);
		return ref;
	}

    /**
     * Construye una unidad de Longitud de Java a partir de la descripción de las
     * unidades lineales utilizadas cargadas de XML
     * @param linearunits Descripción
     * @return Unidad de longitud
     */
	public static Unit<Length> obtenerGeounit(String linearunits) {
		if (linearunits == null)
			return Length.UNIT;
		String unidad = linearunits.split("_")[1];
		if (unidad.startsWith("Meter"))
		{
			return Length.UNIT;
		}
		else if (unidad.startsWith("Foot"))
		{
			String subunidad = unidad.split("_")[1];
			if (unidad.endsWith("Foot"))
			{
				return NonSI.FOOT;
			}
			else if (subunidad.startsWith("US"))
			{
				return NonSI.FOOT_SURVEY_US;
			}
			
		}
		else if (unidad.startsWith("Yard"))
		{
			return NonSI.YARD;
		}
		
		return null;
	}

    /**
     * Obtiene el elipsoide a partir de la descripción de la proyección cargada de XML
     * @param geozone String con la descripción
     * @return El elipsoide utilizado
     */
	public static ReferenceEllipsoid obtenerGeoproj(String geozone) {
		geozone = geozone.split("_")[1];
		if (geozone.equalsIgnoreCase("WGS84") || geozone.equalsIgnoreCase("NAD83"))
		{
			return ReferenceEllipsoid.WGS84;
		}
		else if (geozone.equalsIgnoreCase("WGS72") || geozone.equalsIgnoreCase("WGS72BE"))
		{
			return ReferenceEllipsoid.WGS72;
		}
		else if (geozone.equalsIgnoreCase("ED50"))
		{
			return ReferenceEllipsoid.INTERNATIONAL1924;
		}
		return null;
	}
}
