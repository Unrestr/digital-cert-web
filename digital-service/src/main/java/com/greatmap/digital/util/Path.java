package com.greatmap.digital.util;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author guoan
 */
public class Path implements Comparable, Cloneable{

    private static final boolean WINDOWS = System.getProperty("os.name").startsWith("Windows");
    /**
     * Pre-compiled regular expressions to detect path formats.
     */
    private static final Pattern hasDriveLetterSpecifier = Pattern.compile("^/?[a-zA-Z]:");

    /**
     * The directory separator, a slash.
     */
    public static final String SEPARATOR = "/";
    public static final char SEPARATOR_CHAR = '/';
    public static final String CUR_DIR = ".";

    private String path;

    public static Path get(String parent, String ...children){
        Path returnValue = new Path(parent);
        if(ArrayUtils.isNotEmpty(children)){
            for(int i=0; i<children.length; i++){
                returnValue = Path.get(returnValue,children[i]);
            }
        }
        return returnValue;
    }

    /**
     * Resolve a child path against a parent path.
     */
    public static Path get(String parent, String child) {
        return get(new Path(parent), new Path(child));
    }

    /**
     * Resolve a child path against a parent path.
     */
    public static Path get(Path parent, String child) {
        return get(parent, new Path(child));
    }

    /**
     * Resolve a child path against a parent path.
     */
    public static Path get(String parent, Path child) {
        return get(new Path(parent), child);
    }

    /**
     * Resolve a child path against a parent path.
     */
    public static Path get(Path parent, Path child) {
        String parentPath = parent.getPath();
        String childPath = child.getPath();
        return get(normalize(resolve(parentPath, childPath)));
    }

    public static Path get(String path) {
        return new Path(path);
    }

    /**
     * Construct a path from a String.  Path strings are URIs, but with
     * unescaped elements and some additional normalization.
     */
    protected Path(String pathString) throws IllegalArgumentException {
        if (null == pathString) {
            throw new IllegalArgumentException("Can not create a Path from a null string");
        }

        pathString = normalize(pathString);

        // add a slash in front of paths with Windows drive letters
        if (hasWindowsDrive(pathString) && pathString.charAt(0) != SEPARATOR_CHAR) {
            pathString = SEPARATOR + pathString;
        }

        // add "./" in front of Linux relative paths so that a path containing
        // a colon e.q. "a:b" will not be interpreted as scheme "a".
        if (!WINDOWS && pathString.charAt(0) != SEPARATOR_CHAR && !"./".equals(pathString) && !"../".equals(pathString)) {
            pathString = "./" + pathString;
        }

        this.path = pathString;
    }

    /**
     * Returns the final component of this path.
     */
    public String getName() {
        int slash = path.lastIndexOf(SEPARATOR);
        return path.substring(slash + 1);
    }

    public String getPath() {
        String path = this.path;
        StringBuilder buffer = new StringBuilder();
        if (null != path) {
            if (path.indexOf(SEPARATOR_CHAR) == 0 && hasWindowsDrive(path)) {
                // remove slash before drive
                path = path.substring(1);
            } else if (2 < path.length() && path.indexOf("./") == 0) {
                path = path.substring(2);
            }
            buffer.append(path);
        }
        return buffer.toString();
    }


    /**
     * Returns the parent of a path or null if at root.
     */
    public Path getParent() {
        int lastSlash = path.lastIndexOf(SEPARATOR_CHAR);
        int start = hasWindowsDrive(path) ? 3 : 0;
        // empty path
        // at root
        if ((path.length() == start) || (lastSlash == start && path.length() == start + 1)) {
            return null;
        }
        String parent;
        if (lastSlash == -1) {
            parent = CUR_DIR;
        } else {
            int end = hasWindowsDrive(path) ? 3 : 0;
            parent = path.substring(0, lastSlash == end ? end + 1 : lastSlash);
        }
        return new Path(parent);
    }

    /**
     * True if the path component of this URI is absolute.
     */
    public boolean isAbsolute() {
        int start = hasWindowsDrive(path) ? 3 : 0;
        return path.startsWith(SEPARATOR, start);
    }

    /**
     * @return true if and only if this path represents the root of a file system
     */
    public boolean isRoot() {
        return getParent() == null;
    }

    /**
     * Return the number of elements in this path.
     */
    public int depth() {
        int depth = 0;
        int slash = path.length() == 1 && path.charAt(0) == SEPARATOR_CHAR ? -1 : 0;
        while (slash != -1) {
            depth++;
            slash = path.indexOf(SEPARATOR, slash + 1);
        }
        return depth;
    }

    public boolean startsWith(Path path) {
        return startsWith(path.path);
    }

    public boolean startsWith(String path) {
        String me = normalize(this.path);
        String other = normalize(path);
        return StringUtils.startsWith(me, other, WINDOWS);
    }

    public boolean endsWith(Path path) {
        return endsWith(path.path);
    }

    public boolean endsWith(String path) {
        String me = normalize(this.path);
        String other = normalize(path);
        return StringUtils.endsWith(me, other, WINDOWS);
    }

