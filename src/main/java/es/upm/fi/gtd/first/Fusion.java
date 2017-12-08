package es.upm.fi.gtd.first;

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;

import java.util.HashMap;
import javax.media.jai.BorderExtender;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBicubic2;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;
/**
 * Implementa la funcionalidad algorítmica de la práctica, es decir, los algoritmos de Mallat
 * y à trous. Además está autocontenido en cuanto al algoritmo de Mallat, pues incluye las funciones
 * de cambio de resolución y degradación.
 * 
 * @author Alvar García del Río
 * 
 */
public class Fusion {
	/**
	 * Representa una operación de aumento/convolución vertical
	 */
	private static final int VERT_A = 99;
	/**
	 * Representa una operación de aumento/convolución horizontal
	 */
	private static final int HOR_A = 100;
	/**
	 * Representa una operación de reducción vertical
	 */
	private static final int VERT_R = 101;
	/**
	 * Representa una operación de reducción horizontal
	 */
	private static final int HOR_R = 102;
	/**
	 * Representa una operación de traslación horizontal
	 */
	private static final int MOV_H = 103;
	/**
	 * Representa una operación de traslación vertical
	 */
	private static final int MOV_V = 104;
	/**
	 * Representa una operación de aumento de columnas (escala horizontal)
	 */
	private static final int SCA_H = 105;
	/**
	 * Representa una operación de aumento de filas (escala vertical)
	 */
	private static final int SCA_V = 106;
	/**
	 * Representa el tipo de interpolación Vecino más próximo (Nearest Neighbour)
	 */
	public static final int NEAR = 0;
	/**
	 * Representa el tipo de interpolación Bilineal (Bilinear)
	 */
	public static final int BILIN = 1;
	/**
	 * Representa el tipo de interpolación Bicúbica (Bicubic)
	 */
	public static final int BICUB = 2;
	/**
	 * Representa el tipo de interpolación Bicúbica 2 (Bicubic2)
	 */
	public static final int BICUB2 = 3;
	/**
	 * Representa el tipo de interpolación Lanczos de 3 lóbulos
	 */
	public static final int LANCZOS = 4;
	/**
	 * Imagen producto de la degradación durante atrous (se usa en Ergas)
	 * @see Ergas#muestreoErgas
	 */
	public GeoImg MULTIdegradada = null;
	/**
	 * Imagen no degradada (se usa en Ergas)
	 * @see Ergas#muestreoErgas
	 */
	public GeoImg MULTIcargada = null;
	/**
	 * Coeficientes de PAN (así no hay que degradarla varias veces cuando se eligen varios archivos)
	 */
	public GeoImg [] memPANcoeffs = null;
	
        private ParseXML ic;
        private boolean soloFinales = false;
        
        /**
         * Constructor de la clase
         * @param ic Objeto de datos de traducción
         */
        public Fusion(ParseXML ic)
   {
       this.ic = ic;
   }
	
	/**
	 * Degrada utilizando el algoritmo de Mallat una imagen (como un nombre de archivo) un número de 
	 * niveles determinado.
	 * @param wavelet La wavelet madre que se utilizará para la degradación
	 * @param num_niveles Número de niveles de degradación
	 * @param files Nombre de fichero a degradar
     * @param soloFinales true si se desea obtener sólo la imagen resultado de
     * la transformada wavelet, false si se desean todas las imágenes de detalle
     * y aproximación intermedias
     * @return Un array con las imágenes de aproximación y los coeficientes de detalle de cada nivel
	 */
public GeoImg[] degradar (int wavelet, int num_niveles, File files, boolean soloFinales)
{
	GeoImg output[];
	this.soloFinales = soloFinales;
	if (files!=null)
	{

		// Carga de la imagen
		GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files);
		GeoImg image = 
                        new GeoImg(
                            Info.crearGeoData(ti),
                            PlanarImage.wrapRenderedImage(
                                JAI.create(
                                    "imageread",
                                    files.getPath())),
                                    new File(files.getName()+ic.buscar(154)),
                                    Info.crearStreamData(ti),
                                    Info.crearImageData(ti)); // Tag 154
                return degradar (wavelet, num_niveles, image);
		//infobj.mostrarInfo(image);

		// Definición de los filtros
		//Convolucion conv = new Convolucion() ;
		//Filtros filt = new Filtros();
		
		//Obtener los filtros
		//final float [] hArray = filt.getFiltro(Filtros.LD,wavelet);
		//final float [] gArray = filt.getFiltro(Filtros.HD,wavelet);
		

  
		
			
				
	}
	return null;
}

/**
 * Degrada utilizando el algoritmo de Mallat una imagen (como una imagen) un número de 
 * niveles determinado.
 * @param wavelet Wavelet de la que se obtendrán los filtros utilizados
 * @param num_niveles Número de niveles que se degradará la imagen
 * @param image Imagen origen que será degradada
 * @return Un array con las imágenes de aproximación y los coeficientes de detalle de cada nivel
 */

public GeoImg[] degradar (int wavelet, int num_niveles, GeoImg image)
{
	GeoImg output[];
	
	if (image!=null)
	{

		// Carga de la imagen
		
		Info.mostrarInfo(image.getImage());
                File f = image.getFile();
		
		if (soloFinales)
                {
                    output = new GeoImg[num_niveles];
                }
                else
                {
                    output = new GeoImg[4*num_niveles];
                }
		GeoImg tiled;
		

		
		// Se establece aquí el bucle para degradar sucesivamente la imagen
		for (int i=0; i < num_niveles;i++)
		{
			// Creamos la imagen de salida (A)
			if (i!=0)
			{
				//tiled = conv.convHor2(output[0+(i-1)*4],hArray);
				tiled = convJAI(output[0+(i-1)*4],HOR_A,true,wavelet);
			}
			else
			{
				//tiled = conv.convHor2(image,hArray);
				tiled = convJAI(image,HOR_A,true,wavelet);
			}
			System.out.println(tiled.getImage().getWidth());
			GeoImg PPaso = reducirCols (tiled);
			System.out.println(PPaso.getImage().getWidth());
			tiled = convJAI(PPaso,VERT_A,true,wavelet);
			System.out.println(tiled.getImage().getWidth());
			//tiled = conv.convVert2(PPaso,hArray);
			tiled = reducirFilas(tiled);
			System.out.println(tiled.getImage().getWidth());
                        tiled.setFile(new File(ic.buscar(183)+f.getName())); // Tag 183
                        if(!soloFinales)
                        {
                            output[0+i*4]=tiled;
                        }
                        else
                        {
                            output[i]=tiled;
                        }
			
		
			// Creamos la imagen de salida (Dh)
			tiled = convJAI(PPaso,VERT_A,false,wavelet);
			//tiled = conv.convVert2(PPaso,gArray);
			tiled = reducirFilas(tiled);
                        tiled.setFile(new File(ic.buscar(181)+f.getName())); // Tag 181
                        if (!soloFinales)
                            output[1+i*4]=tiled;
			
		
			// Creamos la imagen de salida (Dv)
			if (i!=0)
			{
				//tiled = conv.convHor2(output[0+(i-1)*4],gArray);
				tiled = convJAI(output[0+(i-1)*4],HOR_A,false,wavelet);

			}
			else
			{	
			    tiled = convJAI(image,HOR_A,false,wavelet);
				//tiled = conv.convHor2(image,gArray);
			}
			GeoImg SPaso = reducirCols (tiled);


		    tiled = convJAI(SPaso,VERT_A,true,wavelet);
			//tiled = conv.convVert2(SPaso,hArray);
			tiled = reducirFilas(tiled);
                        tiled.setFile(new File(ic.buscar(151)+f.getName())); // Tag 151
                        if (!soloFinales)
                            output[2+i*4]=tiled;
			
			// Creamos la imagen de salida (Dd)
		    tiled = convJAI(SPaso,VERT_A,false,wavelet);
			// tiled = conv.convVert2(SPaso,gArray);
			tiled = reducirFilas(tiled);
                        tiled.setFile(new File(ic.buscar(182)+f.getName())); // Tag 182
                        if (!soloFinales)
                            output[3+i*4]=tiled;
			
		}
                
		return output;		
	}
	return null;
}

