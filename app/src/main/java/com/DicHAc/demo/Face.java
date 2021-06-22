package com.DicHAc.demo;

/**
 * Chrif made it
 */

public class Face {
    static {
        System.loadLibrary("Face");
    }

    //Face detection model guide
    public native boolean FaceDetectionModelInit(String faceDetectionModelPath);

    //Face detection, returned bbox, points and other information
    public native int[] FaceDetect(byte[] imageDate, int imageWidth, int imageHeight, int imageChannel);

    //The format of faceInfo: [faceNum,left,top,right,bottom,10*5,……(repeat)]
    public native int[] MaxFaceDetect(byte[] imageDate, int imageWidth, int imageHeight, int imageChannel);

    //Deinitialization of the face detection model
    public native boolean FaceDetectionModelUnInit();

    //Minimum face setting detected
    public native boolean SetMinFaceSize(int minSize);

    //Thread setting
    public native boolean SetThreadsNumber(int threadsNumber);

    //Cycle test times
    public native boolean SetTimeCount(int timeCount);

    //Return characteristic value, jfloatArray type
    public native float[] FaceRecognize(byte[] faceDate1, int w1, int h1, int[] landmarks1);
}
