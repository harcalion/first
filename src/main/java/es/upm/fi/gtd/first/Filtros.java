package es.upm.fi.gtd.first;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * 
 * Implementa los filtros biortogonales necesarios para la fusión de Mallat y el filtro 
 * bidimensional para la fusión à trous, junto con varios métodos de conveniencia.
 * 
 * @author Alvar García del Río
 *
 */

public class Filtros {
	/**
	 * Valores que se utilizan en los filtros
	 */
	final double sq2 = Math . sqrt ( 2 ) ;
	/**
	 * Valores que se utilizan en los filtros
	 */
	final double sq3 = Math . sqrt ( 3 ) ;
	/**
	 * Valores que se utilizan en los filtros
	 */
	final double sq6 = Math . sqrt ( 6 ) ;

	// FILTROS ORTOGONALES DOUBLE
	// LD, HD, LR, HR
	/**
	 * Filtros ortogonales double de 'db1'
	 */
	public final double db1fil[][] = {{sq2/2,sq2/2},{-sq2/2,sq2/2},{sq2/2,sq2/2},{sq2/2,-sq2/2}};
	/**
	 * Filtros ortogonales double de 'db2'
	 */
	public final double db2fil[][] = {	{0.1294D,0.2241D,0.8365D,0.4830D},
								{-0.4830D,    0.8365D,  -0.2241D,  -0.1294D},
								{0.4830D,    0.8365D,    0.2241D,   -0.1294D},
								{-0.1294D,   -0.2241D,    0.8365D,   -0.4830D}};
	/**
	 * Filtros ortogonales double de 'db3'
	 */
	public final double db3fil[][] = {{0.0352D,   -0.0854D,   -0.1350D,    0.4599D,    0.8069D,    0.3327D},
			{-0.3327D,    0.8069D,   -0.4599D,   -0.1350D,    0.0854D,    0.0352D},
			{0.3327D,    0.8069D,    0.4599D,   -0.1350D,   -0.0854D,    0.0352D},
			{0.0352D,    0.0854D,   -0.1350D,   -0.4599D,    0.8069D,   -0.3327D}};
	/**
	 * Filtros ortogonales double de 'db4'
	 */
	public final double db4fil[][] = {
			{0.0106D,    0.0329D,    0.0308D,   -0.1870D,   -0.0280D,    0.6309D,    0.7148D, 0.2304D},
			{-0.2304D,    0.7148D,   -0.6309D,   -0.0280D,    0.1870D,    0.0308D,   -0.0329D, -0.0106D},
			{0.2304D,    0.7148D,    0.6309D,   -0.0280D,   -0.1870D,    0.0308D,    0.0329D, -0.0106D},
			{-0.0106D,   -0.0329D,    0.0308D,    0.1870D,   -0.0280D,   -0.6309D,    0.7148D, -0.2304D}};
	/**
	 * Filtros ortogonales double de 'db5'
	 */
	public final double db5fil[][] = {
			{0.0033D,   -0.0126D,   -0.0062D,    0.0776D,   -0.0322D,   -0.2423D,    0.1384D, 0.7243D,    0.6038D,    0.1601D},
			{-0.1601D,    0.6038D,   -0.7243D,    0.1384D,    0.2423D,   -0.0322D,   -0.0776D, -0.0062D,    0.0126D,    0.0033D},
			{0.1601D,    0.6038D,    0.7243D,    0.1384D,   -0.2423D,   -0.0322D,    0.0776D, -0.0062D,   -0.0126D,    0.0033D},
			{0.0033D,    0.0126D,   -0.0062D,   -0.0776D,   -0.0322D,    0.2423D,    0.1384D, -0.7243D,    0.6038D,   -0.1601D}};
	/**
	 * Filtros ortogonales double de 'db6'
	 */
	public final double db6fil[][] = {
			{-0.0011D,    0.0048D,    0.0006D,   -0.0316D,    0.0275D,    0.0975D,   -0.1298D,    -0.2263D,    0.3153D,    0.7511D,    0.4946D,    0.1115D},
			{ -0.1115D,    0.4946D,   -0.7511D,    0.3153D,    0.2263D,   -0.1298D,   -0.0975D, 0.0275D,    0.0316D,    0.0006D,   -0.0048D,   -0.0011D},
			{0.1115D,    0.4946D,    0.7511D,    0.3153D,   -0.2263D,   -0.1298D,    0.0975D, 0.0275D,   -0.0316D,    0.0006D,    0.0048D,   -0.0011D},
			{ -0.0011D,   -0.0048D,    0.0006D,    0.0316D,    0.0275D,   -0.0975D,   -0.1298D,  0.2263D,    0.3153D,   -0.7511D,    0.4946D,   -0.1115D}};
	// FILTROS ORTOGONALES FLOAT
	/**
	 * Valor que se utilizará en los filtros
	 */
	final float sq2f = (float)Math.sqrt(2);
	/**
	 * Filtros ortogonales float de 'db1'
	 */
	public final float db1filf[][] = {{sq2f/2,sq2f/2},{-sq2f/2,sq2f/2},{sq2f/2,sq2f/2},{sq2f/2,-sq2f/2}};
	/**
	 * Filtros ortogonales float de 'db2'
	 */
	public final float db2filf[][] = {	{0.1294F,0.2241F,0.8365F,0.4830F},
			{-0.4830F,    0.8365F,  -0.2241F,  -0.1294F},
			{0.4830F,    0.8365F,    0.2241F,   -0.1294F},
			{-0.1294F,   -0.2241F,    0.8365F,   -0.4830F}};
	/**
	 * Filtros ortogonales float de 'db3'
	 */
	public final float db3filf[][] = {{0.0352F,   -0.0854F,   -0.1350F,    0.4599F,    0.8069F,    0.3327F},
			{-0.3327F,    0.8069F,   -0.4599F,   -0.1350F,    0.0854F,    0.0352F},
			{0.3327F,    0.8069F,    0.4599F,   -0.1350F,   -0.0854F,    0.0352F},
			{0.0352F,    0.0854F,   -0.1350F,   -0.4599F,    0.8069F,   -0.3327F}};
	/**
	 * Filtros ortogonales float de 'db4'
	 */
	public final float db4filf[][] = {
			{0.0106F,    0.0329F,    0.0308F,   -0.1870F,   -0.0280F,    0.6309F,    0.7148F, 0.2304F},
			{-0.2304F,    0.7148F,   -0.6309F,   -0.0280F,    0.1870F,    0.0308F,   -0.0329F, -0.0106F},
			{0.2304F,    0.7148F,    0.6309F,   -0.0280F,   -0.1870F,    0.0308F,    0.0329F, -0.0106F},
			{-0.0106F,   -0.0329F,    0.0308F,    0.1870F,   -0.0280F,   -0.6309F,    0.7148F, -0.2304F}};
	/**
	 * Filtros ortogonales float de 'db5'
	 */
	public final float db5filf[][] = {
			{0.0033F,   -0.0126F,   -0.0062F,    0.0776F,   -0.0322F,   -0.2423F,    0.1384F, 0.7243F,    0.6038F,    0.1601F},
			{-0.1601F,    0.6038F,   -0.7243F,    0.1384F,    0.2423F,   -0.0322F,   -0.0776F, -0.0062F,    0.0126F,    0.0033F},
			{0.1601F,    0.6038F,    0.7243F,    0.1384F,   -0.2423F,   -0.0322F,    0.0776F, -0.0062F,   -0.0126F,    0.0033F},
			{0.0033F,    0.0126F,   -0.0062F,   -0.0776F,   -0.0322F,    0.2423F,    0.1384F, -0.7243F,    0.6038F,   -0.1601F}};
	/**
	 * Filtros ortogonales float de 'db6'
	 */
	public final float db6filf[][] = {
			{-0.0011F,    0.0048F,    0.0006F,   -0.0316F,    0.0275F,    0.0975F,   -0.1298F,    -0.2263F,    0.3153F,    0.7511F,    0.4946F,    0.1115F},
			{ -0.1115F,    0.4946F,   -0.7511F,    0.3153F,    0.2263F,   -0.1298F,   -0.0975F, 0.0275F,    0.0316F,    0.0006F,   -0.0048F,   -0.0011F},
			{0.1115F,    0.4946F,    0.7511F,    0.3153F,   -0.2263F,   -0.1298F,    0.0975F, 0.0275F,   -0.0316F,    0.0006F,    0.0048F,   -0.0011F},
			{ -0.0011F,   -0.0048F,    0.0006F,    0.0316F,    0.0275F,   -0.0975F,   -0.1298F,  0.2263F,    0.3153F,   -0.7511F,    0.4946F,   -0.1115F}};

	
	// FILTROS IMPARES
	/**
	 * Filtros ortogonales impares (no se usan) double de 'db1'
	 */
	public final double db1filBETA[][] = {{sq2/2,sq2/2,sq2/2},{-sq2/2,-sq2/2,sq2/2},{sq2/2,sq2/2},{sq2/2,-sq2/2}};
	/**
	 * Filtros ortogonales impares (no se usan) double de 'db4'
	 */
	public final double db4filBETA[][] = {
			{0.0106D, 0.0106D,    0.0329D,    0.0308D,   -0.1870D,   -0.0280D,    0.6309D,    0.7148D, 0.2304D},
			{-0.2304D, -0.2304D,    0.7148D,   -0.6309D,   -0.0280D,    0.1870D,    0.0308D,   -0.0329D, -0.0106D},
			{0.2304D,    0.7148D,    0.6309D,   -0.0280D,   -0.1870D,    0.0308D,    0.0329D, -0.0106D},
			{-0.0106D,   -0.0329D,    0.0308D,    0.1870D,   -0.0280D,   -0.6309D,    0.7148D, -0.2304D}};
	/**
	 * Filtros ortogonales impares (no se usan) float de 'db4'
	 */
	public final float db4filfBETA[][] = {
			{0.0106F, 0.0106F,    0.0329F,    0.0308F,   -0.1870F,   -0.0280F,    0.6309F,    0.7148F, 0.2304F},
			{-0.2304F, -0.2304F,    0.7148F,   -0.6309F,   -0.0280F,    0.1870F,    0.0308F,   -0.0329F, -0.0106F},
			{0.2304F,    0.7148F,    0.6309F,   -0.0280F,   -0.1870F,    0.0308F,    0.0329F, -0.0106F},
			{-0.0106F,   -0.0329F,    0.0308F,    0.1870F,   -0.0280F,   -0.6309F,    0.7148F, -0.2304F}};

