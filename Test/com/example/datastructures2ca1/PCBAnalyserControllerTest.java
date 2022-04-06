package com.example.datastructures2ca1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PCBAnalyserControllerTest {
    ArrayList<Integer> testArray = new ArrayList<>();
    int[] testImage={10,11,11,2,8,13,2,7,8,9,11,11,7,8};
    private PCBAnalyserController pcbAnalyserController;

    @BeforeEach
    void setUp() {
        ArrayList<Integer> testArray = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
         int[] testimage = null;
    }

    @Test
    public void testFind(){
        assertEquals(PCBAnalyserController.find(testImage,5) , 8);
    }



    @Test
    public void testUnion(){
        System.out.println("Before union");
        System.out.println(PCBAnalyserController.find(testImage,10));
        //for(int id=0 ;id<testImage.length;id++)
          //  System.out.println("The root of " +id+ " is "+ PCBAnalyserController.find(testImage,id)+" (value: "+testImage[id]+")");
        PCBAnalyserController.union(testImage,13,1); //Union disjoint sets containing elements with ids 13 and 1
        //System.out.println("\nAfter Union\n-------------------------------------------");
        //for(int id=0;id<testImage.length;id++)
          //  System.out.println("The root of element "+id+" is "+PCBAnalyserController.find(testImage,id)+" (value: "+testImage[id]+")");
            //System.out.println("\n");
        System.out.println("After Union");
        System.out.println(PCBAnalyserController.find(testImage,10));

     assertEquals(PCBAnalyserController.find(testImage,10) , 8);
    }
    @Test
    public void testComponentType(){
        double red =0.2;
        double green = 0.18;
        double blue = 0.2;
        assertEquals(PCBAnalyserController.determineComponent(red,green,blue),"Integrated Circuit");

    }

    @Test
    public void testSetUpClusters(){
        System.out.println("Size before" + testArray.size());
        for(int p = 0; p < testImage.length; p++){
            if(testImage[p] !=0 && !testArray.contains(pcbAnalyserController.find(testImage,p))) {
                testArray.add(pcbAnalyserController.findCompress(testImage,p));
            }
        }
        System.out.println("Size After" + testArray.size());
        assertEquals(testArray.size(),4);
    }


}