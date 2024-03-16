import java.util.*;

class Version {
    String data;
    List<Version> children;

    public Version(String data) {
        this.data = data;
        this.children = new ArrayList<>();
    }
}

class VersionControl {
    private Version baseVersion;
    private Map<String, Version> versionMap;

    public VersionControl() {
        this.baseVersion = new Version("Base");
        this.versionMap = new HashMap<>();
        this.versionMap.put("Base", baseVersion);
    }

    public void addVersion(String versionName, String parentVersionName, String delta) {
        Version parentVersion = versionMap.get(parentVersionName);
        if (parentVersion == null) {
            throw new IllegalArgumentException("Parent version not found");
        }

        Version newVersion = new Version(applyDelta(parentVersion.data, delta));
        parentVersion.children.add(newVersion);
        versionMap.put(versionName, newVersion);
    }

    public String applyDelta(String baseData, String delta) {
        // Apply the delta to the base data and return the result
        return baseData + delta;
    }

    public String getVersionData(String versionName) {
        Version version = versionMap.get(versionName);
        if (version == null) {
            throw new IllegalArgumentException("Version not found");
        }

        return version.data;
    }
}

public class Main {
    public static void main(String[] args) {
        VersionControl versionControl = new VersionControl();
        versionControl.addVersion("V1", "Base", "Delta1");
        versionControl.addVersion("V2", "V1", "Delta2");
        versionControl.addVersion("V3", "V2", "Delta3");

        System.out.println(versionControl.getVersionData("V3")); // Output: BaseDelta1Delta2Delta3
    }
}