	// FILTROS BIDIMENSIONALES
	/**
	 * Filtro bidimensional de 'b3spline'
	 */
	public final float b3splinefil[] = {	1/256f,  4/256f,  6/256f,  4/256f,  1/256f,
										4/256f, 16/256f, 24/256f, 16/256f,  4/256f,
										6/256f, 24/256f, 36/256f, 24/256f,  6/256f,
										4/256f, 16/256f, 24/256f, 16/256f,  4/256f,
										1/256f,  4/256f,  6/256f,  4/256f,  1/256f	};
	
	/**
	 * Indican el tipo de filtro
	 */
	public static final int LD = 0 ;
	/**
	 * Indican el tipo de filtro
	 */
	public static final int HD = 1 ;
	/**
	 * Indican el tipo de filtro
	 */
	public static final int LR = 2 ;
	/**
	 * Indican el tipo de filtro
	 */
	public static final int HR = 3 ;
	/**
	 * Indican la wavelet madre
	 */
	public static final int db1 = 0 ;
	/**
	 * Indican la wavelet madre
	 */
	public static final int db2 = 1 ;
	/**
	 * Indican la wavelet madre
	 */
	public static final int db3 = 2 ;
	/**
	 * Indican la wavelet madre
	 */
	public static final int db4 = 3 ;
	/**
	 * Indican la wavelet madre
	 */
	public static final int db5 = 4 ;
	/**
	 * Indican la wavelet madre
	 */
	public static final int db6 = 5 ;
	/**
	 * Indican la wavelet madre
	 */
	public static final int b3spline = 6 ;


