package enums;

public enum PackageSize {
    SMALL, MEDIUM, LARGE;

    public boolean canFit(PackageSize other) {
        return this.ordinal() >= other.ordinal();
    }
}
