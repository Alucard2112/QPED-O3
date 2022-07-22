package eu.qped.java.checkers.classdesign.infos;

import java.util.List;
import java.util.Objects;

public class ExpectedElement {

    private String accessModifier;
    private List<String> nonAccessModifiers;
    private String type;
    private String name;

    public ExpectedElement(String accessMod, List<String> nonAccessMods, String type, String name) {
        this.accessModifier = accessMod;
        this.nonAccessModifiers = nonAccessMods;
        this.type = type;
        this.name = name;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public List<String> getNonAccessModifiers() {
        return nonAccessModifiers;
    }

    public void setNonAccessModifiers(List<String> nonAccessModifiers) {
        this.nonAccessModifiers = nonAccessModifiers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpectedElement that = (ExpectedElement) o;
        return Objects.equals(accessModifier, that.accessModifier) &&
                Objects.equals(nonAccessModifiers, that.nonAccessModifiers) &&
                Objects.equals(type, that.type) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessModifier, nonAccessModifiers, type, name);
    }
}
