package es.upm.fi.gtd.first;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import javax.media.jai.BorderExtender;
import javax.media.jai.Histogram;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import es.upm.fi.gtd.first.jiu.MemoryGray32Image;
import es.upm.fi.gtd.first.jiu.MissingParameterException;
import es.upm.fi.gtd.first.jiu.Resample;
import es.upm.fi.gtd.first.jiu.WrongParameterException;


/**
 * Implementa el cálculo de los valores de ERGAS espacial y espectral para una fusión dada.  También obtiene el mejor valor de ponderación para una fusión, dado un número de muestras sobre los que interpolar las curvas de ERGAS.
 * @author  Alvar García del Río
 */

public class Ergas {
	/**
	 * Variables en las que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static int wvm;
	/**
	 * Variables en las que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static int niv_MULTI;
	/**
	 * Variables en las que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static int niv_PAN;
	/**
	 * Variables en las que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static int interpol;
	/**
	 * Variables en las que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static int precis;
	/**
	 * Variable en la que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static boolean separado;
	/**
	 * Variable en la que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static double ponder;
	/**
	 * Variable en la que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static File [] files;
	/**
	 * Variable en la que Atrous almacena los valores de fusión al ser llamado desde muestreoErgas
	 */
	private static List<GeoImg> images_pan;
	/**
         * Lista de imágenes multiespectrales
         */
	private static List<GeoImg> images_multi;
	/**
	 * Panel en el que se pintan las curvas de ERGAS espacial y ERGAS espectral
	 */
	public Panelon b = null;
	/**
	 * Objeto principal, necesario para acceder a sus variables de estado
	 */
	private Atrous principal;
	/**
	 * Variable que le va a indicar al objeto si se desea una fusión para calcular el Ergas, o
	 * si una de las imágenes ya está fusionada 
	 */
	private boolean yafus;
        /**
         * Ratio L/H entre las resoluciones espaciales de pancromática y 
         *  multiespectral
         */
	private double ratio;

    private double minPond;

    private double maxPond;
    
    ParseXML ic;
	
	
	
    /**
     * Constructor
     * @param principal2 El objeto principal de la aplicación
     */
	public Ergas(Atrous principal2)
	{
		principal = principal2;	
                ic = principal.getIC();
	}
	
	
	
    
    
	
	/**
	 * Modifica los valores de los atributos de una instancia de la clase
	 * 
	 * @param wavelet Nuevo valor para wvm
	 * @param niv1 Nuevo valor para niv_MULTI
	 * @param niv2 Nuevo valor para niv_PAN
	 * @param interp Nuevo valor para interpol
	 * @param prec Nuevo valor para precis
	 * @param images_pan2 Nuevo valor para images_pan
	 * @param images_multi2 Nuevo valor para images_multi
	 * @param ponderacion Nuevo valor para ponder
         * @param sep Nuevo valor para sep
         * @param yaFus Nuevo valor para yafus
         * @param rat Nuevo valor para ratio
	 */
	
	public void setAll (int wavelet, int niv1, int niv2, int interp, int prec, 
                List<GeoImg> images_pan2, List<GeoImg> images_multi2, 
                double ponderacion, boolean sep, boolean yaFus, double rat)
	{
		wvm = wavelet;
		niv_MULTI = niv2;
		niv_PAN = niv1;
		interpol = interp;
		precis = prec;

		ponder = ponderacion;
		images_pan = images_pan2;
		images_multi = images_multi2;
		separado = sep;
		yafus = yaFus;
		ratio = rat;
	}

	/**
	 * Obtiene varios valores relacionados con los valores ERGAS de una fusión.
	 * Si muestras = 1, devuelve en resultado[0] ERGAS espacial y en resultado[1] ERGAS espectral
	 * Si muestras > 1, devuelve en resultado[0] el valor óptimo medio de ponderación para esta fusión
	 * 
	 * @param muestras Número de muestras sobre las que interpolar las curvas de ERGAS	 
	 * @param t Frame principal de Atrous, necesario para mostrar las curvas de ERGAS
     * @param fast true si se desea usar el algoritmo con menos operaciones,
     * false en caso contrario
     * @return Diversos valores, ver la descripción
	 */
	
