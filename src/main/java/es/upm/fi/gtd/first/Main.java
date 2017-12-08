/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first;

/**
 * Clase principal de la aplicación
 * @author Alvar
 */
public class Main {
    
    /**
     * Punto de entrada a la aplicación
     * @param args Parámetros de línea de comandos que se le han pasado a la
     * aplicación
     */
    public static void main(String[] args) {
        // Cargar las DLL's, para no tener que instalarlas en el JRE

        //System.out.println("Este es el directorio actual"+System.getProperty("user.dir"));
        //String cwd = Main.class.getClassLoader().getResource(".").getPath();
        //Quitarle el slash inicial
        //cwd = cwd.substring(1);
        //Cambiar los slashes
        //cwd = cwd.replace('/','\\');
        //System.out.println(cwd);
        //System.out.println(Main.class.getClassLoader().getResource(".").getPath());
        //String s = System.getProperty("user.dir");
        //System.out.println(cwd+"clib_jiio.dll");
        //System.load(cwd+"clib_jiio.dll");
        //System.load(cwd+"clib_jiio_sse2.dll");
        //System.load(cwd+"clib_jiio_util.dll");
        //System.load(cwd+"mlib_jai.dll");
        //System.load(cwd+"mlib_jai_mmx.dll");
        //System.load(cwd+"mlib_jai_util.dll");
        Atrous a = new Atrous();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                Atrous.createAndShowGUI();
            }
        });

    }

}
