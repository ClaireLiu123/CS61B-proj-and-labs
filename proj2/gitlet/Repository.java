package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

/**
 * Represents a gitlet repository.
 * does at a high level.
 *
 * @author Claire Liu
 */
public class Repository implements Serializable {
    /**
     *
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /**
     * The Blob directory.
     */
    public static final File BLOB_DIR = join(GITLET_DIR, "Blob");

    /**
     * The commit directory.
     */
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");

    /**
     * Path of the repo object.
     */
    public static final File REPO = join(GITLET_DIR, "repo");

    /**
     * map of the branch name and key is the sha1 hash code
     */
    private final HashMap<String, String> _branches;

    /**
     * name of the current branch and this will be the key for the branches HashMap
     */
    private String _currBranchName;

    /**
     * map of all commit made so far save all the sha1 code
     */
    private final HashSet<String> _allCommitMade;

    /**
     * map of removed file save that the file has been removed
     * we just need the file name because remove does not need to
     * know the content
     */
    private final HashSet<String> _removedFile;

    /**
     * map of add file save that the file has been add
     * we need the file name and the content because we need to
     * see if the file have same content
     */
    private final HashMap<String, String> _addedFile;


    private final HashMap<String, String> _remotes;

    void addRemote(String name, String path) {
        if (_remotes.containsKey(name)) {
            System.out.println("A remote with that name already exists.");
            return;
        }
        _remotes.put(name, path);
        serializeRepo();
    }

    void rmRemote(String name) {
        if (!_remotes.containsKey(name)) {
            System.out.println("A remote with that name does not exist.");
            return;
        }
        _remotes.remove(name);
        serializeRepo();
    }

    Repository getRemoteRepo(String remoteName) {
        return Utils.readObject(new File(_remotes.get(remoteName) + "/repo"), Repository.class);
    }

    void push(String remoteName, String remoteBranch) {
        if (!new File(_remotes.get(remoteName)).exists()) {
            System.out.println("Remote directory not found.");
            return;
        }
        Repository remoteRepo = getRemoteRepo(remoteName);
        Commit curHeadCommit = getCommit(sha1CurrentHeadCommit());
        if (!findAncestor(sha1CurrentHeadCommit()).contains
                (remoteRepo._branches.get(remoteBranch))) {
            System.out.println("Please pull down remote "
                    + "changes before pushing.");
            return;
        }
        Commit tracker = curHeadCommit;
        while (!remoteRepo._branches.get(remoteBranch).equals(tracker.getSha1Commit())) {
            remoteRepo._allCommitMade.add(tracker.getSha1Commit());
            addLocalCommit(remoteName, tracker.getSha1Commit());
            for (String blobID : tracker.getMyBlob().values()) {
                addLocalBlob(remoteName, blobID);
            }
            tracker = getCommit(tracker.getParentID());
        }
        remoteRepo._branches.put(remoteBranch, sha1CurrentHeadCommit());
        saveRemoteT(remoteName, remoteRepo);
    }

    void fetch(String remoteName, String remoteBranch) {
        if (!new File(_remotes.get(remoteName)).exists()) {
            System.out.println("Remote directory not found.");
            return;
        }
        Repository remoteRepo = getRemoteRepo(remoteName);
        if (!remoteRepo._branches.containsKey(remoteBranch)) {
            System.out.println("That remote does not have that branch.");
            return;
        }
        Commit tracker = getRemoteC(remoteName, remoteRepo._branches.get(remoteBranch));
        while (!_allCommitMade.contains(tracker.getSha1Commit())) {
            addRemoteCommit(remoteName, tracker.getSha1Commit());
            _allCommitMade.add(tracker.getSha1Commit());
            for (String blobID : tracker.getMyBlob().values()) {
                addRemoteBlob(remoteName, blobID);
            }
            tracker = getRemoteC(remoteName, tracker.getParentID());
        }
        String newBranchName = remoteName + "/" + remoteBranch;
        _branches.put(newBranchName, getRemoteC(remoteName,
                remoteRepo._branches.get(remoteBranch)).getSha1Commit());
        serializeRepo();
    }

    void pull(String remoteName, String remoteBranch) {
        fetch(remoteName, remoteBranch);
        merge(remoteName + "/" + remoteBranch);
        serializeRepo();
    }

    void addRemoteBlob(String remoteName, String blobID) {
        getRemoteB(remoteName, blobID).serialize();
    }