	public double [] muestreoErgas (int muestras, JFrame t, boolean fast)
	{				
		// Se puede consultar el archivo XML con resultados para ahorrarnos el cálculo
		//int res = consultaXML(muestras);
		/*if (res != 0)
		{
			return null;
		}*/
		// Casos:
		// 1: Si se quiere calcular ERGAS 
		// 1.1: Si se quiere el valor de ERGAS por separado (devuelve un array doble de espaciales y espectrales)


		

		double [] result = null;		
		
		if (yafus == true)
		{
			result = calcularERGASYaFus();
			return result;
		}
		
		if (muestras == 1)
		{
			result = calcularERGAS();
			

		}
		else
		{ 
                    if (fast)
                    {
                        result = calcularPondRapida(muestras, t);
                    }
                    else
                    {
			result = calcularPond(muestras, t);
                    }
			
			
		}
		return result;
	}
				
		

	
    /**
     * Calcula el ERGAS entre una imagen ya fusionada y una imagen pancromática
     * o multiespectral
     * @return Valores de ERGAS
     */
	private double[] calcularERGASYaFus() {
		
		int longitud = images_multi.size();
		double [] ergasEspa = new double[longitud];
		double [] ergasEspe = new double[longitud];
		
		PlanarImage fus = images_pan.get(0).getImage();
		int fusWidth = fus.getWidth();
		int fusHeight = fus.getHeight();
		int tamFus = fusWidth*fusHeight;
		double mediaPan = 0.0;
		boolean pasosIntermedios = false;
		for (int i = 0; i < longitud; i++ )
		{

			GeoImg comp = images_multi.get(i);	

			

			
			comp = FusionAux.expandirRango(images_multi.get(i));
			
			if (pasosIntermedios)
				JAI.create("imagewrite",comp.getImage(),"ORIGexpandida.tif","TIFF");

			int compWidth = comp.getImage().getWidth();
			int compHeight = comp.getImage().getHeight();
			int tamComp = compWidth*compHeight;
			if (tamFus > tamComp)
			{
				if (false)
				{
					// Se trata de FUS <-> MULTI, Ajusto el tamaño
					float factor_escala_hor = fusWidth/compWidth;
					float factor_escala_vert = fusHeight/compHeight;
					ParameterBlock pb = new ParameterBlock();
					pb.addSource(comp.getImage());
					pb.add(factor_escala_hor);
					pb.add(factor_escala_vert);
					pb.add(0.0F);
					pb.add(0.0F);
					pb.add(new InterpolationBilinear());
					RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
							BorderExtender.createInstance(BorderExtender.BORDER_COPY));
					comp.setImage(JAI.create("scale",pb,rh));
					if (pasosIntermedios)
						JAI.create("imagewrite",comp.getImage(),"ORIGexpandidayescalada.tif","TIFF");
				}
				else
				{// Interpolación de Lanczos
					MemoryGray32Image mim = new MemoryGray32Image(compWidth,compHeight);
					int [] samples = new int [compWidth*compHeight]; 
					comp.getImage().getData().getPixels(0, 0, compWidth, compHeight, samples);
					mim.putSamples(0, 0, 0, compWidth, compHeight, samples, 0);
					Resample res = new Resample();
					res.setInputImage(mim);
					res.setSize(fusWidth, fusHeight);
					res.setFilter(Resample.FILTER_TYPE_LANCZOS3);
					
					try {
						res.process();
					} catch (MissingParameterException e) {
						
						e.printStackTrace();
					} catch (WrongParameterException e) {
						
						e.printStackTrace();
					}
					MemoryGray32Image mom = (MemoryGray32Image) res.getOutputImage();
					samples = new int[mom.getWidth()*mom.getHeight()];
					mom.getSamples(0, 0, 0, mom.getWidth(), mom.getHeight(), samples, 0);
					DataBufferInt dbuffer = new DataBufferInt(samples, samples.length);
					SampleModel samplemod = RasterFactory.createBandedSampleModel(
							DataBuffer.TYPE_INT, mom.getWidth(), mom.getHeight(), 1);
					ColorModel colormod = PlanarImage.createColorModel(samplemod);
					Raster raster = RasterFactory.createWritableRaster(samplemod,
							dbuffer, new Point(0, 0));
					TiledImage tiled = new TiledImage(0, 0, mom.getWidth(), mom.getHeight(), 0, 0,
							samplemod, colormod);
					tiled.setData(raster);
					if (pasosIntermedios)
						JAI.create("imagewrite",tiled,"ORIGexpandidayescalada.tif","TIFF");
					comp.setImage(tiled);
					
					
					
				}
				
			}
			else
			{ // Si se trata de ERGAS espacial
				
				
				if (true)
				{
					double [] media = (double []) JAI.create("mean",comp.getImage()).getProperty("mean");
					mediaPan = media[0];
				}
				else
				{
					mediaPan = media(comp.getImage());
				}
				//comp = Filtros.igualarHistograma(images_pan.get(0), comp);
				if (pasosIntermedios)
				JAI.create("imagewrite", comp.getImage(),"PANajustada.tif","TIFF");
				
			}
			
			
			if (comp == null)
			{
				System.out.println("Comp es null");
			}			
			PlanarImage [] hack = {comp.getImage()};
			PlanarImage [] hack2 = {fus};


			// Como la PAN es siempre igual, calcular la media fuera
			// Para no liar al usuario, voy a poner a 0 el ERGAS no 
			// correspondiente
			if (mediaPan!=0.0)
			{ // FUS <-> PAN
				ergasEspa[i] = ergasEspacial (hack,mediaPan,ratio,hack2);
				ergasEspe[i] = 0.0;
			}
			else
			{ // FUS <-> MULTI
				ergasEspa[i] = 0.0;
				ergasEspe[i] = ergasEspectral (hack,ratio,hack2);
			}
			
			
		}
		double [] result = new double [longitud];
		result = new double[(longitud)*2];
		for (int p = 0; p < (longitud); p++)
		{
			result[2*p]=ergasEspa[p];
			result[2*p+1]=ergasEspe[p];
		}
		return result;
			
	}

	


        
        private double [] calcularPondRapida(int muestras, JFrame t)
        {
            RunnableFuture update = principal.getUpdate();
            double [] arrayAlfas = null;
            double [] arrayMin = null;
            Fusion f = new Fusion(ic);
            GeoImg pan = images_pan.get(0);	
            int longitud = images_multi.size();
            GeoImg [] melt = new GeoImg[muestras+1];		
            GeoImg [] MULTI = new GeoImg[muestras+1];
		
	    double[][] ergasEspa = new double[longitud][muestras + 1];
            double[][] ergasEspe = new double[longitud][muestras + 1];
            double[] xvalues = new double[muestras + 1];
            boolean[] SPA_sobre_SPE = new boolean[longitud];
            Arrays.fill(SPA_sobre_SPE, true);
            double[] intersecciones = new double[longitud];
            Arrays.fill(intersecciones, 0.0);
            double paso = (maxPond - minPond) / muestras;
            for (int j = 0; j < longitud; j++)
		{
			
                        for (int i = 0; i < (muestras+1); i++)
			
			{
                            double fl = minPond+i*paso;
                            
				xvalues[i] = fl;
                                
				melt[i] =  f.atrous(images_pan.get(0), images_multi.get(j), wvm,niv_PAN,niv_MULTI,interpol,precis,fl);
				System.out.println("Valor de ponderación:"+fl);
				//Hay que hacerlo igual que al fusionar
				//pan = FusionAux.expandirRango(pan);


				//MULTIdeg[i] =  f.getDegradada(); // La necesitamos para ergasEspectral
				//MULTI[i] = f.getCargada(); // La necesitamos para H, no nos vale MULTIdeg,porque tam(MULTIdeg)=tam(PAN)
				//MULTI[i] = Fusion.expandirRango(MULTI[i]);
				MULTI[i] = images_multi.get(j);
				//MULTI[i] = FusionAux.expandirRango(MULTI[i]);
				


				ergasEspa[j][i] = ergasEspacial (new PlanarImage [] {pan.getImage()},ratio,new PlanarImage [] {melt[i].getImage()});
				// Issue nº 13: Cambiar Multi degradada por multi original
				ergasEspe[j][i] = ergasEspectral (new PlanarImage [] {MULTI[i].getImage()},ratio, new PlanarImage[] {melt[i].getImage()});
				System.out.println("ERGAS Espacial, banda: "+j+" , paso: "+i+" = "+ergasEspa[j][i]);
				System.out.println("ERGAS Espectral, banda: "+j+" , paso: "+i+" = "+ergasEspe[j][i]);							

				//calcularProgreso(j,longitud,muestras,i);
                                principal.setProgreso(((i+1)/(muestras+1))*100);
				update.run();
				
			}
                        
		}
            AreaTrabajo dpanel = principal.getDPane();
                

		arrayMin = new double [longitud];
		for (int s = 0; s < longitud; s++)
		{
			double [] espaInter = ergasEspa[s];
			double [] espeInter = ergasEspe[s];
			/* Usemos JFreeChart*/
			b = new Panelon(principal);
			b.setData(xvalues,espaInter,espeInter);
			arrayMin[s] = b.getMinimo();
			intersecciones[s] = b.getInter();
			double [] valoresEspacial = b.getValoresEspacial();
			double [] valoresEspectral = b.getValoresEspectral();
			double [] valoresX = b.getValoresX();
			
			// Hay dos opciones, o usar los valores de ERGAS (a pelo) o usar los
			// interpolados
			DefaultXYDataset ysds = new DefaultXYDataset();
			/*double [][] datos = {xvalues,ergasEspa[s]};
			ysds.addSeries("ERGAS Espacial", datos);
			double [][] datos2 = {xvalues,ergasEspe[s]};
			ysds.addSeries("ERGAS Espectral", datos2);*/
			double [][] datos = {valoresX,valoresEspacial};
			ysds.addSeries(ic.buscar(147), datos); // Tag 147
			double [][] datos2 = {valoresX,valoresEspectral};
			ysds.addSeries(ic.buscar(148), datos2); // Tag 148
			JFreeChart jfc = ChartFactory.createXYLineChart(
                                "ERGAS",
                                ic.buscar(149),
                                "ERGAS",
                                ysds,
                                PlotOrientation.VERTICAL,
                                true,
                                true,
                                false); // Tag 149
			ChartPanel cp = new ChartPanel(jfc);

			//System.out.println ("MINIMO, banda "+s+", = "+arrayMin[s]);
			JInternalFrame jif = 
                                new JInternalFrame(
                                    ic.buscar(150)
                                    +(s+1),
                                    true,
                                    true,
                                    true,
                                    true); // Tag 150
			//jif.getContentPane().add(b);
                        jif.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                        jif.getContentPane().add(cp);
                        if (dpanel == null) {
                            dpanel = new AreaTrabajo(principal);
                            
                            
                        }
                        jif.addInternalFrameListener(dpanel);
                        dpanel.add(jif,s);
			jif.setVisible(true);
			jif.setBounds(20*(s%10), 20*(s%10), 500, 600);
                        jif.pack();

		}
		dpanel.setVisible(true);
		t.add(dpanel,BorderLayout.CENTER);
                t.setExtendedState(t.getExtendedState()|JFrame.MAXIMIZED_BOTH);                
		t.setVisible(true);
//                dpanel.addLista(null);
		
		t.repaint();

		arrayAlfas = new double [longitud];
		for (int u = 0; u < longitud; u++)
		{
			if (intersecciones[u]==0.0)
			{ // No hay intersección
				arrayAlfas[u] = arrayMin[u];
			}
			else
			{ // Hay intersección
				System.out.println("Valores, banda: "+u+" , intersección: "+intersecciones[u]+" , minimo "+arrayMin[u]+"\n");
				arrayAlfas[u] = Math.min(intersecciones[u],arrayMin[u]);
			}
		}
		return arrayAlfas;            
        }

    /**
     * Calcula la ponderación óptima fusionando con varios valores e interpolando
     * entre ellos
     * @param muestras Número de valores para los que fusionar
     * @param t JFrame principal de la aplicación
     * @return Valores de la ponderación, depende de la variable separado
     */
	private double [] calcularPond(int muestras, JFrame t) {
		RunnableFuture update = principal.getUpdate();
		double [] arrayAlfas = null;
		double [] arrayMin = null;
		Fusion f = new Fusion(ic);
		GeoImg pan = images_pan.get(0);	
		int longitud = images_multi.size();
		GeoImg [] melt = new GeoImg[muestras+1];
		
		GeoImg [] MULTI = new GeoImg[muestras+1];
		
		double [][] ergasEspa = new double[longitud][muestras+1];
		double [][] ergasEspe = new double[longitud][muestras+1];
		double [] xvalues = new double[muestras+1];
		boolean [] SPA_sobre_SPE = new boolean[longitud];
		Arrays.fill(SPA_sobre_SPE,true);
		double [] intersecciones = new double [longitud];
		Arrays.fill(intersecciones,0.0d);
                double paso = (maxPond-minPond)/muestras;
                System.out.println("El tamaño de paso es: "+paso);
		for (int j = 0; j < longitud; j++)
		{
			
                        for (int i = 0; i < (muestras+1); i++)
			
			{
                            double fl = minPond+i*paso;
                            
				xvalues[i] = fl;
                                
				melt[i] =  f.atrous(images_pan.get(0), images_multi.get(j), wvm,niv_PAN,niv_MULTI,interpol,precis,fl);
				System.out.println("Valor de ponderación:"+fl);
				//Hay que hacerlo igual que al fusionar
				pan = FusionAux.expandirRango(pan);
                                // Reducir a 8 bits
                                //melt[i].setImage(FusionAux.reducirRango(melt[i].getImage(), 0, 255));

				//MULTIdeg[i] =  f.getDegradada(); // La necesitamos para ergasEspectral
				//MULTI[i] = f.getCargada(); // La necesitamos para H, no nos vale MULTIdeg,porque tam(MULTIdeg)=tam(PAN)
				//MULTI[i] = Fusion.expandirRango(MULTI[i]);
				MULTI[i] = images_multi.get(j);
				MULTI[i] = FusionAux.expandirRango(MULTI[i]);
				
                                

				ergasEspa[j][i] = ergasEspacial (new PlanarImage [] {pan.getImage()},ratio,new PlanarImage [] {melt[i].getImage()});
				// Issue nº 13: Cambiar Multi degradada por multi original
				ergasEspe[j][i] = ergasEspectral (new PlanarImage [] {MULTI[i].getImage()},ratio, new PlanarImage[] {melt[i].getImage()});
				System.out.println("ERGAS Espacial, banda: "+j+" , paso: "+i+" = "+ergasEspa[j][i]);
				System.out.println("ERGAS Espectral, banda: "+j+" , paso: "+i+" = "+ergasEspe[j][i]);							

				//calcularProgreso(j,longitud,muestras,i);
                                principal.setProgreso(((i+1)/(muestras+1))*100);
				update.run();
				
			}
                        
		}

		AreaTrabajo dpanel = principal.getDPane();
                

		arrayMin = new double [longitud];
		for (int s = 0; s < longitud; s++)
		{
			double [] espaInter = ergasEspa[s];
			double [] espeInter = ergasEspe[s];
			/* Usemos JFreeChart*/
			b = new Panelon(principal);
			b.setData(xvalues,espaInter,espeInter);
			arrayMin[s] = b.getMinimo();
			intersecciones[s] = b.getInter();
			double [] valoresEspacial = b.getValoresEspacial();
			double [] valoresEspectral = b.getValoresEspectral();
			double [] valoresX = b.getValoresX();
			
			// Hay dos opciones, o usar los valores de ERGAS (a pelo) o usar los
			// interpolados
			DefaultXYDataset ysds = new DefaultXYDataset();
			/*double [][] datos = {xvalues,ergasEspa[s]};
			ysds.addSeries("ERGAS Espacial", datos);
			double [][] datos2 = {xvalues,ergasEspe[s]};
			ysds.addSeries("ERGAS Espectral", datos2);*/
			double [][] datos = {valoresX,valoresEspacial};
			ysds.addSeries(ic.buscar(147), datos); // Tag 147
			double [][] datos2 = {valoresX,valoresEspectral};
			ysds.addSeries(ic.buscar(148), datos2); // Tag 148
			JFreeChart jfc = ChartFactory.createXYLineChart(
                                "ERGAS",
                                ic.buscar(149),
                                "ERGAS",
                                ysds,
                                PlotOrientation.VERTICAL,
                                true,
                                true,
                                false); // Tag 149
			ChartPanel cp = new ChartPanel(jfc);

			//System.out.println ("MINIMO, banda "+s+", = "+arrayMin[s]);
			JInternalFrame jif = 
                                new JInternalFrame(
                                    ic.buscar(150)
                                    +(s+1),
                                    true,
                                    true,
                                    true,
                                    true); // Tag 150
			//jif.getContentPane().add(b);
                        jif.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                        jif.getContentPane().add(cp);
                        if (dpanel == null) {
                            dpanel = new AreaTrabajo(principal);
                            
                            
                        }
                        jif.addInternalFrameListener(dpanel);
                        dpanel.add(jif,s);
			jif.setVisible(true);
			jif.setBounds(20*(s%10), 20*(s%10), 500, 600);
                        jif.pack();

		}
		dpanel.setVisible(true);
		t.add(dpanel,BorderLayout.CENTER);
                t.setExtendedState(t.getExtendedState()|JFrame.MAXIMIZED_BOTH);                
		t.setVisible(true);
//                dpanel.addLista(null);
		
		t.repaint();

		arrayAlfas = new double [longitud];
		for (int u = 0; u < longitud; u++)
		{
			if (intersecciones[u]==0.0)
			{ // No hay intersección
				arrayAlfas[u] = arrayMin[u];
			}
			else
			{ // Hay intersección
				System.out.println("Valores, banda: "+u+" , intersección: "+intersecciones[u]+" , minimo "+arrayMin[u]+"\n");
				arrayAlfas[u] = Math.min(intersecciones[u],arrayMin[u]);
			}
		}
		return arrayAlfas;


	}
		
    /**
     * Auxiliar que calcula el progreso de la barra de progreso
     * @param j Iteración de banda
     * @param longitud Máximo de iteraciones
     * @param muestras Número de valores para los que fusionar
     * @param i Iteración de muestra
     */
	private void calcularProgreso (int j, int longitud, int muestras, int i)
	{
		if (j == (longitud-1))
		{					
			principal.setProgreso(100);
		}
		else
		{
			int pasoBanda = (100/(longitud-1));
			int progresoBanda = pasoBanda*(j-1);
			int progresoMuestra = (pasoBanda/muestras)*(i+1);
			int progresoTotal = progresoBanda + progresoMuestra;
			principal.setProgreso(progresoTotal);
		}
	}


    /**
     * Calcula el ERGAS fusionando las imágenes que se le han pasado mediante
     * setAll
     * @see setAll
     * @return Los valores de ERGAS espacial y espectral
     */
	private double[] calcularERGAS() {

		RunnableFuture update = principal.getUpdate();
		Fusion f = new Fusion(ic);
		//Filtros a = new Filtros();
		GeoTiffIIOMetadataAdapter ti = new GeoTiffIIOMetadataAdapter(files[0]);
		GeoImg PAN = new GeoImg(
                            Info.crearGeoData(ti),
                            JAI.create("imageread",files[0].getPath()), 
                            files[0], 
                            Info.crearStreamData(ti),
                            Info.crearImageData(ti));
		PAN = FusionAux.expandirRango(PAN);
		
		//int l = PAN.getHeight();
		int longitud = images_multi.size();

		File[] files_fus;
		GeoImg [] melt = new GeoImg[longitud-1];
		GeoImg [] MULTIdeg = new GeoImg[longitud-1];
		GeoImg [] MULTI = new GeoImg[longitud-1];
		GeoImg [] PANeq = new GeoImg[longitud-1];
		double [] ergasEspa = new double[longitud-1];
		double [] ergasEspe = new double[longitud-1];
		

		
		for (int j=1; j<longitud; j++)
		{
			files_fus = new File[2];
			files_fus[0] = files[0];

			files_fus[1] = files[j];
			System.out.println("Los valores que se le pasan a atrous son, wvm: "+wvm+" niv_PAN: "+niv_PAN+" niv_MULTI: "+niv_MULTI+" interpol: "+interpol+" precis: "+precis+" ponder: "+ponder);
			melt[j-1] =  f.atrous(images_pan.get(0), images_multi.get(j), wvm,niv_PAN,niv_MULTI,interpol,precis,ponder);

			
			MULTIdeg[j-1] = f.MULTIdegradada;
			MULTI[j-1] = f.MULTIcargada;
			
			PANeq[j-1] = Filtros.igualarHistograma(melt[j-1],PAN); //Servirá para calcular el ergas espacial
                                                

                        PlanarImage [] MultiDegradada = new PlanarImage[1];
                        PlanarImage [] MultiOriginal = new PlanarImage[1];
                        if (true)
                        {
                            MultiDegradada[0] = MULTIdeg[j-1].getImage();
                        }
                        else
                        {
                            MultiOriginal[0] = MULTI[j-1].getImage();
                        }
			PlanarImage [] hack2 = {melt[j-1].getImage()};
			PlanarImage [] hack3 = {PANeq[j-1].getImage()};

			ergasEspa[j-1] = ergasEspacial (hack3,ratio,hack2);
			ergasEspe[j-1] = ergasEspectral (MultiDegradada,ratio,hack2);
			

			if (j == (longitud-1))
			{
				
				principal.setProgreso(100);
			}
			else
			{
				principal.setProgreso((100/(longitud-1))*(j));
			}
			update.run();

		}
		
		if (!separado)
		{
			double sumEspa = 0;
			double sumEspe = 0;
			for (double v:ergasEspa)
			{
				sumEspa += v;
			}
			for (double v:ergasEspe)
			{
				sumEspe += v;
			}
			double mediaEspa = sumEspa/ergasEspa.length;
			double mediaEspe = sumEspe/ergasEspe.length;
			double interEspa = 0;
			double interEspe = 0;
			for (double v:ergasEspa)
			{
				interEspa += Math.pow(v-mediaEspa,2);
			}
			interEspa /= ergasEspa.length;
			interEspa = Math.sqrt(interEspa);
			for (double v:ergasEspe)
			{
				interEspe += Math.pow(v-mediaEspe,2);					
			}
			interEspe /= ergasEspe.length;
			interEspe = Math.sqrt(interEspe);
			double [] result = new double[5];
			// Valores de ERGAS espacial
			result[0] = mediaEspa;
			result[1] = interEspa; // Desviación estándar ERGAS espacial
			result[2] = mediaEspe;
			result[3] = interEspe; // Desviación estándar ERGAS espectral
			result[4] = (mediaEspa+mediaEspe)/2; // Promedio
			return result;
		}
		else
		{
			double [] result = new double [longitud-1];
			result = new double[(longitud-1)*2];
			for (int p = 0; p < (longitud-1); p++)
			{
				result[2*p]=ergasEspa[p];
				result[2*p+1]=ergasEspe[p];
			}
			return result;
			
		}
	}


	

	/**
	 * Devuelve el valor espectral de una fusión à trous multibanda (o monobanda)
	 * 
	 * @param MULTI Array con las imágenes originales multiespectrales
	 * @param ratio Relación L/H
	 * @param FUS Array con las imágenes fusionadas
	 * @return ERGAS espectral de la fusión
	 */
	
	public double ergasEspectral (PlanarImage [] MULTI, double ratio, PlanarImage [] FUS)
	{
double media_MULTI, media_FUS, stdDev, rmse;
		double [] razon = new double[FUS.length];



		for (int i = 0; i < MULTI.length; i++)
		{
                    
			
		
			media_MULTI = media(MULTI[i]);
			media_FUS = media (FUS[i]);
                        
                        razon[i] = calcularRazon(FUS[i],media_MULTI,MULTI[i]);
			/*PlanarImage sub_MULTI_FUS = resta (MULTI[i],FUS[i]);
			stdDev = desviacion(sub_MULTI_FUS,media(sub_MULTI_FUS));
			rmse = media_MULTI-media_FUS;			
			rmse = Math.pow(rmse,2);			
			rmse += Math.pow(stdDev,2);			
			razon[i]= rmse / Math.pow(media_MULTI,2);*/
									
		}
			double sum= 0;
			for (double v:razon)
			{
				sum += v;
			}
			double media = 0;
			media = sum/razon.length;

		double ergas = 100*Math.pow(ratio,2)*Math.sqrt(media);																		
		return ergas;
						
	}
	
    /**
     * Auxiliar que calcula el valor razón en el cálculo de ERGAS
     * @param fus Imagen fusionada
     * @param mediaPan Media de la imagen pancromática
     * @param comp Imagen a comparar (pancromática en el caso de ERGAS espacial,
     * multiespectral en el caso de ERGAS espectral)
     * @return La razón entre las dos imágenes
     */
	private double calcularRazon(PlanarImage fus, double mediaPan, PlanarImage comp)
	{
		double rmse = 0;
		double stdDev = 0;
		double razon = 0;
                boolean pasosIntermedios = false;
		if (true)
		{ // Versión utilizando JAI
			RenderedOp rop = null;
			double [] media = null;
			double media_FUS = 0;
			RenderedImage sumaPan = null;
			ParameterBlock pb = null;
			if (false)
			{ // Versión utilizando el método de sumar la media
				rop = JAI.create("mean",fus);
				media = (double []) rop.getProperty("mean");
				media_FUS = media[0];
				System.out.println("La media de PAN (JAI) es "+mediaPan);
				System.out.println("La media de FUS (JAI) es "+media_FUS);
				double [] dif_OF = {mediaPan-media_FUS};
				if (dif_OF[0] < 0)
				{
					dif_OF[0] = -dif_OF[0];
				}
				pb = new ParameterBlock();
				pb.addSource(comp);
				pb.add(dif_OF);
				sumaPan = JAI.create("addconst",pb);
                                if (pasosIntermedios)
                                    JAI.create("imagewrite", sumaPan, "sumaPan.tif","TIFF");
			}
			else
			{ // Método usando mi función de igualar histogramas
				rop = JAI.create("mean",fus);
				media = (double []) rop.getProperty("mean");
				media_FUS = media[0];
				System.out.println("La media de FUS (Igualar Histograma) es "+media_FUS);
				sumaPan = Filtros.igualarHistograma(
                                        new GeoImg(null,fus,null,null,null),new GeoImg(null,comp,null,null,null)).getImage();
                                if (pasosIntermedios)
                                    JAI.create("imagewrite", sumaPan, "sumaPan.tif","TIFF");
			}
			rop = JAI.create("mean",sumaPan);
			media = (double []) rop.getProperty("mean");
			double mediaSumaPanJAI = media[0];
			System.out.println("La media de SumaPAN (JAI) es "+mediaSumaPanJAI);
			pb = new ParameterBlock();
			pb.addSource(sumaPan);
			pb.addSource(fus);
			RenderedImage subPanFus = JAI.create("subtract",pb);
			rop = JAI.create("histogram",subPanFus);
			Histogram hist = (Histogram) rop.getProperty("histogram");			
			double mediaSubPanFusJAI = hist.getMean()[0];
                        if (Double.isNaN(mediaSubPanFusJAI))
                        {
                            
                            
                            Dialogos.informarError(ic.buscar(152)); // Tag 152
                            mediaSubPanFusJAI =0;
                        }
                        //media = (double [])rop.getProperty("mean");
                        //double mediaSubPanFusJAI = media[0];
			System.out.println("La media de SubPanFus (JAI) es "+mediaSubPanFusJAI);
			double stdDevSubPanFus = hist.getStandardDeviation()[0];
                        if (Double.isNaN(stdDevSubPanFus))
                        {
                            
                            Dialogos.informarError(ic.buscar(152)); // Tag 152
                            stdDevSubPanFus =0;
                        }
			System.out.println("La desviación típica de SubPanFus (JAI) es "+stdDevSubPanFus);
			rmse = mediaPan-media_FUS;
			rmse = Math.pow(rmse,2);
			//rmse += Math.pow(stdDevSubPanFus,2);
                        rmse += stdDevSubPanFus;
		}
		else
		{
			// Versión utilizando mis funciones
			//PlanarImage sumaPAN = Filtros.igualarHistograma(FUS[i], PANeq[i]);
			double media_FUS = media(fus);
			double dif_OF = mediaPan-media_FUS;
			if (dif_OF < 0)
			{
				dif_OF = -dif_OF;
			}
			PlanarImage sumaPAN = sumaconst(comp,dif_OF);


			double mediaSumaPan = media(sumaPAN);

			System.out.println("La media de SumaPAN es"+mediaSumaPan);
			//PlanarImage sub_PAN_FUS = resta (PANeq[i],FUS[i]);
			PlanarImage sub_PAN_FUS = resta (sumaPAN,fus);
			

			double mediaSuBPan = media(sumaPAN);

			System.out.println("La media de SumaPAN es"+mediaSuBPan);
			stdDev = desviacion(sub_PAN_FUS,media(sub_PAN_FUS));
			System.out.println("Desviación típica: "+stdDev);
			rmse = mediaPan-media_FUS;
			rmse = Math.pow(rmse,2);
			rmse += Math.pow(stdDev,2);
		}
		razon = rmse / Math.pow(mediaPan,2);		
		return razon;
	}
	
    /**
     * Calcula el ERGAS espacial entre una imagen fusionada y una imagen 
     * pancromática
     * @param PANeq Imagen pancromática ecualizada
     * @param mediaPan Media de la imagen pancromática 
     * @param ratio Ratio L/H entre las resoluciones espaciales
     * @param FUS Imagen fusionada
     * @return ERGAS espacial
     */
	private double ergasEspacial(PlanarImage[] PANeq, double mediaPan, double ratio, PlanarImage[] FUS) {
		double[] razon = new double[FUS.length];



		for (int i = 0; i < FUS.length; i++)
		{		
			razon[i]=calcularRazon(FUS[i],mediaPan,PANeq[0]);
		}

		double sum= 0;
		for (double v:razon)
		{
			sum += v;
		}		
		double media = sum/razon.length;
		System.out.println("H/L: "+ratio);
		
		double ergas = 100*Math.pow(ratio,2)*Math.sqrt(media);
		return ergas;
	}
	
	/**
	 * Devuelve el valor del ERGAS espacial de una fusión à trous multibanda (o monobanda)
	 * 
	 * @param PANeq Imágenes PAN originales (con el histograma igualado a las MULTI)
	 * @param ratio Relación L/H
	 * @param FUS Imágenes fusionadas
	 * @return ERGAS espacial de la fusión
	 */
	
	public double ergasEspacial (PlanarImage [] PANeq, double ratio, PlanarImage [] FUS)
	{
		double[] razon = new double[FUS.length];

		RenderedOp rop;

		for (int i = 0; i < FUS.length; i++)
		{		
			rop = JAI.create("mean",PANeq[0]);
			double [] media = (double []) rop.getProperty("mean");
			double mediaPan = media[0];
			
			razon[i] = calcularRazon(FUS[i],mediaPan,PANeq[i]);
		}

		double sum= 0;
		for (double v:razon)
		{
			sum += v;
		}		
		double media = sum/razon.length;
		System.out.println("L/H: "+ratio);
		
		double ergas = 100*Math.pow(ratio,2)*Math.sqrt(media);
		return ergas;

	}





    /**
     * Calcula la desviación estándar en el conjunto de píxeles de una imagen
     * @param sub_PAN_FUS Imagen
     * @param d Media del conjunto de píxeles
     * @return La desviación estándar
     */
	private double desviacion(PlanarImage sub_PAN_FUS, double d) {
		int anchura = sub_PAN_FUS.getWidth();
		int altura = sub_PAN_FUS.getHeight();
		Raster ras = sub_PAN_FUS.getData();
		double raiz = 0;
		if (ras.getTransferType() == DataBuffer.TYPE_INT) {
			int[] pixeles = new int[anchura * altura];
			ras.getDataElements(0, 0, anchura, altura, pixeles);			
			BigDecimal acum = BigDecimal.ZERO;
			for (int i = 0; i < pixeles.length; i++) {

				acum = acum.add(new BigDecimal(Math.pow(pixeles[i] - d, 2)));

			}
			
			raiz = acum.doubleValue();
			raiz /= (double) pixeles.length;
			raiz = Math.sqrt(raiz);
		}
		else if (ras.getTransferType() == DataBuffer.TYPE_USHORT)
		{
			short[] pixeles = new short[anchura * altura];
			ras.getDataElements(0, 0, anchura, altura, pixeles);
			BigDecimal acum = BigDecimal.ZERO;
			for (int i = 0; i < pixeles.length; i++) {
				acum = acum.add(new BigDecimal(Math.pow(pixeles[i] - d, 2)));

			}
			raiz = acum.doubleValue();
			raiz /= (double) pixeles.length;
			raiz = Math.sqrt(raiz);
		}
		else if (ras.getTransferType() == DataBuffer.TYPE_BYTE)
		{
			byte[] pixeles = new byte[anchura * altura];
			ras.getDataElements(0, 0, anchura, altura, pixeles);			
			BigDecimal acum = BigDecimal.ZERO;
			for (int i = 0; i < pixeles.length; i++) {
				acum = acum.add(new BigDecimal(Math.pow(pixeles[i] - d, 2)));

			}
			raiz = acum.doubleValue();
			raiz /= (double) pixeles.length;
			raiz = Math.sqrt(raiz);
		}
		return raiz;
	}


    /**
     * Calcula la resta entre dos imágenes
     * @param Imagen origen 1
     * @param Imagen origen 2
     * @return Una imagen que es la resta entre origen1 y origen2
     */
	private PlanarImage resta(PlanarImage sumaPAN, PlanarImage FUS) {
		int anchura = sumaPAN.getWidth();
		int altura = sumaPAN.getHeight();
		Raster ras_P = sumaPAN.getData();
		Raster ras_F = FUS.getData();
		TiledImage tiled = null;
		if (ras_P.getTransferType() == DataBuffer.TYPE_INT) {
			int[] pixeles_P = new int[anchura * altura];
			ras_P.getDataElements(0, 0, anchura, altura, pixeles_P);

			int[] pixeles_F = new int[anchura * altura];
			ras_F.getDataElements(0, 0, anchura, altura, pixeles_F);
			for (int i = 0; i < pixeles_P.length; i++) {
				pixeles_P[i] -= pixeles_F[i];
			}
			DataBufferInt dbuffer = new DataBufferInt(pixeles_P,
					pixeles_P.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(
					DataBuffer.TYPE_INT, anchura, altura, 1);
			ColorModel colormod = PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod,
					dbuffer, new Point(0, 0));
			tiled = new TiledImage(0, 0, anchura, altura, 0, 0,
					samplemod, colormod);
			tiled.setData(raster);
		}
		else if (ras_P.getTransferType() == DataBuffer.TYPE_USHORT)
		{
			short[] pixeles_P = new short[anchura * altura];
			ras_P.getDataElements(0, 0, anchura, altura, pixeles_P);

			short[] pixeles_F = new short[anchura * altura];
			ras_F.getDataElements(0, 0, anchura, altura, pixeles_F);
			for (int i = 0; i < pixeles_P.length; i++) {
				pixeles_P[i] -= pixeles_F[i];
			}
			DataBufferUShort dbuffer = new DataBufferUShort(pixeles_P,
					pixeles_P.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(
					DataBuffer.TYPE_USHORT, anchura, altura, 1);
			ColorModel colormod = PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod,
					dbuffer, new Point(0, 0));
			tiled = new TiledImage(0, 0, anchura, altura, 0, 0,
					samplemod, colormod);
			tiled.setData(raster);
			
		}
		else if (ras_P.getTransferType() == DataBuffer.TYPE_BYTE)
		{
			byte[] pixeles_P = new byte[anchura * altura];
			ras_P.getDataElements(0, 0, anchura, altura, pixeles_P);

			byte[] pixeles_F = new byte[anchura * altura];
			ras_F.getDataElements(0, 0, anchura, altura, pixeles_F);
			for (int i = 0; i < pixeles_P.length; i++) {
				pixeles_P[i] -= pixeles_F[i];
			}
			DataBufferByte dbuffer = new DataBufferByte(pixeles_P,
					pixeles_P.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(
					DataBuffer.TYPE_BYTE, anchura, altura, 1);
			ColorModel colormod = PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod,
					dbuffer, new Point(0, 0));
			tiled = new TiledImage(0, 0, anchura, altura, 0, 0,
					samplemod, colormod);
			tiled.setData(raster);
			
		}
		return tiled;
	}


    /**
     * Calcula una imagen que es la resultante de sumar a todos los píxeles de 
     * image el valor dif_OF
     * @param image La imagen de entrada
     * @param dif_OF El valor a sumar
     * @return La suma del valor dif_OF a todos los pixeles de image
     */
	private static PlanarImage sumaconst(PlanarImage image, double dif_OF) {
		int anchura = image.getWidth();
		int altura = image.getHeight();
		Raster ras = image.getData();
		TiledImage tiled = null;
		if (ras.getTransferType()==DataBuffer.TYPE_INT)
		{
			int [] pixeles = new int[anchura*altura]; 
			ras.getDataElements(0,0,anchura,altura,pixeles);				
			for (int i = 0; i < pixeles.length; i ++)
			{
				pixeles[i] += dif_OF;
			}
			DataBufferInt dbuffer = new DataBufferInt(pixeles,pixeles.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, anchura, altura, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
			tiled.setData(raster);
		}
		else if (ras.getTransferType()==DataBuffer.TYPE_USHORT)
		{
			short [] pixeles = new short[anchura*altura]; 
			ras.getDataElements(0,0,anchura,altura,pixeles);				
			for (int i = 0; i < pixeles.length; i ++)
			{
				pixeles[i] += dif_OF;
			}
			DataBufferUShort dbuffer = new DataBufferUShort(pixeles,pixeles.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_USHORT, anchura, altura, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
			tiled.setData(raster);
		}
		else if (ras.getTransferType()==DataBuffer.TYPE_BYTE){
			byte [] pixeles = new byte[anchura*altura]; 
			ras.getDataElements(0,0,anchura,altura,pixeles);				
			for (int i = 0; i < pixeles.length; i ++)
			{
				pixeles[i] += dif_OF;
			}
			DataBufferByte dbuffer = new DataBufferByte(pixeles,pixeles.length);
			SampleModel samplemod = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE, anchura, altura, 1);
			ColorModel colormod =  PlanarImage.createColorModel(samplemod);
			Raster raster = RasterFactory.createWritableRaster(samplemod, dbuffer, new Point(0,0));
			tiled = new TiledImage(0,0,anchura, altura, 0, 0, samplemod, colormod);
			tiled.setData(raster);
		}
		
		return tiled;
		
	}


    /**
     * Calcula la media de los valores de pixel de una imagen
     * @param image Imagen de entrada
     * @return La media de los valores de pixel
     */
	private static double media(PlanarImage image) {
		int anchura = image.getWidth();
		int altura = image.getHeight();
		Raster ras = image.getData();
		BigInteger acum = BigInteger.ZERO;
		double res = 0;
		if (image.getData().getTransferType() == DataBuffer.TYPE_BYTE)
		{
			byte [] pixeles = new byte[anchura*altura]; 
			ras.getDataElements(0,0,anchura,altura,pixeles);
			
			
			for (int i = 0; i < pixeles.length; i ++)
			{
				acum = acum.add(BigInteger.valueOf(pixeles[i]));
			}
			res = acum.doubleValue();
			res /= (double) pixeles.length;
			
		}
		else if (image.getData().getTransferType() == DataBuffer.TYPE_USHORT)
		{
			short [] pixeles = new short[anchura*altura]; 
			ras.getDataElements(0,0,anchura,altura,pixeles);
			
			
			for (int i = 0; i < pixeles.length; i ++)
			{
				acum = acum.add(BigInteger.valueOf(pixeles[i]));
			}
			res = acum.doubleValue();
			res /= (double) pixeles.length;
			
		}
		else if (image.getData().getTransferType() == DataBuffer.TYPE_INT)
		{
			int [] pixeles = new int[anchura*altura]; 
			ras.getDataElements(0,0,anchura,altura,pixeles);
			
			
			for (int i = 0; i < pixeles.length; i ++)
			{
				acum = acum.add(BigInteger.valueOf(pixeles[i]));
			}
			
			res = acum.doubleValue();
			res /= (double) pixeles.length;
		}
		
		return res;
	}
	
    /**
     * Ecualiza una imagen sumándole a la imagen destino la diferencia de la
     * original con respecto a la destino, para igualarlas
     * @param source Imagen origen
     * @param target Imagen destino
     * @return Imagen origen ecualizada a destino
     */
	public static PlanarImage ecualizar (PlanarImage source, PlanarImage target)
	{
		double media_PAN = media(source);
		double media_FUS = media(target);
		double dif_OF = media_PAN-media_FUS;
		PlanarImage sumaPAN = sumaconst(source,dif_OF);
		return sumaPAN;
		
	}

    void setMinMax(double min, double max) {
        minPond = min;
        maxPond = max;
    }
	
	
	

	

	

	
}
