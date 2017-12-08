package es.upm.fi.gtd.first;

import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.IDFTDescriptor;

import com.sun.media.jai.codecimpl.util.DataBufferFloat;

/**
 * Implementa la funcionalidad necesaria para realizar fusiones por el método 
 * de Contourlets
 */

public class Contour {
	
	
	private float[] C;
	private float[] S;
	private int[] bitrev;
	private float[] tempArr;
	private int maxN;

	Atrous principal;
	
	
    /**
     * Clase que implementa funciones de apoyo para la transformada de Fourier
     * @param princ Objeto de estado y GUI principal de la aplicación
     */
    public Contour(Atrous princ)
	{
		C=null;
		S=null;
		bitrev=null;

		principal = princ;
		
	}
    /**
     * Transformada de Fourier directa de una imagen o imágenes
     * @param images_pan La imagen o imágenes de entrada
     * @return La transformada de Fourier resultado
     */
    public GeoImg fourier(List<GeoImg> images_pan)
	{
		PlanarImage transformada = transform (false, images_pan.get(0).getImage());

		GeoImg gimg = new GeoImg(null,transformada,null, null,null);
		return gimg;	
	}
	
    /** Returns true of this FHT contains a square image with a width that is a power of two.
     * @param width Anchura
     * @param height Altura
     * @return true si la imagen cargada es cuadrada y su anchura es potencia de 2
     * false en caso contrario
     */
	public boolean powerOf2Size(int width, int height) {
		int i=2;
		while(i<width) i *= 2;
		return i==width && width==height;
	}
	
	void makeSinCosTables(int maxN) {
		int n = maxN/4;
		C = new float[n];
		S = new float[n];
		double theta = 0.0;
		double dTheta = 2.0 * Math.PI/maxN;
		for (int i=0; i<n; i++) {
			C[i] = (float)Math.cos(theta);
			S[i] = (float)Math.sin(theta);
			theta += dTheta;
		}
	}
	
	
	private boolean btst (int  x, int bit) {
		//int mask = 1;
		return ((x & (1<<bit)) != 0);
	}
	
	int log2 (int x) {
		int count = 15;
		while (!btst(x, count))
			count--;
		return count;
	}
	
	private int bitRevX (int  x, int bitlen) {
		int  temp = 0;
		for (int i=0; i<=bitlen; i++)
			if ((x & (1<<i)) !=0)
				temp  |= (1<<(bitlen-i-1));
		return temp & 0x0000ffff;
	}
	
	void makeBitReverseTable(int maxN) {
		bitrev = new int[maxN];
		int nLog2 = log2(maxN);
		for (int i=0; i<maxN; i++)
			bitrev[i] = bitRevX(i, nLog2);
	}
	
	void BitRevRArr (float[] x, int base, int bitlen, int maxN) {
		for (int i=0; i<maxN; i++)
			tempArr[i] = x[base+bitrev[i]];
		for (int i=0; i<maxN; i++)
			x[base+i] = tempArr[i];
	}
	