	/**
	 * Obtiene un filtro, dado su tipo (LD = Low-pass Decompose, HD = High-pass Decompose, LR = Low-pass
	 * Reconstruction, HR = High-pass Reconstruction) y la wavelet madre ('db1' a 'db6' y 'b3spline')
	 * @param filtro Tipo de filtro (ver descripción)
	 * @param wavelet Wavelet madre (ver descripción)
	 * @return Un filtro como un array unidimensional
	 * @see Fusion#atrous
	 */
	public float[] getFiltro(int filtro, int wavelet)
	{
		switch(wavelet)
		{
		case 0:
			return db1filf[filtro];
		case 1:
			return db2filf[filtro];
		case 2:
			return db3filf[filtro];
		case 3:
			return db4filf[filtro];
		case 4:
			return db5filf[filtro];
		case 5:
			return db6filf[filtro];
		case 6:
			return b3splinefil;
		}
		return null;
	}
	
	/**
	 * Obtiene un filtro degradado para la fusión à trous
	 * @param input Filtro inicial
	 * @return Filtro degradado
	 */
	
	public float[] degradarFiltro(float [] input)
	{

			
			int tam = (int) Math.sqrt(input.length); 	// Obtenemos la dim del filtro
			tam+=tam-1;										// Y la cifra que va a dar la nueva dimensión
			float[] output = new float[tam*tam];
			int pin = 0;
			for (int i = 0; i < tam*tam; i++)
			{
				if (((int)Math.floor(i/tam))%2 == 0 )
				{// Fila par, hay valores
								
					if (i % 2 == 0)
					{
						output[i] = input[pin];
						pin++;
					}
					else
					{
						output[i] = 0;
					}
					
				}
				else
				{// Fila impar, sólo ceros
					output[i] = 0;
				}
					
			}

			return output;

	}
	
/**
 * Versión igualando medias y desviaciones típicas
 */
	
