package es.upm.fi.gtd.first.janet;

import java.io.Serializable;

/**
 * The interface XMLExpressible indicates that the
 * implementing class is able to express itself
 * in XML format.
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @version 1.0
 **/
public interface XMLExpressible 
    extends Serializable {

    /**
     * The method should return an XML version of the object.
     **/
    public abstract String toXML();

} // XMLExpressible