    /** Performs an optimized 1D FHT.
     * @param x Datos de imagen como array de píxeles en coma flotante
     * @param base Base de la transformada de Fourier
     * @param inverse true si es la transformada inversa
     * @param maxN Número de datos en el array de píxeles
     */
	public void dfht3 (float[] x, int base, boolean inverse, int maxN) {
		int i, stage, gpNum, gpSize, numGps, Nlog2;
		int bfNum, numBfs;
		int Ad0, Ad1, Ad2, Ad3, Ad4, CSAd;
		float rt1, rt2, rt3, rt4;

		Nlog2 = log2(maxN);
		BitRevRArr(x, base, Nlog2, maxN);	//bitReverse the input array
		gpSize = 2;     //first & second stages - do radix 4 butterflies once thru
		numGps = maxN / 4;
		for (gpNum=0; gpNum<numGps; gpNum++)  {
			Ad1 = gpNum * 4;
			Ad2 = Ad1 + 1;
			Ad3 = Ad1 + gpSize;
			Ad4 = Ad2 + gpSize;
			rt1 = x[base+Ad1] + x[base+Ad2];   // a + b
			rt2 = x[base+Ad1] - x[base+Ad2];   // a - b
			rt3 = x[base+Ad3] + x[base+Ad4];   // c + d
			rt4 = x[base+Ad3] - x[base+Ad4];   // c - d
			x[base+Ad1] = rt1 + rt3;      // a + b + (c + d)
			x[base+Ad2] = rt2 + rt4;      // a - b + (c - d)
			x[base+Ad3] = rt1 - rt3;      // a + b - (c + d)
			x[base+Ad4] = rt2 - rt4;      // a - b - (c - d)
		 }

		if (Nlog2 > 2) {
			 // third + stages computed here
			gpSize = 4;
			numBfs = 2;
			numGps = numGps / 2;
			//IJ.write("FFT: dfht3 "+Nlog2+" "+numGps+" "+numBfs);
			for (stage=2; stage<Nlog2; stage++) {
				for (gpNum=0; gpNum<numGps; gpNum++) {
					Ad0 = gpNum * gpSize * 2;
					Ad1 = Ad0;     // 1st butterfly is different from others - no mults needed
					Ad2 = Ad1 + gpSize;
					Ad3 = Ad1 + gpSize / 2;
					Ad4 = Ad3 + gpSize;
					rt1 = x[base+Ad1];
					x[base+Ad1] = x[base+Ad1] + x[base+Ad2];
					x[base+Ad2] = rt1 - x[base+Ad2];
					rt1 = x[base+Ad3];
					x[base+Ad3] = x[base+Ad3] + x[base+Ad4];
					x[base+Ad4] = rt1 - x[base+Ad4];
					for (bfNum=1; bfNum<numBfs; bfNum++) {
					// subsequent BF's dealt with together
						Ad1 = bfNum + Ad0;
						Ad2 = Ad1 + gpSize;
						Ad3 = gpSize - bfNum + Ad0;
						Ad4 = Ad3 + gpSize;

						CSAd = bfNum * numGps;
						rt1 = x[base+Ad2] * C[CSAd] + x[base+Ad4] * S[CSAd];
						rt2 = x[base+Ad4] * C[CSAd] - x[base+Ad2] * S[CSAd];

						x[base+Ad2] = x[base+Ad1] - rt1;
						x[base+Ad1] = x[base+Ad1] + rt1;
						x[base+Ad4] = x[base+Ad3] + rt2;
						x[base+Ad3] = x[base+Ad3] - rt2;

					} /* end bfNum loop */
				} /* end gpNum loop */
				gpSize *= 2;
				numBfs *= 2;
				numGps = numGps / 2;
			} /* end for all stages */
		} /* end if Nlog2 > 2 */

		if (inverse)  {
			for (i=0; i<maxN; i++)
			x[base+i] = x[base+i] / maxN;
		}
	}
	
	void transposeR (float[] x, int maxN) {
		int   r, c;
		float  rTemp;

		for (r=0; r<maxN; r++)  {
			for (c=r; c<maxN; c++) {
				if (r != c)  {
					rTemp = x[r*maxN + c];
					x[r*maxN + c] = x[c*maxN + r];
					x[c*maxN + r] = rTemp;
				}
			}
		}
	}
	