	// ND' = a*ND+b
	// a = sigmaDest/sigmaOrig
	// b = mediaDest - (a*mediaOrig)
	
	/**
	 * Obtiene una imagen con el histograma igualado a la imagen de entrada source
	 * @param img Imagen a partir de la cual se modificará el histograma de target
	 * @param img2 Imagen que será modificada
	 * @return Imagen con el histograma igualado al de source
	 */
	
	public static GeoImg igualarHistograma (GeoImg img, GeoImg img2)
	{
		
		int cols_lores = img.getImage().getWidth();
		int lins_lores = img.getImage().getHeight();
		double nbins;
	    double [] extremos = FusionAux.hallarExtremos(img.getImage());
	    double minValue = extremos[0];
	    double maxValue = extremos[1];
	    maxValue++; //maxValue es exclusivo!!!
		nbins = maxValue-minValue;

			nbins++; // HAY QUE CONTAR EL CEROOO!

		
		/*System.out.println("Máxima diferencia entre valores: " + nbins);
		System.out.println("Valor mínimo: " + minValue);
		System.out.println("Valor máximo: " + maxValue);*/
		Histogram hist_MULTI;
		ParameterBlock pbhist = new ParameterBlock();
		pbhist.addSource(img.getImage()); 		//Imagen
		pbhist.add(null);				//ROI = Imagen
		pbhist.add(1);
		pbhist.add(1);
		//pbhist.add(new int[]{256});
		
		pbhist.add(new int[]{(int)nbins});
		pbhist.add(new double[]{minValue});
		pbhist.add(new double[]{maxValue});

		RenderedOp rop = JAI.create("histogram",pbhist);
		hist_MULTI = (Histogram)rop.getProperty("histogram");
		int [][] bins = hist_MULTI.getBins();
		int numbins = hist_MULTI.getNumBins(0);
		//System.out.println("Número de bins obtenidos: "+ numbins);
		// Creo la CDF
		float [][] CDFmulti = new float[1][numbins];
		//System.out.println(numbins);
		for (int i = 0; i < numbins; i++)
		{
			if (i == 0)
			{
				//System.out.println("Valor de los bins"+bins[0][0]);
				CDFmulti[0][0]= (float)bins[0][0] / (float) (cols_lores*lins_lores);
				//System.out.println("Valor del CDF"+CDFmulti[0][0]);
			}
			else if (CDFmulti [0][i-1]>= 1)
			{// Redondeamos para que el último valor sea 1
				CDFmulti[0][i-1] = Math.round(CDFmulti[0][i-1]);
				CDFmulti[0][i] = Math.round(CDFmulti[0][i-1]);
				//System.out.println("Valor del CDF"+CDFmulti[0][i]);
			}
			else if (i==(numbins-1 ))
			{// Si no llega a uno hay que hacer otro apaño
				if (CDFmulti[0][i-1]<1)
				{
					CDFmulti[0][i] = (float) Math.ceil(CDFmulti[0][i-1]);
				}
			}
			else
			{
				//System.out.println("Valor de los bins"+bins[0][i]);
				CDFmulti[0][i] = CDFmulti[0][i-1] + ((float)bins[0][i] / (float)(cols_lores*lins_lores));
				//System.out.println("Valor del CDF"+CDFmulti[0][i]);
			}
		}
		//Aplico el histograma de MULTI a PAN
		//Usando matchcdf
		//Fabrico el histograma de PAN igual que el de MULTI
		
		pbhist = new ParameterBlock();
		pbhist.addSource(img2.getImage()); 		//Imagen
		pbhist.add(null);				//ROI = Imagen
		pbhist.add(1);
		pbhist.add(1);
		//pbhist.add(new int[]{256});
		//System.out.println("Diferencia entre extremos PAN: "+((int)diferenciaPAN));
		pbhist.add(new int[]{(int)nbins});		
		pbhist.add(new double[]{minValue});
		pbhist.add(new double[]{maxValue});
		img2 = new GeoImg (img2.getData(),JAI.create("histogram",pbhist),img2.getFile(),img2.getStreamMetadata(),img2.getImageMetadata());
		pbhist = new ParameterBlock();
		pbhist.addSource(img2.getImage());
		pbhist.add(CDFmulti);
		img2 = new GeoImg (img2.getData(),JAI.create("matchcdf", pbhist),img2.getFile(),img2.getStreamMetadata(),img2.getImageMetadata());
		
		return img2;
	}
        
	

}