/**
 * Obtiene una imagen fusionada por el método de Mallat
 * @param img GeoImg que contiene a la imagen PAN
 * @param img2 GeoImg que contiene a la imagen MULTI
 * @param wavelet Wavelet de la que se obtendrán los filtros utilizados en la degradación
 * @param niv_PAN Niveles que se ha de degradar PAN
 * @param niv_MULTI Niveles que se ha de degradar MULTI
 * @return Imagen fusionada
 */

public GeoImg fusionarEscalando (GeoImg img, GeoImg img2, int wavelet, int niv_PAN, int niv_MULTI)
{
	boolean sinConvoluciones = false;
        boolean pasosIntermedios = true;
        
	// Comprobar que los niveles son iguales
	if (niv_PAN != niv_MULTI)
	{
		Dialogos.informarError(ic.buscar(153)); // Tag 153
		return null;
		
	}
	img = FusionAux.expandirRango(img);
        if (pasosIntermedios)
            JAI.create("imagewrite", img.getImage(), "PANexpandida.tif", "TIFF");
        
	img2 = FusionAux.expandirRango(img2);
        if (pasosIntermedios)
            JAI.create("imagewrite", img2.getImage(), "MULTIexpandida.tif", "TIFF");
        
	int colsPan = img.getImage().getWidth();
	int linsPan = img.getImage().getHeight();
	int colsMulti = img2.getImage().getWidth();
	int linsMulti = img2.getImage().getWidth();
	float ratioX = (float) colsPan/colsMulti;
	float ratioY = (float) linsPan/linsMulti;
	
	ParameterBlock pb = new ParameterBlock();
	pb.addSource(img2.getImage());
	pb.add(ratioX);
	pb.add(ratioY);
	pb.add(0.0f);
	pb.add(0.0f);
	pb.add(new InterpolationBicubic(8));
	RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
			BorderExtender.createInstance(BorderExtender.BORDER_COPY));
	img2.setImage(JAI.create("scale",pb,rh));
	// Degrado las dos imágenes
	GeoImg [] outputPan;
	GeoImg [] outputMulti;
	GeoImg Acum=null;
	GeoImg Amin=null;
	
	outputPan = degradar(wavelet, niv_PAN, img);
	outputMulti = degradar(wavelet, niv_PAN, img2);
	
	for (int i = niv_PAN; i > 0; i--)
	{

		if (i == niv_PAN)
		{
			Amin = outputMulti[(niv_MULTI-1)*4];
		}
		else
		{
			Amin = Acum;
		}
			if (pasosIntermedios)
				JAI.create("imagewrite", Amin.getImage(), "Aminoriginal"+i+".tiff", "TIFF");
		// 	Sumo cols
		Amin = sumarCols(Amin);
				
		pb = new ParameterBlock();
		if (!sinConvoluciones)
		Amin = convJAI(Amin, HOR_A, true, wavelet);
		GeoImg Dhmin = outputPan[(i-1)*4+1];
		Dhmin = sumarCols(Dhmin);
		if (!sinConvoluciones)
		Dhmin = convJAI(Dhmin, HOR_A, false, wavelet);
		pb = new ParameterBlock();
		pb.addSource(Amin.getImage());
		pb.addSource(Dhmin.getImage());
		GeoImg AplusH = 
                        new GeoImg(
                            Amin.getData(),
                            JAI.create("add", pb),
                            new File(""),
                            Amin.getStreamMetadata(),
                            Amin.getImageMetadata());		
		AplusH = sumarFilas(AplusH);
		//pb = crearPB(SCA_V,AplusH,0,0);
		//AplusH = JAI.create("scale",pb);

		AplusH = convJAI(AplusH, VERT_A, true, wavelet);
	
		GeoImg Dvmin = outputPan[(i-1)*4+2];
		Dvmin = sumarCols(Dvmin);
		//pb = crearPB(SCA_H, Dvmin, 0, 0);
		//Dvmin = JAI.create("scale",pb);
		if (!sinConvoluciones)
		Dvmin = convJAI(Dvmin, HOR_A, true, wavelet);
	
		GeoImg Ddmin = outputPan[(i-1)*4+3];
		Ddmin = sumarCols(Ddmin);
		//pb = crearPB(SCA_H, Ddmin, 0, 0);
		//Ddmin = JAI.create("scale",pb);
		if (!sinConvoluciones)
		Ddmin = convJAI(Ddmin, HOR_A, false, wavelet);
		pb = new ParameterBlock();
		pb.addSource(Dvmin.getImage());
		pb.addSource(Ddmin.getImage());
		GeoImg VplusD = 
                        new GeoImg(
                            Dvmin.getData(),
                            JAI.create("add", pb),
                            new File(""),
                            Dvmin.getStreamMetadata(),
                            Dvmin.getImageMetadata());
		VplusD = sumarFilas(VplusD);
		//pb = crearPB(SCA_V,VplusD,0,0);
		//VplusD = JAI.create("scale",pb);
		if (!sinConvoluciones)
		VplusD = convJAI(VplusD, VERT_A, false, wavelet);
		pb = new ParameterBlock();
		pb.addSource(AplusH.getImage());
		pb.addSource(VplusD.getImage());
		GeoImg Amax = 
                    new GeoImg(
                        AplusH.getData(),
                        JAI.create("add", pb),
                        new File(
                            ic.buscar(155)
                            +img.getFile().getName()
                            +" & "+img2.getFile().getName()),
                        AplusH.getStreamMetadata(),
                        AplusH.getImageMetadata()); // Tag 155
		Acum = Amax;
	}
	return Acum;
}


/**
 * Obtiene una imagen fusionada por el método de Mallat
 * @param img GeoImg que contiene a la imagen PAN
 * @param img2 GeoImg que contiene a la imagen MULTI
 * @param wavelet Wavelet de la que se obtendrán los filtros utilizados en la degradación
 * @param niv_PAN Niveles que se ha de degradar PAN
 * @param niv_MULTI Niveles que se ha de degradar MULTI
 * @return Imagen fusionada
 */