    /** Performs a 2D FHT (Fast Hartley Transform).
     * @param x Datos de imagen como array de píxeles en coma flotante
     * @param maxN Longitud del array de píxeles
     * @param inverse true si es transformada directa, false en caso contrario
     */
	public void rc2DFHT(float[] x, boolean inverse, int maxN) {
		//IJ.write("FFT: rc2DFHT (row-column Fast Hartley Transform)");
		for (int row=0; row<maxN; row++)
			dfht3(x, row*maxN, inverse, maxN);		
		principal.setProgreso(40);
		transposeR(x, maxN);
		principal.setProgreso(50);
		for (int row=0; row<maxN; row++)		
			dfht3(x, row*maxN, inverse, maxN);
		principal.setProgreso(70);
		transposeR(x, maxN);
		principal.setProgreso(80);

		int mRow, mCol;
		float A,B,C2,D,E;
		for (int row=0; row<=maxN/2; row++) { // Now calculate actual Hartley transform
			for (int col=0; col<=maxN/2; col++) {
				mRow = (maxN - row) % maxN;
				mCol = (maxN - col)  % maxN;
				A = x[row * maxN + col];	//  see Bracewell, 'Fast 2D Hartley Transf.' IEEE Procs. 9/86
				B = x[mRow * maxN + col];
				C2 = x[row * maxN + mCol];
				D = x[mRow * maxN + mCol];
				E = ((A + D) - (B + C2)) / 2;
				x[row * maxN + col] = A - E;
				x[mRow * maxN + col] = B + E;
				x[row * maxN + mCol] = C2 + E;
				x[mRow * maxN + mCol] = D - E;
			}
		}
		principal.setProgreso(95);
	}
	
	PlanarImage transform(boolean inverse, PlanarImage image) {
		//IJ.log("transform: "+maxN+" "+inverse);
		int width,height;
		width= image.getWidth();
		height= image.getHeight();
		if (!powerOf2Size(width,height))
			throw new  IllegalArgumentException("Image not power of 2 size or not square: "+width+"x"+height);
		maxN = width;
		if (S==null) {
			makeSinCosTables(maxN);
			makeBitReverseTable(maxN);
			tempArr = new float[maxN];
		}
		float[] fht = new float[width*height];
		image.getData().getPixels(0,0,width,height,fht);

	 	rc2DFHT(fht, inverse, maxN);
	 	//boolean isFrequencyDomain = !inverse;
	 	DataBufferFloat dbuffer = new DataBufferFloat(fht,fht.length);
		SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_FLOAT, width, height, 1);
		ColorModel colormod =  PlanarImage.createColorModel(samplemod);
		Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
		TiledImage tiled = new TiledImage(0,0,width, height, 0, 0, samplemod, colormod);
		tiled.setData(raster);
		return tiled;
		
	}
    /**
     * Calcula una transformada de Fourier a partir de la definición de la
     * transformada
     * @param images_pan Imagen o imágenes de entrada
     * @return Transformada de Fourier de la entrada
     */
    public static GeoImg fourierAMano(List<GeoImg> images_pan)
{
	PlanarImage resultado=null;
	ParameterBlock fourierpb = new ParameterBlock();
	fourierpb.addSource(images_pan.get(0));
	resultado = JAI.create("DFT",fourierpb);
	
	// Procesar los pares complejos
    SampleModel sm = (ComponentSampleModel) resultado.getSampleModel();
    Raster ras = resultado.getData();
    DataBuffer db = sm.createDataBuffer();
    db = ras.getDataBuffer();
    int anchura = sm.getWidth();
    int altura = sm.getHeight();
    int [] salida = new int[altura*anchura];
    float [] pixel = new float[sm.getNumDataElements()];
    int valor;
	for (int i = 0; i < anchura; i++)
	{// Recorriendo las columnas
		for (int j = 0; j < altura;j++)
		{
			pixel = (float []) sm.getDataElements(i,j,null,db);
			valor = (int) Math.sqrt(Math.pow(pixel[0], 2)+Math.pow(pixel[1],2));
			salida[i*j]=valor;
		}		
	}
	DataBufferInt dbuffer = new DataBufferInt(salida,salida.length);
	SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_USHORT, anchura, altura, 1);
	ColorModel colormod =  PlanarImage.createColorModel(samplemod);
	Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
	TiledImage tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
	tiled.setData(raster);
	return null;
}

/**
 *
 * @param images_pan
 * @return
 */
public static GeoImg invfourier(List<GeoImg> images_pan) {
	//PlanarImage resultado=null;
	ParameterBlock fourierpb = new ParameterBlock();
	fourierpb.addSource(images_pan.get(0));
	fourierpb.add(IDFTDescriptor.SCALING_DIMENSIONS);
	fourierpb.add(IDFTDescriptor.REAL_TO_COMPLEX);
	//resultado = JAI.create("IDFT",fourierpb);
	return null;
}
}
