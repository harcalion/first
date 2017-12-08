/*
 * Created on Jun 9, 2005
 * @author Rafael Santos (rafael.santos@lac.inpe.br)
 * 
 * Part of the Java Advanced Imaging Stuff site
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI)
 * 
 * STATUS: Complete.
 * 
 * Redistribution and usage conditions must be done under the
 * Creative Commons license:
 * English: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.en
 * Portuguese: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.pt
 * More information on design and applications are on the projects' page
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI).
 */
package es.upm.fi.gtd.first.algorithms;

import java.util.Arrays;
import es.upm.fi.gtd.first.data.ImageRegion;

/**
 * This class contains static utility methods to cluster detection and analysis
 * of images regions using the K-Means algorithm.
 */
public class KMeansRegionClusterer
  {
  /**
   * This method clusters the pixels on an image region using the K-Means algorithm.
   * This method does not run on its own thread.
   * @param r the image region. 
   * @param numClusters the number of clusters.
   * @param maxIterations the maximum number of iterations.
   * @param epsilon the minimum variance to keep clustering.
   * @return an array of KMeansClusterDescriptor with the cluster descriptors.
   */
  public static KMeansClusterDescriptor[] clusterImageRegion(ImageRegion r,
                                                             int numClusters,int maxIterations,
                                                             double epsilon)
    {
    // We need arrays to store the clusters' centers and assignment counts.
    int numBands = r.getImageBands();
    float[][] clusterCenters = new float[numClusters][numBands];
    int[] clusterAssignmentCount = new int[numClusters];
    // Initialize the cluster centers with random values using extrema (maximum) 
    // of data values.
    float[][] extrema = r.getAllExtrema();
    for(int cluster=0;cluster<numClusters;cluster++)
      for(int band=0;band<numBands;band++)
        clusterCenters[cluster][band] = (float)(extrema[band][1]*Math.random());
    // Get all pixels in one structure.
    float[][] pixels = r.getPixels();
    // Create a structure to hold the pixels' clusters.
    int[] clusterIndex = new int[r.getNumberOfPixels()];
    // Start the clustering.    
    double lastSumOfDistances=0;
    iterations: // Label for main loop.
      for(int iteration=0;iteration<maxIterations;iteration++)
        {
        // 0 - Clean the cluster assignment vector.
        Arrays.fill(clusterAssignmentCount,0);
        // 1 - Scan the image and calculate the assignment vector.
        for(int p=0;p<pixels.length;p++)
          {
          // Let's discover that pixel's cluster.
          float closestSoFar = Float.MAX_VALUE;
          short thisPixelsCluster = 0;
          for(short cluster=0;cluster<numClusters;cluster++)
            {
            // Calculate the (quick) distance from this pixel to that cluster center.
            float distance = 0f;
            for(int band=0;band<numBands;band++) distance += Math.abs(clusterCenters[cluster][band]-pixels[p][band]);
            if (distance < closestSoFar)
              {
              closestSoFar = distance;
              thisPixelsCluster = cluster;
              }
            }
          // Store this pixels' cluster.
          clusterIndex[p] = thisPixelsCluster;
          // Update the cluster assignment.
          clusterAssignmentCount[thisPixelsCluster]++;
          }
        // 2 - Scan the assignment vector and recalculate the cluster centers.
        for(int cluster=0;cluster<numClusters;cluster++) Arrays.fill(clusterCenters[cluster],0f);
        for(int p=0;p<pixels.length;p++)
          {
          int theCluster = clusterIndex[p];
          for(int band=0;band<numBands;band++)
            clusterCenters[theCluster][band] += pixels[p][band];
          }
        // 2a - Recalculate the centers.
        for(int cluster=0;cluster<numClusters;cluster++)
          if (clusterAssignmentCount[cluster] > 0)
            for(int band=0;band<numBands;band++)
              clusterCenters[cluster][band]/=clusterAssignmentCount[cluster];
        // 3 - Calculate statistics and repeat from 1 if needed.
        double sumOfDistances = 0;
        for(int p=0;p<pixels.length;p++)
          {
          // To which class does this pixel belong ?
          int pixelsClass = clusterIndex[p];
          // Calculate the distance between this pixel's values and its
          // assigned cluster center values.
          double distance = 0;
          for(int band=0;band<numBands;band++)
            {
            double e1 = pixels[p][band];
            double e2 = clusterCenters[pixelsClass][band];
            double diff = (e1-e2)*(e1-e2);
            distance += diff;
            }
          distance = Math.sqrt(distance);
          sumOfDistances += distance;
          }
        // Is it converging ?
        if (iteration > 0)
          if (Math.abs(lastSumOfDistances-sumOfDistances) < epsilon) break iterations;
        lastSumOfDistances = sumOfDistances;
        } // end of the iterations loop.
    // Lets return the results as a array of KMeansClusterDescriptors.
    KMeansClusterDescriptor[] descriptors = new KMeansClusterDescriptor[numClusters];
    for(int d=0;d<numClusters;d++)
      {
      descriptors[d] = new KMeansClusterDescriptor();
      descriptors[d].setCentroid(clusterCenters[d]);
      descriptors[d].setNumberOfAssignedPoints(clusterAssignmentCount[d]);
      }
    return descriptors;
    }
  
 /**
  * This method analyses a array of KMeansClusterDescriptors and returns the number
  * of non-empty clusters.
  * @param descriptors the array of KMeansClusterDescriptors.
  * @return the number of non-empty clusters.
  */
  public static int getNonEmptyClusters(KMeansClusterDescriptor descriptors[])
    {
    int nonEmpty = 0;
    for(int d=0;d<descriptors.length;d++) 
      if (descriptors[d].getNumberOfAssignedPoints() > 0) nonEmpty++;
    return nonEmpty;
    }
 
 /**
  * This method analyses a array of KMeansClusterDescriptors and returns the instance
  * of KMeansClusterDescriptor with the largest number of assigned points.
  * @param descriptors the array of KMeansClusterDescriptors.
  * @return the "winning" KMeansClusterDescriptor (with the largest assignment count)
  */
  public static KMeansClusterDescriptor getLargestCluster(KMeansClusterDescriptor[] descriptors)
    {
    int largestCount = descriptors[0].getNumberOfAssignedPoints();
    int indexLargest = 0;
    for(int d=1;d<descriptors.length;d++) 
      if (descriptors[d].getNumberOfAssignedPoints() > largestCount)
        {
        indexLargest = d;
        largestCount = descriptors[d].getNumberOfAssignedPoints();
        }
    return descriptors[indexLargest];
    }
  
  }