public GeoImg fusionar (GeoImg img, GeoImg img2, int wavelet, int niv_PAN, int niv_MULTI)
{
	//	
	GeoImg output[];
	GeoImg outlores[]=null;
	boolean pasosIntermedios = false;

	int cols_PAN = img.getImage().getWidth();
	int cols_MULTI = img2.getImage().getWidth();
	
	
	img = FusionAux.expandirRango(img);
	img2 = FusionAux.expandirRango(img2);
	
	
	// Diferencia de resolución entre una imagen y la otra
	int diferencia_diadica = cols_PAN / cols_MULTI;
	
	
	if (diferencia_diadica != 1)
	{
		diferencia_diadica /= 2;
	} // Evitamos esta división que haría cero niveles
	//niveles = (int)Math.max(niv_PAN,niv_MULTI);
	GeoImg Acum=null;
	GeoImg Amin=null;	
	
	if (niv_MULTI!=0)
	{

			outlores = degradar(wavelet, niv_MULTI, img2);

	}
//	 Deberé igualarlo a la MULTI degradada, pues al degradar, cambia
	if (outlores != null)
	{
		img = Filtros.igualarHistograma(outlores[(niv_MULTI-1)*4], img);
	}
	else
	{
		img = Filtros.igualarHistograma(img2, img);
	}
	output = degradar(wavelet, niv_PAN, img);
	// Tengo todos los datos que necesito en el atributo output	
	// Cojo el A ajustado al nivel 
	// El bucle no es por niveles, sino niv_PAN(queremos obtener una MULTI del tamaño de la PAN)
	// Interpolación avanzada
	
	for (int i = niv_PAN; i > 0; i--)
	{
		if ((i == niv_PAN) && (niv_MULTI == 0))
		{
			Amin = img2;
		}
		else if ((i == niv_PAN) && (niv_MULTI != 0))
		{
			Amin = outlores[(niv_MULTI-1)*4];
		}
		else
		{
			Amin = Acum;
		}
		// 	Sumo cols
		Amin = sumarCols(Amin);
		if (pasosIntermedios)
			JAI.create("imagewrite", Amin.getImage(), "Amin.tiff", "TIFF");		
		ParameterBlock pb = new ParameterBlock();
		//pb=crearPB (SCA_H,Amin,0,0);
		//Amin = JAI.create("scale",pb);		
		Amin = convJAI(Amin, HOR_A, true, wavelet);
		GeoImg Dhmin = output[(i-1)*4+1];
		Dhmin = sumarCols(Dhmin);
		//pb = crearPB (SCA_H,Dhmin,0,0);		
		//Dhmin = JAI.create("scale",pb);
		Dhmin = convJAI(Dhmin, HOR_A, false, wavelet);
		pb = new ParameterBlock();
		pb.addSource(Amin.getImage());
		pb.addSource(Dhmin.getImage());
		GeoImg AplusH = 
                        new GeoImg(
                            Amin.getData(),
                            JAI.create("add", pb),
                            new File(""),
                            Amin.getStreamMetadata(),
                            Amin.getImageMetadata());		
		AplusH = sumarFilas(AplusH);
		//pb = crearPB(SCA_V,AplusH,0,0);
		//AplusH = JAI.create("scale",pb);

		AplusH = convJAI(AplusH, VERT_A, true, wavelet);
	
		GeoImg Dvmin = output[(i-1)*4+2];
		Dvmin = sumarCols(Dvmin);
		//pb = crearPB(SCA_H, Dvmin, 0, 0);
		//Dvmin = JAI.create("scale",pb);
		Dvmin = convJAI(Dvmin, HOR_A, true, wavelet);
	
		GeoImg Ddmin = output[(i-1)*4+3];
		Ddmin = sumarCols(Ddmin);
		//pb = crearPB(SCA_H, Ddmin, 0, 0);
		//Ddmin = JAI.create("scale",pb);
		Ddmin = convJAI(Ddmin, HOR_A, false, wavelet);
		pb = new ParameterBlock();
		pb.addSource(Dvmin.getImage());
		pb.addSource(Ddmin.getImage());
		GeoImg VplusD = 
                    new GeoImg(
                        Dvmin.getData(),
                        JAI.create("add", pb),
                        new File(""),
                        Dvmin.getStreamMetadata(),
                        Dvmin.getImageMetadata());
		VplusD = sumarFilas(VplusD);
		//pb = crearPB(SCA_V,VplusD,0,0);
		//VplusD = JAI.create("scale",pb);
		VplusD = convJAI(VplusD, VERT_A, false, wavelet);
		pb = new ParameterBlock();
		pb.addSource(AplusH.getImage());
		pb.addSource(VplusD.getImage());
		GeoImg Amax = 
                    new GeoImg(
                        AplusH.getData(),
                        JAI.create("add", pb),
                        new File(
                            ic.buscar(155)
                            +img.getFile().getName()
                            +" & "
                            +img2.getFile().getName()),
                        AplusH.getStreamMetadata(),
                        AplusH.getImageMetadata()); // Tag 155
		Acum = Amax;		
		
	}	
	
	return Acum;
}







   



   

/**
 * Obtiene una fusión mediante el método à trous.
 * @param img Imagen PAN original
 * @param img2 Imagen MULTI original
 * @param wavelet Wavelet a partir de la cual se obtendrán los filtros para degradar la imagen
 * @param niv_PAN Niveles que se ha de degradar PAN
 * @param niv_MULTI Niveles que se ha de degradar MULTI
 * @param interp Interpolación utilizada para hacer MULTI del tamaño de PAN (NEAR, BILIN, BICUB)
 * @param precision Precisión en los decimales (necesaria para la interpolación bicúbica)
 * @param ponder Ponderación con la que se realizará la fusión à trous
 * @return Imagen fusionada
 */

