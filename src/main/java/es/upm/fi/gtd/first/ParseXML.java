package es.upm.fi.gtd.first;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Clase que implementa un interfaz al almacenamiento persistente en XML usado
 * por la aplicación
 * @author Alvar
 */
public class ParseXML {
    /**
     * Archivo cargado
     */
	private File archivo;
        /**
         * Documento en el que se cargará el contenido XML una vez analizado
         */
	private org.w3c.dom.Document doc;
        /**
         * @deprecated Si se va a almacenar un valor de ERGAS o de POND
         */
         @Deprecated
	public static final int ERGAS = 0;
       /**
         * @deprecated Si se va a almacenar un valor de ERGAS o de POND
         */
         @Deprecated
	public static final int POND = 1;
	
        private Node nodoActual; 
         
    /**
     * Constructor
     * @param archivo2 Archivo XML a cargar y analizar 
     */
	public ParseXML(String archivo2)
	{
		archivo = new File(archivo2);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		//docFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {

			Dialogos.informarExcepcion(e);
		}
		
		try {
			doc = docBuilder.parse(archivo2);
		} catch (SAXException e) {

			Dialogos.informarExcepcion(e);					
		} catch (IOException e) {
			Dialogos.informarExcepcion(e);
		}
		
		//doc.normalize();
	}
        
    /**
     * Cambia los valores de una serie de registros
     * @param valores Valores nuevos de los registros
     */
    public void sustituirValores(List<String []> valores)
        {
            NodeList nodos = doc.getElementsByTagName("*");
            Element n;
            for (int i = 1; i < nodos.getLength(); i++)
            {
                n = (Element) nodos.item(i);
                if (n.getTextContent().compareTo(valores.get(i-1)[0])==0)
                {
                    n.setAttribute("ver", valores.get(i-1)[1]);
                }
            }
        }
        
    /**
     * Obtiene todos los valores almacenados en el fichero XML
     * @return Todos los valores como una Lista de arrays de String
     */
    public List<String []> recuperarValores()
        {
            NodeList nodos = doc.getElementsByTagName("*");
            Element n;
            
            List<String []> res = new ArrayList<String []>();
            
            for (int i = 1; i < nodos.getLength(); i++)
            {
                n = (Element) nodos.item(i);
                
                
                
                String [] resultado = new String[2];
                
                

                //System.out.println(n.getAttribute("ver"));    
                
                //System.out.println(n.getTextContent());
                resultado[0] = n.getTextContent();
                resultado[1] = n.getAttribute("ver");
                res.add(resultado);
            }
            return res;
        }
        
        /**
         * Recorre todos los registros almacenados en el fichero XML
         * @return Todos los registros como un array de String
         */
        public String[] recorrer()
        {
            Node nodo = null;
            if (nodoActual == null)
            {
                nodo = doc.getFirstChild();
                
                
                nodo = nodo.getFirstChild();
            }
            else
            {
                nodo = nodoActual.getNextSibling();
                if (nodo == null)
                {
                    return null;
                }
            }
            String [] res = new String[2]; // Nombre, versión
            nodoActual = nodo;
            System.out.println(nodo.getNodeName());
            System.out.println(nodo.getNodeValue());
            System.out.println("El nodo tiene atributos?"+nodo.hasAttributes());
            
            NamedNodeMap nnm = nodo.getAttributes();
            
            Node nombre = nnm.getNamedItem("jar");
            if (nombre == null)
            {
                nombre = nnm.getNamedItem("dll");
            }
            
                res [0] = nombre.getNodeValue();
            
                
            
            res [1] = nnm.getNamedItem("version").getNodeValue();
            return res;
        }

        /**
         * Busca un valor determinado en el registro identificado por key
         * @param key Clave del registro
         * @return Valor del registro
         */
        public String buscar(int key)
        {
            // En el caso de haber caracteres de nueva línea en el XML 
            // introducirlos en el String
            String res = buscar(String.valueOf(key),"value");
            try
            {
             res =    res.replaceAll("\\\\n",System.getProperty("line.separator"));
            }
            catch(NullPointerException npe)
            {
                Dialogos.informarError("Cadena de texto no encontrada en el fichero XML de idioma");
            }

            return res;
        }
    /**
     * Busca y devuelve un valor 
     * @param bint Identificador de la entrada
     * @param atributo Atributo del que queremos conocer el valor
     * @return Valor del atributo
     */
	public String buscar(String bint,String atributo)
	{

		//Element elem = doc.getElementById(bint.toString());
		Element elem = doc.getElementById(bint);
                
		String resultado = new String();
		
		if (elem == null)
		{
			
			resultado = null;
		}
		else
		{
			resultado = elem.getAttribute(atributo);
			
		}
		return resultado;
	}
    /**
     * Insertar una entrada de valores de ERGAS en un fichero XML
     * @param hash1S Hash del primer fichero
     * @param path Ruta del primer fichero
     * @param digest_PANS Hash del segundo fichero
     * @param path2 Ruta del segundo fichero
     * @param wvm Wavelet utilizada
     * @param niv_PAN Niveles de degradación de la imagen pancromática
     * @param niv_MULTI Niveles de degradación de la imagen multiespectral
     * @param interpol Interpolación utilizada
     * @param precis Precisión de submuestra en el caso de interpolación bicúbica
     * @param ponderacion Ponderación utilizada en el caso concreto
     * @param d Valor de ERGAS espacial
     * @param e Valor de ERGAS espectral
     */
	public void insertarErgas(String hash1S, String path, String digest_PANS, String path2, int wvm, int niv_PAN, int niv_MULTI, int interpol, int precis, double ponderacion, double d, double e) {



		

		Node valores = doc.getFirstChild().getNextSibling();

		Node ergas = doc.createElement("fusion");
		//int num = doc.getElementsByTagName("fusion").getLength();
		ergas.setTextContent("666");

		NamedNodeMap ergasAttrs = ergas.getAttributes();
		Attr hash1 = doc.createAttribute("hash1");
		hash1.setValue(hash1S);
		ergasAttrs.setNamedItem(hash1);
		Attr file1 = doc.createAttribute("file1");
		file1.setValue(path);
		ergasAttrs.setNamedItem(file1);
		Attr digest_PAN = doc.createAttribute("hash2");
		digest_PAN.setValue(digest_PANS);
		ergasAttrs.setNamedItem(digest_PAN);
		Attr file2 = doc.createAttribute("file2");
		file2.setValue(path2);
		ergasAttrs.setNamedItem(file2);
		Attr wavelet = doc.createAttribute("wavelet");
		wavelet.setValue(String.valueOf(wvm));
		ergasAttrs.setNamedItem(wavelet);
		Attr niveles_PAN = doc.createAttribute("niveles_PAN");
		niveles_PAN.setValue(String.valueOf(niv_PAN));
		ergasAttrs.setNamedItem(niveles_PAN);
		Attr niveles_MULTI = doc.createAttribute("niveles_MULTI");
		niveles_MULTI.setValue(String.valueOf(niv_MULTI));
		ergasAttrs.setNamedItem(niveles_MULTI);
		Attr interpolacion = doc.createAttribute("interpolación");
		interpolacion.setValue(String.valueOf(interpol));
		ergasAttrs.setNamedItem(interpolacion);
		Attr precision = doc.createAttribute("precisión");
		precision.setValue(String.valueOf(precis));
		ergasAttrs.setNamedItem(precision);
		Attr ponder = doc.createAttribute("ponderación");
		ponder.setValue(String.valueOf(ponderacion));
		ergasAttrs.setNamedItem(ponder);
		Attr ergasEspa = doc.createAttribute("ERGAS_espacial");
		ergasEspa.setValue(String.valueOf(d));
		ergasAttrs.setNamedItem(ergasEspa);
		Attr ergasEspe = doc.createAttribute("ERGAS_espectral");
		ergasEspe.setValue(String.valueOf(e));
		ergasAttrs.setNamedItem(ergasEspe);
		valores.appendChild(ergas);
		
		
		
		
		
		
	}
	
    /**
     * Actualiza el archivo en disco con los cambios realizados al árbol XML
     */
	public void actualizar ()
	{
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			Dialogos.informarExcepcion(e);
		} catch (TransformerFactoryConfigurationError e) {

			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		//transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "Valores_de_ERGAS [<!ATTLIST fusion hash1 ID #REQUIRED>]");
                //transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "ergas.dtd");

//		initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(archivo);
		if (result == null)
		{
			System.out.println("Odiemos XML");
		}
		
		DOMSource source = new DOMSource(doc);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			Dialogos.informarExcepcion(e);
		}

		//String xmlString = result.getWriter().toString();
		//System.out.println(xmlString);
	}
    /**
     * Insertar una entrada con el resultado del cálculo de una ponderación óptima
     * @param string Hash primer fichero
     * @param path Ruta primer fichero
     * @param digest_PANS Hash segundo fichero
     * @param path2 Ruta segundo fichero
     * @param wvm Wavelet utilizada
     * @param niv_PAN Niveles de degradación de la imagen pancromática
     * @param niv_MULTI Niveles de degradación de la imagen multiespectral
     * @param interpol Interpolación utilizada
     * @param precis Precisión de submuestra en el caso de interpolación bicúbica
     * @param muestras Muestras tomadas para el cálculo de la ponderación
     * @param d Valor de la ponderación
     */
	public void insertarPond(String string, String path, String digest_PANS, String path2, int wvm, int niv_PAN, int niv_MULTI, int interpol, int precis, int muestras, double d) {		
		Node valores = doc.getFirstChild().getNextSibling();

		Node ergas = doc.createElement("fusion");
		//int num = doc.getElementsByTagName("fusion").getLength();
		ergas.setTextContent("1");

		NamedNodeMap ergasAttrs = ergas.getAttributes();
		Attr hash1 = doc.createAttribute("hash1");
		hash1.setValue(string);
		ergasAttrs.setNamedItem(hash1);
		Attr file1 = doc.createAttribute("file1");
		file1.setValue(path);
		ergasAttrs.setNamedItem(file1);
		Attr digest_PAN = doc.createAttribute("hash2");
		digest_PAN.setValue(digest_PANS);
		ergasAttrs.setNamedItem(digest_PAN);
		Attr file2 = doc.createAttribute("file2");
		file2.setValue(path2);
		ergasAttrs.setNamedItem(file2);
		Attr wavelet = doc.createAttribute("wavelet");
		wavelet.setValue(String.valueOf(wvm));
		ergasAttrs.setNamedItem(wavelet);
		Attr niveles_PAN = doc.createAttribute("niveles_PAN");
		niveles_PAN.setValue(String.valueOf(niv_PAN));
		ergasAttrs.setNamedItem(niveles_PAN);
		Attr niveles_MULTI = doc.createAttribute("niveles_MULTI");
		niveles_MULTI.setValue(String.valueOf(niv_MULTI));
		ergasAttrs.setNamedItem(niveles_MULTI);
		Attr interpolacion = doc.createAttribute("interpolación");
		interpolacion.setValue(String.valueOf(interpol));
		ergasAttrs.setNamedItem(interpolacion);
		Attr precision = doc.createAttribute("precisión");
		precision.setValue(String.valueOf(precis));
		ergasAttrs.setNamedItem(precision);
		Attr muestr = doc.createAttribute("precisión");
		precision.setValue(String.valueOf(muestras));
		ergasAttrs.setNamedItem(muestr);
		Attr ponder = doc.createAttribute("ponderación_óptima");
		ponder.setValue(String.valueOf(d));
		ergasAttrs.setNamedItem(ponder);
		valores.appendChild(ergas);
		System.out.println("Viva yo");
	}
    /**
     * @deprecated Crear o actualizar un archivo XML con resultados de ERGAS o POND
     * @param files Archivos comparados
     * @param res Resultados
     * @param tipo Tipo de operación
     * @param erg Objeto Ergas
     * @param wvm Wavelet usada
     * @param niv_PAN Niveles de degradación de la imagen pancromática
     * @param niv_MULTI Niveles de degradación de la imagen multiespectral
     * @param interpol Interpolación utilizada
     * @param precis Precisión de submuestra (en caso de usar interpolación bicúbica)
     * @param muestras Muestras usadas en el caso de un cálculo de ponderación
     * @param ponderacion Ponderación utilizada en cada caso concreto
     */
        @Deprecated
	public static void crearOActualizar (File[] files, double[] res, int tipo, Ergas erg, int wvm, int niv_PAN, int niv_MULTI, int interpol, int precis, int muestras, double ponderacion)
	{
		if (files[0]==null)
		{
			return;
		}
	
		if (tipo == ERGAS)
		{

			// Creando el archivo XML
			OutputStream fout = null;
			File ful = new File("ergas.xml");
			if (!ful.exists())
			{
				try {
					fout = new FileOutputStream("ergas.xml");
				} catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
				OutputStream bout= new BufferedOutputStream(fout);
				OutputStreamWriter out = null;
				try {
					out = new OutputStreamWriter(bout, "UTF8");
				} catch (UnsupportedEncodingException e) {
					
					Dialogos.informarExcepcion(e);
				
				}
				try {
					out.write("<?xml version=\"1.0\" ");
					out.write("encoding=\"UTF-8\"?>\r\n");
					out.write("<!DOCTYPE Valores_de_ERGAS SYSTEM \"ergas.dtd\">\r\n");
					out.write("<Valores_de_ERGAS>\r\n");
					String digest_PAN = new String(ParseXML.digest(files[0]).toString());
					for (int i = 1; i < files.length; i++)
					{
						out.write("   <fusion " + 
	        			"hash1=\"" +
	        			ParseXML.digest(files[i]).toString() +
	        			"\" "+
	        			"file1=\"" +
	        			files[i].getPath() +
	        			"\" "+
	        			"hash2=\"" +
	        			digest_PAN +
	        			"\" "+
	        			"file2=\"" +
	        			files[0].getPath() +
	        			"\" "+
	        			"wavelet=\"" +
	        			wvm +
	        			"\" "+
	        			"niveles_PAN=\"" +
	        			niv_PAN +
	        			"\" "+
	        			"niveles_MULTI=\"" +
	        			niv_MULTI +
	        			"\" "+
	        			"interpolación=\"" +
	        			interpol +
	        			"\" "+
	        			"precisión=\"" +
	        			precis +
	        			"\" "+
	        			"ponderación=\"" +
	        			ponderacion +
	        			"\" "+
	        			"ERGAS_espacial=\"" +
	        			res[2*(i-1)] +
	        			"\" "+
	        			"ERGAS_espectral=\"" +
	        			res[2*(i-1)+1] +
	        			"\""+
	        			">" + i + "</fusion>\r\n"
					    );
		        }
		        out.write("</Valores_de_ERGAS>\r\n");
		        out.flush();  // Don't forget to flush!
		        out.close();
			} catch (Exception e) {
				
				Dialogos.informarExcepcion(e);
			}
		}
		else
		{// El archivo existe
			System.out.println("El archivo existe y lo actualizo");
			ParseXML pxml = new ParseXML("ergas.xml");
				String digest_PAN;
				try {
					digest_PAN = new String(ParseXML.digest(files[0]).toString());
					for (int i = 1; i < files.length; i++)
			        {
			        	pxml.insertarErgas(ParseXML.digest(files[i]).toString(),files[i].getPath(),digest_PAN,files[0].getPath(),wvm,niv_PAN,niv_MULTI,interpol,precis, ponderacion,res[2*(i-1)],res[2*(i-1)+1]);
			        	
			        }
					pxml.actualizar();
				} catch (Exception e) {

					e.printStackTrace();
				}
		        
			}
	
	}
	else if (tipo == POND)
	{
//		 Creando el archivo XML
		OutputStream fout = null;
		File ful = new File("ergas.xml");
		if (!ful.exists())
		{
			try {
				fout = new FileOutputStream("ergas.xml");
			} catch (FileNotFoundException e) {
				
				Dialogos.informarExcepcion(e);
			}
			OutputStream bout= new BufferedOutputStream(fout);
			OutputStreamWriter out = null;
			try {
				out = new OutputStreamWriter(bout, "UTF8");
			} catch (UnsupportedEncodingException e) {
			
				Dialogos.informarExcepcion(e);
			}
			try {
				out.write("<?xml version=\"1.0\" ");
				out.write("encoding=\"UTF-8\"?>\r\n");
				out.write("<!DOCTYPE Valores_de_ERGAS SYSTEM \"ergas.dtd\">\r\n");
				out.write("<Valores_de_ERGAS>\r\n");
				String digest_PAN = new String(ParseXML.digest(files[0]).toString());
		        for (int i = 0; i < res.length; i++)
		        {

		        		out.write("   <fusion " + 
	        			"hash1=\"" +
	        			ParseXML.digest(files[i+1]).toString() +
	        			"\" "+
	        			"file1=\"" +
	        			files[i].getPath() +
	        			"\" "+
	        			"hash2=\"" +
	        			digest_PAN +
	        			"\" "+
	        			"file2=\"" +
	        			files[0].getPath() +
	        			"\" "+
	        			"wavelet=\"" +
	        			wvm +
	        			"\" "+
	        			"niveles_PAN=\"" +
	        			niv_PAN +
	        			"\" "+
	        			"niveles_MULTI=\"" +
	        			niv_MULTI +
	        			"\" "+
	        			"interpolación=\"" +
	        			interpol +
	        			"\" "+
	        			"precisión=\"" +
	        			precis +
	        			"\" "+
	        			"ponderación_óptima=\"" +
	        			res[i] +
	        			"\""+        			
	        			">" + i + "</fusion>\r\n"
					    );

		        }
		        out.write("</Valores_de_ERGAS>\r\n");
		        out.flush();  // Don't forget to flush!
		        out.close();
			} catch (Exception e) {
				
				Dialogos.informarExcepcion(e);
			}
		}
		else
		{// El archivo existe
			
			ParseXML pxml = new ParseXML("ergas.xml");
				String digest_PAN;
				try {
					digest_PAN = new String(ParseXML.digest(files[0]).toString());
					for (int i = 1; i < files.length; i++)
			        {			        	
			        	pxml.insertarPond(ParseXML.digest(files[i]).toString(),files[i].getPath(),digest_PAN,files[0].getPath(),wvm,niv_PAN,niv_MULTI,interpol,precis,muestras,res[i-1]);
			        }
					pxml.actualizar();
				} catch (Exception e) {

					Dialogos.informarExcepcion(e);
				}
		        
			}
	}
	}
	
    /**
     * Crea un hash de un fichero
     * @param archivo Fichero de entrada
     * @return Hash en formato BigInteger
     * @throws Exception
     */
	public static BigInteger digest (File archivo) throws Exception
	{
		FileChannel channel = new RandomAccessFile(archivo, "r").getChannel();
        ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
        MessageDigest md = MessageDigest.getInstance("md5");
        md.update(buffer.duplicate());
        byte[] digest = md.digest();
        BigInteger hash = new BigInteger(1, digest);
        return hash;
	}
	
}
