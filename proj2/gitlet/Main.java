package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Claire Liu
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                Repository.init();
                return;
            case "add":
                String filename = args[1];
                Repository r = readRepo();
                if (r != null) {
                    r.addFile(filename);
                }
                return;
            case "commit":
                String msg = args[1];
                r = readRepo();
                if (r != null) {
                    r.commit(msg);
                }
                return;
            case "rm":
                filename = args[1];
                r = readRepo();
                if (r != null) {
                    r.remove(filename);
                }
                return;
            case "log":
                r = readRepo();
                if (r != null) {
                    r.log();
                }
                return;
            case "checkout":
                r = readRepo();
                if (r != null) {
                    if (args[1].equals("--")) {
                        r.checkOut1(args[2]);
                    } else if (args.length == 2) {
                        r.checkOut3(args[1]);
                    } else if (args[2].equals("--")) {
                        r.checkOut2(args[3], args[1]);
                    }
                    if (args.length == 3 || args.length == 4) {
                        if (!args[args.length - 2].equals("--")) {
                            System.out.println("Incorrect operands.");
                            return;
                        }
                    }
                }
                return;
            case "global-log":
                r = readRepo();
                if (r != null) {
                    r.globalLog();
                }
                return;
            case "find":
                r = readRepo();
                if (r != null) {
                    r.find(args[1]);
                }
                return;
            case "status":
                r = readRepo();
                if (r != null) {
                    r.status();
                }
                return;
            case "branch":
                r = readRepo();
                if (r != null) {
                    r.branch(args[1]);
                }
                return;
            case "rm-branch":
                r = readRepo();
                if (r != null) {
                    r.rmBranch(args[1]);
                }
                return;
            case "reset":
                r = readRepo();
                if (r != null) {
                    r.reset(args[1]);
                }
                return;
            case "merge":
                r = readRepo();
                if (r != null) {
                    r.merge(args[1]);
                }
                return;
            case "push":
                r = readRepo();
                if (r != null) {
                    r.push(args[1], args[2]);
                }
                return;
            case "fetch":
                r = readRepo();
                if (r != null) {
                    r.fetch(args[1], args[2]);
                }
                return;
            case "pull":
                r = readRepo();
                if (r != null) {
                    r.pull(args[1], args[2]);
                }
                return;
            case "add-remote":
                r = readRepo();
                if (r != null) {
                    r.addRemote(args[1], args[2]);
                }
                return;
            case "rm-remote":
                r = readRepo();
                if (r != null) {
                    r.rmRemote(args[1]);
                }
                return;


        }

        System.out.println("No command with that name exists.");
        return;
    }

    /**
     * get the repository
     */
    public static Repository readRepo() {
        if (!Repository.REPO.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return null;
        }
        return Utils.readObject(Repository.REPO, Repository.class);
    }
}