public GeoImg atrous (GeoImg img, GeoImg img2, int wavelet, int niv_PAN, int niv_MULTI, int interp, int precision, double ponder)
{

	boolean pasosIntermedios = false;

	// Pasamos el número de niveles de gris a 32 bits
	
	GeoImg imgexp = FusionAux.expandirRango(img);
	GeoImg img2exp = FusionAux.expandirRango(img2);
	
	MULTIcargada = img2;
	
	// Ecualizo PAN
	Filtros a = new Filtros();
	int cols_PAN = img.getImage().getWidth();
	int cols_MULTI = img2.getImage().getWidth();
	
	
	// Ajusto el tamaño
	float factor_escala = cols_PAN/cols_MULTI;
	ParameterBlock pb = new ParameterBlock();
	RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
			BorderExtender.createInstance(BorderExtender.BORDER_COPY));
	if (interp != LANCZOS)
	{

		pb.addSource(img2exp.getImage());
		pb.add(factor_escala);
		pb.add(factor_escala);
		pb.add(0.0F);
		pb.add(0.0F);
		System.out.println("La interpolación que se va a usar es "+interp);
		switch(interp)
		{
		case (NEAR):
		{
			pb.add(new InterpolationNearest());
			break;
		}
		case (BILIN):
		{
			pb.add(new InterpolationBilinear());
			break;
		}
		case (BICUB):
		{
			pb.add(new InterpolationBicubic(precision));
			break;
		}
		case (BICUB2):
		{
			pb.add(new InterpolationBicubic2(precision));
		}
		}
		img2exp.setImage(JAI.create("scale",pb,rh));
		
	}
	else
	{   //Interpolación Lanczos
		img2exp = new GeoImg (img2.getData(),InterpolationLanczos.interpolar(img2.getImage(), img.getImage().getWidth(), img.getImage().getHeight()),img2.getFile(),img2.getStreamMetadata(),img2.getImageMetadata());
	}
	if (pasosIntermedios)
		JAI.create("imagewrite",img2exp.getImage(),"PANEscalada.tif","TIFF");
	// Definición de las matrices de resultados que necesitamos
	GeoImg [] coeffs = new GeoImg[niv_PAN];
	GeoImg [] PANdeg = new GeoImg[niv_PAN];
	GeoImg MULTIdeg=null;
	
	// Degradación de MULTI
	float [] filtro;
	
	if (niv_MULTI==0)
	{		
		MULTIdegradada = img2exp;
		MULTIdeg = img2exp;				
	}
	else
	{
		
		filtro = a.getFiltro(0,wavelet);
		for (int i = 0; i < niv_MULTI; i++)
		{
			int dim = (int) Math.sqrt(filtro.length);

			KernelJAI filtro2d = new KernelJAI(dim,dim,filtro);
			if (i == 0)
			{
				pb = new ParameterBlock();
				pb.addSource(img2exp.getImage());
				pb.add(filtro2d);
				MULTIdeg = 
                                        new GeoImg(img2exp.getData(),
                                        JAI.create("convolve",pb,rh),
                                        new File(""),
                                        img2exp.getStreamMetadata(),
                                        img2exp.getImageMetadata());
														
			}
			else
			{
				pb = new ParameterBlock();
				pb.addSource(MULTIdeg.getImage());
				pb.add(filtro2d);
				MULTIdeg.setImage(JAI.create("convolve",pb,rh));	
			}
			if (pasosIntermedios)
				JAI.create("imagewrite",MULTIdeg.getImage(),"MULTIdeg"+i+".tif","TIFF");
			
			filtro = a.degradarFiltro(filtro);
		}
		MULTIdegradada = MULTIdeg;
			
	}
	// Si la ponderación es 0, no hace falta degradar PAN porque melt = MULTIdeg
	GeoImg melt = null;
	if (ponder == 0.0d)
	{
		System.out.println("Ponder es 0");
		melt = MULTIdeg;
                melt.setFile(
                    new File(
                        ic.buscar(156)
                        +img.getFile().getName()
                        +" & "
                        +img2.getFile().getName())); // Tag 156
		return melt;
	}
	
	// Aplicamos la degradación a la imagen PAN
		filtro = a.getFiltro(0,wavelet);
		if (memPANcoeffs == null)
		{
			for (int i = 0; i < niv_PAN; i++)
			{
				int dim = (int) Math.sqrt(filtro.length);
				KernelJAI filtro2d = new KernelJAI(dim,dim,filtro);

				if (i == 0)
				{
					pb = new ParameterBlock();
					pb.addSource(imgexp.getImage());
					pb.add(filtro2d);
					PANdeg[0] = 
                                                new GeoImg(
                                                    img.getData(),
                                                    JAI.create("convolve",pb,rh),
                                                    new File(""),
                                                    img.getStreamMetadata(),
                                                    img.getImageMetadata());
					pb = new ParameterBlock();
					pb.addSource(imgexp.getImage());
					pb.addSource(PANdeg[0].getImage());
					coeffs[0] = 
                                                new GeoImg(
                                                    img.getData(),
                                                    JAI.create("subtract", pb),
                                                    new File(""),
                                                    img.getStreamMetadata(),
                                                    img.getImageMetadata());
					// Matrices de coeficientes de la PAN

				}
				else
				{
					pb = new ParameterBlock();
					pb.addSource(PANdeg[i-1].getImage());
					pb.add(filtro2d);			
					PANdeg[i] = 
                                                new GeoImg(
                                                    PANdeg[i-1].getData(),
                                                    JAI.create("convolve",pb,rh),
                                                    new File(""),
                                                    PANdeg[i-1].getStreamMetadata(),
                                                    PANdeg[i-1].getImageMetadata());
					pb = new ParameterBlock();
					pb.addSource(PANdeg[i-1].getImage());
					pb.addSource(PANdeg[i].getImage());
					coeffs[i] = 
                                                new GeoImg(
                                                    PANdeg[i-1].getData(),
                                                    JAI.create("subtract", pb),
                                                    new File(""),
                                                    PANdeg[i-1].getStreamMetadata(),
                                                    PANdeg[i-1].getImageMetadata());
//					Matrices de coeficientes de la PAN

				}
				if (pasosIntermedios)
				{
					JAI.create("imagewrite",PANdeg[i].getImage(),"PANdeg"+i+".tif","TIFF");
					JAI.create("imagewrite",coeffs[i].getImage(),"coeffs"+i+".tif","TIFF");
				}
				filtro = a.degradarFiltro(filtro);
			}
			memPANcoeffs = new GeoImg[coeffs.length];
			System.arraycopy(coeffs,0,memPANcoeffs,0,coeffs.length);
		}
		else
		{
			coeffs = new GeoImg[memPANcoeffs.length];
			System.arraycopy(memPANcoeffs,0,coeffs,0,memPANcoeffs.length);
		}
		
	// Ya podemos sumar a MULTI los coeficientes de detalle
	// Se introduce la ponderación
	
	double [] ponderacion = {ponder};

	// sumCoeffs es la suma de todos los coeficientes
	GeoImg sumCoeffs= null;

	if (niv_PAN == 1)
	{
		sumCoeffs = coeffs[0];

	}
	else
	{
		for (int i = 0; i < niv_PAN-1; i++)
		{
			pb = new ParameterBlock();
			pb.addSource(coeffs[i].getImage());
			pb.addSource(coeffs[i+1].getImage());
			sumCoeffs = 
                                new GeoImg(
                                    coeffs[i].getData(),
                                    JAI.create("add",pb),
                                    null,
                                    coeffs[i].getStreamMetadata(),
                                    coeffs[i].getImageMetadata());
			if (pasosIntermedios)			
				JAI.create(
                                        "imagewrite",
                                        sumCoeffs.getImage(),
                                        "sumCoeffs"+i+".tif","TIFF");
		}
	}
	
	// Multiplicación por la ponderación

	if (ponder == 1)
	{
		pb = new ParameterBlock();
		pb.addSource(MULTIdeg.getImage());
		pb.addSource(sumCoeffs.getImage());
		melt = 
                        new GeoImg(
                            img.getData(),
                            JAI.create("add",pb),
                            new File(
                                ic.buscar(156)
                                +img.getFile().getName()
                                +" & "
                                +img2.getFile().getName()),
                            img.getStreamMetadata(),
                            img.getImageMetadata()); // Tag 156
	}
	else
	{

		pb = new ParameterBlock();
		pb.addSource(sumCoeffs.getImage());
		pb.add(ponderacion);
		sumCoeffs = 
                        new GeoImg(
                            sumCoeffs.getData(),
                            JAI.create("multiplyconst", pb),
                            new File(""),
                            sumCoeffs.getStreamMetadata(),
                            sumCoeffs.getImageMetadata());
		pb = new ParameterBlock();
		pb.addSource(MULTIdeg.getImage());
		pb.addSource(sumCoeffs.getImage());
		melt = 
                        new GeoImg(
                            img.getData(),
                            JAI.create("add",pb),
                            new File(
                                ic.buscar(156)
                                +img.getFile().getName()
                                +" & "
                                +img2.getFile().getName()),
                            img.getStreamMetadata(),
                            img.getImageMetadata()); // Tag 156
	}

	return melt;
}

