package edu.najah.cap.data.exportdatafeature.processdata.intf;

import com.dropbox.core.DbxException;
import edu.najah.cap.exceptions.InvalidUploadTypeException;

import java.io.IOException;

public interface IUploadService {
     void upload(byte[] data, String fileName) throws IOException, DbxException, InvalidUploadTypeException;
}
