package edu.najah.cap.data.exportdatafeature.strategy.implementation;

import edu.najah.cap.data.exportdatafeature.strategy.interfaces.IActionable;
import edu.najah.cap.data.exportdatafeature.strategy.interfaces.IFileProcess;


public class UploadToStorage implements IActionable {
    private final IFileProcess fileProcessor;

    public UploadToStorage() {
        this.fileProcessor = new FileProcessor("output_UploadToStorage", "output.zip");
    }

    @Override
    public void actionProcess(byte[] data) {
        fileProcessor.actionFileProcess(data);
    }
}