/**
 * Introduce valores intermedios (0 o interpolación más avanzada) al cambiar el tamaño en la
 * fusión de Mallat
 * @param amin Imagen de entrada
 * @return Imagen con valores introducidos
 */

GeoImg sumarCols (GeoImg amin)
{
	int cols = amin.getImage().getWidth();
	int lins = amin.getImage().getHeight();
	//	 Tengo que añadir ceros cada dos columnas...
	Raster input = amin.getImage().getData();
	int transfer = input.getTransferType();
	if (false)
	{
		if (transfer == DataBuffer.TYPE_BYTE)
		{
			byte [] pixeles = new byte[cols*lins];				
			input.getDataElements(0,0,cols,lins,pixeles);
			// Se aumenta el tamaño
			cols *= 2;
			byte [] salida = new byte[cols*lins];
			int pin = 0;
			for (int i = 0; i < cols*lins; i++)
			{		
				if ((i % 2) == 0)
				{// Pixel par
					salida[i]=pixeles[pin];
					pin++;
				}
				else
				{// Hay que rellenar
					// Interpolación más avanzada
					/*if ((pin > 0) && (pin < ((cols/2)*lins)-1))
				{
					short pixant = (short) (0x00FF & ((short)pixeles[pin-1]));
					short pixact = (short) (0x00FF & ((short)pixeles[pin]));
					short media = (short) ((pixant+pixact)/2);
					salida[i] = (byte)media;

				}*/
					salida[i] = (byte)0;


				}
			}

			// Creamos la imagen de salida (c*2)
			DataBufferByte dbuffer = new DataBufferByte(salida,cols*lins);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, cols, lins, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
			tiled.setData(raster);
			return new GeoImg(amin.getData(),
                                tiled,
                                new File(""),
                                amin.getStreamMetadata(),
                                amin.getImageMetadata());
		}	
		if (transfer == DataBuffer.TYPE_INT)
		{
			int [] pixeles = new int[cols*lins];			
			input.getPixels(0,0,cols,lins,pixeles);

//			Se aumenta el tamaño
			cols *= 2;
			int [] salida = new int[cols*lins];
			int pin = 0;
			for (int i = 0; i < cols*lins; i++)
			{		
				if ((i % 2) == 0)
				{// Pixel par
					salida[i]=pixeles[pin];
					pin++;
				}
				else
				{
					salida[i]=0;
				}
			}

			// Creamos la imagen de salida (c/2)
			DataBufferInt dbuffer = new DataBufferInt(salida,cols*lins);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cols, lins, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
			tiled.setData(raster);
			return new GeoImg (amin.getData(),
                                tiled,
                                new File(""),
                                amin.getStreamMetadata(),
                                amin.getImageMetadata());
		}
		if (transfer == DataBuffer.TYPE_FLOAT)
		{
			float [] pixeles = new float[cols*lins];			

			input.getDataElements(0,0,cols,lins,pixeles);
//			Se aumenta el tamaño
			cols *= 2;
			float [] salida = new float[cols*lins];
			int pin = 0;
			for (int i = 0; i < cols*lins; i++)
			{		
				if ((i % 2) == 0)
				{// Pixel par
					salida[i]=pixeles[pin];
					pin++;
				}
				else
				{
					salida[i]=(float)0;
				}
			}

			// Creamos la imagen de salida (c/2)
			DataBufferFloat dbuffer = new DataBufferFloat(salida,cols*lins);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, cols, lins, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
			tiled.setData(raster);
			return new GeoImg(amin.getData(),
                                tiled,
                                new File(""),
                                amin.getStreamMetadata(),
                                amin.getImageMetadata());
		}
		if (transfer == DataBuffer.TYPE_DOUBLE)
		{
			double [] pixeles = new double[cols*lins];			

			input.getDataElements(0,0,cols,lins,pixeles);
//			Se aumenta el tamaño
			cols *= 2;
			double [] salida = new double[cols*lins];
			int pin = 0;
			for (int i = 0; i < cols*lins; i++)
			{		
				if ((i % 2) == 0)
				{// Pixel par
					salida[i]=pixeles[pin];
					pin++;
				}
				else
				{
					salida[i]=(double)0;
				}
			}

			// Creamos la imagen de salida (c/2)
			DataBufferDouble dbuffer = new DataBufferDouble(salida,cols*lins);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cols, lins, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
			tiled.setData(raster);
			return new GeoImg(amin.getData(),
                                tiled,
                                new File(""),
                                amin.getStreamMetadata(),
                                amin.getImageMetadata());
		}
		return null;
	}
	else
	{// Usando scale
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(amin.getImage());
		pb.add(2.0f);
		pb.add(1.0f);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(new InterpolationBicubic(8));
		RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
				BorderExtender.createInstance(BorderExtender.BORDER_COPY));
		amin.setImage(JAI.create("scale",pb,rh));
		return amin;
	}
}

/**
 * Introduce filas de valores intermedios (0 ó interpolación más avanzada) como filas al cambiar el 
 * tamaño de la imagen en la fusión de Mallat 
 * @param aplusH Imagen original
 * @return Imagen con los valores introducidos
 */

