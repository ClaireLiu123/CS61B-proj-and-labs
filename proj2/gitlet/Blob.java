package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private final String _fileName;
    private final String _stringContent;
    private final byte[] _byteContent;
    private final String _sha1ID;

    public void serialize() {
        File newFile = Utils.join(Repository.BLOB_DIR, _sha1ID);
        Utils.writeObject(newFile, this);
    }

    public Blob(String fileName) {
        _fileName = fileName;
        File thisFile = Utils.join(Repository.CWD, fileName);
        _stringContent = Utils.readContentsAsString(thisFile);
        _byteContent = Utils.readContents(thisFile);
        _sha1ID = Utils.sha1(fileName, "###", _stringContent);
        serialize();

    }

    public String getSha1ID() {
        return _sha1ID;
    }

    public String getFileName() {
        return _fileName;
    }

    public byte[] getByteContent() {
        return _byteContent;
    }

    public String getStringContent() {
        return _stringContent;
    }

    /**
     * return the blob object and the blob ID
     */
    public static Blob getBlob(String blobID) {
        File currentBlobLocation = Utils.join(Repository.BLOB_DIR, blobID);
        return Utils.readObject(currentBlobLocation, Blob.class);
    }

    /**
     * write this file into the current working directory
     * since there is no change to the blob so no need to
     * serialize it. This just saved the content into the
     * current working directory
     */
    public void writeIntoDir() {
        Utils.writeContents(new File(_fileName), _stringContent);
    }


}
