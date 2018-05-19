package com.example.pravin.angreziok.interfaces;

/**
 * Created by HP on 10-08-2017.
 */

public interface ProgressUpdate {
    void onProgressUpdate(int progress);
    void onZipDownloaded(boolean isDownloaded);
    void onZipExtracted(boolean isExtracted);
    void lengthOfTheFile(int length);
}