GeoImg sumarFilas (GeoImg aplusH)
{
	Raster input = aplusH.getImage().getData();
	int cols = aplusH.getImage().getWidth();
	int lins = aplusH.getImage().getHeight();
	int transfer = input.getTransferType();
	if (false)
	{
	if (transfer == DataBuffer.TYPE_BYTE)
	{
		byte[] pixeles = new byte[cols*lins];				
		input.getDataElements(0,0,cols,lins,pixeles);
		// Se aumenta el tamaño
		lins *= 2;
		byte[] salida = new byte[cols*lins];
		int fila_actual = 0;
		int pin = 0;
		//int columnas=0;
		for (int i = 0; i< cols*lins; i++)
		{
			if ((fila_actual % 2) == 0)
			{// Fila par
				salida[i]=pixeles[pin];
				pin++;						
			}
			else
			{
				salida [i] = (byte) 0;

			}
	
			if ((i % cols) == (cols-1))
			{
				fila_actual++;
				//columnas = cols-1;
			}
		}
		// Creamos la imagen de salida (f*2)
		DataBufferByte dbuffer = new DataBufferByte(salida,cols*lins);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, cols, lins, 1);
		ColorModel colormod = PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return new GeoImg(aplusH.getData(),
                        tiled,
                        new File(""),
                        aplusH.getStreamMetadata(),
                        aplusH.getImageMetadata());
	}
	if (transfer == DataBuffer.TYPE_BYTE || transfer == DataBuffer.TYPE_INT)
	{
		int[] pixeles = new int[cols*lins];				
		input.getPixels(0,0,cols,lins,pixeles);
		// Se aumenta el tamaño
		lins *= 2;
		int[] salida = new int[cols*lins];
		int fila_actual = 0;
		int pin = 0;
		for (int i = 0; i< cols*lins; i++)
		{
			if ((fila_actual % 2) == 0)
			{// Fila par
				salida[i]=pixeles[pin];
				pin++;						
			}
			else
			{

					salida[i]=0;
				
			}
	
			if ((i % cols) == (cols-1))
			{
				fila_actual++;
			}
		}
		// Creamos la imagen de salida (f*2)
		DataBufferInt dbuffer = new DataBufferInt(salida,cols*lins);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cols, lins, 1);
		ColorModel colormod = PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return new GeoImg(aplusH.getData(),
                        tiled,
                        new File(""),
                        aplusH.getStreamMetadata(),
                        aplusH.getImageMetadata());
	}
	if (transfer == DataBuffer.TYPE_FLOAT)
	{
		float[] pixeles = new float[cols*lins];				
		input.getDataElements(0,0,cols,lins,pixeles);
		// Se aumenta el tamaño
		lins *= 2;
		float[] salida = new float[cols*lins];
		int fila_actual = 0;
		int pin = 0;
		for (int i = 0; i< cols*lins; i++)
		{
			if ((fila_actual % 2) == 0)
			{// Fila par
				salida[i]=pixeles[pin];
				pin++;						
			}
			else
			{
				salida[i]=(float)0;
			}
	
			if ((i % cols) == (cols-1))
			{
				fila_actual++;
			}
		}
		// Creamos la imagen de salida (f*2)
		DataBufferFloat dbuffer = new DataBufferFloat(salida,cols*lins);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, cols, lins, 1);
		ColorModel colormod = PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return new GeoImg(aplusH.getData(),
                        tiled,
                        new File(""),
                        aplusH.getStreamMetadata(),
                        aplusH.getImageMetadata());
	}
	if (transfer == DataBuffer.TYPE_DOUBLE)
	{
		double[] pixeles = new double[cols*lins];				
		input.getDataElements(0,0,cols,lins,pixeles);
		// Se aumenta el tamaño
		lins *= 2;
		double[] salida = new double[cols*lins];
		int fila_actual = 0;
		int pin = 0;
		for (int i = 0; i< cols*lins; i++)
		{
			if ((fila_actual % 2) == 0)
			{// Fila par
				salida[i]=pixeles[pin];
				pin++;						
			}
			else
			{
				salida[i]=(double)0;
			}
	
			if ((i % cols) == (cols-1))
			{
				fila_actual++;
			}
		}
		// Creamos la imagen de salida (f*2)
		DataBufferDouble dbuffer = new DataBufferDouble(salida,cols*lins);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cols, lins, 1);
		ColorModel colormod = PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return new GeoImg(aplusH.getData(),
                        tiled,
                        new File(""),
                        aplusH.getStreamMetadata(),
                        aplusH.getImageMetadata());
	}
	}
	else
	{
//		 Usando scale
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(aplusH.getImage());
		pb.add(1.0f);
		pb.add(2.0f);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(new InterpolationBicubic(8));
		RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
				BorderExtender.createInstance(BorderExtender.BORDER_COPY));
		aplusH.setImage(JAI.create("scale",pb,rh));
		return aplusH;
	}
	return null;
}

/**
 * Reduce una de cada dos columnas de la imagen de entrada en la reducción de tamaño de la fusión de 
 * Mallat
 * @param tiled2 Imagen original
 * @return Imagen con la mitad de columnas
 */

GeoImg reducirCols (GeoImg tiled2)
{
	int cols = tiled2.getImage().getWidth();
	int lins = tiled2.getImage().getHeight();
//	 Tengo que eliminar una de cada dos columnas...
	Raster input = tiled2.getImage().getData();
	int transfer = input.getTransferType();
	if (true)
	{			
	if (transfer == DataBuffer.TYPE_BYTE)
	{
	byte [] pixeles = new byte[cols*lins];			
	//input.getPixels(0,0,cols,lins,pixeles);
	input.getDataElements(0,0,cols,lins,pixeles);
	// Se reduce el tamaño
	cols /= 2;
	byte [] salida = new byte[cols*lins];
	for (int i = 0; i < cols*lins; i++)
	{
		salida[i]=pixeles[2*i];

	}
	
	// Creamos la imagen de salida (c/2)
	DataBufferByte dbuffer = new DataBufferByte(salida,cols*lins);
	SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, cols, lins, 1);
	ColorModel colormod =  PlanarImage.createColorModel(samplemod);
	Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
	TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
	tiled.setData(raster);
	return new GeoImg(tiled2.getData(),
                tiled,
                new File(""),
                tiled2.getStreamMetadata(),
                tiled2.getImageMetadata());
	}
	if (transfer == DataBuffer.TYPE_INT)
	{
	int [] pixeles = new int[cols*lins];			
	input.getPixels(0,0,cols,lins,pixeles);
	
	// Se reduce el tamaño
	cols /= 2;
	int [] salida = new int[cols*lins];
	for (int i = 0; i < cols*lins; i++)
	{
		salida[i]=pixeles[2*i];

	}
	
	// Creamos la imagen de salida (c/2)
	DataBufferInt dbuffer = new DataBufferInt(salida,cols*lins);
	SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cols, lins, 1);
	ColorModel colormod =  PlanarImage.createColorModel(samplemod);
	Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
	TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
	tiled.setData(raster);
	return new GeoImg(tiled2.getData(),
                tiled,
                new File(""),
                tiled2.getStreamMetadata(),
                tiled2.getImageMetadata());
	}
	if (transfer == DataBuffer.TYPE_FLOAT)
	{
	float [] pixeles = new float[cols*lins];			
	//input.getPixels(0,0,cols,lins,pixeles);
	input.getDataElements(0,0,cols,lins,pixeles);
	// Se reduce el tamaño
	cols /= 2;
	float [] salida = new float[cols*lins];
	for (int i = 0; i < cols*lins; i++)
	{
		salida[i]=pixeles[2*i];

	}
	
	// Creamos la imagen de salida (c/2)
	DataBufferFloat dbuffer = new DataBufferFloat(salida,cols*lins);
	SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, cols, lins, 1);
	ColorModel colormod =  PlanarImage.createColorModel(samplemod);
	Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
	TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
	tiled.setData(raster);
	return new GeoImg(tiled2.getData(),
                tiled,
                new File(""),
                tiled2.getStreamMetadata(),
                tiled2.getImageMetadata());
	}
	if (transfer == DataBuffer.TYPE_DOUBLE)
	{
	double [] pixeles = new double[cols*lins];			
	//input.getPixels(0,0,cols,lins,pixeles);
	input.getDataElements(0,0,cols,lins,pixeles);
	// Se reduce el tamaño
	cols /= 2;
	double [] salida = new double[cols*lins];
	for (int i = 0; i < cols*lins; i++)
	{
		salida[i]=pixeles[2*i];

	}
	
	// Creamos la imagen de salida (c/2)
	DataBufferDouble dbuffer = new DataBufferDouble(salida,cols*lins);
	SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cols, lins, 1);
	ColorModel colormod =  PlanarImage.createColorModel(samplemod);
	Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
	TiledImage tiled = new TiledImage(0,0,cols, lins, 0, 0, samplemod, colormod);
	tiled.setData(raster);
	return new GeoImg(tiled2.getData(),
                tiled,
                new File(""),
                tiled2.getStreamMetadata(),
                tiled2.getImageMetadata());
	}
	}
	else
	{
//		 Usando scale
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(tiled2.getImage());
		pb.add(0.5f);
		pb.add(1.0f);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(new InterpolationBicubic(8));
		RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
				BorderExtender.createInstance(BorderExtender.BORDER_COPY));
		tiled2.setImage(JAI.create("scale",pb,rh));
		return tiled2;
	}
	return null;
}

