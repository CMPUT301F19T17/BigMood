package edu.ualberta.cmput301f19t17.bigmood.database.listener;

public interface ImageProgressListener {

    /**
     * This method gets called as part of the Firebase Storage image upload/download process. It's suppsoed to provide a callback interface to update any progress bars whilst downloading or uploading a file.
     * @param progress The progress in a 0-100 integer range
     */
    void onProgress(int progress);

}
