/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.fi.gtd.first.algorithms;

import es.upm.fi.gtd.first.GeoImg;
import java.awt.RenderingHints;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.BorderExtender;
import javax.media.jai.JAI;
import javax.media.jai.operator.DFTDataNature;
import javax.media.jai.operator.DFTDescriptor;
import javax.media.jai.operator.DFTScalingType;

/**
 *
 * @author Alvar
 */
public class FFT {
    public GeoImg FFT(GeoImg img)
    {
        DFTDataNature ddn = DFTDescriptor.REAL_TO_COMPLEX;
        DFTScalingType dst = DFTDescriptor.SCALING_NONE;
        ParameterBlock pb = new ParameterBlock().addSource(img.getImage()).add(dst).add(ddn);
        RenderingHints rh = new RenderingHints(JAI.KEY_BORDER_EXTENDER,
                    BorderExtender.createInstance(BorderExtender.BORDER_COPY));
        img.setImage(JAI.create("dft",pb,rh));
        return img;
    }

}