/**
 * Reduce una de cada dos filas en la reducción de tamaño de la fusión de Mallat
 * @param tiled2 Imagen original
 * @return Imagen con la mitad de filas
 */

GeoImg reducirFilas (GeoImg tiled2)
{
	Raster input = tiled2.getImage().getData();
	int cols = tiled2.getImage().getWidth();
	int lins = tiled2.getImage().getHeight();
	int transfer = input.getTransferType();
	   if (true) {
        if (transfer == DataBuffer.TYPE_BYTE) {


            byte[] pixeles = new byte[cols * lins];
            //input.getPixels(0,0,cols,lins,pixeles);
            input.getDataElements(0, 0, cols, lins, pixeles);
            // Se reduce el tamaño
            lins /= 2;
            byte[] salida = new byte[cols * lins];
            int indice_out = 0, indice_in = 0;
            int fila_actual = 0;
            while (indice_out < cols * lins) {
                salida[indice_out++] = pixeles[indice_in++];
                if ((indice_in + 1) % (cols) == 0 && (fila_actual % 2) != 0) {
                    indice_in += cols;
                    fila_actual++;

                }
                if ((indice_in + 1) % (cols) == 0) {
                    fila_actual++;
                }


            }
//	 Creamos la imagen de salida (c/2)
            DataBufferByte dbuffer = new DataBufferByte(salida, cols * lins);
            SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, cols, lins, 1);
            ColorModel colormod = PlanarImage.createColorModel(samplemod);
            Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0, 0));
            TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
            tiled.setData(raster);
            return new GeoImg(tiled2.getData(),
                    tiled,
                    new File(""),
                    tiled2.getStreamMetadata(),
                    tiled2.getImageMetadata());
        }
        if (transfer == DataBuffer.TYPE_INT) {
            int[] pixeles = new int[cols * lins];
            input.getPixels(0, 0, cols, lins, pixeles);

            // Se reduce el tamaño
            lins /= 2;
            int[] salida = new int[cols * lins];
            int indice_out = 0, indice_in = 0;
            int fila_actual = 0;
            while (indice_out < cols * lins) {
                salida[indice_out++] = pixeles[indice_in++];
                if ((indice_in + 1) % (cols) == 0 && (fila_actual % 2) != 0) {
                    indice_in += cols;
                    fila_actual++;

                }
                if ((indice_in + 1) % (cols) == 0) {
                    fila_actual++;
                }


            }
//	 Creamos la imagen de salida (c/2)
            DataBufferInt dbuffer = new DataBufferInt(salida, cols * lins);
            SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cols, lins, 1);
            ColorModel colormod = PlanarImage.createColorModel(samplemod);
            Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0, 0));
            TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
            tiled.setData(raster);
            return new GeoImg(tiled2.getData(),
                    tiled,
                    new File(""),
                    tiled2.getStreamMetadata(),
                    tiled2.getImageMetadata());
        }
        if (transfer == DataBuffer.TYPE_FLOAT) {
            float[] pixeles = new float[cols * lins];
            //input.getPixels(0,0,cols,lins,pixeles);
            input.getDataElements(0, 0, cols, lins, pixeles);
            // Se reduce el tamaño
            lins /= 2;
            float[] salida = new float[cols * lins];
            int indice_out = 0, indice_in = 0;
            int fila_actual = 0;
            while (indice_out < cols * lins) {
                salida[indice_out++] = pixeles[indice_in++];
                if ((indice_in + 1) % (cols) == 0 && (fila_actual % 2) != 0) {
                    indice_in += cols;
                    fila_actual++;

                }
                if ((indice_in + 1) % (cols) == 0) {
                    fila_actual++;
                }


            }
//	 Creamos la imagen de salida (c/2)
            DataBufferFloat dbuffer = new DataBufferFloat(salida, cols * lins);
            SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, cols, lins, 1);
            ColorModel colormod = PlanarImage.createColorModel(samplemod);
            Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0, 0));
            TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
            tiled.setData(raster);
            return new GeoImg(tiled2.getData(),
                    tiled,
                    new File(""),
                    tiled2.getStreamMetadata(),
                    tiled2.getImageMetadata());
        }
        if (transfer == DataBuffer.TYPE_DOUBLE) {
            double[] pixeles = new double[cols * lins];
            //input.getPixels(0,0,cols,lins,pixeles);
            input.getDataElements(0, 0, cols, lins, pixeles);
            // Se reduce el tamaño
            lins /= 2;
            double[] salida = new double[cols * lins];
            int indice_out = 0, indice_in = 0;
            int fila_actual = 0;
            while (indice_out < cols * lins) {
                salida[indice_out++] = pixeles[indice_in++];
                if ((indice_in + 1) % (cols) == 0 && (fila_actual % 2) != 0) {
                    indice_in += cols;
                    fila_actual++;

                }
                if ((indice_in + 1) % (cols) == 0) {
                    fila_actual++;
                }


            }
//	 Creamos la imagen de salida (c/2)
            DataBufferDouble dbuffer = new DataBufferDouble(salida, cols * lins);
            SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cols, lins, 1);
            ColorModel colormod = PlanarImage.createColorModel(samplemod);
            Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0, 0));
            TiledImage tiled = new TiledImage(0, 0, cols, lins, 0, 0, samplemod, colormod);
            tiled.setData(raster);
            return new GeoImg(tiled2.getData(),
                    tiled,
                    new File(""),
                    tiled2.getStreamMetadata(),
                    tiled2.getImageMetadata());
        }
    } else {
//		 Usando scale
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(tiled2.getImage());
        pb.add(1.0f);
        pb.add(0.5f);
        pb.add(0.0f);
        pb.add(0.0f);
        //pb.add(new InterpolationBicubic(8));
        pb.add(new InterpolationNearest());
        RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
                BorderExtender.createInstance(BorderExtender.BORDER_COPY));
        tiled2.setImage(JAI.create("scale", pb, rh));
        return tiled2;
    }
    return null;

}

/**
 * Crea un bloque de parámetros que se utilizan durante la fusión de Mallat
 * NOTA: No se utiliza en la versión final, en la que se usa la convolución de JAI y 
 * los extensores de valores en los bordes (ver memoria)
 * @param orient Tipo de operación y orientación (HOR, VERT)
 * @param image Imagen de entrada
 * @param pad1 Relleno necesario (arriba, izquierda)
 * @param pad2 Relleno necesario (abajo, derecha)
 * @return Un bloque de parámetros para un operador de JAI
 */