    /**
     * Adds a suffix to the final name in the path.
     */
    public Path suffix(String suffix) {
        return get(getParent(), getName() + suffix);
    }

    public Path resolve(String child) {
        return get(this, child);
    }

    public Path resolve(String... children) {
        Path parent = new Path(this.path);
        for (String child : children) {
            parent = parent.resolve(child);
        }
        return parent;
    }

    public Path resolve(Path child) {
        return get(this, child);
    }

    public Path relativize(Path base) {
        return new Path(relativize(base.getPath()));
    }

    public String relativize(String base) {
        return relativize(getPath(), base);
    }

    public File asFile() {
        return new File(getPath());
    }

    @Override
    public int compareTo(Object o) {
        Path that = (Path) o;
        return this.path.compareTo(that.path);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Path)) {
            return false;
        }
        Path that = (Path) o;
        return this.path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return getPath();
    }

    public static String resolve(String base, String child) {
        if (null == base) {
            return child;
        }
        if (null == child) {
            return base;
        }
        child = !child.startsWith(SEPARATOR) ? child : child.substring(1);
        return base + (base.endsWith(SEPARATOR) ? child : (SEPARATOR + child));
    }

    public static String relativize(String path, String base) {
        base = normalize(base);
        path = normalize(path);

        if (path.length() < 1 || base.length() < 1 || !path.startsWith(base)) {
            throw new IllegalArgumentException("path not has same parent path");
        }

        StringBuilder buffer = new StringBuilder();
        String[] basePairs = base.split("/");
        String[] pathPairs = path.split("/");
        int i = 0;
        int len = Math.min(basePairs.length, pathPairs.length);

        for (; i < len && basePairs[i].equals(pathPairs[i]); i++) ;

        for (int j = i; j < basePairs.length; j++) {
            buffer.append("../");
        }

        for (int j = i; j < pathPairs.length; j++) {
            if (pathPairs[j].length() < 1) {
                continue;
            }
            buffer.append(pathPairs[j]).append("/");
        }

        len = buffer.length();
        return 0 < len ? buffer.deleteCharAt(len - 1).toString() : "";
    }

    /**
     * Normalize a relative URI path that may have relative values ("/./",
     * "/../", and so on ) it it.  <strong>WARNING</strong> - This method is
     * useful only for normalizing application-generated paths.  It does not
     * try to perform security checks for malicious input.
     * Normalize operations were was happily taken from org.apache.catalina.util.RequestUtil in
     * Tomcat trunk, r939305
     *
     * @param path Relative path to be normalized
     * @return normalized path
     */
    public static String normalize(String path) {
        return normalize(path, true);
    }

    /**
     * Normalize a relative URI path that may have relative values ("/./",
     * "/../", and so on ) it it.  <strong>WARNING</strong> - This method is
     * useful only for normalizing application-generated paths.  It does not
     * try to perform security checks for malicious input.
     *
     * @param path             Relative path to be normalized
     * @param replaceBackSlash Should '\\' be replaced with '/'
     * @return normalized path
     */
    private static String normalize(String path, boolean replaceBackSlash) {
        if (path == null) {
            return null;
        }

        if (path.indexOf('/') == 0 && hasWindowsDrive(path)) {
            path = path.substring(1);
        }

        // Create a place for the normalized path
        String normalized = path;
        if (replaceBackSlash && normalized.indexOf('\\') >= 0) {
            normalized = normalized.replace('\\', '/');
        }

        if ("/.".equals(normalized)) {
            return "/";
        }

        // Add a leading "/" if necessary
        /*
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        */

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0) {
                break;
            }
            normalized = normalized.substring(0, index) + normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0) {
                break;
            }
            normalized = normalized.substring(0, index) + normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0) {
                break;
            }
            if (index == 0) {
                // Trying to go outside our context
                return (null);
            }
            int index2 = normalized.lastIndexOf('/', index - 1);
            if (index2 < 0) {
                normalized = normalized.substring(index + 4);
            } else {
                normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
            }
        }

        if (0 == normalized.length()) {
            normalized = "./";
        }
        // Return the normalized path that we have completed
        return (normalized);
    }

    /**
     * Determine whether a given path string represents an absolute path on
     * Windows. e.g. "C:/a/b" is an absolute path. "C:a/b" is not.
     *
     * @param pathString Supplies the path string to evaluate.
     * @param slashed    true if the given path is prefixed with "/".
     * @return true if the supplied path looks like an absolute path with a Windows
     * drive-specifier.
     */
    public static boolean isWindowsAbsolutePath(final String pathString, final boolean slashed) {
        int start = (slashed ? 1 : 0);

        return hasWindowsDrive(pathString) && pathString.length() >= (start + 3)
                && ((pathString.charAt(start + 2) == SEPARATOR_CHAR) || (pathString.charAt(start + 2) == '\\'));
    }

    private static boolean hasWindowsDrive(String path) {
        return (WINDOWS && hasDriveLetterSpecifier.matcher(path).find());
    }

    public static void main(String[] args){
        System.out.println(Path.get("first","second","third","forth","fifth"));
    }
}
