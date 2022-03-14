package gitlet;



import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Date;


/**
 * Represents a gitlet commit object.
 *
 * does at a high level.
 *
 * @author Claire Liu
 */
public class Commit implements Serializable {
    /**
     *
     * <p>
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    private final String _timestamp;
    private final String _parentID;
    private final String parentID2;
    private final HashMap<String, String> _myBlob;
    private final String _sha1Commit;

    /**
     * The message of this Commit.
     */
    private final String _message;

    /**
     * Constructor of Commit for merge.
     */
    public Commit(HashMap<String, String> myBlob, String parentID,
                  String parentID2, String message) {
        this._parentID = parentID;
        this.parentID2 = parentID2;

        /** check if it is the first commit
         * if is the first commit then the time should be
         * 00:00:00 UTC, Thursday, 1 January 1970
         * else if is not the first commit
         * the time will be the current time*/
        if (this._parentID == null) {
            this._timestamp = Utils.formatt().format(new Date(0)); // initial's date
        } else {
            this._timestamp = Utils.formatt().format(new Date());
        }

        this._message = message;

        if (myBlob == null) {
            this._myBlob = new HashMap<String, String>();
        } else {
            this._myBlob = myBlob;
        }
        String preSha1 = parentID + parentID2 + message + _timestamp;
        for (String s : _myBlob.values()) {
            Blob b = Blob.getBlob(s);
            preSha1 += b.getByteContent();
        }
        this._sha1Commit = Utils.sha1(preSha1);
        this.serialize();

    }

    public String getTimestamp() {
        return this._timestamp;
    }


    public String getSha1Commit() {
        return _sha1Commit;
    }

    public HashMap<String, String> getMyBlob() {
        return _myBlob;
    }

    public void serialize() {
        File fileCommit = Utils.join(Repository.COMMIT_DIR, _sha1Commit);
        Utils.writeObject(fileCommit, this);
    }

    public String getParentID() {
        return _parentID;
    }

    public String getParentID2() {
        return parentID2;
    }


    public void commitInfo() {
        System.out.println("===");
        System.out.println("commit " + getSha1Commit());
        System.out.println("Date: " + getTimestamp());
        System.out.println(_message);
        System.out.println();

    }


    public String getMessage() {
        return this._message;
    }

}