ParameterBlock crearPB (int orient, PlanarImage image, int pad1, int pad2)
{
	switch (orient)
	{
	case (HOR_A):
	{
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(pad1);
		pb.add(pad2);
		pb.add(0);
		pb.add(0);
		pb.add(BorderExtender.createInstance(BorderExtender.BORDER_COPY));
		return pb;
	}
	case (VERT_A):
	{
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(0);
		pb.add(0);
		pb.add(pad1);
		pb.add(pad2);
		pb.add(BorderExtender.createInstance(BorderExtender.BORDER_COPY));
		return pb;
	}
	case (HOR_R):
	{
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add((float)(pad1-1));
		pb.add(0.0F);
		pb.add((float)(image.getWidth()-(pad1+pad2)));
		pb.add((float)(image.getHeight()));		 
		return pb;
	}
	case (VERT_R):
	{
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(0.0F);
		pb.add((float)pad1);
		pb.add((float)(image.getWidth()));
		pb.add((float)(image.getHeight()-(pad1+pad2)));		
		return pb;
	}
	case (MOV_H):
	{
	    ParameterBlock pb = new ParameterBlock();
    	pb.addSource(image);
    	pb.add((float)(-(pad1-1)));
    	pb.add(0.0F);
    	return pb;
	}
	case (MOV_V):
	{
	    ParameterBlock pb = new ParameterBlock();
    	pb.addSource(image);
    	pb.add(0.0F);
    	pb.add((float)-pad1);
    	return pb;
	}
	case (SCA_H):
	{
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(2.0f);
		pb.add(1.0f);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(new InterpolationBilinear(32));		
		return pb;
	}
	case (SCA_V):
	{
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(1.0f);
		pb.add(2.0f);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(new InterpolationBilinear(32));	
		return pb;
	}
}
	return null;
	
}


/**
 * Obtiene una imagen convolucionada con un filtro en la fusión de Mallat
 * @param img Imagen original
 * @param orient Orientación de la convolución (HOR, VERT)
 * @param H Indica si se usa el filtro H o no (G)
 * @param wavelet Wavelet a partir de la cual se elegirá el filtro
 * @return Imagen convolucionada
 */

    @SuppressWarnings("unchecked")
public GeoImg convJAI (GeoImg img, int orient, boolean H, int wavelet)
{
	Filtros filt = new Filtros();
	
	//Obtener los filtros
	final float [] hArray = filt.getFiltro(Filtros.LD,wavelet);
	final float [] gArray = filt.getFiltro(Filtros.HD,wavelet);
	
	//final float [] hArray = filt.db4filfBETA[0];
	//final float [] gArray = filt.db4filfBETA[1];
	//Kernels
	KernelJAI kernelhh = new KernelJAI(hArray.length,1,hArray);	
	KernelJAI kernelhv = new KernelJAI(1,hArray.length,hArray);	
	KernelJAI kernelgh = new KernelJAI(hArray.length,1,gArray);	
	KernelJAI kernelgv = new KernelJAI(1,hArray.length,gArray);
	
	HashMap hm = new HashMap();
        hm.put(JAI.KEY_BORDER_EXTENDER,
		BorderExtender.createInstance(BorderExtender.BORDER_COPY));
        hm.put(JAI.KEY_INTERPOLATION,new InterpolationNearest());
	RenderingHints rh = new RenderingHints(hm);
	
	
	if (H)
	{
		switch(orient)
		{
			case(HOR_A):
			{
				/*ParameterBlock pbh = crearPB(HOR_A, image, kernelhh.getLeftPadding(),kernelhh.getRightPadding());
				PlanarImage tiled = JAI.create("border",pbh);*/	
				ParameterBlock pb = new ParameterBlock();
				pb.addSource(img.getImage());
				pb.add(kernelhh);
				PlanarImage tiled = JAI.create("convolve", pb, rh);
				/*pbh = crearPB(HOR_R, tiled,kernelhh.getLeftPadding(),kernelhh.getRightPadding());
				tiled = JAI.create("crop",pbh);
				System.out.println("Origen cropped: "+tiled.getMinX()+","+tiled.getMinY());
				pbh = crearPB(MOV_H,tiled,kernelhh.getLeftPadding(),kernelhh.getRightPadding());			    
				tiled = JAI.create("translate",pbh);*/				
				return new GeoImg(img.getData(),
                                        tiled,
                                        new File(""),
                                        img.getStreamMetadata(),
                                        img.getImageMetadata());
			}
			case(VERT_A):
			{
			/*	ParameterBlock pbh = crearPB(VERT_A, image, kernelhv.getTopPadding(), kernelhv.getBottomPadding());
				PlanarImage tiled = JAI.create("border",pbh);*/	
				ParameterBlock pb = new ParameterBlock();
				pb.addSource(img.getImage());
				pb.add(kernelhv);
				PlanarImage tiled = JAI.create("convolve", pb, rh);
				/*pbh = crearPB(VERT_R, tiled, kernelhv.getTopPadding(),kernelhv.getBottomPadding());
				tiled = JAI.create("crop",pbh);
				pbh = crearPB(MOV_V,tiled,kernelhv.getTopPadding(),kernelhv.getBottomPadding());			    
				tiled = JAI.create("translate",pbh);*/
				return new GeoImg(img.getData(),
                                        tiled,
                                        new File(""),
                                        img.getStreamMetadata(),
                                        img.getImageMetadata());
			}
		}
	}
	else
	{
		switch(orient)
		{
			case(HOR_A):
			{
				/*ParameterBlock pbh = crearPB(HOR_A, image, kernelgh.getLeftPadding(), kernelgh.getRightPadding());
				PlanarImage tiled = JAI.create("border",pbh);*/
				ParameterBlock pb = new ParameterBlock();
				pb.addSource(img.getImage());
				pb.add(kernelgh);
				PlanarImage tiled = JAI.create("convolve", pb, rh);				
				/*pbh = crearPB(HOR_R, tiled, kernelgh.getLeftPadding(), kernelgh.getRightPadding());
				tiled = JAI.create("crop",pbh);
				pbh = crearPB(MOV_H,tiled, kernelgh.getLeftPadding(), kernelgh.getRightPadding());			    
				tiled = JAI.create("translate",pbh);*/
				return new GeoImg(img.getData(),
                                        tiled,
                                        new File(""),
                                        img.getStreamMetadata(),
                                        img.getImageMetadata());
			}
			case(VERT_A):
			{
			/*	ParameterBlock pbh = crearPB(VERT_A, image, kernelgv.getTopPadding(), kernelgv.getBottomPadding());
				PlanarImage tiled = JAI.create("border",pbh);*/
				ParameterBlock pb = new ParameterBlock();
				pb.addSource(img.getImage());
				pb.add(kernelgv);
				PlanarImage tiled = JAI.create("convolve", pb, rh);
				/*pbh = crearPB(VERT_R, tiled, kernelgv.getTopPadding(), kernelgv.getBottomPadding());
				tiled = JAI.create("crop",pbh);
				pbh = crearPB(MOV_V,tiled, kernelgv.getTopPadding(), kernelgv.getBottomPadding());			    
				tiled = JAI.create("translate",pbh);*/
				return new GeoImg (img.getData(),
                                        tiled,
                                        new File(""),
                                        img.getStreamMetadata(),
                                        img.getImageMetadata());
			}
		}
	}
return null;
	
	}

    /**
     * Obtiene la imagen multiespectral degradada durante el proceso de fusión 
     * À trous
     * @return La imagen multiespectral degradada 
     */
public GeoImg getDegradada() {

	return MULTIdegradada;
}

    /**
     * Obtiene la imagen multiespectral cargada durante el proceso de fusión À 
     * trous
     * @return La imagen multiespectral
     */
public GeoImg getCargada() {

	return MULTIcargada;
}
}