    void addRemoteCommit(String remoteName, String commitID) {
        getRemoteC(remoteName, commitID).serialize();
    }

    Blob getRemoteB(String remoteName, String blobID) {
        return Utils.readObject(new File(_remotes.get(remoteName) + "/Blob/" + blobID), Blob.class);
    }

    Commit getRemoteC(String remoteName, String commitID) {
        return Utils.readObject(new File(_remotes.get(remoteName)
                + "/commit/" + commitID), Commit.class);
    }

    void addLocalCommit(String remoteName, String commitID) {
        Utils.writeObject(new File(_remotes.get(remoteName)
                + "/commit/" + commitID), getCommit(commitID));
    }

    void addLocalBlob(String remoteName, String blobID) {
        Utils.writeObject(new File(_remotes.get(remoteName)
                + "/Blob/" + blobID), Blob.getBlob(blobID));
    }

    void saveRemoteT(String remoteName, Repository remoteRepo) {
        Utils.writeObject(new File(_remotes.get(remoteName) + "/repo"), remoteRepo);
    }


    /**
     * constructor of Repository
     */
    public Repository() {
        _currBranchName = "master";
        _branches = new HashMap<String, String>();
        _allCommitMade = new HashSet<String>();
        _removedFile = new HashSet<String>();
        _addedFile = new HashMap<String, String>();
        _remotes = new HashMap<>();

        /** make an initial commit */
        Commit initialCommit = new Commit(null, null, null, "initial commit");
        _allCommitMade.add(initialCommit.getSha1Commit());
        _branches.put(_currBranchName, initialCommit.getSha1Commit());

    }

    /**
     * Handles the init command
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the current directory.");
        } else {
            GITLET_DIR.mkdir();
            BLOB_DIR.mkdir();
            COMMIT_DIR.mkdir();
        }
        Repository r = new Repository();
        r.serializeRepo();
    }


    /**
     * return the current commit ID
     */
    public String sha1CurrentHeadCommit() {
        return _branches.get(_currBranchName);
    }

    /**
     * return the last commit object and its ID
     */
    public Commit getCommit(String commitID) {
        File currentLocation = join(COMMIT_DIR, commitID);
        return Utils.readObject(currentLocation, Commit.class);
    }


    /**
     * Add the file to the staging area
     */
    public void addFile(String fileName) {
        File thisFile = new File(fileName);
        if (!thisFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        /** get the latest commit */
        Commit lastCommit = getCommit(sha1CurrentHeadCommit());
        /** get the last commit files from this blob and
         * save it in a map where map from the file name
         * sha1 code of this blob to the content in sha1
         * code of this file*/
        HashMap<String, String> lastCommitFile = lastCommit.getMyBlob();
        Blob b = new Blob(fileName);

        /** first check if the staging area does not have the same
         * file name then add the file name of this blob
         * and the sha1 code of this blob.
         * Then check if they have the same name
         * do they have the same content
         * if they have same content then do nothing
         * if they have different content then add the
         * new file sha1 code to the staging area*/

        if (!lastCommitFile.containsKey(fileName)) {
            /** put the current file name of this blob and
             * the sha1 id of this blob in to the staging area */
            _addedFile.put(b.getFileName(), b.getSha1ID());
            _removedFile.remove(fileName);

            /** after add the sha1 code of the name of the file
             * and the shai id of the blob then serialize it
             * which turn the object to bytes*/
            b.serialize();
        } else {
            /** if the file does not have same name
             * then make a file which have the path
             * of the current working directory*/
            File currFile = join(CWD, fileName);
            byte[] currFileVersionContents = Utils.readContents(currFile);
            /** get the latest commit blob and get the file
             * from the latest commit blob */
            Blob lastBlobCommit = Blob.getBlob(lastCommitFile.get(fileName));
            /** get the last blob commit file content in bytes */
            byte[] lastBlobCommitVersionContent = lastBlobCommit.getByteContent();
            /** check if the file with same file name have same contents or not
             * if they don't have the same contents then add to the staging area
             * if they have same contents then remove the file name from the
             * staging area*/
            if (!Arrays.equals(currFileVersionContents, lastBlobCommitVersionContent)) {
                _addedFile.put(b.getFileName(), b.getSha1ID());
                _removedFile.remove(fileName);
                b.serialize();
            } else {
                _addedFile.remove(b.getFileName());
                _removedFile.remove(fileName);
            }
        }
        this.serializeRepo();

    }

    public void serializeRepo() {
        Utils.writeObject(REPO, this);
    }

    public void commit(String message) {
        if (_addedFile.isEmpty() && _removedFile.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        }

        /** get the last commit */
        Commit recentCommit = getCommit(sha1CurrentHeadCommit());

        /** map from file name to blob sha1 code from last commit */
        HashMap<String, String> prevSnapShot = recentCommit.getMyBlob();

        /** map from file name to blob sha1 code from current commit */
        HashMap<String, String> snapShot = new HashMap<String, String>();

        /** add the file name and blob sha1 code
         * from the previous snap shot into the
         * current snap shot */
        for (String s : prevSnapShot.keySet()) {
            if (_removedFile.contains(s)) {
                continue;
            }

            /** check if the file name is in the
             * staging area then we use the new blob
             * sha1 code and put the new file in the snap shot
             * is the file name is not in the staging area then
             * we use the previous commit blob
             * sha1 code and put the previous version commit
             * file into the snap shot */
            if (_addedFile.containsKey(s)) {
                snapShot.put(s, _addedFile.get(s));
            } else {
                snapShot.put(s, prevSnapShot.get(s));
            }
        }

        /** look through the current staging area and
         * see if there is new file added
         * if there is a new file added then add it
         * in the snap shot from the staging area*/
        for (String a : _addedFile.keySet()) {
            if (_removedFile.contains(a)) {
                continue;
            }
            /** if the snap shot does not contains
             * the file name then we add the file
             * to the snapshot from the staging area*/
            if (!snapShot.containsKey(a)) {
                snapShot.put(a, _addedFile.get(a));
            }
        }

        Commit newCommit = new Commit(snapShot, sha1CurrentHeadCommit(), null, message);

        /** clear the staging area after each commit */
        _addedFile.clear();
        _removedFile.clear();
        _branches.put(_currBranchName, newCommit.getSha1Commit());
        _allCommitMade.add(newCommit.getSha1Commit());
        this.serializeRepo();

    }

    /**
     * if the file is in the staging area then remove it
     * from the staging area. If the file is in the
     * working directory then remove the file complete
     */
    public void remove(String removeFileName) {
        Commit headCommit = getCommit(sha1CurrentHeadCommit());
        HashMap<String, String> headCommitFiles = headCommit.getMyBlob();
        if (!headCommitFiles.containsKey(removeFileName)
                && !_addedFile.containsKey(removeFileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        _addedFile.remove(removeFileName);


        if (headCommitFiles.containsKey(removeFileName)) {
            _removedFile.add(removeFileName);
            File[] cwdfiles = CWD.listFiles();
            for (int i = 0; i < cwdfiles.length; i++) {
                if (cwdfiles[i].getName().equals(removeFileName)) {
                    cwdfiles[i].delete();
                }
            }
        }

        this.serializeRepo();

    }

    public void log() {
        /** get the head commit */
        Commit trackCommit = getCommit(sha1CurrentHeadCommit());
//        System.out.println(trackCommit.get_parentID());
//        System.out.println(trackCommit.get_parentID_2());
        trackCommit.commitInfo();
        while (trackCommit.getParentID() != null) {
            /** if the trackCommit is not equal to init commit
             * then move the commit to its parent commit */
            trackCommit = getCommit(trackCommit.getParentID());
            trackCommit.commitInfo();
        }
    }

    public void checkOut1(String fileName) {
        /** get the head commit */
        Commit getHead = getCommit(sha1CurrentHeadCommit());
        HashMap<String, String> currHeadFiles = getHead.getMyBlob();

        if (!currHeadFiles.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        /** find the blob that has the fileName
         * and then get that blob and store into b */
        Blob b = Blob.getBlob(currHeadFiles.get(fileName));
        b.writeIntoDir();
    }

    public void checkOut2(String fileName, String id) {
        if (id.length() < 40) {
            String full = id;
            for (String s : _allCommitMade) {
                if (s.startsWith(id)) {
                    full = s;
                }
            }
            id = full;
        }
        if (!_allCommitMade.contains(id)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit getCommitWithId = getCommit(id);
        HashMap<String, String> commitWithIdFiles = getCommitWithId.getMyBlob();
        if (!commitWithIdFiles.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        /** find the blob that has the fileName
         * and then get that blob and store into b */
        Blob b = Blob.getBlob(commitWithIdFiles.get(fileName));
        b.writeIntoDir();
    }

    public void checkOut3(String branchName) {
        if (!_branches.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (_currBranchName.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        if (!unTrackFiles().isEmpty()) {
            System.out.println("There is an untracked file in "
                    + "the way; delete it, or add and commit it first.");
            return;
        }

        /** get the head commit of the given branch. */
        Commit branchHeadCommit = getCommit(_branches.get(branchName));
        /** clean all the files in the current working directory
         * and then add the file from the branch head commit */
        clearCWD();
        HashMap<String, String> branchHeadCommitBlob = branchHeadCommit.getMyBlob();
        for (String s : branchHeadCommitBlob.values()) {
            Blob b = Blob.getBlob(s);
            b.writeIntoDir();
        }
        _currBranchName = branchName;
        serializeRepo();
    }

    public ArrayList<String> unTrackFiles() {
        ArrayList<String> result = new ArrayList<String>();
        File[] files = CWD.listFiles();
        Commit recentCommit = getCommit(sha1CurrentHeadCommit());
        HashMap<String, String> currentCommitBlob = recentCommit.getMyBlob();
        for (File f : files) {
            if (f.isDirectory()) {
                continue;
            }
            if (!currentCommitBlob.containsKey(f.getName())
                    && !_addedFile.containsKey(f.getName())) {
                result.add(f.getName());
            }
        }
        return result;
    }

    public void clearCWD() {
        File[] files = CWD.listFiles();
        for (File f : files) {
            if (!f.isDirectory()) {
                f.delete();
            }
        }
    }


    public void globalLog() {
        for (String s : _allCommitMade) {
            Commit c = getCommit(s);
            c.commitInfo();
        }
    }

    public void find(String inputMessage) {
        boolean exist = false;
        for (String s : _allCommitMade) {
            Commit c = getCommit(s);
            if (c.getMessage().equals(inputMessage)) {
                System.out.println(s);
                exist = true;
            }
        }
        if (!exist) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void status() {
        System.out.println("=== Branches ===");
        for (String bName : sortSet(_branches.keySet())) {
            if (_currBranchName.equals(bName)) {
                System.out.print("*");
            }
            System.out.println(bName);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String stageName : sortSet(_addedFile.keySet())) {
            System.out.println(stageName);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String removeName : sortSet(_removedFile)) {
            System.out.println(removeName);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String s : sortSet(modifiedStatus())) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        for (String s : sortSet(untrackStatus())) {
            System.out.println(s);
        }
    }


    HashSet<String> modifiedStatus() {
        HashSet<String> result = new HashSet<>();
        HashMap<String, String> latest
                = getCommit(sha1CurrentHeadCommit()).getMyBlob();
        ArrayList<String> allF = new ArrayList<String>();
        allF.addAll(Arrays.asList(CWD.list()));
        for (String f : CWD.list()) {
            File path = new File(f);
            if (_addedFile.containsKey(f)) {
                if (!Arrays.equals(Blob.getBlob(_addedFile.get(f)).getByteContent(),
                        Utils.readContents(path))) {
                    result.add(f + " (modified)");
                }
            } else if (latest.containsKey(f)) {
                if (!_addedFile.containsKey(f)
                        && !Arrays.equals(Blob.getBlob(latest.get(f)).getByteContent(),
                        Utils.readContents(path))) {
                    result.add(f + " (modified)");
                }
            }
        }

        for (String f : latest.keySet()) {
            if (!allF.contains(f) & !_removedFile.contains(f)) {
                result.add(f + " (deleted)");
            }
        }
        for (String f : _addedFile.keySet()) {
            if (!allF.contains(f)) {
                result.add(f + " (deleted)");
            }
        }
        return result;
    }

    HashSet<String> untrackStatus() {
        HashSet<String> result = new HashSet<>();
        HashMap<String, String> latest
                = getCommit(sha1CurrentHeadCommit()).getMyBlob();
        for (String f : CWD.list()) {
            if (f.startsWith(".")) {
                continue;
            }
            if (!latest.containsKey(f) & !_addedFile.containsKey(f)) {
                result.add(f);
            }
            if (_removedFile.contains(f)) {
                result.add(f);
            }
        }
        return result;
    }

    /** This method takes any collection of Strings,
     * put all the strings into an arraylist and sort them.
     * then return the sorted arraylist. */
    /**
     * old can be any collection of String
     */
    public ArrayList<String> sortSet(Collection<String> old) {
        ArrayList<String> result = new ArrayList<String>();
        /** add all the old strings into the result */
        result.addAll(old);
        /** sort all the Strings in the result */
        result.sort(String::compareTo);
        return result;
    }


    public void branch(String branchName) {
        if (_branches.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        _branches.put(branchName, sha1CurrentHeadCommit());
        serializeRepo();
    }

    public void rmBranch(String branchName) {
        if (!_branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        } else if (_currBranchName.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }

        _branches.remove(branchName);
        serializeRepo();

    }

    public void reset(String id) {
        if (id.length() < 40) {
            for (String s : _allCommitMade) {
                if (s.startsWith(id)) {
                    id = s;
                }
            }
        }
        if (!_allCommitMade.contains(id)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        if (!unTrackFiles().isEmpty()) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            return;
        }

        clearCWD();
        Commit givenCommit = getCommit(id);
        HashMap<String, String> givenCommitBlob = givenCommit.getMyBlob();
        for (String s : givenCommitBlob.values()) {
            Blob b = Blob.getBlob(s);
            b.writeIntoDir();
        }
        _addedFile.clear();
        _removedFile.clear();
        _branches.put(_currBranchName, id);
        serializeRepo();

    }

    public void merge(String otherBranch) {
        if (!_addedFile.isEmpty() || !_removedFile.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (!_branches.containsKey(otherBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (otherBranch.equals(_currBranchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        if (!unTrackFiles().isEmpty()) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            return;
        }
        postMerge(otherBranch);
    }
    public void postMerge(String otherBranch) {
        Commit curr = getCommit(sha1CurrentHeadCommit());
        Commit given = getCommit(_branches.get(otherBranch));
        Commit split = getCommit(findSplitPoint(otherBranch));
        if (split.getSha1Commit().equals(given.getSha1Commit())) {
            System.out.println("Given branch is an ancestor "
                    + "of the current branch.");
            _branches.put(otherBranch, sha1CurrentHeadCommit());
            serializeRepo();
            return;
        } else if (split.getSha1Commit().equals(curr.getSha1Commit())) {
            System.out.println("Current branch fast-forwarded.");
            _branches.put(_currBranchName, given.getSha1Commit());
            clearCWD();
            HashMap<String, String> givenBlob = given.getMyBlob();
            for (String s : givenBlob.values()) {
                Blob b = Blob.getBlob(s);
                b.writeIntoDir();
            }
            serializeRepo();
            return;
        }
        int conflict = 0;
        for (String f : split.getMyBlob().keySet()) {
            if (given.getMyBlob().containsKey(f)) {
                if (curr.getMyBlob().containsKey(f)) {
                    if (!given.getMyBlob().get(f).equals(split.getMyBlob().get(f))
                            & curr.getMyBlob().get(f).equals(split.getMyBlob().get(f))) {
                        Blob givenVerion = Blob.getBlob(given.getMyBlob().get(f));
                        givenVerion.writeIntoDir();
                        _addedFile.put(f, givenVerion.getSha1ID());
                    } else if (!given.getMyBlob().get(f).equals(split.getMyBlob().get(f))
                            & !curr.getMyBlob().get(f).equals(split.getMyBlob().get(f))) {
                        if (!curr.getMyBlob().get(f).equals(given.getMyBlob().get(f))) {
                            writeConflict(f, curr.getMyBlob().get(f), given.getMyBlob().get(f));
                            conflict += 1;
                        }
                    }
                } else {
                    if (!split.getMyBlob().get(f).equals(given.getMyBlob().get(f))) {
                        writeConflict(f, curr.getMyBlob().get(f), given.getMyBlob().get(f));
                        conflict += 1;
                    }
                }
            } else if (!given.getMyBlob().containsKey(f) & curr.getMyBlob().containsKey(f)) {
                if (split.getMyBlob().get(f).equals(curr.getMyBlob().get(f))) {
                    Utils.restrictedDelete(f);
                    _removedFile.add(f);
                    continue;
                }
                writeConflict(f, curr.getMyBlob().get(f), given.getMyBlob().get(f));
                conflict += 1;
            }
        }
        for (String f : given.getMyBlob().keySet()) {
            if (!split.getMyBlob().containsKey(f)) {
                if (curr.getMyBlob().containsKey(f)) {
                    if (!curr.getMyBlob().get(f).equals(given.getMyBlob().get(f))) {
                        writeConflict(f, curr.getMyBlob().get(f), given.getMyBlob().get(f));
                        conflict += 1;
                    }
                } else {
                    Blob givenBlob = Blob.getBlob(given.getMyBlob().get(f));
                    givenBlob.writeIntoDir();
                    _addedFile.put(f, givenBlob.getSha1ID());
                }
            }
        }
        String parent2 = _branches.get(otherBranch);
        String newCommit = commit("Merged " + otherBranch + " into "
                + _currBranchName + ".", sha1CurrentHeadCommit(), parent2);
        _allCommitMade.add(newCommit);
        _branches.put(_currBranchName, newCommit);
        if (conflict > 0) {
            System.out.println("Encountered a merge conflict.");
        }
        _addedFile.clear();
        _removedFile.clear();
        serializeRepo();
    }

    public String commit(String msg, String parent1, String parent2) {
        Commit par = getCommit(sha1CurrentHeadCommit());
        HashMap<String, String> myBlobs = new HashMap<>();
        for (String s : par.getMyBlob().keySet()) {
            if (!_removedFile.contains(s)) {
                if (_addedFile.containsKey(s)) {
                    myBlobs.put(s, _addedFile.get(s));
                } else {
                    myBlobs.put(s, par.getMyBlob().get(s));
                }
            }
        }
        for (String s : _addedFile.keySet()) {
            if (!par.getMyBlob().containsKey(s)) {
                myBlobs.put(s, _addedFile.get(s));
            }
        }
        Commit thisC = new Commit(myBlobs, parent1, parent2, msg);
        return thisC.getSha1Commit();
    }

    void writeConflict(String fileName, String curBlobID, String givenBlobID) {
        StringBuilder content = new StringBuilder();
        Blob curBlob = null;
        Blob givenBlob = null;
        if (curBlobID != null) {
            curBlob = Blob.getBlob(curBlobID);
        }
        if (givenBlobID != null) {
            givenBlob = Blob.getBlob(givenBlobID);
        }
        content.append("<<<<<<< HEAD\n");
        if (curBlobID != null & givenBlobID != null) {
            content.append(curBlob.getStringContent());
            content.append("=======\n");
            content.append(givenBlob.getStringContent());
        } else {
            if (curBlobID != null) {
                content.append(curBlob.getStringContent());
                content.append("=======\n");
            } else if (givenBlobID != null) {
                content.append("=======\n");
                content.append(givenBlob.getStringContent());
            }
        }
        content.append(">>>>>>>\n");
        Utils.writeContents(new File(fileName), content.toString());
        _addedFile.put(fileName, new Blob(fileName).getSha1ID());
    }


    public HashSet<String> findAncestor(String id) {
        /** stack is to store the nodes we're about to visit.
         * result is to store the visited nodes. */
        HashSet<String> result = new HashSet<String>();
        ArrayList<String> stack = new ArrayList<String>();
        stack.add(id);
        while (!stack.isEmpty()) {
            String nextId = stack.get(0);
            stack.remove(0);
            result.add(nextId);
            /** get the commit with the nextId */
            Commit givenIdCommit = getCommit(nextId);
            /** add all the parents of the nextId commit
             * to the stack. if it is not null */
            if (givenIdCommit.getParentID() != null) {
                stack.add(givenIdCommit.getParentID());
            }

            if (givenIdCommit.getParentID2() != null) {
                stack.add(givenIdCommit.getParentID2());
            }

        }

        return result;
    }


    public String findSplitPoint(String givenBranchName) {
        HashSet<String> givenBranchAncestors = findAncestor(_branches.get(givenBranchName));
        ArrayList<String> stack = new ArrayList<String>();
        HashSet<String> visitedNode = new HashSet<String>();
        stack.add(sha1CurrentHeadCommit());
        while (!stack.isEmpty()) {
            String next = stack.get(0);
            stack.remove(0);
            if (visitedNode.contains(next)) {
                continue;
            }
            visitedNode.add(next);
            if (givenBranchAncestors.contains(next)) {
                return next;
            }

            /** get the commit with the nextId */
            Commit givenIdCommit = getCommit(next);
            /** add all the parents of the nextId commit
             * to the stack. if it is not null */
            if (givenIdCommit.getParentID() != null) {
                stack.add(givenIdCommit.getParentID());
            }

            if (givenIdCommit.getParentID2() != null) {
                stack.add(givenIdCommit.getParentID2());
            }

        }
        throw new GitletException("No common ancestor for current and given branch");
    }


}
